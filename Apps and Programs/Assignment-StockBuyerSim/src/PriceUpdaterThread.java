import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 */

/**
 * @author dwwer
 * Price updater updates price of shares every once in a while
 * 11/12/2019
 */
public class PriceUpdaterThread extends Thread  {

	public Stock stock;
	private final ScheduledExecutorService ex = Executors.newScheduledThreadPool(1);
	

	private TransactionLoggerThread tlt;
	private DateTimeFormatter dtf;
	private LocalDateTime now;
	
	/**
	 * Constructor accepts stock and transaction logger
	 * @param stock
	 * @param tlt
	 */
	public PriceUpdaterThread(Stock stock, TransactionLoggerThread tlt) {
		this.stock = stock;
		this.tlt = tlt;
		this.dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
		this.now = LocalDateTime.now();
	}

	@Override
	public void run() {
		String str;
		//update price periodically
		Random r = new Random();
		for(int i = 0; i < 30; i++) {
			if(r.nextBoolean() == true) { //random chance to run
				stock.updatePrice();
				str = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(now) + ": Updated price to $" + stock.updatePrice() + "\n";
				tlt.updateInputList(str);
				System.out.println(str);
			}
			else {
				try {
					Thread.sleep(2000); //sleep if not running
				} catch (InterruptedException e) {
					System.out.println(Thread.currentThread() + ": Thread interrupted");
				}	
			}
			
		}
		
		
	}
}
