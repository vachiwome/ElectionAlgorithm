package test;

import core.BullyProcess;

public class ProcessC {
	public static void main(String args[]) {
		BullyProcess bp = new BullyProcess(10, 2016);
		
		bp.addProcess(0, 2014);
		bp.addProcess(5, 2015);
		
		bp.start();
		
		bp.holdElection();
	}

}
