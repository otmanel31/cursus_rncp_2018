package com.loncoto.AirlineAnalysisForm.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

// Writable -> hadoop pourra le transmettre/stocké entre mapper et reducteur
public class StatsVol implements Writable{

	public static final int NORMAL = 0;
	public static final int CANCELLED = 1;
	public static final int DIVERTED = 2;

	public Text aeroportDepart = new Text();
	public Text codeAeroportDepart = new Text();
	public LongWritable totalVols = new LongWritable();
	public LongWritable totalCancelled = new LongWritable();
	public LongWritable totalDiverted = new LongWritable();
	public DoubleWritable retardDepartMoyen = new DoubleWritable();
	public DoubleWritable retardArriveeMoyen = new DoubleWritable();
	
	// lire depuis le flux (hadoop) un objet InfosVols
	@Override
	public void readFields(DataInput in) throws IOException {
		this.aeroportDepart.readFields(in);
		this.codeAeroportDepart.readFields(in);
		this.totalVols.readFields(in);
		this.totalCancelled.readFields(in);
		this.totalDiverted.readFields(in);
		this.retardDepartMoyen.readFields(in);
		this.retardArriveeMoyen.readFields(in);
	}
	
	// ecrire dans le flux (hadoop) un objet InfosVols
	@Override
	public void write(DataOutput out) throws IOException {
		this.aeroportDepart.write(out);
		this.codeAeroportDepart.write(out);
		this.totalVols.write(out);
		this.totalCancelled.write(out);
		this.totalDiverted.write(out);
		this.retardDepartMoyen.write(out);
		this.retardArriveeMoyen.write(out);
	}
	
	
}
