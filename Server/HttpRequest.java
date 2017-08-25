/*Name: Rahul Middha
UTA ID: 1001238177*/

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpRequest implements Runnable {

	private final String CRLF = "\r\n"; 
	private Socket socket; 
	private final String SP = " "; 
	private int clientID; //unique clientID passed by WebServer 
	
	
	public HttpRequest(Socket cs, int cID) {
		this.socket = cs;
		this.clientID = cID;
	}

	@Override
	public void run() {
		
		BufferedReader inputStream = null; //input stream
		DataOutputStream outputStream = null; //output stream
		
		FileInputStream fis = null; 
		
		try {
			inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			outputStream = new DataOutputStream(socket.getOutputStream());

			String packet = inputStream.readLine();
			
			if(packet != null)
			{
				System.out.println("(SERVER - CLIENT " + clientID + ") Request received: " + packet);

				//split request line 
				String[] msgParts = packet.split(SP);
				
				// check if the request type is GET
				if (msgParts[0].equals("GET") && msgParts.length == 3) 
				{
					String path = msgParts[1];
					
					//add "/" to filepath
					if(path.indexOf("/") != 0)
					{
						path = "/" + path;
					}
					
					
					System.out.println("(SERVER - CLIENT " + clientID + ") Requested path: " + path);
					
					//if path == null 
					if(path.equals("/"))
					{
						System.out.println("(SERVER - CLIENT " + clientID + ") Respond with default /index.html file");
						path = path + "index.html";
					}
					
					path = "." + path;

					
					File file = new File(path);
					try {
					
						if (file.isFile() && file.exists()) 
						{
							
							//All OK (200)
							String responseLine = "HTTP/1.0" + SP + "200" + SP + "OK" + CRLF;
							outputStream.writeBytes(responseLine);

							outputStream.writeBytes("Content-type: " + getContentType(path) + CRLF);
							
							outputStream.writeBytes(CRLF);
							
							fis = new FileInputStream(file);

							byte[] buffer = new byte[1024];
							int bytes = 0;
							
							while((bytes = fis.read(buffer)) != -1 ) 
							{
								outputStream.write(buffer, 0, bytes);
							}
							
							System.out.println("(SERVER - CLIENT " + clientID + ") Sending Response with status line: " + responseLine);
							outputStream.flush();
							System.out.println("(SERVER - CLIENT " + clientID + ") HTTP Response sent");
							
						} else {
							//Requested file does not exist
							System.out.println("(SERVER - CLIENT " + clientID + ") ERROR: Requested path " + path + " does not exist");

							//404 Not Found 
							String responseLine = "HTTP/1.0" + SP + "404" + SP + "Not Found" + CRLF;
							outputStream.writeBytes(responseLine);

							outputStream.writeBytes("Content-type: text/html" + CRLF);
							
							outputStream.writeBytes(CRLF);
							
							outputStream.writeBytes(getErrorFile());
							
							System.out.println("(SERVER - CLIENT " + clientID + ") Sending Response with status line: " + responseLine);
							
							outputStream.flush();
							System.out.println("(SERVER - CLIENT " + clientID + ") HTTP Response sent");
						}
						
					} catch (FileNotFoundException e) {
						System.err.println("(SERVER - CLIENT " + clientID + ") EXCEPTION: Requested path " + path + " does not exist");
					} catch (IOException e) {
						System.err.println("(SERVER - CLIENT " + clientID + ") EXCEPTION in processing request." + e.getMessage());
					}
				} else {
					System.err.println("(SERVER - CLIENT " + clientID + ") Invalid HTTP GET Request. " + msgParts[0]);
				}
			}
			else
			{
				System.err.println("(SERVER - CLIENT " + clientID + ") Discarding a NULL/unknown HTTP request.");
			}

		} catch (IOException e) 
		{
			System.err.println("(SERVER - CLIENT " + clientID + ") EXCEPTION in processing request." + e.getMessage());
			
		} finally {
			//closing 
			try {
				if (fis != null) {
					fis.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
				if (socket != null) {
					socket.close();
					System.out.println("(SERVER - CLIENT " + clientID + ") Closing the connection.\n");
				}
			} catch (IOException e) {
				System.err.println("(SERVER - CLIENT " + clientID + ") EXCEPTION in closing resource." + e);
			}
		}
	}
	
	
	
	private String getContentType(String path)
	{
		//check if file type is html
		if(path.endsWith(".html") || path.endsWith(".htm"))
		{
			return "text/html";
		}
	
		return "application/octet-stream";
	}
	
	//get contents of error file
	private String getErrorFile ()
	{
		String errorFileContent = 	"<!doctype html>" + "\n" +
									"<html lang=\"en\">" + "\n" +
									"<head>" + "\n" +
									"    <meta charset=\"UTF-8\">" + "\n" +
									"    <title>Error 404</title>" + "\n" +
									"</head>" + "\n" +
									"<body>" + "\n" +
									"    <b>ErrorCode:</b> 404" + "\n" +
									"    <br>" + "\n" +
									"    <b>Error Message:</b> The requested file does not exist on this server." + "\n" +
									"</body>" + "\n" +
									"</html>";
		return errorFileContent;
	}
}