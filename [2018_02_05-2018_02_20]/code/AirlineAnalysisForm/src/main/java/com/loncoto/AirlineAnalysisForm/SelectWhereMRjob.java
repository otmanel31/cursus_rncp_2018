package com.loncoto.AirlineAnalysisForm;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.loncoto.AirlineAnalysisForm.utils.AirlineDataUtils;

/**
 * Hello world!
 *
 */
public class SelectWhereMRjob extends Configured implements Tool
{
	
	
	public static class MyMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

		private int delayInMinutes = 0;
		
		/*
		 * 
		 * ligne de commande exemple
		 * 
		 * hadoop jar AirlineAnalysisForm-0.0.1-SNAPSHOT.jar
		 * 		  com.loncoto.AirlineAnalysisForm.SelectWhereMRjob
		 * 		  -D map.where.delay=45 
		 * 	      /user/formation/airlinedata/inputwhere
		 * 		  /user/formation/airlinedata/outputwhere2
		 */
		
		@Override
		protected void setup(
				Mapper<LongWritable, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			// a l'execution, grace au generic option parser
			// on peut renseigner ce parametre avec
			// -D map.where.delay=valeur
			this.delayInMinutes = context.getConfiguration().getInt("map.where.delay", 1);
		}



		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			// sauter la ligne d'en-tête
			if (!AirlineDataUtils.isHeader(value)) {
				// extrait les champs avec getSelectedColumns
				String[] values = AirlineDataUtils.getSelectedColumnsA(value);
				//on verifie pour ne garder que les lignes dont le retard au depart est supérieur à 15
				if (AirlineDataUtils.parseMinutes(values[8], 0) > this.delayInMinutes) {
					StringBuilder sb = AirlineDataUtils.mergeStringArray(values, ",");
					// on envoie la ligne à la sortie
					context.write(NullWritable.get(), new Text(sb.toString()));
				}
			}
		}
	}
	
    public static void main( String[] args ) throws Exception
    {
    	Configuration conf = new Configuration();
    	ToolRunner.run(new SelectWhereMRjob(), args);
    }

    // methode de Tool
    // cette méthode démarrera le job
	@Override
	public int run(String[] args) throws Exception {
		// initialisation du job en récupérant notre configuration
		Job job = Job.getInstance(getConf());
		
		job.setJarByClass(SelectWhereMRjob.class);
		
		// formlat fichier en entree/sortie
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		// format clé/valeur en sortie, ici pas de clé d'ou NullWritable
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		job.setMapperClass(MyMapper.class);
		
		// pas de reducteur
		job.setNumReduceTasks(0);
		
		// cette classe permet de mettre dans la configuration les arguments standard connus par hadoop
		// automatiquement, en nous renvoyant ensuite les autres arguments restants
		String[] arguments = new GenericOptionsParser(getConf(), args).getRemainingArgs();
		
		// chemin de lecture/ecriture dans hdfs
		FileInputFormat.setInputPaths(job, new Path(arguments[0]));
		FileOutputFormat.setOutputPath(job, new Path(arguments[1]));
		
		// true --> verbose
		boolean status = job.waitForCompletion(true);
		if (status)
			return 0;
		else
			return 1;
	}
}
