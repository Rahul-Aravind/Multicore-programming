
public class ConcurrencyCorrectnessDriver {
	
	/**
	 * Timer to calculate the running time
	 */

	private static int phase = 0;
	private static long startTime, endTime, elapsedTime;
	private final static int ALGO_ITER = 1;

	public static void timer() {
		if (phase == 0) {
			startTime = System.currentTimeMillis();
			phase = 1;
		} else {
			endTime = System.currentTimeMillis();
			elapsedTime = endTime - startTime;
			System.out.println("Time: " + elapsedTime + " msec.");
			memory();
			phase = 0;
		}
	}

	/**
	 * This method determines the memory usage
	 */
	public static void memory() {
		long memAvailable = Runtime.getRuntime().totalMemory();
		long memUsed = memAvailable - Runtime.getRuntime().freeMemory();
		System.out.println("Memory: " + memUsed / 1000000 + " MB / " + memAvailable / 1000000 + " MB.");
	}
	
	public static boolean testCorrectness(ConcurrentUnboundedQueueInterface concurrentUnboundedQueueInterface, int nThreads, int nOps, boolean print) {
		
		int threadArr[];
		threadArr = new int[nThreads + 1];
		
		int totOps = nThreads * nOps;
		
		for(int i = 0; i < totOps; i++) {
			Result res = concurrentUnboundedQueueInterface.deque();
			int tId = res.getThreadId();
			int key = res.getKey();
			
			if(key - threadArr[tId] != 1) {
				return false;
			}
			
			if(print) {
				System.out.println("Main Thread dequed key " + key + " which was enqued by Thread Id " + tId);
			}
			threadArr[tId] += 1;
		}
		
		return true;
	}

	public static void main(String[] args) throws InterruptedException {

		if (args.length < 3) {
			System.out.println("Please enter the input in proper format");
			System.out.println("format: <number of threads> <number of operations> <print-console-output>");
			System.out.println("<print-console-output> takes values 0, 1: 0 - turn off console output 1 - turn on console output");
			System.out.println("Concurrency correctness for Unbounded queue");
			System.out.println("Lock based synchronization");
			System.out.println("Lock free synchronization");
			System.exit(0);
		}

		int noOfThreads = Integer.parseInt(args[0]);
		int noOfOperations = Integer.parseInt(args[1]);
		boolean print = Integer.parseInt(args[2]) == 0 ? false : true;

		System.out.println("*******************************************");
		System.out.println("********Test Statistics********************");
		System.out.println("Number of Threads: " + noOfThreads);
		
		int algo = ALGO_ITER;

		while (algo <= 2) {
			Thread threads[] = new Thread[noOfThreads];
			ConcurrentUnboundedQueueInterface concurrentQueue = null;

			if (algo == 1) {
				System.out.println("Concurrent Queue Lock based Synchronization");
				concurrentQueue = new ConcurrentUnboundedQueueLockBased();
			} else if (algo == 2) {
				System.out.println("Concurrent Queue Lock free Synchronization");
				concurrentQueue = new ConcurrentUnboundedQueueLockFree();
			} else if (algo == 3) {
			}

			for (int i = 0; i < noOfThreads; i++) {
				threads[i] = new Thread(new Application(concurrentQueue, noOfOperations, i + 1, print));
			}

			// start the timer

			timer();
			for (int i = 0; i < noOfThreads; i++) {
				threads[i].start();
			}

			for (int i = 0; i < noOfThreads; i++) {
				threads[i].join();
			}
			timer();
			
			System.out.println(noOfThreads +" threads have finished their enque operations. No of Operations " + noOfOperations);
			boolean check = testCorrectness(concurrentQueue, noOfThreads, noOfOperations, print);
			
			if(!check) {
				System.out.println("Concurrency correctness FAILED!!!");
			} else {
				System.out.println("Concurrency correctness PASSED!!!");
			}
			
			System.out.println("*********************************************");
			
			
			
			algo += 1;
		}
	}

}
