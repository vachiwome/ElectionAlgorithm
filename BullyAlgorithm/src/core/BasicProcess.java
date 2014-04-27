package core;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import network.SocketManager;

public class BasicProcess {

	/**
	 * This is basic process that can participate in an election
	 * id is the identifier of the process
	 * coordinator is the current coordinator of the cluster
	 * holdingElection is true when the process is already participating in an election
	 * isTheBoss is true when this process is the "bully", the one with the highest identifier
	 * sockets store the sockets to other processes in the cluster
	 * pending pings keeps track of any dead processes
	 * pendingElectionResps can be used to know if the current process is actually the "bully"
	 */
	protected int id;
	protected int coordinator = -1;
	protected boolean holdingElection = false;
	protected boolean isTheBoss = false;
	protected SocketManager sockManager;
	protected Map<Integer, SocketManager> sockets = new HashMap<Integer, SocketManager>();
	protected Map<Integer, Boolean> pendingPings = new HashMap<Integer, Boolean>();
	protected Map<Integer, Boolean> pendingElecResps = new HashMap<Integer, Boolean>();
	
	public BasicProcess(int id, int port) {
		this.id = id;
		this.sockManager = new SocketManager(port);
		try {
			this.sockManager.setSocket(new DatagramSocket(port));
		}
		catch(SocketException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *tell this process about some other process together with
	 *the port that the process is listening on
	 */
	
	public void addProcess(int id, int port) {
		sockets.put(id, new SocketManager(port));
	}

	public SocketManager getSockManager() {
		return sockManager;
	}

	public void setSockManager(SocketManager sockManager) {
		this.sockManager = sockManager;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
