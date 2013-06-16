package me.tatetian;

import java.util.Arrays;

import processing.serial.Serial;

public class TouchEventHandler implements Runnable{
	public static void start(Engine e) {
		String[] portNames = Serial.list();
		String portName = null;
		for(String pn: portNames) {
			if(pn.startsWith("/dev/tty.usbserial-")) {
				portName = pn;
				break;
			}
		}
		
		if(portName != null) {
			System.out.println(portName);
			Serial serial = new Serial(e, portName, 9600);
			new Thread(new TouchEventHandler(e, serial)).start();
		}
		else
			System.err.println("Warning: no serial port found");
	}
	
	private Engine E;
	private Serial serial;
	private char lastKey;
	
	private static char[][] KEYS = {
		//   		 1   	2    3    4    5    6    7    8 
		/*1*/	{ 't', 'm', 't', 0/*'3'*/, 'm', 't',  0 ,  0  },
		/*2*/	{ '8', '9', 'a', 'b', '7', '4', '2', '5' },
		/*3*/	{ 'j', 'z', 'h', 'm', 'm', 'z',  0 ,  0  },
		/*4*/	{ '1', '1', '1', 'c', '0', '6', 0/*'1'*/,  0  }
	};
	
	private TouchEventHandler(Engine e, Serial s) {
		this.E = e;
		this.serial = s;
		this.lastKey = 0;
	}

	private int seq = 0;
	
	public void run() {		
		byte[] touchInfo = new byte[4];
		while(true) {
			serial.write(128);
			serial.readBytes(touchInfo);		
			char key = 0;
			int i = 0, b = 0;
			for(; i < 4; i++) {
				int bits = touchInfo[i];
				for(b = 0; b < 7; b++) {
					int bit = (bits >> b) & 1;
					if(bit == 0) continue;
					
					key = KEYS[i][b];
					if(key > 0) break;
					//System.out.println((seq++) + ": key = " + key + ", pos = " + (i+1) + "-" + (b + 1));
				}
				if(key > 0) break;
			}
			if(key > 0 && key != lastKey) {
				System.out.println((seq++) + ": key = " + key + ", pos = " + (i+1) + "-" + (b + 1));
				E.queueKeyEvent(key);
			}
			lastKey = key;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
