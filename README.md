# simple-web-server
Simple Web Server and Client

Simple Web Server & Client 

# System Configuration: 
- Programming Language: Java (jdk 1.8)
- IDE: Eclipse Luna
- OS: Windows 10
- Windows Command Prompt used to run the program


# To compile the code:
- Open Command prompt window

- Compile all the server and client files in their respective folders using
			javac *name*.java	

- If no port argument is given it will use default port address 8080. 
			java StartServer
			
- Or you may run StartServer.java in Server folder with an argument port. 
			java StartServer 1234

- Server has now started at the mentioned port number

- Open a new command prompt and execute the WebClient.java file with the serverHost as an argument (default port 8080 is used)
			java WebClient localhost
			
- If a port number needs to be specified, enter it as an argument after serverHost (that port number is used)
			java WebClient localhost 1234
			
			
- In the above case, it will search for a file in the default file path which currently contains a file called index.html

- If the file path is different enter the command as follows
			java WebClient localhost 1234 *path to file*

- The file can be viewed at the browser at address http://localhost:*port number*

# References
- Computer Networks CSE 5344 Project 1 Simple Web Server & Client(Reference) - JAVA
- Computer Networking. A Top Down Approach. Sixth Edition by James F. Kurose, Keith W. Ross. 
- MultiThreading from Jenkov.com (http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html)
- Socket Communications from Oracle (http://www.oracle.com/technetwork/java/socket-140484.html)
