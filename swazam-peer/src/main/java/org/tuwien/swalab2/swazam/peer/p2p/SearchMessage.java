package org.tuwien.swalab2.swazam.peer.p2p;

import java.net.UnknownHostException;


import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class SearchMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3042905115036541745L;
	
	private Fingerprint fingerPrint;
	
        // TODO: ip als InetAddress statt String speichern
	public SearchMessage(String ip, int port, Fingerprint f, String id) throws UnknownHostException {
		super(ip, port, id);
		this.fingerPrint = f;
	}
	
	public Fingerprint getFingerprint() {
		return this.fingerPrint;
	}
	

}
