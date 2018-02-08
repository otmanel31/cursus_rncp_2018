package com.loncoto.AirlineAnalysisForm;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
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
public class SelectAggregationDistanceMRjob extends Configured implements Tool
{

	public static final IntWritable DISTANCE_0_100 = new IntWritable(0);
	public static final IntWritable DISTANCE_100_200 = new IntWritable(1);
	public static final IntWritable DISTANCE_200_400 = new IntWritable(2);
	public static final IntWritable DISTANCE_400_800 = new IntWritable(3);
	public static final IntWritable DISTANCE_800_MARS = new IntWritable(4);
	
	
	public static class MyMapper extends Mapper<LongWritable, Text, IntWritable, BooleanWritable> {

		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, IntWritable, BooleanWritable>.Context context)
				throws IOException, InterruptedException {
			// sauter la ligne d'en-tête
			if (!AirlineDataUtils.isHeader(value)) {
				// extrait les champs avec getSelectedColumns
				String[] values = AirlineDataUtils.getSelectedColumnsC(value);
				
				int distance = AirlineDataUtils.parseMinutes(values[5], 0);
				boolean enRetard = AirlineDataUtils.parseMinutes(values[9], 0) > 15;
				
				
				if (distance <= 100)
					context.write(DISTANCE_0_100, new BooleanWritable(enRetard));
				else if (distance <= 200)
					context.write(DISTANCE_100_200, new BooleanWritable(enRetard));
				else if (distance <= 400)
					context.write(DISTANCE_200_400, new BooleanWritable(enRetard));
				else if (distance <= 800)
					context.write(DISTANCE_400_800, new BooleanWritable(enRetard));
				else
					context.write(DISTANCE_800_MARS, new BooleanWritable(enRetard));
			}
		}
	}
	
	// 
	public static class MyReducer extends Reducer<IntWritable, BooleanWritable, NullWritable, Text> {

		@Override
		protected void reduce(IntWritable distance, Iterable<BooleanWritable> ponctualites,
				Reducer<IntWritable, BooleanWritable, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			
			double  totalFlight = 0;
			double en_retard = 0;
			double ponctuel = 0;
			
			
			for (BooleanWritable retard : ponctualites) {
				totalFlight++;
				if (retard.get())
					en_retard++;
				else
					ponctuel++;
			}
			
			StringBuilder sb = new StringBuilder("" + distance);
			DecimalFormat df = new DecimalFormat("0.0000");
			
			sb.append(',').append(totalFlight);
			sb.append(',').append(df.format(ponctuel/totalFlight));
			sb.append(',').append(df.format(en_retard/totalFlight));
			
			context.write(NullWritable.get(), new Text(sb.toString()));
		}
		
	}
	
    public static void main( String[] args ) throws Exception
    {
    	Configuration conf = new Configuration();
    	ToolRunner.run(new SelectAggregationDistanceMRjob(), args);
    }

    // methode de Tool
    // cette méthode démarrera le job
	@Override
	public int run(String[] args) throws Exception {
		// initialisation du job en récupérant notre configuration
		Job job = Job.getInstance(getConf());
		
		job.setJarByClass(SelectAggregationDistanceMRjob.class);
		
		// formlat fichier en entree/sortie
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		// format clé/valeur en sortie, ici pas de clé d'ou NullWritable
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		// format de sortie du mapper
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(BooleanWritable.class);
		
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		
		// un reducteur
		job.setNumReduceTasks(2);
		
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
