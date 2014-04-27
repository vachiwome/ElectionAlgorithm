package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import network.Message;
import network.SocketManager;

public class SendingProcess extends BasicProcess {

	/**
	 * This is a process that is capable of communicating
	 * with other processes in the cluster. It can send messages
	 * to other peers. 
	 */
	public SendingProcess(int id, int port) {
		super(id, port);
	}

	/**
	 * Send a specific message to your peer
	 */
	public void sendMessageToPeer(int peerId, int type, int data) {
		SocketManager sock = sockets.get(peerId);
		Message m = new Message(id, type);
		List<Object> input = new ArrayList<Object>();
		input.add(data);
		m.setData(input);	
		sock.sendMessage(m, sock.getAddr(),sock.getPort());
	}
	
	/**
	 * Method for sending an election message to a peer
	 */
	public void sendElectionMessageToPeer(int peerId) {
		System.out.println("P" + id + " sending election message to P" + peerId);
		sendMessageToPeer(peerId, Message.ELECTION, 0);
	}
	
	/**
	 * Send an OK message to a peer
	 */
	public void sendOkMessageToPeer(int peerId) {
		sendMessageToPeer(peerId, Message.OK, 0);
	}
	
	/**
	 * Tell a peer that you are now the new coordinator
	 */
	public void sendCoordinatorMessageToPeer(int peerId) {
		sendMessageToPeer(peerId, Message.COORDINATOR, id);
	}
	
	/**
	 * Ping a peer to see if they are still alive
	 */
	public void sendPingToPeer(int peerId) {
		sendMessageToPeer(peerId, Message.PING, 0);
		synchronized (pendingPings) {
			pendingPings.put(peerId, true);			
		}
	}

	/**
	 * Tell all your peers that you are now the coordinator
	 */
	public void sendCoordinatorMessageToPeers() {
		for (Map.Entry<Integer, SocketManager> entry : sockets.entrySet()) {
			sendCoordinatorMessageToPeer(entry.getKey());
		}
		
	}
	/**
	 * Hold an election by asking your elders for the coordinator
	 */
	public void sendElectionMessageToPeers() {
		for (Map.Entry<Integer, SocketManager> entry : sockets.entrySet()) {
			if (entry.getKey() > id){
				sendElectionMessageToPeer(entry.getKey());
				synchronized (pendingElecResps) {
					pendingElecResps.put(entry.getKey(), false);					
				}
			}
		}
	}
}
