package com.loncoto.AirlineAnalysisForm.utils;

import org.apache.hadoop.io.WritableComparator;

// c'est un comparateur hadoop, qui vas nous permettre
// de dire a hadoop comment trier les donnée entre le mapper
// et le reducteur
// ici en l'occurence, on lui dit de comparer "classiquement" les volCompagnieClef
public class AeroportSortComparator extends WritableComparator
{
	public AeroportSortComparator() {
		super(VolAeroportClef.class, true);
	}
}

