package com.loncoto.firstJunitForm;

public class Calculatrice {
	
	public int addition(int x, int y) {
		return x + y;
	}
	
	public int multiplication(int x, int y) {
		return x * y;
	}
	
	public int division(int x, int y) {
		return x / y;
	}

	public double multiplication(double x, double y) {
		return x * y;
	}
	
	// calcul qui prend du temps
	public double calculComplexeEtlent(double x) {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {e.printStackTrace();}
		return x * 2.0;
	}
	
}
