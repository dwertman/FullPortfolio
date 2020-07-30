/**
 * Assignment 2 - T-Shirt Server
 * This application acts as the server to hold a pool of t-shirts as well as the client handler for client requests
 * Created by Dillon Wertman
 * 10/7/19
 */


import java.awt.List;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class TShirtServer {

	public static void main(String[] args) {
		CopyOnWriteArrayList<String> merch = fillShirtList();//holds t-shirts in thread-safe structure
		ServerSocket server = null; //server socket for server
		ExecutorService pool = Executors.newCachedThreadPool();//create thread pool
		try {
			server = new ServerSocket(32001); //set server to use provided port
			server.setReuseAddress(true); //allows socket to be bound even if previous connection in timeout state

			//accepts connections from multiple clients
			while(true) {
				Socket client = server.accept();
				System.out.println("New client connected " + client.getInetAddress());
				ClientHandler clientsock = new ClientHandler(client, merch);
				pool.execute(clientsock);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	/**
	 * @author dwwer
	 * ClientHandler implements Runnable and is used to process client requests to server
	 */
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket; //socket of client
		private final CopyOnWriteArrayList<String> shirts; //shirt list

		//constructor for the socket and shirt list
		public ClientHandler(Socket socket, CopyOnWriteArrayList<String> shirts) {
			this.clientSocket = socket;
			this.shirts = shirts;

		}

		@Override //override runnable run thread
		public void run() {
			Random r = new Random();
			int clientNum = r.nextInt();//
			PrintWriter out = null; //used to work with server output
			BufferedReader in = null; //used to work with server input
			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true); //gets client request
				
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //gets client input
		
				String line = null; //holds client request
				line = in.readLine();
				/**/
				//checks if client request is in list
				//this is where the magic happens!!!!
				if(!shirts.isEmpty()) {
					//ensures clients are serviced one at a time and aren't skipped
					if(shirts.contains(line)) { //if shirt size is still available, find it
						shirts.remove(shirts.indexOf(line));	
						//System.out.println("Here");
						System.out.println("Client " + clientNum + " successfully ordered (" + "1) " + line + " shirt");
					}
					else { //if shirt size isn't available, tell client it's sold out then move on
						System.out.println("Client " + clientNum + ": shirt request sold out");
						line = in.readLine();
					}

				}
				else {
					System.out.println("All shirts sold out");
					
				}

				out.println(line); //have server output client request
			}catch (IOException e) {
				e.printStackTrace();
			} finally { //error handling and close output and input
				try {
					if (out != null) {
						out.close();
					}
					if(in != null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}



	/**
	 * fills a linked list with different sizes of shirts
	 * returns LinkedList<String>
	 */
	public static CopyOnWriteArrayList<String> fillShirtList(){
		CopyOnWriteArrayList<String> l = new CopyOnWriteArrayList<String>();
		Random r = new Random();
		
		//three stacks of shirts
		Stack sizeL = new Stack<String>();
		for(int i = 0; i < 100; i++) {
			sizeL.push("large");
		}
		Stack sizeM = new Stack<String>();
		for(int i = 0; i < 100; i++) {
			sizeM.push("medium");
		}
		Stack sizeS = new Stack<String>();
		for(int i = 0; i < 100; i++) {
			sizeS.push("small");
		}

		//Following while loop fills the merch list with different shirt sizes
		int i = 0;
		while(i < 300) {
			int rand = r.nextInt(3);
			if(rand == 0 && !sizeL.empty()) {
				l.add((String) sizeL.pop());
				i++;

			}
			else if(rand == 1 && !sizeM.empty()) {
				l.add((String) sizeM.pop());
				i++;

			}

			else if(rand == 2 && !sizeS.empty()) {
				l.add((String) sizeS.pop());
				i++;
			}
		}
		return l;

	}

}

