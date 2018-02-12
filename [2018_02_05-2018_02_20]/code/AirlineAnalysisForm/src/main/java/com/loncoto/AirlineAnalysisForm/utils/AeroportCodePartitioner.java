package com.loncoto.AirlineAnalysisForm.utils;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class AeroportCodePartitioner extends Partitioner<VolAeroportClef, Text> 
{

	// hadoop appelera la methode getPartition de notre partitioner pour décider
	// vers quel reducteur envoyer la donnée
	@Override
	public int getPartition(VolAeroportClef clef, Text valeur, int nb_partitions) {
		
		return (clef.aeroport_code.hashCode() & Integer.MAX_VALUE) % nb_partitions;
	}
	
}
