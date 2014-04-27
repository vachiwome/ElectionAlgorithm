package core;

import java.util.Map;

import util.Util;
import network.Message;
import network.SocketManager;

public class ReceivingProcess extends SendingProcess {
	
	/**
	 * This is a process that is capable of receiving a message
	 * It can do both sending and receiving, hence it extends SendingProcess
	 */
	public ReceivingProcess(int id, int port) {
		super(id, port);
	}

	/**
	 * Receive a message and process it differently according to
	 * its type
	 */
	public void receiveMessage(Message m) {
		switch(m.getType()) {
			case Message.ELECTION:
				receiveElectionMessage(m);
				break;
			case Message.COORDINATOR:
				receiveCoordinatorMessage(m);
				break;
			case Message.OK:
				receiveOkMessage(m);
				break;
			case Message.PING:
				receivePingMessage(m);
				break;
			case Message.PONG:
				receivePongMessage(m);
				break;
			default:
				throw new RuntimeException("Unknown message type " + m.getType());
		}
	}
	
	/**
	 * Process an election message from your lower id peers
	 */
	public void receiveElectionMessage (Message m) {
		System.out.println("P" + id + " received election message from P" + m.getSourceId());
		if (m.getSourceId() < id) {
			sendOkMessageToPeer(m.getSourceId());
		}
		if (!holdingElection) {
			holdElection();
		}
	}
	
	/**
	 * Receive information from the new coordinator
	 */
	public void receiveCoordinatorMessage(Message m) {
		System.out.println("P" + id + " received coordinator message from P" + m.getSourceId());
		int coord = (Integer) m.getData().get(0);
		this.coordinator = coord;
	}
	
	/**
	 * Process a message when a peer says okay
	 */
	public void receiveOkMessage(Message m) {
		System.out.println("P" + id + " received ok message from P" + m.getSourceId());
		this.isTheBoss = false;
		synchronized (pendingElecResps) {
			if (pendingElecResps.containsKey(m.getSourceId())) {
				pendingElecResps.remove(m.getSourceId());
			}			
		}
		
	}
	
	/**
	 * Receive a ping and send a pong
	 */
	public void receivePingMessage(Message m) {
		//System.out.println("P" + id + " received ping from P" + m.getSourceId());
		sendMessageToPeer(m.getSourceId(), Message.PONG, 0);
	} 
	
	/**
	 * Process a pong message, just take note that the peer is alive
	 */
	public void receivePongMessage(Message m) {
		//System.out.println("P"+id + " received pong from P" + m.getSourceId());
		synchronized (pendingPings) {
			if (pendingPings.containsKey(m.getSourceId())) {
				pendingPings.remove(m.getSourceId());
			}			
		}
	}
	
	/**
	 * Hold a new election
	 */
	public void holdElection() {
		System.out.println("P" + id + " starting an election");
		holdingElection = true;
		sendElectionMessageToPeers();
		Util.sleep(1000);
		//System.out.println(sockets);
		for (Map.Entry<Integer, SocketManager> entry : sockets.entrySet()) {
			if (entry.getKey() > id) {
				synchronized (pendingElecResps) {
					if (!pendingElecResps.containsKey(entry.getKey())) {
						return;
					}					
				}
			}
		}
		System.out.println("P" + id + " set itself as coordinator");
		this.coordinator = id;
		sendCoordinatorMessageToPeers();
		holdingElection = false;
	}

}
