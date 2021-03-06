package com.loncoto.AirlineAnalysisForm;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
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
public class SelectAggregationCombinerMRjob extends Configured implements Tool
{

	public static final IntWritable FLIGHT = new IntWritable(0);
	public static final IntWritable DEPARTURE_DELAY = new IntWritable(1);
	public static final IntWritable DEPARTURE_ONTIME = new IntWritable(2);
	public static final IntWritable ARRIVAL_DELAY = new IntWritable(3);
	public static final IntWritable ARRIVAL_ONTIME = new IntWritable(4);
	public static final IntWritable CANDELLED = new IntWritable(5);
	public static final IntWritable DIVERTED = new IntWritable(6);
	
	// clef pour la map
	public static final IntWritable TYPE = new IntWritable(0);
	public static final IntWritable VALUE = new IntWritable(1);
	
	
	public static class MyMapper extends Mapper<LongWritable, Text, Text, MapWritable> {

		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, Text, MapWritable>.Context context)
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
				context.write(new Text(month), getMapWritable(FLIGHT, new IntWritable(1)));
				if (isCancelled) 
					context.write(new Text(month), getMapWritable(CANDELLED, new IntWritable(1)));
				else if (isDiverted)
					context.write(new Text(month), getMapWritable(DIVERTED, new IntWritable(1)));
				else {
					if (delayArrival >= 10)
						context.write(new Text(month), getMapWritable(ARRIVAL_DELAY, new IntWritable(1)));
					else
						context.write(new Text(month), getMapWritable(ARRIVAL_ONTIME, new IntWritable(1)));
					
					if (delayDeparture >= 10)
						context.write(new Text(month), getMapWritable(DEPARTURE_DELAY, new IntWritable(1)));
					else
						context.write(new Text(month), getMapWritable(DEPARTURE_ONTIME, new IntWritable(1)));
				}
			}
		}
		
		// { type : 0, value : 1}  -> compter 1 vol
		private MapWritable getMapWritable(IntWritable type, IntWritable value) {
			MapWritable map = new MapWritable();
			map.put(TYPE, type);
			map.put(VALUE, value);
			return map;
		}
	}
	
	// 
	public static class MyCombiner extends Reducer<Text, MapWritable, Text, MapWritable> {

		@Override
		protected void reduce(Text month, Iterable<MapWritable> codes,
				Reducer<Text, MapWritable, Text, MapWritable>.Context context)
				throws IOException, InterruptedException {
			
			int  totalFlight = 0;
			int  totalCancelled = 0;
			int  totalDiverted = 0;
			int  totalDepartureOnTime = 0;
			int  totalArrivalOnTime = 0;
			int  totalDepartureDelay = 0;
			int  totalArrivalDelay = 0;
			
			
			for (MapWritable code : codes) {
				IntWritable type = (IntWritable)code.get(TYPE);
				IntWritable value = (IntWritable)code.get(VALUE);
				
				if (type.equals(FLIGHT))
					totalFlight += value.get();
				else if (type.equals(CANDELLED))
					totalCancelled += value.get();
				else if (type.equals(DIVERTED))
					totalDiverted += value.get();
				else if (type.equals(DEPARTURE_ONTIME))
					totalDepartureOnTime += value.get();
				else if (type.equals(DEPARTURE_DELAY))
					totalDepartureDelay += value.get();
				else if (type.equals(ARRIVAL_ONTIME))
					totalArrivalOnTime += value.get();
				else if (type.equals(ARRIVAL_DELAY))
					totalArrivalDelay += value.get();
			}
		
			context.write(new Text(month.toString()), getMapWritable(FLIGHT, new IntWritable(totalFlight)));
			context.write(new Text(month.toString()), getMapWritable(CANDELLED, new IntWritable(totalCancelled)));
			context.write(new Text(month.toString()), getMapWritable(DIVERTED, new IntWritable(totalDiverted)));
			context.write(new Text(month.toString()), getMapWritable(DEPARTURE_ONTIME, new IntWritable(totalDepartureOnTime)));
			context.write(new Text(month.toString()), getMapWritable(DEPARTURE_DELAY, new IntWritable(totalDepartureDelay)));
			context.write(new Text(month.toString()), getMapWritable(ARRIVAL_ONTIME, new IntWritable(totalArrivalOnTime)));
			context.write(new Text(month.toString()), getMapWritable(ARRIVAL_DELAY, new IntWritable(totalArrivalDelay)));
		}
		// { type : 0, value : 1}  -> compter 1 vol
		private MapWritable getMapWritable(IntWritable type, IntWritable value) {
			MapWritable map = new MapWritable();
			map.put(TYPE, type);
			map.put(VALUE, value);
			return map;
		}
	}
	
	// 
	public static class MyReducer extends Reducer<Text, MapWritable, NullWritable, Text> {

		@Override
		protected void reduce(Text month, Iterable<MapWritable> codes,
				Reducer<Text, MapWritable, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			
			double  totalFlight = 0;
			double  totalCancelled = 0;
			double  totalDiverted = 0;
			double  totalDepartureOnTime = 0;
			double  totalArrivalOnTime = 0;
			double  totalDepartureDelay = 0;
			double  totalArrivalDelay = 0;
			
			
			for (MapWritable code : codes) {
				IntWritable type = (IntWritable)code.get(TYPE);
				IntWritable value = (IntWritable)code.get(VALUE);
				
				if (type.equals(FLIGHT))
					totalFlight += value.get();
				else if (type.equals(CANDELLED))
					totalCancelled += value.get();
				else if (type.equals(DIVERTED))
					totalDiverted += value.get();
				else if (type.equals(DEPARTURE_ONTIME))
					totalDepartureOnTime += value.get();
				else if (type.equals(DEPARTURE_DELAY))
					totalDepartureDelay += value.get();
				else if (type.equals(ARRIVAL_ONTIME))
					totalArrivalOnTime += value.get();
				else if (type.equals(ARRIVAL_DELAY))
					totalArrivalDelay += value.get();
			}
			
			StringBuilder sb = new StringBuilder(month.toString());
			DecimalFormat df = new DecimalFormat("0.0000");
			
			sb.append(',').append(totalFlight);
			sb.append(',').append(df.format(totalCancelled/totalFlight));
			sb.append(',').append(df.format(totalDiverted/totalFlight));
			sb.append(',').append(df.format(totalDepartureOnTime/totalFlight));
			sb.append(',').append(df.format(totalDepartureDelay/totalFlight));
			sb.append(',').append(df.format(totalArrivalOnTime/totalFlight));
			sb.append(',').append(df.format(totalArrivalDelay/totalFlight));
			
			context.write(NullWritable.get(), new Text(sb.toString()));
		}
		
	}
	
    public static void main( String[] args ) throws Exception
    {
    	Configuration conf = new Configuration();
    	ToolRunner.run(new SelectAggregationCombinerMRjob(), args);
    }

    // methode de Tool
    // cette méthode démarrera le job
	@Override
	public int run(String[] args) throws Exception {
		// initialisation du job en récupérant notre configuration
		Job job = Job.getInstance(getConf());
		
		job.setJarByClass(SelectAggregationCombinerMRjob.class);
		
		// formlat fichier en entree/sortie
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		// format clé/valeur en sortie, ici pas de clé d'ou NullWritable
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		// format de sortie du mapper
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(MapWritable.class);
		
		job.setMapperClass(MyMapper.class);
		job.setCombinerClass(MyCombiner.class);
		job.setReducerClass(MyReducer.class);
		
		
		// un reducteur
		job.setNumReduceTasks(1);
		
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
