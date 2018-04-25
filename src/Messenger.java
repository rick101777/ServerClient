import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Messenger {
	
	
	
	public static void main(String[] args) {
/*-------------------------------------------------------Exit Condition------------------------------------------------------------------*/
		if (args.length < 1) {
			System.err.println("There were no arguments passed!");
			System.out.println("Server Arguments: [Server Address] [Port Number]");
			System.out.println("Client Arguments: [Port Number] Optional [Server Address]");
			return;
		}
/*----------------------------------------------------------------------------------------------------------------------------------------------*/
		
/*-------------------------------------------------Server Code-------------------------------------------------------------------------*/
		if (args[0].equals("-l")) {
			try {
				int portNumber = Integer.valueOf(args[1]);																// Receives the port number from arguments
				ServerSocket serverSocket = new ServerSocket(portNumber);							// Opens a server socket
				Socket clientSocket = serverSocket.accept();																// Waits for clients to connect
				serverSocket.close();																											// Closes the server socket once someone joins
				InputThread ThreadIn = new InputThread(clientSocket);										// Constructs the Thread Object
				Thread t1 = new Thread(ThreadIn);																				// Creates a Thread
				t1.start();																																	// Starts the Thread
				
				BufferedReader standardInput = new BufferedReader(new InputStreamReader(System.in));		// Receieves input from the keyboard
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);										// Constructs an ouput stream sender
				String userMessage = standardInput.readLine();																								// Reads input from the keyboard
				while (!clientSocket.isClosed()) {																																// while loop continues if the socket isnt closed
					writer.println(userMessage);																							// Sends message to the client
					if (userMessage == null) {																									// Checks if the message was null
						break;																																				// if true, break out of while loop
					}																											
					userMessage = standardInput.readLine();																	// if not, read another line from standard input
				}
				
				standardInput.close();																												// closes the standard input
				writer.close();																																// closes the sender
				clientSocket.close();																													// closes the socket	
				
				try {
					t1.join();																																	// recollects resources that were allocated to the thread
				}catch(Exception e) {}
			}catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
			return;
		}

/*--------------------------------------------------Client Code------------------------------------------------------------------------------------------*/

		else {
			try {
				int portNumber = Integer.valueOf(args[0]);																// Gets the port from Arguments passed
				Socket ServerSocket = new Socket("localhost", portNumber);								// makes a connection to the server with the port and local host address
				InputThread ThreadIn = new InputThread(ServerSocket);										// Creates a new Thread object
				Thread t2 = new Thread(ThreadIn);																				// Creates the Actual thread and passes it the class that will be ran
				t2.start();																																	// starts running the thread
				
				BufferedReader standardInput = new BufferedReader(new InputStreamReader(System.in));	// Reads input from the keyboard
				PrintWriter writer = new PrintWriter(ServerSocket.getOutputStream(), true);									// takes keyboard input and sends it through the socket to the reciever
				String userMessage = standardInput.readLine();																							// reads a line from standard input
				while (!ServerSocket.isClosed()) {																														// while loop will continue unless the socket is closed
					writer.println(userMessage);																															// messages are sent
					if (userMessage == null) {																																	// if Ctrl-d is pressed...
						break;																																										// the loop will break
					}
					userMessage = standardInput.readLine();																									// else read another line from standard input
				}
				
				standardInput.close();																																				// closing standard input
				writer.close();																																								// closing the sender
				ServerSocket.close();																																				// closing the socket
				
				try {
					t2.join();																																									// recollecting resources that were allocated to the thread i.e closing the thread
				}catch(Exception e) {}
			}catch (Exception e) {
				System.err.println("Error:" + e.getMessage());
			}
			return;
		}

/*------------------------------------------------------------------------------------------------------------------------------------------------*/		
		
	}
}


/*------------------------------------------Input Thread Class------------------------------------------------------------------------*/
class InputThread implements Runnable{
	
	/*
	 *  This class takes care of recieving input from the other connection.
	 *   It receives the data from the socket
	 *   Then it outputs it to the screen
	 */
	
	private String Accessor;
	private Socket inputSocket;
	
/*--------------------------------Thread Object Constructor------------------------------------------------------------------------*/
	public InputThread(Socket socket) {
		this.inputSocket  = socket;
	}
	
/*--------------------------------------Accepts input from Sender and Ouputs it----------------------------------------*/
	@Override
	public void run() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(inputSocket.getInputStream()));
			String line = input.readLine();
			
			while (true) {
				if (line.equals("null")) {
					break;
				}
				System.out.println(line);
				line = input.readLine();
			}
			input.close();
			return;
		}catch(Exception e) {}
	}
	
	
	
}












