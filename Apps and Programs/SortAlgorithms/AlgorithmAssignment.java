/**
 *Dillon Wertman
 *9/5/2018
 */
package assignments;


/**
 * @author Dillon Wertman
 * This program displays the average processing times
 * of four sort algorithms: Selection, Bubble, Insertion, 
 * and Quick.
 */
public class AlgorithmAssignment {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//declare constant and set arrays to size 1000 to pass to each method
		final int arraySize = 1000;
		final int timeArraySize = 500;
		final int maxValue = 500;
		
		int [] arrayA = new int[arraySize];
		int [] arrayB = new int[arraySize];
		int [] arrayC = new int[arraySize];
		int [] arrayD = new int[arraySize];
		
		//low and high values for Quick sort
		int low = 0;
		int high = arrayD.length - 1;
		
		//iterations for while loop condition.
		int iterations = 0;
		
		//declare times for method run calculations
		long startTime;
		long estimatedTime;
		
		//declare arrays to hold times
		long[] timeA = new long[timeArraySize];
		long[] timeB = new long[timeArraySize];
		long[] timeC = new long[timeArraySize];
		long[] timeD = new long[timeArraySize];
		
		//variable i for putting estimated times in arrays
		int i = 0;
		
		
		//while statement to fill arrays with times of each algorithm
		while (iterations < timeArraySize) {
			//fill arrays randomly
			fillArray(arrayA);
			fillArray(arrayB);
			fillArray(arrayC);
			fillArray(arrayD);
			
			//calculates time for iteration of sort, store time in respective array
			startTime = System.nanoTime();
			selectionSort(arrayA);
			estimatedTime = System.nanoTime() - startTime;
			timeA[i] = estimatedTime;
			
			startTime = System.nanoTime();
			bubbleSort(arrayB);
			estimatedTime = System.nanoTime() - startTime;
			timeB[i] = estimatedTime;
			
			startTime = System.nanoTime();
			insertionSort(arrayC);
			estimatedTime = System.nanoTime() - startTime;
			timeC[i] = estimatedTime;
			
			startTime = System.nanoTime();
			quickSort(arrayD, low, high);
			estimatedTime = System.nanoTime() - startTime;
			timeD[i] = estimatedTime;
			
			//increment values
			iterations++;
			i++;
		}//end while
	
