package test;

import core.BullyProcess;

public class ProcessA {

	public static void main(String args[]) {
		BullyProcess bp = new BullyProcess(0, 2014);
		
		bp.addProcess(5, 2015);
		bp.addProcess(10, 2016);

		bp.start();

		bp.holdElection();
		
	}
}
