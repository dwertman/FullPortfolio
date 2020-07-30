import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Random;

/**
 * 
 */

/**
 * @author Dillon Wertman
 * Traders buy and sell stock randomly and alert logger to log transactions
 * 11/12/2019
 */
public class TraderThread extends Thread {

	private Stock stock; //stock
	private int shares; //shares
	private String name; //name of trader
	private TransactionLoggerThread tlt; //transaction logger object
	private DateTimeFormatter dtf; //date time for formatting
	private LocalDateTime now; //date time for now
	private double wallet; //available funds of trader
	private double cost; //cost of shares

	
	/**
	 * Constructor accepts stock, name , and transaction logger
	 * @param stock
	 * @param name
	 * @param tlt
	 */
	public TraderThread(Stock stock, String name, TransactionLoggerThread tlt) {
		this.stock = stock;
		this.shares = 0;
		this.name = name;
		this.tlt = tlt;
		this.dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
		this.now = LocalDateTime.now();
		this.wallet = 10000.0;
		this.cost = 0;
	}
	
	@Override
	public void run() {
		int g = 0;
		Random r = new Random();
		int currentShares;
		int pivotV;
		for(int i = 0; i < 10 ; i++) {
			if(i == 0)
				g = 0;
			else
				g = r.nextInt(2);

			currentShares = r.nextInt(50);
			shares += currentShares;
			pivotV = shares;
			if(g == 0 && wallet != 0) { //buying and funds available
				try {
					//update wallet, increment transaction counter, and tell transaction logger to log
					cost = stock.purchaseStock(currentShares);
					wallet += cost;
					tlt.updateInputList(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(now) + ": Bought " + shares + " shares for $" + cost + "\n");
					tlt.incrementCounter();
					Thread.sleep(2000); //thread sleep to allow other threads to fire
				} catch (InterruptedException e) {
					System.out.println(Thread.currentThread() + ": Thread interrupted");
				}
			}
			else if(g == 1 && shares != 0) {
				try {
					//update wallet, increment transaction counter, and tell logger to log 
					cost = stock.sellStock(currentShares); 
					wallet -= cost;
					tlt.updateInputList(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(now) + ": Sold " + shares + " shares for $" + cost + "\n");
					tlt.incrementCounter();
					shares = pivotV - currentShares;
					Thread.sleep(2000); //thread sleep to allow other threads to fire
				} catch (InterruptedException e) {
					System.out.println(Thread.currentThread() + ": Thread interrupted");
				}
			}

		}
	}

	/**
	 * Check for profit/loss
	 * @return
	 */
	public String getChange() {
		//profit
		if((10000.0 - wallet) <= 0)
			return "Buyer " + name + " gained $" + ((-1)*(10000.0 - wallet));
		//loss
		return "Buyer " + name + " lost $" + ((-1) * (wallet - 10000.0));
	}
}

