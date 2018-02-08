package com.loncoto.AirlineAnalysisForm;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Hello world!
 *
 */
public class SelectWhereMRjob 
{
	
	
	public static class MyMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			
		}
		
		
	}
	
    public static void main( String[] args )
    {
        
    }
}
