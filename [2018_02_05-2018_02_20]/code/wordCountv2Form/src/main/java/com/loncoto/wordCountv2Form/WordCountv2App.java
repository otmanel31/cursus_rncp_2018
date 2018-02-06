package com.loncoto.wordCountv2Form;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * Hello world!
 *
 */
public class WordCountv2App 
{
	// en API haddop v2, on hérite d'une classe Mapper directement
	public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		
		@Override
		protected void map(LongWritable key,
							Text value,
							Mapper<LongWritable, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			String mot = value.toString();
			if (mot != null && !mot.trim().isEmpty()) {
				// le context encapsule l'ecrivain et le reporter de l'api V1
				context.write(new Text(mot.trim()), new IntWritable(1));
			}
			
		}
		
	}
	
	// idem pour le reducteur, en api v2 on herite d'une simple classe Reducer
	public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

		@Override
		protected void reduce(Text clef,
							  Iterable<IntWritable> values,
				Reducer<Text, IntWritable, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			context.write(clef, new IntWritable(sum));
		}
	}
	
	
	
    public static void main( String[] args ) throws Exception
    {
    	// on passe maintenant par un objet Job (api v2)
        Job job = Job.getInstance(new Configuration());
        
        // association avec notre classe
        job.setJarByClass(WordCountv2App.class);
        
        // configuration sortie
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        // pas besoin de definir les types entree, il le déduit du mapper
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);
        
        // definir les lecteurs (fichier entree) et ecrivain (fichier sortie)
        // ATTENTION, piege, c'est une classe avec le meme nom qu'en V1, mais pas la même en fait (package différent)
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        
        // attention la aussi a prendre le bon import (pas le même qu'en v1)
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean status = job.waitForCompletion(true);
        if (status) {
        	System.exit(0);
        }
        else {
        	System.exit(1);
        }
        
        
        
    }
}
