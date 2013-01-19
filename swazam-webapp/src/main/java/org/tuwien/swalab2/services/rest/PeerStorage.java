/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.services.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Singleton;

/**
 *
 * @author gh
 */

public final class PeerStorage implements Serializable{
    
    private static PeerStorage peerStorage;
    private ArrayList<String> peers = new ArrayList<String>();
    
    private PeerStorage(){
       
    }
    
    public synchronized static PeerStorage getInstance() 
    {
        if (peerStorage == null) 
        {
            peerStorage = new PeerStorage();
        }
        return peerStorage;
    }
    
    
    public void addPeerToStorage(String address){
        peers.add(address);
        System.out.println("Added new peer to list "+ address);
    
    }
    
    public ArrayList<String> getRegisteredPeerList(){
        return peers;
    }
    
}