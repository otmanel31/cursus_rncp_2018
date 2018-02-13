package com.loncoto.AirlineAnalysisForm.utils;

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MyXmlOutputFormat extends FileOutputFormat<NullWritable, Text> {

	
	public static class MyXMLRecordWriter extends RecordWriter<NullWritable, Text> {

		// flux de sortie (le fichier dans lequel il va ecrire (dans HDFS)
		private DataOutputStream out;
		
		public MyXMLRecordWriter(DataOutputStream out) throws IOException {
			// a la construction de mon writer
			// je recoit le flux ou ecrire, et je commence par ouvrir une balise racine "records"
			this.out = out;
			this.out.writeBytes("<records>");
		}
		
		
		
		@Override
		public void close(TaskAttemptContext arg0) throws IOException,
				InterruptedException {
			// cette méthode est appelée lors de la fin du travail d'un reducteur
			// pour clore son fichier de sortie
			try {
				out.writeBytes("</records>");
			}
			finally {
				out.close();
			}
			
		}

		// cette fonction est appelé pour chaque sortie du reducteur (ou mapper)
		// c.a.d a chaque fois qu'on a un context.write coté map-reduce
		@Override
		public void write(NullWritable cle, Text value) throws IOException,
				InterruptedException {
			out.writeBytes("<record>");
			String[] champs = value.toString().split(",");
			for (String champ : champs) {
				out.writeBytes("<champ>");
				out.writeBytes(champ);
				out.writeBytes("</champ>");
			}
			out.writeBytes("</record>");
		}
	}
	
	
	
	// cette fonction sera appelée par hadoop pour récupérer chaque exemplaire de writer
	// pour chaque reducteur du job
	@Override
	public RecordWriter<NullWritable, Text> getRecordWriter(
			TaskAttemptContext job) throws IOException, InterruptedException {
		// le parametre de type TaskAttemptContext contient toutes les informations
		// sur le job en cours ainsi que des fonctions utilitaires
		
		String extension = ".xml";
		
		// je veux recuperer le chemin (path) du fichier dans lequel je vais devoir travailler dans HDFS
		// ce nom de fichier depend du job et du reducteur
		// exemple "part-r0000.xml"
		Path fichier = getDefaultWorkFile(job, extension);
		// ensuite je récupere un objet FileSystem, qui permet de "manipuler" le systeme de fichier HDFS
		FileSystem fs = fichier.getFileSystem(job.getConfiguration());
		
		// j'ouvre un flux vers le fichier hdfs
		FSDataOutputStream sortie = fs.create(fichier, false);
		
		// je renvoie le recordWriter correctement configuré avec le flux de sortie sur le bon fichier dans
		// HDFS
		return new MyXMLRecordWriter(sortie);
	}
	
	

}
