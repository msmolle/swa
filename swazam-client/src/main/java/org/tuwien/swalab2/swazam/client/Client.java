package org.tuwien.swalab2.swazam.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tuwien.swalab2.swazam.client.clientUI.Cli;
import org.tuwien.swalab2.swazam.client.clientUI.SwingUI;
import org.tuwien.swalab2.swazam.client.communication.TcpDispatcher;
import org.tuwien.swalab2.swazam.peer.SearchMessage;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

import org.tuwien.swalab2.swazam.peer.ClientRestClient;

public class Client {

    private static SwingUI swingUI;
    private static Cli cli;
    private InetAddress ip;
    //private Integer port;
    private Socket socket = null;
    private ObjectOutputStream out = null;
    private TcpDispatcher tcpDispatcher = null;
    private List<KnownPeer> knownPeers = new ArrayList<>();
    private Integer localPort;
    private InetAddress currentIp;
    private Integer currentPort;
    private ClientRestClient restClient = new ClientRestClient();

    public static void main(String[] args) {
        Client client = new Client();

        System.out.println("Welcome to the SWAzam client.");
        
        client.setUp();
        //if (!client.setUp()) {
        //    client.shutdown();
        //}
        //client.startSwingUI(args);
        cli = new Cli(client);
        //swingUI = new SwingUI(client);
    }
    
    private void setUp() {
                
        File knowPeersFile = new File("knownPeers.swazam");
        
        // a: try known peers from local list
        if (knowPeersFile.exists()) {
            
            System.out.println("Loading known peers from file " + knowPeersFile.getName() + ".");

            try {
                FileInputStream knownPeersFileIn = new FileInputStream(knowPeersFile);
                ObjectInputStream knownPeersStreamIn = new ObjectInputStream(knownPeersFileIn);

                knownPeers = (List<KnownPeer>) knownPeersStreamIn.readObject();
                
                socket = connectToPeer(knownPeers);
                
                if (socket == null) {
                    socket = connectToPeer(getPeersFromServer());
                    
                    if (socket == null) {
                        System.out.println("Could not connect to SWAzam network");
                    }               
                }               

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // b: connect to server and get list of known peers
        else {
            System.out.println("No locally known peers.");
            
            socket = connectToPeer(getPeersFromServer());
        } 
        
        try {
            FileOutputStream knownPeersFileOut = new FileOutputStream(knowPeersFile);
            ObjectOutputStream knownPeersStreamOut = new ObjectOutputStream(knownPeersFileOut);
            knownPeersStreamOut.writeObject(knownPeers); 
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Socket connectToPeer(List<KnownPeer> knownPeers) {

        Socket initSocket;
        
        for (KnownPeer currentPeer : knownPeers) {
            
            currentIp = currentPeer.getIp();
            currentPort = currentPeer.getPort();

            //create connection to peer  
            try {
                initSocket = new Socket(currentIp, currentPort);
                //out = new ObjectOutputStream(initSocket.getOutputStream());

                System.out.println("Connected to peer " + currentIp + ":" + currentPort + ".");

                // add new knownPeers to list
                if (!knownPeers.contains(currentPeer)) {
                    knownPeers.add(currentPeer);
                    //break;
                }

                return initSocket;

            } catch (IOException ex) {
                System.out.println("Could not connect to peer " + currentIp + ":" + currentPort + ".");
                //knownPeers.remove(currentPeer);
            }
        }
        
        return null;

    }
    
    public List<KnownPeer> getPeersFromServer() {
    
        System.out.println("Trying to get list of peers from server...");

        List<KnownPeer> knownPeersFromServer = new ArrayList<>();
        
            String list = (String) restClient.getPeerList();

            String[] peers = list.split("\\-");

            for (int i = 0; i < peers.length; i++) {

                try {
                    String[] result = peers[i].split("\\:");
                    if (result.length == 2) {
                        InetAddress adr;
                        adr = InetAddress.getByName(result[0]);
                        Integer port = Integer.valueOf(result[1]);
                        System.out.println("DEBUG: (getPeersFromServer)" + result[0].toString() + ":" + result[1].toString());
                        KnownPeer thisPeer;
                        thisPeer = new KnownPeer(adr, port, port.toString());
                        knownPeersFromServer.add(thisPeer);
                    }

                } catch (UnknownHostException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            return knownPeersFromServer;
    }

    public void submitRequest(Fingerprint fingerprint) {

        System.out.println("Submitting FingerPrint to network...");

        //Create the port we are listening on
        try {
            System.out.println("DEBUG: building TcpDispatcher...");
            tcpDispatcher = new TcpDispatcher(new ServerSocket(localPort + 1));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //create the message
        Date d = new Date();
        SearchMessage searchMessage = null;
        try {
            System.out.println("DEBUG: building searchMessage...");
            searchMessage = new SearchMessage(ip.getHostAddress(), localPort, fingerprint, ip.getHostAddress() + localPort.toString() + d.toString());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //send message to peer
        try {

            if (socket.isClosed()) {
                socket = new Socket(currentIp, currentPort);
            }      
            
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(searchMessage);
            out.flush();

        } catch (UnknownHostException e) {
            System.err.println("Cannot find the peer  " + ip + ":" + localPort + ".");
            //serverSocket.close();
            System.exit(1); //TODO: remove
        } catch (IOException e) {
            System.err.println("Could not send the search request to peer " + ip + ".");
            e.printStackTrace();
            //System.exit(1);
        }

        //close the connection at this point we dont care about errors any more
        try {
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //give the listening thread 10 seconds time to wait for answers then kill it
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tcpDispatcher.kill();
    }

    public void shutdown() {

        try {
            out.close();
            socket.close();
            //swingUI.close();
            cli.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }
}
