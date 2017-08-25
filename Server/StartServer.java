/*Name: Rahul Middha
UTA ID: 1001238177*/


public class StartServer {
	//starts server
	public static void main(String[] args) {

		int port = 8080; //default port = 8080
		
		if(args.length == 1)
		{
			
			try {
				port = Integer.parseInt(args[0]); //check if port is an integer
			}
			catch (NumberFormatException nfe)
			{
				System.err.println("(SERVER) No port entered. Server will start at default port.");
			}
		}

		System.out.println("(SERVER) Server Port : " + port);
		
		WebServer ws = new WebServer(port);
		
		new Thread(ws).start();
	}
}