		//print to console
		System.out.println("Array size: " + arraySize);
		System.out.println("Max value: " + maxValue);
		System.out.println("Number of iterations: " + timeArraySize);
		System.out.println("Average run time (in nano seconds):");
		histogram(timeA, timeB, timeC, timeD);
		
		
	}
	
	/**
	 * Method to make histogram and print to console
	 * @param A, a long array
	 * @param B, a long array
	 * @param C, a long array
	 * @param D, a long array
	 */
	public static void histogram(long[] A, long[] B, long[] C, long[] D) {
		int asterisk;
		float percentage;
		int sSortTime = averageArray(A);
		int bSortTime = averageArray(B);
		int iSortTime = averageArray(C);
		int qSortTime = averageArray(D);
		String[] sortNames = new String[]{"Selection Sort", "Bubble Sort", "Insertion Sort", "Quick Sort"};
		int[] sortTimes = new int[] {sSortTime, bSortTime, iSortTime, qSortTime};
		int totalTime = sSortTime + bSortTime + iSortTime + qSortTime;
		
		for(int i = 0; i < sortTimes.length; i++) {
			
			percentage = (float) sortTimes[i]/totalTime;
			asterisk = (int) Math.round(percentage * 50);
			
			//formats and prints asterisks
			switch (sortNames[i]) {
			case "Selection Sort" : System.out.print("\t" + sortNames[i] + ":\t  "); break;
			case "Bubble Sort" : System.out.print("\t" + sortNames[i] + ":\t  "); break;
			case "Insertion Sort" : System.out.print("\t" + sortNames[i] + ":\t  "); break;
			case "Quick Sort" : System.out.print("\t" + sortNames[i] + ":\t  ");
			
			}
			//print asterisks
			for(int j = 0; j < asterisk; j++) {
				System.out.print("*");
				
			}
			//formats sort names
			switch (sortNames[i]) {
			case "Selection Sort" : System.out.println("   " + sortTimes[i]); break;
			case "Bubble Sort" : System.out.println("   " + sortTimes[i]); break;
			case "Insertion Sort" : System.out.println("   " + sortTimes[i]); break;
			case "Quick Sort" : System.out.println("   " + sortTimes[i]);
			
			}
		}
		
	}
	
	
	/**
	 * totals array values and averages contents
	 * @parem A, an int Array
	 * @return average, an average of the contents of A
	 */
	public static int averageArray(long[] A) {
		int total = 0;
		int average;
		for(int i = 0; i < A.length - 1; i++) {
			total += A[i];
		}
		
		average = total/A.length;
		return average;
	}
	
	
	
	/**
	 * Fills array with random numbers from 0-500
	 * @param A, an int array
	 * @return A, a filled array
	 */
	public static int[] fillArray(int[] A) {
		//int count = 0;
		//step through array, assigning a random number to each element
		for(int i = 0; i < A.length; i++) {
			A[i] = (int)(Math.random()*500);
			//System.out.println(A[i]);
			//count ++;
		}//end for
		//return array
		return A;
	}//end method
	
	
	/**
	 * Takes array and sorts it using selection algorithm
	 * @param A, an int array
	 * @return A, a sorted array
	 */
	public static int[] selectionSort(int[] A) {
		//step through array, setting minIndex to i
		for(int i = 0; i < A.length-1; i++) {
			int minIndex = i;
			//get value after minIndex
			for(int j= i + 1; j < A.length; j++) {
				if(A[j] < A[minIndex]) {
					minIndex = j;
				}//end if
			}//end for
			
			int temp = A[minIndex];
			A[minIndex] = A[i];
			A[i] = temp;
		}//end for
		
		//return sorted array
		return A;
		
	}//end method
	
	/**
	 * sorts array using bubble algorithm
	 * @param A, an int array
	 * @return
	 */
	public static int[] bubbleSort(int[] A) {
		for(int i =0; i < A.length-1; i++) {
			for(int j = 0; j < A.length-1-i; j++) {
				if(A[j] > A[j+1]) {
					int temp = A[j+1];
					A[j+1] = A[j];
					A[j] = temp;
				}//end if
			}//end for
		}//end for
		return A;
		
	}//end method
	
	/**
	 * sorts array using insertion algorithm
	 * @param A, an int array
	 * @return
	 */
	public static int[] insertionSort(int[] A) {
		for (int i = 1; i < A.length; i++){
			int key = A[i];
			int j = i- 1;
			
			while(j >= 0 && A[j] > key){
			A[j+1] = A[j];
			j--;
			}//end while
			
			A[j + 1] = key;	
		}//end for
		return A;
	}//end method
	
	/**
	 * sorts array using Quick algorithm
	 * @param A, an int array
	 * @param low, the lowest value of the array
	 * @param high, the greatest value of the array
	 */
	public static void quickSort(int[] A, int low, int high) {
		if (A == null || A.length == 0)
			return;
		
		if(low >= high)
			return;
		
		//pick pivot
		int middle = low + (high - low) / 2;
		int pivot = A[middle];
 
		//make left < pivot and right > pivot
		int i = low, j = high;
		while (i <= j) {
			while (A[i] < pivot) {
				i++;
			}//end while
 
			while (A[j] > pivot) {
				j--;
			}//end while
 
			if (i <= j) {
				int temp = A[i];
				A[i] = A[j];
				A[j] = temp;
				i++;
				j--;
			}//end if
		}//end while
 
		//recursively sort two sub parts
			if (low < j)
				quickSort(A, low, j);
	 
			if (high > i)
				quickSort(A, i, high);
	
	}//end method
}//end sub


