
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 */

/**
 * @author Dillon Wertman
 * Main to run threads
 * 11/12/2019
 */
public class Traders {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		int shares = 50; //beginning no. of shares
		String file = "src/TransactionLog.txt"; //file to print to


		//instantiate stock object
		Stock stock = new Stock("DIS");

		//instantiate threads
		TransactionLoggerThread tlt = new TransactionLoggerThread(new File(file));
		PriceUpdaterThread put = new PriceUpdaterThread(stock, tlt);
		TraderThread tt1 = new TraderThread(stock, "tt1", tlt);
		TraderThread tt2 = new TraderThread(stock, "tt2", tlt);
		TraderThread tt3 = new TraderThread(stock, "tt3", tlt);

		//start threads
		put.start();
		tt1.start();
		tt2.start();
		tt3.start();
		tlt.start();

		//join threads to have main wait till after they are done to find price change
		put.join();
		tt1.join();
		tt2.join();
		tt3.join();
		tlt.join();




		//print out profit/loss
		System.out.println("Buyer 1 lost: $" + tt1.getChange());
		System.out.println("Buyer 1 lost: $" + tt2.getChange());
		System.out.println("Buyer 1 lost: $" + tt3.getChange());


	}

}
