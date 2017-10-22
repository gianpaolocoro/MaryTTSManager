package org.unirender.tts.mary;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.util.Scanner;

public class BlueToothAudio {

	public Process process;

	public BlueToothAudio() {

	}

	public static boolean checkSpeakersConnected() throws Exception{
		
		Process p = Runtime.getRuntime().exec("pacmd list-sinks");
	    p.waitFor();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

	    String line = "";
	    StringBuffer sb = new StringBuffer();
	    while ((line = reader.readLine())!= null) {
	    	sb.append(line + "\n");
	    }

	    //System.out.println(sb);
	    if (sb.toString().contains("name: <bluez_sink."))
	    	return true;
	    else
	    	return false;
	}
	
	public static void main(String args[]) throws Exception {
		
		//new BlueToothAudio().checkAudio();
		boolean connected = checkSpeakersConnected();
		System.out.println(connected);
	}

}
