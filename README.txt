##########################
# swa                    #
##########################

#Lab 2 (2012)




In order to deploy the webapp you need JDK7 and Glassfish 3.1.2.2
Start glassfish from C:\glassfish-3.1.2.2\glassfish\bin  with startsrv.bat domain1
navigate to localhost:4848
under applications hit deploy and select the war file from \swa\swazam-webapp\target  change the name to swazam-webapp under contxt-root this is important otherwise the rest api wont work
hit ok
Under resources/ JDBC/JDBC Resources make sure the jdbc/__default is present otherwise create with derby connection pool
profit

The source code of the assignment is available at https://github.com/msmolle/swa and can be built with mvn clean install.

#Peer
In order to start the peer you will need a peer.properties file.
A sample configuration is included in swa/swazam-peer. All properties except
"bindToIp" are necessary.
If bindToIp is not specified the peer will search for the first none virtual
network intface which is online and use it's first assigned IP address.
To start the client use the following command:
java -jar swazam-peer-1.0-jar-with-dependencies.jar PORT UNIQUE_ID
where port is used to listen for new peer connections and UNIQUE_ID is a
string.
The peer will open a second port (PORT+1) to listen for incoming client
requests, keep that in mind while starting mutliple peers.

#Client

start with java -jar swa-client-1.0-jar-with-dependencies.jar

Known issues:
Subsequent requests to the system can only be made every 15 seconds, specifically after the "DEBUG: wait for client threads" message appears on the console.


Copy music in music folder

in case something fails... take those requests and use it in your browser manually to show rest interface functions:

testusers: user1/pass1  user2/pass2

http://localhost:8080/swazam-webapp/api/account/update/filenameXY/1/1       // send a search request to server for userId 1
http://localhost:8080/swazam-webapp/api/account/update/filenameXYZ/1/2           // send a search request to server for userId 1

login in on website and see the transactions listed.


http://localhost:8080/swazam-webapp/api/peer/add/127.0.0.1:37000      //add a peer to peerlist
http://localhost:8080/swazam-webapp/api/peer/add/127.0.0.1:37002  //add a peer to peerlist
http://localhost:8080/swazam-webapp/api/peer/peerlist               //get list of all registered peers

http://localhost:8080/swazam-webapp/api/account/login/user1/pass1         // returns userId for reference on client side