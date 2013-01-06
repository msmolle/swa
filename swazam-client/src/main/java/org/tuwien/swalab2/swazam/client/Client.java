package org.tuwien.swalab2.swazam.client;

import ac.at.tuwien.infosys.swa.audio.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


/**
 * Hello world!
 *
 */
public class Client
{
    public static void main( String[] args )
    {
    System.out.println("Welcome to the SWAzam client.");

  String filename = null;

  // TODO:   || args[0] != "query"
  if (args.length != 2){
   System.out.println("Usage: java Client query <filename>");
  }
  else {

   filename = args[1];    
   System.out.println(filename.toString());    

   File fileIn = new File(filename);

   try {
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);

    System.out.println("Trying to fingerprint " + fileIn.getName());

    Fingerprint resultFingerpring =  FingerprintSystem.fingerprint(audioInputStream);
    
    System.out.println("Fingerprinting of file " + fileIn.getName() + " is: "+ resultFingerpring.toString());

   }
   catch (Exception e){
    System.out.println("Somewhere something went horribly wrong...");
    System.out.println(e.toString());
   }

  }

 }

}
