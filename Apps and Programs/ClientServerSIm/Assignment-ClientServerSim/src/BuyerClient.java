/**
 * Assignment 2 - T-Shirt Buyer Client Application
 * This application acts as a client to request an order of a t-shirt from the server
 * Created by Dillon Wertman
 * 10/7/2019
 */


import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.Socket;

public class BuyerClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String host = "127.0.0.1";	//host server
		int port = 32001; //port to be used by server
		int rand;
		ClientThread thread = new ClientThread(host, port);
		ExecutorService pool = Executors.newFixedThreadPool(500);//create thread pool
		int i = 0;
		while(i < 500) {
			//new Thread(thread).start();//creates multiple threads
			pool.execute(thread);
			i++;
		}
		
		
		
	}
	/**
	 * ClientThread handles individual client threads
	 * @author dwwer
	 *
	 */
	private static class ClientThread implements Runnable{
		private final String host; //socket of client
		private final int port; //shirt list
		public ClientThread(String host, int port) {
			this.host = host;
			this.port = port;
		}
		@Override
		public void run() {
			Random r = new Random(); //random number
			int randomSize; //holds size of random t-shirt
			
			try(Socket socket = new Socket(host, port)){
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true); //print writer for output stream
				String line = null; //holds client request
				//if/else randomizes which shirt size a client asks for
				randomSize = r.nextInt(3);
				if(randomSize == 0) {
					line = "small";
				}
				else if(randomSize == 1) {
					line = "medium";
				}
				else if(randomSize == 2) {
					line = "large";
				}
				out.println(line); //print request to output which is read by server
				out.flush();//clears output after server reads it
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}


