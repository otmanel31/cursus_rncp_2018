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
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.loncoto.AirlineAnalysisForm.utils.AirlineDataUtils;
import com.loncoto.AirlineAnalysisForm.utils.CompagnieCodePartitioner;
import com.loncoto.AirlineAnalysisForm.utils.CompanyGroupComparator;
import com.loncoto.AirlineAnalysisForm.utils.CompanySortComparator;
import com.loncoto.AirlineAnalysisForm.utils.InfosVol;
import com.loncoto.AirlineAnalysisForm.utils.VolCompagnieClef;

/**
 * Hello world!
 *
 */
public class SelectJoinMRjob extends Configured implements Tool
{

	// mapper qui s'occupe des fichiers contenant les vols
	public static class VolMapper extends Mapper<LongWritable, Text, VolCompagnieClef, Text> {

		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, VolCompagnieClef, Text>.Context context)
				throws IOException, InterruptedException {
			// sauter la ligne d'en-tête
			if (!AirlineDataUtils.isHeader(value)) {
				// informations du vol
				InfosVol infos = AirlineDataUtils.parseInfosVolsDelayFromText(value);
				// j'indique que j'envoie au reducteur un enregistrement type vol pour
				// telle compagnie aerienne (carrier)
				VolCompagnieClef clef = 
						new VolCompagnieClef(VolCompagnieClef.TYPE_VOL, infos.compagnie.toString());
				// ecriture vers le reducteur
				context.write(clef, AirlineDataUtils.infosVolToText(infos));
			}
		}
	}
	
	// mapper qui s'occupe des fichiers contenant les vols
	public static class CompagnieMapper extends Mapper<LongWritable, Text, VolCompagnieClef, Text> {

		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, VolCompagnieClef, Text>.Context context)
				throws IOException, InterruptedException {
			
				String[] detailsCompagnie = AirlineDataUtils.parseCompanyDetails(value);
				// je genere la clef pour le reducteur
				VolCompagnieClef clef = 
						new VolCompagnieClef(VolCompagnieClef.TYPE_COMPAGNIE, detailsCompagnie[0].trim());
				Text infocompagnie = new Text(detailsCompagnie[1]);
				context.write(clef, infocompagnie);
		}
	}
		
	
	

	public static class MyReducer extends Reducer<VolCompagnieClef, Text, NullWritable, Text> {

		private String compagnieCourante = "inconnue";
		
		@Override
		protected void reduce(VolCompagnieClef clef, Iterable<Text> infos,
				Reducer<VolCompagnieClef, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			
			
			double  totalFlight = 0;
			double  totalCancelled = 0;
			double  totalDiverted = 0;
			/*double  totalDepartureOnTime = 0;
			double  totalArrivalOnTime = 0;
			double  totalDepartureDelay = 0;
			double  totalArrivalDelay = 0;
			*/
			
			for (Text info : infos) {
				
				if (clef.type_record.get() == VolCompagnieClef.TYPE_COMPAGNIE) {
					// j'ai recu un nom de compagnie
					this.compagnieCourante = info.toString();
				}
				else {
					totalFlight++;
					InfosVol vol = AirlineDataUtils.TextToVols(info);
					if (vol.statut.get() == InfosVol.CANCELLED)
						totalCancelled++;
					else if (vol.statut.get() == InfosVol.DIVERTED)
						totalDiverted++;
				}
			}
			
			if (totalFlight > 0) {
				StringBuilder sb = new StringBuilder(this.compagnieCourante);
				DecimalFormat df = new DecimalFormat("0.0000");
				
				sb.append(',').append(totalFlight);
				sb.append(',').append(df.format(totalCancelled/totalFlight));
				sb.append(',').append(df.format(totalDiverted/totalFlight));
				
				context.write(NullWritable.get(), new Text(sb.toString()));
			}
		}
		
	}
	
    public static void main( String[] args ) throws Exception
    {
    	Configuration conf = new Configuration();
    	ToolRunner.run(new SelectJoinMRjob(), args);
    }

    // methode de Tool
    // cette méthode démarrera le job
	@Override
	public int run(String[] args) throws Exception {
		// initialisation du job en récupérant notre configuration
		Job job = Job.getInstance(getConf());
		
		job.setJarByClass(SelectJoinMRjob.class);
		
		// formlat fichier en entree/sortie
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		// format clé/valeur en sortie, ici pas de clé d'ou NullWritable
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		// format de sortie du mapper
		job.setMapOutputKeyClass(VolCompagnieClef.class);
		job.setMapOutputValueClass(Text.class);
		
		//job.setMapperClass(MyMapper.class);
		// cette classe permet de mettre dans la configuration les arguments standard connus par hadoop
		// automatiquement, en nous renvoyant ensuite les autres arguments restants
		String[] arguments = new GenericOptionsParser(getConf(), args).getRemainingArgs();
		
		// volMapper s'occupe des fichier dans ce repertoire
		MultipleInputs.addInputPath(job, new Path(arguments[0]), TextInputFormat.class, VolMapper.class);
		// CompagnieMapper s'occupe des fichier dans le deuxieme repertoire
		MultipleInputs.addInputPath(job, new Path(arguments[1]), TextInputFormat.class, CompagnieMapper.class);
		
		FileOutputFormat.setOutputPath(job, new Path(arguments[2]));
		
		job.setSortComparatorClass(CompanySortComparator.class);
		job.setGroupingComparatorClass(CompanyGroupComparator.class);
		job.setPartitionerClass(CompagnieCodePartitioner.class);
		
		
		job.setReducerClass(MyReducer.class);
		// un reducteur
		job.setNumReduceTasks(3);
		
		
		// true --> verbose
		boolean status = job.waitForCompletion(true);
		if (status)
			return 0;
		else
			return 1;
	}
}
