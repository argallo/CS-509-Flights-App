package com.csanon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class OffsetLatLong implements Serializable {
	/**
	 * 
	 */

	public class OffsetLatLongHolder implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final double lat;
		private final double lon;
		private int offset;
		
		OffsetLatLongHolder(double aLat, double aLon, int aOffset) {
			lat = aLat;
			lon = aLon;
			offset = aOffset;
		}
		
		public boolean doesMatch(double aLat, double aLon) {
			return (lat == aLat) && (lon == aLon);
		}
		
		public int getOffset() {
			return offset;
		}
		
		public int setOffset(int aOffset) {
			offset = aOffset;
			return offset;
		}
	}
	
	private static final long serialVersionUID = 1L;
	private static final OffsetLatLong INSTANCE = new OffsetLatLong();
	private static final String FILENAME = "OffsetLatLong.ser";
	private final List<OffsetLatLongHolder> offsets;
	
	
	private OffsetLatLong() {
		File f = new File(FILENAME);
		if(!f.exists()) { 
			offsets = new LinkedList<OffsetLatLongHolder>();
			write();
		} else {
			OffsetLatLong result = null;
			try {
				// read object from file
				FileInputStream fis = new FileInputStream(FILENAME);
				ObjectInputStream ois = new ObjectInputStream(fis);
				result = (OffsetLatLong) ois.readObject();
				ois.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (result == null) {
				offsets = new LinkedList<OffsetLatLongHolder>();
			} else {
				offsets = result.offsets;
			}
		}
	}
	
	public static OffsetLatLong getInstance() {
		return INSTANCE;
	}
	
	public OffsetLatLongHolder getOffset(double aLat, double aLon) {
		
		for (OffsetLatLongHolder ollh : offsets) {
			if(ollh.doesMatch(aLat, aLon)) {
				return ollh;
			}
		}
		
		return null;
	}
	
	public OffsetLatLongHolder setOffset(double aLat, double aLon, int aOffset) {
		OffsetLatLongHolder ollh = getOffset(aLat, aLon);
		if (ollh == null) {
			ollh = new OffsetLatLongHolder(aLat, aLon, aOffset);
			offsets.add(ollh);
		} else {
			ollh.setOffset(aOffset);
		}
		
		write();
		
		return ollh;
	}
	
	private void write() {
		
		try {
			FileOutputStream fos;
			fos = new FileOutputStream(FILENAME);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clear() {
		try {
			Files.deleteIfExists(Paths.get(FILENAME));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
