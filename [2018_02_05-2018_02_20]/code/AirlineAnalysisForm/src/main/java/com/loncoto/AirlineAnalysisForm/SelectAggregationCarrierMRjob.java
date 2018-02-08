package com.loncoto.AirlineAnalysisForm;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
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
public class SelectAggregationCarrierMRjob extends Configured implements Tool
{

	public static final IntWritable DISTANCE_0_100 = new IntWritable(0);
	public static final IntWritable DISTANCE_100_200 = new IntWritable(1);
	public static final IntWritable DISTANCE_200_400 = new IntWritable(2);
	public static final IntWritable DISTANCE_400_800 = new IntWritable(3);
	public static final IntWritable DISTANCE_800_MARS = new IntWritable(4);
	
	
	public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// sauter la ligne d'en-tête
			if (!AirlineDataUtils.isHeader(value)) {
				// extrait les champs avec getSelectedColumns
				String[] values = AirlineDataUtils.getSelectedColumnsC(value);
				
				String carrier = values[12];
				int distance = AirlineDataUtils.parseMinutes(values[5], 0);
				
				if (distance <= 100)
					context.write(new Text(carrier), DISTANCE_0_100);
				else if (distance <= 200)
					context.write(new Text(carrier), DISTANCE_100_200);
				else if (distance <= 400)
					context.write(new Text(carrier), DISTANCE_200_400);
				else if (distance <= 800)
					context.write(new Text(carrier), DISTANCE_400_800);
				else
					context.write(new Text(carrier), DISTANCE_800_MARS);
			}
		}
	}
	
	// 
	public static class MyReducer extends Reducer<Text, IntWritable, NullWritable, Text> {

		@Override
		protected void reduce(Text carrier, Iterable<IntWritable> codes,
				Reducer<Text, IntWritable, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			
			double  totalFlight = 0;
			double total_0_100 = 0;
			double total_100_200 = 0;
			double total_200_400 = 0;
			double total_400_800 = 0;
			double total_800_mars = 0;
			
			
			for (IntWritable code : codes) {
				totalFlight++;
				if (code.equals(DISTANCE_0_100))
					total_0_100++;
				else if (code.equals(DISTANCE_100_200))
					total_100_200++;
				else if (code.equals(DISTANCE_200_400))
					total_200_400++;
				else if (code.equals(DISTANCE_400_800))
					total_400_800++;
				else if (code.equals(DISTANCE_800_MARS))
					total_800_mars++;
			}
			
			StringBuilder sb = new StringBuilder(carrier.toString());
			DecimalFormat df = new DecimalFormat("0.0000");
			
			sb.append(',').append(totalFlight);
			sb.append(',').append(df.format(total_0_100/totalFlight));
			sb.append(',').append(df.format(total_100_200/totalFlight));
			sb.append(',').append(df.format(total_200_400/totalFlight));
			sb.append(',').append(df.format(total_400_800/totalFlight));
			sb.append(',').append(df.format(total_800_mars/totalFlight));
			
			context.write(NullWritable.get(), new Text(sb.toString()));
		}
		
	}
	
    public static void main( String[] args ) throws Exception
    {
    	Configuration conf = new Configuration();
    	ToolRunner.run(new SelectAggregationCarrierMRjob(), args);
    }

    // methode de Tool
    // cette méthode démarrera le job
	@Override
	public int run(String[] args) throws Exception {
		// initialisation du job en récupérant notre configuration
		Job job = Job.getInstance(getConf());
		
		job.setJarByClass(SelectAggregationCarrierMRjob.class);
		
		// formlat fichier en entree/sortie
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		// format clé/valeur en sortie, ici pas de clé d'ou NullWritable
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		// format de sortie du mapper
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
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
