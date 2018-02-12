package com.loncoto.AirlineAnalysisForm.utils;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class AeroportGroupComparator extends WritableComparator {
	
	public AeroportGroupComparator() {
		super(VolAeroportClef.class, true);
	}

	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		VolAeroportClef cle1 = (VolAeroportClef)a;
		VolAeroportClef cle2 = (VolAeroportClef)b;
		return cle1.aeroport_code.compareTo(cle2.aeroport_code);
	}
	
	
}
