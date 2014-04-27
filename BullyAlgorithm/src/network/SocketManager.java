/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */

package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import util.Util;

public class SocketManager {

	public static final int MESSAGE_SIZE = 1024;
	private DatagramSocket socket;
	private int port;
	private InetAddress addr; 
	
	public SocketManager(int port) {
		try {
			socket = new DatagramSocket();
			addr = InetAddress.getByName("localhost");
			this.port = port;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getAddr() {
		return addr;
	}

	public void setAddr(InetAddress addr) {
		this.addr = addr;
	}
	
	/**
	 * method for send message m to address addr at port port
	 * It the method that is used by processes when they talk
	 * to each other.
	 */
	public void sendMessage(Message m, InetAddress addr, int port) {
		byte[] buff = Util.serialize(m);
        DatagramPacket packet = new DatagramPacket(buff, buff.length, addr, port);
        sendPacket(packet);
	}
	
	/**
	 * Send an already well formed datagram packet
	 */
	public void sendPacket(DatagramPacket packet) {
		try {
			//System.out.println("Sending packet to port " + packet.getPort());
			socket.send(packet);
			//System.out.println("Packet sent..");
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Process a datagram packet and convert it into
	 * a message.
	 */
	public Message processPacket(DatagramPacket packet) {
		int sourcePort = packet.getPort();
		InetAddress sourceAddr = packet.getAddress();
		Message m = (Message) Util.deserialize(packet.getData());
		//System.out.println("processing : " + m.getData());
		m.setSourceAddr(sourceAddr);
		m.setSourcePort(sourcePort);
		return m;
	}
	
	/**
	 * This method waits for incoming methods from clients.
	 */
	public DatagramPacket receivePacket() {
		byte[] buff = new byte[MESSAGE_SIZE];
		DatagramPacket packet = new DatagramPacket(buff, buff.length);
        try {
        	socket.receive(packet);
        	return packet;
        }
        catch (IOException ex) {
        	ex.printStackTrace();
        	return null;
        }
	}
}
