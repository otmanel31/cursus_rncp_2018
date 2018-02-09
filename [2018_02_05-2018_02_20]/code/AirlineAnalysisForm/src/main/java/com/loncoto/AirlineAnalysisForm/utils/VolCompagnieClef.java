package com.loncoto.AirlineAnalysisForm.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class VolCompagnieClef implements WritableComparable<VolCompagnieClef>
{

	public static final int TYPE_VOL = 1;
	public static final int TYPE_COMPAGNIE = 0;
	
	public IntWritable type_record = new IntWritable();
	public Text compagnie_code = new Text();
	
	// (type_record = 0,  compagnie_code = "PS") ---> (le nom de la compagnie)
	// (type_record = 1,  compagnie_code = "PS") ---> (le vols de la compagnie)
	
	public VolCompagnieClef() {} 
	
	public VolCompagnieClef(int type_record, String compagnie_code) {
		this.type_record.set(type_record);
		this.compagnie_code.set(compagnie_code);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.type_record.readFields(in);
		this.compagnie_code.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		this.type_record.write(out);
		this.compagnie_code.write(out);
	}

	@Override
	public int compareTo(VolCompagnieClef o) {
		// 0 si egal, - 1 si < , + 1 si >
		int comparaison = this.compagnie_code.compareTo(o.compagnie_code);
		if (comparaison != 0) return comparaison;
		// tri de base par code compagnie (en l'occurence alphabetique
		
		// si jamais les deux clef concerne la même compagnie
		// envoyer d'abord le nom de la compagnie, et ensuite les vols
		// type = TYPE_COMPAGNIE < type = vols
		return this.type_record.compareTo(o.type_record);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((compagnie_code == null) ? 0 : compagnie_code.hashCode());
		result = prime * result
				+ ((type_record == null) ? 0 : type_record.hashCode());
		return result;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VolCompagnieClef other = (VolCompagnieClef) obj;
		if (compagnie_code == null) {
			if (other.compagnie_code != null)
				return false;
		} else if (!compagnie_code.equals(other.compagnie_code))
			return false;
		if (type_record == null) {
			if (other.type_record != null)
				return false;
		} else if (!type_record.equals(other.type_record))
			return false;
		return true;
	}
	
	
	
	
}
