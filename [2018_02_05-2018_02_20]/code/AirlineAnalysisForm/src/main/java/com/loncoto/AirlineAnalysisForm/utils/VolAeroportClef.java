package com.loncoto.AirlineAnalysisForm.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class VolAeroportClef implements WritableComparable<VolAeroportClef>
{
	public static final int TYPE_VOL = 1;
	public static final int TYPE_AEROPORT = 0;
	
	public IntWritable type_record = new IntWritable();
	public Text aeroport_code = new Text();
	
	public VolAeroportClef() {}
	
	public VolAeroportClef(int type_record, String aeroport_code) {
		super();
		this.type_record.set(type_record);
		this.aeroport_code.set(aeroport_code);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.type_record.readFields(in);
		this.aeroport_code.readFields(in);
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		this.type_record.write(out);
		this.aeroport_code.write(out);
		
	}

	@Override
	public int compareTo(VolAeroportClef o) {
		int comparaison = this.aeroport_code.compareTo(o.aeroport_code);
		if (comparaison != 0) return comparaison;
		return this.type_record.compareTo(o.type_record);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((aeroport_code == null) ? 0 : aeroport_code.hashCode());
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
		VolAeroportClef other = (VolAeroportClef) obj;
		if (aeroport_code == null) {
			if (other.aeroport_code != null)
				return false;
		} else if (!aeroport_code.equals(other.aeroport_code))
			return false;
		if (type_record == null) {
			if (other.type_record != null)
				return false;
		} else if (!type_record.equals(other.type_record))
			return false;
		return true;
	}
	
	
	
	
}
