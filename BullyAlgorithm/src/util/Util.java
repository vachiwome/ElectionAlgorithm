package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Util {

	public static byte[] serialize(Object obj) {
		try {
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    ObjectOutputStream os = new ObjectOutputStream(out);
		    os.writeObject(obj);
		    return out.toByteArray();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	/**
	 * Deserialize a byte array into a message object
	 */
	public static Object deserialize(byte[] data) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			ObjectInputStream is = new ObjectInputStream(in);
			return is.readObject();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		
	}


}
