package org.unirender.tts.mary;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.util.Scanner;

public class MaryServer implements Runnable{

	
	public Process process ;
	public String maryPath ;
	
	
	public MaryServer(String maryPath){
		this.maryPath = maryPath;
	}
	

	
	public void shutdownServer() throws Exception {
		System.out.println("Cleaning the TTS environment");
		//String killPreviousServers = "sudo pkill -f marytts-server sudo pkill -f marytts-5.2";
		String killPreviousServers = "sudo pkill -f marytts-server";
		exec(killPreviousServers);
		String killOtherInstances = "sudo pkill -f marytts-5.2";
		exec(killOtherInstances);
	
	}
	
	private void startMaryServer() throws Exception {
		
		shutdownServer();
		//String userDir = System.getProperty("user.dir");
		
		
		
		//URL maryDir = ClassLoader.getSystemClassLoader().getResource(
				//"marytts-5.2/");
		
		//String userDir = new File(maryDir.toURI()).getAbsolutePath();
		String userDir = maryPath;
		System.out.println("Starting server from " + userDir);
		
		String processCommandArray =  "sudo "+userDir + "/bin/marytts-server" ;
		
		exec(processCommandArray);
		}

	public void exec(String execString) throws Exception{
		String[] execStringArray = execString.split(" ");
		ProcessBuilder pb = new ProcessBuilder(execStringArray);
		//pb.redirectOutput(Redirect.INHERIT);
		//pb.redirectError(Redirect.INHERIT);

		process = pb.start();
		//System.out.println("Waiting..");
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// System.out.println("Shutting server down");
				if (process != null){
					process.destroyForcibly();
					process.destroy();
				}
				//System.out.println("Server shutdown");
			}
		});

		//System.out.println("Waiting for server");
		process.waitFor();
		//System.out.println("Waited");
		process.destroy();
		
	}
	
	public static void main(String args[]) throws Exception {
		new MaryServer("./").startServer();
		//MaryServer.shutdownServer();
	}

	@Override
	public void run() {
		try {
			startMaryServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void startServer(){
		MaryServer server = new MaryServer(maryPath);
		Thread t = new Thread(server);
		t.start();
		//System.out.println("Thread started");
		boolean started = false;
		while (!started){
			if (server.process != null){
				try{
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(server.process.getErrorStream()), 1);
	                String line;
	                while ((line = bufferedReader.readLine()) != null) {
	                    //System.out.println(">"+line);
	                    if (line.contains("started in")){
	                    	System.out.println(line);
	                    	started = true;
	                    	break;
	                    }
	                }
				}catch(Exception e){
					//e.printStackTrace();
				}
			}
			if (started)
				break;
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("TTS Server started.");
	}

}
