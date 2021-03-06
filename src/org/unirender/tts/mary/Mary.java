package org.unirender.tts.mary;

import javax.sound.sampled.AudioInputStream;

import marytts.MaryInterface;
import marytts.client.RemoteMaryInterface;
import marytts.util.data.audio.AudioPlayer;

public class Mary {
	// NOTE: requires to run marytts-5.2/bin/marytts-server

	public String voiceName;
	public MaryInterface marytts ;
	public boolean useBluetoothAudio;
	public Mary(String voiceName) throws Exception {
		this.voiceName = voiceName;
		marytts = new RemoteMaryInterface("127.0.0.1", 59125);
		System.out.println("Connected to Local Mary Server");
		marytts.setVoice(voiceName);
		// See all the parameters by running ./marytts-client
		marytts.setAudioEffects("Robot(amount:100.0), Stadium(amount:100.0)");
		//marytts.setAudioEffects("Robot(amount:100.0)");
		useBluetoothAudio = false;
	}
	
	public void setBlueToothAudio(boolean useBluetoothAudio){
		this.useBluetoothAudio = useBluetoothAudio;
	}
	
	public void say(String input) throws Exception {
		// Set<String> voices = marytts.getAvailableVoices();
		// System.out.println("Available voices: " + voices + "\n");
		//if (useBluetoothAudio){
			//System.out.println("waiting for BT audio...");
			//while (!BlueToothAudio.checkSpeakersConnected()){
				//Thread.sleep(2000);
			//}
		//}
		AudioPlayer ap = new AudioPlayer();
		System.out.println("streaming audio...");
		System.out.println(input);
		AudioInputStream audio = marytts.generateAudio(input);
		// System.out.println("Setting audio");
		ap.setAudio(audio);
		
		// ap.start();
		// System.out.println("Running audio");
		ap.run();
		System.out.println("...stop audio streaming");
		// System.out.println("Cancelling audio");
		// ap.cancel();
		// System.out.println("Closing audio");
		// audio.close();
		// audio.reset();
		//marytts = null;
		// System.gc();
	}

	public static void main(String args[]) throws Exception {
		new MaryServer("./marytts-5.2").startServer();
		System.out.println("Synthesising");
		
		Mary m = new Mary("istc-lucia-hsmm");
		m.setBlueToothAudio(true);
		m.say("Eccomi!");
		
		m.say("Robot attivato! Ora, dirò una poesia:");
		
		m.say("Il coccodrilletto, un dì al fiume discese,");
		m.say("E di colpo sorprese, di pesci, un bel gruppetto,");
		m.say("E tutto arcigiulivo, nel fiume si calò,");
		m.say("Dischiuse le sue fauci; e i pesci, si mangiò.");
	}

}
