package com.loncoto.helloHadoopForm;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

/**
 * Hello Hadoop
 *
 * compte les mots
 * mapper -> ligne(1 mot) -> (mot, 1)
 * reducteur (mot, List<Valeur>) -> (mot, total)
 * le mapper est une classe qui etend la classe MapReduceBase (en hadoop v1)
 * 
 */
public class WordCountApp 
{
	public static class MyMapper extends MapReduceBase 
								implements Mapper<LongWritable, Text, Text, IntWritable> {

		@Override
		public void map(LongWritable noLigne,
						Text contenuLigne,
						OutputCollector<Text, IntWritable> sortie,
						Reporter reporter)
				throws IOException {
			// recuperation du texte de la ligne
			String ligne = contenuLigne.toString();
			if (ligne != null  && !ligne.trim().isEmpty()) {
				// un mot a transmettre
				sortie.collect(new Text(ligne), new IntWritable(1));
				// ('mot', 1)
			}
		}
		
	}
	
	public static class MyReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

		@Override
		public void reduce(Text mot,
				Iterator<IntWritable> values,
				OutputCollector<Text, IntWritable> sortie, Reporter reporter)
				throws IOException {
			int sum = 0;
			// j'additionne tous les même mots rencontrés 
			while (values.hasNext()) {
				sum += values.next().get();
			}
			// ecriture ('mot' , nb occurence)
			sortie.collect(mot, new IntWritable(sum));
		}
		
	}
	
	
	
    public static void main( String[] args ) throws IOException
    {
    	// objet configuration de la tache hadoop
    	// c'est via cet objet que nous parametrons la tache (hadoop v1)
       JobConf configuration = new JobConf(WordCountApp.class);
       
       configuration.setJobName("wordcount");
       
       // quelle est le format de sortie de la tache
       configuration.setOutputKeyClass(Text.class);
       configuration.setOutputValueClass(IntWritable.class);
       
       // quel mapper utiliser
       configuration.setMapperClass(MyMapper.class);
       
       // quel combiner utiliser
       //configuration.setCombinerClass(MyReducer.class);
       
       // quel reducteur utiliser
       configuration.setReducerClass(MyReducer.class);
       
       // combien de reducteur utiliser
       configuration.setNumReduceTasks(1);
       
       // format de fichier d'entree
       // il lira ligne par ligne un fichier texte et envera chaque ligne individuelle
       // au mapper
       configuration.setInputFormat(TextInputFormat.class);
       // format de fichier de sortie
       configuration.setOutputFormat(TextOutputFormat.class);
       
       // le chemin ou lire les fichier en entree (chemin dans HDFS)
       FileInputFormat.setInputPaths(configuration, new Path(args[0]));
       
    // le chemin ou ecrire le resultat (chemin dans HDFS)
       FileOutputFormat.setOutputPath(configuration, new Path(args[1]));
       
       // lancer le job
       JobClient.runJob(configuration);
       
       
    }
}
