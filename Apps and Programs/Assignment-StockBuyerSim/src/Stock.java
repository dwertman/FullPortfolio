/**
 * Defines stock object
 * created by Alicia McNett
 * 11/12/2019
 */

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Stock {

	private double price; //price of stock

	private AtomicInteger sharesAvailable = new AtomicInteger(); //amount of shares of stock object

	private LocalDateTime now; //for formatting output
	//private int shares; //

	private String symbol; //e.g. DIS for Disney
	
	/**
	 * Constructor
	 * @param symbol for stock
	 * @throws IOException
	 */
	public Stock(String symbol) throws IOException

	{
		price = getInitialPrice(); //set price
		sharesAvailable.set(500); //set amount of shares
		this.symbol = symbol; //set symbol
		this.now = LocalDateTime.now(); //set date time for formatting

	}

	/**
	 * Returns price of stock
	 * @return
	 */
	public double getPrice() {
		return this.price;
	}
	
	/**
	 * getShares returns available shares
	 * @return
	 */
	public int getShares() {
		return sharesAvailable.get();
	}

	// You will need to modify this method -- I've put some basic logic in place for you
	//Probably where I need to do a lock

	/**
	 * purchaseStock buys stock
	 * @param shares
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized double purchaseStock(int shares) throws InterruptedException{

		if(sharesAvailable.get() >= shares){ //if there are enough shares left
			sharesAvailable.getAndAdd(-shares); //remove shares from available shares
			notifyAll(); //notify other threads to wake up
			return shares * price; //return the price of bought shares
		}
		wait(); //if you can't buy, wait
		return -1; // could not buy



	}

	/**
	 * sellStock sells stock
	 * @param shares
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized double sellStock(int shares) throws InterruptedException{

		if(shares > 0){ //if there are shares to sell
			sharesAvailable.getAndAdd(shares); //add shares back to available shares 
			notifyAll(); //notify other threads that shares are back
			return shares * price; //return the price of sold shares
		}
		wait(); //wait if you can't sell
		return -1; // could not sell

	}



	// gets the initial share cost 
	public double getInitialPrice(){
		Random r = new Random();
		// modify this to get the actual price for extra credit (can combine with method below if you'd like)
		price = 100;
		return price;

	}

	// gets the current stock price

	/**
	 * updates price of shares
	 * @return
	 */
	public double updatePrice(){
		// modify this to get the actual price for extra credit
		Random r = new Random();
		return price = r.nextInt(100);

	}


}




