package com.loncoto.AirlineAnalysisForm.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

// Writable -> haddop pourra le transmettre/stocké entre mapper et reducteur
public class InfosVol implements Writable{

	public static final int NORMAL = 0;
	public static final int CANCELLED = 1;
	public static final int DIVERTED = 2;
	
	public IntWritable annee = new IntWritable();
	public IntWritable mois = new IntWritable();
	public IntWritable date = new IntWritable();
	public IntWritable retardDepart = new IntWritable();
	public IntWritable retardArrive = new IntWritable();
	public Text aeroportDepart = new Text();
	public Text aeroportArrive = new Text();
	public Text compagnie = new Text();
	public IntWritable statut = new IntWritable();
	
	// lire depuis le flux (hadoop) un objet InfosVols
	@Override
	public void readFields(DataInput in) throws IOException {
		this.annee.readFields(in);
		this.mois.readFields(in);
		this.date.readFields(in);
		this.retardDepart.readFields(in);
		this.retardArrive.readFields(in);
		this.aeroportDepart.readFields(in);
		this.aeroportArrive.readFields(in);
		this.compagnie.readFields(in);
		this.statut.readFields(in);
	}
	
	// ecrire dans le flux (hadoop) un objet InfosVols
	@Override
	public void write(DataOutput out) throws IOException {
		this.annee.write(out);
		this.mois.write(out);
		this.date.write(out);
		this.retardDepart.write(out);
		this.retardArrive.write(out);
		this.aeroportDepart.write(out);
		this.aeroportArrive.write(out);
		this.compagnie.write(out);
		this.statut.write(out);
		
	}
	
	
}
