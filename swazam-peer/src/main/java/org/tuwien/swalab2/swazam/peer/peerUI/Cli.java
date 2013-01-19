/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.swazam.peer.peerUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;

import org.tuwien.swalab2.swazam.peer.musiclibrary.Library;
import org.tuwien.swalab2.swazam.peer.musiclibrary.Mp3File;
import org.tuwien.swalab2.swazam.util.fingerprint.FingerprintFile;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import ac.at.tuwien.infosys.swa.audio.SubFingerprint;

import com.kenmccrary.jtella.NodeConnection;
import com.kenmccrary.jtella.SearchSession;



public class Cli implements Runnable{

	private String cmd = "";
	private String filename = "";
	private Library library;
	//		Fingerprint fingerprint = null;



	public Cli(Library library) {
		super();
		this.library = library;
	}


	public void run() {

		usage();

		boolean run = true;
		while (run) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String input;
			Scanner s = null;

			try {
				input = in.readLine();
				s = new Scanner(input);

			} catch (IOException e) {
				System.err.println("Couldn't read line.");
			}

			if (s.hasNext()) {
				cmd = s.next();

				if (cmd.equals("add")) {

					if (s.hasNextInt()) {
						// TODO: send file to peer
						System.out.println("TODO: send file to peer");
						Integer id = Integer.valueOf(s.next());
					} else {
						if (s.hasNext()){
							filename = s.next();

							File file = new File(filename);
							try {
								if (file.getName().contains("mp3")
										|| file.getName().contains("MP3")) {
									library.add(file);
								} else {
									System.out
									.println("The file must be of type mp3 \n");
								}
							} catch (Exception e) {
								System.out.println("Couldn't find file \n");

							}
						}
					}
				} else if (cmd.equals("remove")) {
					if (s.hasNext()){
						filename = s.next();
						File file = new File(filename);
						try {
							if (file.getName().contains("mp3")
									|| file.getName().contains("MP3")) {
								library.remove(file);
							} else {
								System.out
								.println("The file must be of type mp3 \n");
							}
						} catch (Exception e) {
							System.out.println("Couldn't find file \n");

						}
					}


				} else if (cmd.equals("list")) {
					// org.tuwien.swalab2.swazam.util.Fingerprint.fingerprint("file");

					library.list();


				} else if (cmd.equals("usage")) {
					usage();
				} else if (cmd.equals("quit")) {
					run = false;
				} else {
					usage();
				}
			}
		}
	}
	private void usage() {
		System.out.println("\n Interactive commands:"
				+ "\n - add <path to mp3>" + "\n - add <id> <path to mp3>"
				+ "\n - remove <path to mp3>" + "\n - list" + "\n - quit"
				+ "\n - list"
				+ "\n - usage" + "\n");
	}


}