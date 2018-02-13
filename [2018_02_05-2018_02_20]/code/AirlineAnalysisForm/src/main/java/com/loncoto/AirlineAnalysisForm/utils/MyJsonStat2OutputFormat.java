package com.loncoto.AirlineAnalysisForm.utils;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MyJsonStat2OutputFormat extends FileOutputFormat<NullWritable, StatsVol> {

	
	public static class MyJsonStat2RecordWriter extends RecordWriter<NullWritable, StatsVol> {

		// flux de sortie (le fichier dans lequel il va ecrire (dans HDFS)
		private DataOutputStream out;
		private JsonGenerator jsonGenerator;
		
		public MyJsonStat2RecordWriter(DataOutputStream out) throws IOException {
			// a la construction de mon writer
			// je recoit le flux ou ecrire, et je commence par ouvrir une balise racine "records"
			this.out = out;
			// je creer un jsonGenerator sur mon flux de sortie
			this.jsonGenerator = Json.createGenerator(this.out);
			// ouvre un tableau json
			this.jsonGenerator.writeStartArray();
		}
		
		
		
		@Override
		public void close(TaskAttemptContext arg0) throws IOException,
				InterruptedException {
			// cette méthode est appelée lors de la fin du travail d'un reducteur
			// pour clore son fichier de sortie
			try {
				this.jsonGenerator.writeEnd();
				this.jsonGenerator.close();
			}
			finally {
				out.close();
			}
			
		}

		// cette fonction est appelé pour chaque sortie du reducteur (ou mapper)
		// c.a.d a chaque fois qu'on a un context.write coté map-reduce
		@Override
		public void write(NullWritable cle, StatsVol value) throws IOException,
				InterruptedException {
			String[] champs = value.toString().split(",");
			this.jsonGenerator.writeStartObject();
				this.jsonGenerator.write("codeAeroport", value.codeAeroportDepart.toString());
				this.jsonGenerator.write("nomAeroport", value.aeroportDepart.toString());
				this.jsonGenerator.write("totalVols", value.totalVols.get());
				this.jsonGenerator.write("retardMoyenDepart", value.retardDepartMoyen.get());
				this.jsonGenerator.write("retardMoyenArrivee", value.retardArriveeMoyen.get());
			this.jsonGenerator.writeEnd();
		}
	}
	
	
	
	// cette fonction sera appelée par hadoop pour récupérer chaque exemplaire de writer
	// pour chaque reducteur du job
	@Override
	public RecordWriter<NullWritable, StatsVol> getRecordWriter(
			TaskAttemptContext job) throws IOException, InterruptedException {
		// le parametre de type TaskAttemptContext contient toutes les informations
		// sur le job en cours ainsi que des fonctions utilitaires
		
		String extension = ".json";
		
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
		return new MyJsonStat2RecordWriter(sortie);
	}
	
	

}
