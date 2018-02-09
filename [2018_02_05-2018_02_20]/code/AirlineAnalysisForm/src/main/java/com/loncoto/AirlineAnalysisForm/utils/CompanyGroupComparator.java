package com.loncoto.AirlineAnalysisForm.utils;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class CompanyGroupComparator extends WritableComparator {
	
	public CompanyGroupComparator() {
		super(VolCompagnieClef.class, true);
	}

	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		VolCompagnieClef cle1 = (VolCompagnieClef)a;
		VolCompagnieClef cle2 = (VolCompagnieClef)b;
		// je ne compare que le code compagnie, et surtout pas le type (vol ou nom de compagnie)
		// comme ca, haddoop regroupera ensemble le clef nom de compagnie et vols pour la même compagnie
		// et mon reducteur le recevra en même temps
		return cle1.compagnie_code.compareTo(cle2.compagnie_code);
	}
	
	
}
