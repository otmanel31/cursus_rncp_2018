package com.loncoto.AirlineAnalysisForm;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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
public class SelectAggregationMRjob extends Configured implements Tool
{

	public static final IntWritable FLIGHT = new IntWritable(0);
	public static final IntWritable DEPARTURE_DELAY = new IntWritable(1);
	public static final IntWritable DEPARTURE_ONTIME = new IntWritable(2);
	public static final IntWritable ARRIVAL_DELAY = new IntWritable(3);
	public static final IntWritable ARRIVAL_ONTIME = new IntWritable(4);
	public static final IntWritable CANDELLED = new IntWritable(5);
	public static final IntWritable DIVERTED = new IntWritable(6);
	
	
	public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// sauter la ligne d'en-tête
			if (!AirlineDataUtils.isHeader(value)) {
				// extrait les champs avec getSelectedColumns
				String[] values = AirlineDataUtils.getSelectedColumnsB(value);
				
				String month = values[0];
				int delayArrival = AirlineDataUtils.parseMinutes(values[9], 0);
				int delayDeparture = AirlineDataUtils.parseMinutes(values[8], 0);
				boolean isCancelled = AirlineDataUtils.parseBoolean(values[10], false);
				boolean isDiverted = AirlineDataUtils.parseBoolean(values[11], false);
				
				// compter un vol
				context.write(new Text(month), FLIGHT);
				
				
			}
		}
	}
	
    public static void main( String[] args ) throws Exception
    {
    	Configuration conf = new Configuration();
    	ToolRunner.run(new SelectAggregationMRjob(), args);
    }

    // methode de Tool
    // cette méthode démarrera le job
	@Override
	public int run(String[] args) throws Exception {
		// initialisation du job en récupérant notre configuration
		Job job = Job.getInstance(getConf());
		
		job.setJarByClass(SelectAggregationMRjob.class);
		
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
