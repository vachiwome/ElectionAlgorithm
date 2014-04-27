package core;


import util.Util;
import network.Message;

public class BullyProcess extends ReceivingProcess {
	

	public BullyProcess(int id, int port){
		super(id, port);
		//holdElection();
	}
	
	public void start() {
		talker.start();
		listener.start();		
	}
	
	/**
	 * Check if a coordinator is alive
	 */
	public boolean isCoordinatorAlive() {
		// System.out.println(id + " checking if the coordinator is alive");
		if (coordinator == id) {
			// if you are the coordinator, then you know you are alive
			return true;
		}
		else if (coordinator != -1) {
			// ping the coordinator
			sendPingToPeer(coordinator);
			// wait for its response
			Util.sleep(1000);
			// if the coordinator did not reply with a pong, the its dead
			if (pendingPings.containsKey(coordinator)) {
				System.out.println("Old Coordinator " + coordinator + " died");
				pendingPings.remove(coordinator);
				return false;
			}
			return true;
		}

		coordinator = id;
		return true;
//		else {
//			throw new RuntimeException("Coordinator is -1");
//		}

	}
		
	/**
	 * Create a new thread whose job is to basically listen for messages
	 * from peers.
	 */
	private Thread listener = new Thread () {
		
		public void run() {
			System.out.println("Listener running for " + id);
			while (true) {
				Message m = sockManager.processPacket(sockManager.receivePacket());
				receiveMessage(m);
			}
			
		}
	};
	
	/**
	 * This thread is responsible for talking with the peers
	 */
	private Thread talker = new Thread () {			
		public void run() {	
			System.out.println("Talker running for " + id);
			while (true) {
				if (!isCoordinatorAlive()) {
					holdElection();
				}
				Util.sleep(3000);
			}
			
		}
		
	};

}
