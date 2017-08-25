/*Name: Rahul Middha
UTA ID: 1001238177*/

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class WebClient {

	public static void main(String[] args) {
		
		long timeStart = System.currentTimeMillis(); //Start RTT

		final String CRLF = "\r\n"; 
		final String SP = " "; 
		String serverHost = null;
		
		//serverPort with default port value 8080
		int serverPort = 8080;
		
		//initializing filePath with default file path
		String filePath = "/";
		
		//checking command line arguments for serverhost, port, and filePath		
		if(args.length == 1)
		{
			serverHost = args[0];
		}
		else if (args.length == 2){
			
			serverHost = args[0];
			

			try {
				serverPort = Integer.parseInt(args[1]); 
			}
			catch (NumberFormatException nfe)
			{
				System.err.println("(CLIENT) No arguments given. 8080 Server port will be used.");
				filePath = args[1];
			}
		}
		else if (args.length == 3){
			
			serverHost = args[0];
			
			
			try {
				serverPort = Integer.parseInt(args[1]); //checking if port is integer
			}
			catch (NumberFormatException nfe)
			{
				System.err.println("(CLIENT) No port number given. 8080 Server port will be used.");
			}
			
			filePath = args[2];
		}
		else
		{
			System.err.println("(CLIENT)  Not enough arguments given. Please provide serverHost. ");
			System.exit(-1);
		}
		
		System.out.println("(CLIENT) Server Port: " + serverPort);
		System.out.println("(CLIENT) FilePath: " + filePath);
		
				
		//define a socket
		Socket sock = null;
		BufferedReader inStream = null; 
		DataOutputStream outStream = null; 
		
		FileOutputStream fos = null; 
		
		try {
			
			InetAddress serverInet = InetAddress.getByName(serverHost); //inet address 
			
			sock = new Socket(serverInet, serverPort); //connect to the server
			System.out.println("(CLIENT) Server connected at " + serverHost + ":" + serverPort);
			System.out.println("(CLIENT) Socket Family : AF_INET");
			
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - timeStart;
			System.out.println("(CLIENT) RTT : " + totalTime); //RTT
			
			inStream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			outStream = new DataOutputStream(sock.getOutputStream());

			String request = "GET" + SP + filePath + SP +"HTTP/1.0" + CRLF;
			System.out.println("(CLIENT) Sending HTTP GET request: " + request);
			
			outStream.writeBytes(request);
			outStream.writeBytes(CRLF);
			outStream.flush();
			
			System.out.println("(CLIENT) Waiting for response");
			
			String responseLine = inStream.readLine();
			System.out.println("(CLIENT) Received HTTP Response: " + responseLine);

			String contentType = inStream.readLine();
			System.out.println("(CLIENT) Received " + contentType);
			inStream.readLine();

			System.out.println("(CLIENT) Received response Body:");
			
			
			StringBuilder content = new StringBuilder();
			String response;
			while((response = inStream.readLine()) != null)
			{
				content.append(response + "\n");
				
				System.out.println(response);
			}
			
			String fileName = GET(content.toString());
			
			fos = new FileOutputStream(fileName);
			fos.write(content.toString().getBytes());
			fos.flush();
			
			System.out.println("(CLIENT) Received HTTP Response. File created: " + fileName);

		} catch (IllegalArgumentException iae) {
			System.err.println("(CLIENT) Error in connecting to SERVER: " + iae.getMessage());
		} catch (IOException e) {
			System.err.println("(CLIENT) Error " + e);
		}
		finally {
			try {
				//closing everything
				if (inStream != null) {
					inStream.close();
				}
				if (outStream != null) {
					outStream.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (sock != null) {
					sock.close();
					System.out.println("(CLIENT) Closing Connection.");
				}
			} catch (IOException e) {
				System.err.println("(CLIENT) EXCEPTION in closing resource." + e);
			}
		}
	}

	//allots default filename
	private static String GET(String content)
	{
		String filename = "";
		
		filename = content.substring(content.indexOf("<title>")+("<title>").length(), content.indexOf("</title>"));
		
		if(filename.equals(""))
		{
			filename = "index";
		}
		
		filename = filename+".html";
		
		return filename;
	}
}