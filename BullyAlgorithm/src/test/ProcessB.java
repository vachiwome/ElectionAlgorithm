package test;

import core.BullyProcess;

public class ProcessB {
	public static void main(String args[]) {
		BullyProcess bp = new BullyProcess(5, 2015);
		
		bp.addProcess(0, 2014);
		bp.addProcess(10, 2016);
		
	    bp.start();

	    System.out.println("holding an election");
	    bp.holdElection();
	}

}
