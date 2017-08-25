/*Name: Rahul Middha
UTA ID: 1001238177*/

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

//starts server Socket and responds to client's request

public class WebServer implements Runnable {

	private ServerSocket socketServer; 
	private String serverHost; 
	private int serverPort; 
	
	//Default host and port values 
	private final String DEFAULT_HOST = "localhost";
	private final int DEFAULT_PORT = 8080;
	
	//Default constructor 
	public WebServer ()
	{
		this.serverHost = DEFAULT_HOST; 
		this.serverPort = DEFAULT_PORT; 
	}
	
	
	//Port + Serverhost are passed
	public WebServer (String sHost, int port)
	{
		this.serverHost = sHost; 
		this.serverPort = port; 
	}
	
	
	//Port is passed
	public WebServer (int port)
	{
		this.serverHost = DEFAULT_HOST; 
		this.serverPort = port; 
	}

	
	@Override
	public void run() {
		
		try {

			//host inet address 
			InetAddress serverInet = InetAddress.getByName(serverHost);
			
			//initializing serverSocket
			socketServer = new ServerSocket(serverPort, 0, serverInet);
						
			System.out.println("(SERVER) SERVER started \n  Host: " + socketServer.getInetAddress() + " Port: " + socketServer.getLocalPort() + "\n");
			
			int clientID=0;
			
			while(true){
				
				Socket clientSocket = socketServer.accept();
				
				//client connected 
				System.out.println("(SERVER - CLIENT"+clientID+") Connection established \n Host:" + clientSocket.getInetAddress() + "Port:" + clientSocket.getPort());
				
				HttpRequest rh = new HttpRequest(clientSocket, clientID);
				
				new Thread(rh).start();
				clientID++;
			}
			
		} catch (UnknownHostException e) {
			System.err.println("(SERVER) Hostname Exception " + serverHost);
		} catch (IllegalArgumentException iae) {
			System.err.println("(SERVER) EXCEPTION in starting the SERVER: " + iae.getMessage());
		}
		catch (IOException e) {
			System.err.println("(SERVER) EXCEPTION in starting the SERVER: " + e.getMessage());
		}
		finally {
				try {
					if(socketServer != null){ //close sockets
						socketServer.close();
					}
				} catch (IOException e) {
					System.err.println("(SERVER) EXCEPTION in closing the server socket." + e);
				}
		}
	}
}