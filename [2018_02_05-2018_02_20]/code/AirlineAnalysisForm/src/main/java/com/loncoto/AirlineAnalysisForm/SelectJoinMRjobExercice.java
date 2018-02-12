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

import com.loncoto.AirlineAnalysisForm.utils.AeroportCodePartitioner;
import com.loncoto.AirlineAnalysisForm.utils.AeroportGroupComparator;
import com.loncoto.AirlineAnalysisForm.utils.AeroportSortComparator;
import com.loncoto.AirlineAnalysisForm.utils.AirlineDataUtils;
import com.loncoto.AirlineAnalysisForm.utils.CompagnieCodePartitioner;
import com.loncoto.AirlineAnalysisForm.utils.CompanyGroupComparator;
import com.loncoto.AirlineAnalysisForm.utils.CompanySortComparator;
import com.loncoto.AirlineAnalysisForm.utils.InfosVol;
import com.loncoto.AirlineAnalysisForm.utils.VolAeroportClef;
import com.loncoto.AirlineAnalysisForm.utils.VolCompagnieClef;

/**
 * Hello world!
 *
 */
public class SelectJoinMRjobExercice extends Configured implements Tool
{

	// mapper qui s'occupe des fichiers contenant les vols
	public static class VolMapper extends Mapper<LongWritable, Text, VolAeroportClef, Text> {

		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, VolAeroportClef, Text>.Context context)
				throws IOException, InterruptedException {
			// sauter la ligne d'en-tête
			if (!AirlineDataUtils.isHeader(value)) {
				// informations du vol
				InfosVol infos = AirlineDataUtils.parseInfosVolsDelayFromText(value);
				if (infos.statut.get() == infos.NORMAL) {
					// j'indique que j'envoie au reducteur un enregistrement type vol pour
					// telle compagnie aerienne (carrier)
					VolAeroportClef clef = 
							new VolAeroportClef(VolAeroportClef.TYPE_VOL, infos.aeroportDepart.toString());
					// ecriture vers le reducteur
					context.write(clef, AirlineDataUtils.infosVolToText(infos));
				}
			}
		}
	}
	
	// mapper qui s'occupe des fichiers contenant les vols
	public static class AeroportMapper extends Mapper<LongWritable, Text, VolAeroportClef, Text> {

		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, VolAeroportClef, Text>.Context context)
				throws IOException, InterruptedException {
			
				if (!AirlineDataUtils.isHeaderAitport(value)) {
					String[] detailsAeroport = AirlineDataUtils.parseAeroportDetails(value);
					VolAeroportClef clef = new VolAeroportClef(VolAeroportClef.TYPE_AEROPORT, detailsAeroport[0]);
					Text infoAeroport = new Text(detailsAeroport[1] + 
												"," + detailsAeroport[5] +
												"," + detailsAeroport[6]);
					context.write(clef, infoAeroport);
				}
		}
	}


	public static class MyReducer extends Reducer<VolAeroportClef, Text, NullWritable, Text> {

		private String AeroportCourant = "mars one,0,0";
		
		@Override
		protected void reduce(VolAeroportClef clef, Iterable<Text> infos,
				Reducer<VolAeroportClef, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			
			
			double  totalFlight = 0;
			double  totalDepartureDelay = 0;
			double  totalArrivalDelay = 0;
			
			for (Text info : infos) {
				
				if (clef.type_record.get() == VolAeroportClef.TYPE_AEROPORT) {
					// j'ai recu un nom de compagnie
					this.AeroportCourant = info.toString();
				}
				else {
					totalFlight++;
					InfosVol vol = AirlineDataUtils.TextToVols(info);
					totalDepartureDelay += (vol.retardDepart.get() > 0) ? vol.retardDepart.get() : 0;
					totalArrivalDelay += (vol.retardArrive.get() > 0) ? vol.retardArrive.get() : 0;
				}
			}
			
			if (totalFlight > 0) {
				StringBuilder sb = new StringBuilder(this.AeroportCourant);
				DecimalFormat df = new DecimalFormat("0.0000");
				
				sb.append(',').append(totalFlight);
				sb.append(',').append(df.format(totalDepartureDelay/totalFlight));
				sb.append(',').append(df.format(totalArrivalDelay/totalFlight));
				
				context.write(NullWritable.get(), new Text(sb.toString()));
			}
			this.AeroportCourant = "mars one,0,0";
		}
		
	}
	
    public static void main( String[] args ) throws Exception
    {
    	Configuration conf = new Configuration();
    	ToolRunner.run(new SelectJoinMRjobExercice(), args);
    }

    // methode de Tool
    // cette méthode démarrera le job
	@Override
	public int run(String[] args) throws Exception {
		// initialisation du job en récupérant notre configuration
		Job job = Job.getInstance(getConf());
		
		job.setJarByClass(SelectJoinMRjobExercice.class);
		
		// formlat fichier en entree/sortie
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		// format clé/valeur en sortie, ici pas de clé d'ou NullWritable
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		// format de sortie du mapper
		job.setMapOutputKeyClass(VolAeroportClef.class);
		job.setMapOutputValueClass(Text.class);
		
		// cette classe permet de mettre dans la configuration les arguments standard connus par hadoop
		// automatiquement, en nous renvoyant ensuite les autres arguments restants
		String[] arguments = new GenericOptionsParser(getConf(), args).getRemainingArgs();
		
		// volMapper s'occupe des fichier dans ce repertoire
		MultipleInputs.addInputPath(job, new Path(arguments[0]), TextInputFormat.class, VolMapper.class);
		// CompagnieMapper s'occupe des fichier dans le deuxieme repertoire
		MultipleInputs.addInputPath(job, new Path(arguments[1]), TextInputFormat.class, AeroportMapper.class);
		
		FileOutputFormat.setOutputPath(job, new Path(arguments[2]));
		
		job.setSortComparatorClass(AeroportSortComparator.class);
		job.setGroupingComparatorClass(AeroportGroupComparator.class);
		job.setPartitionerClass(AeroportCodePartitioner.class);
		
		
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
