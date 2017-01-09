
public class PerformanceDriver {

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

	public static void main(String[] args) throws InterruptedException {

		if (args.length < 4) {
			System.out.println("Please enter the input in proper format");
			System.out.println("format: <test-flavor> <max-Thread-Limit> <no-of-operations> <print-console-output>");
			System.out.println("<test-falvor> takes values 1, 2");
			System.out.println("1: 50% Enq 50% Deq");
			System.out.println("2: 40% Enq 40% Deq 20% Empty");
			System.out.println(
					"<print-console-output> takes values 0, 1: 0 - turn off console output 1 - turn on console output");
			System.exit(0);
		}

		int testFlavor = Integer.parseInt(args[0]);
		int maxThreadLimit = Integer.parseInt(args[1]);
		int noOfOperations = Integer.parseInt(args[2]);
		boolean print = Integer.parseInt(args[3]) == 0 ? false : true;

		if (testFlavor == 1) {
			System.out.println("*******Test Results************");
			System.out.println("50% Enque 50% Deque");
		} else if (testFlavor == 2) {
			System.out.println("******Test Results*********");
			System.out.println("40% Enque 40% Deque 20% isEmpty");
		}

		/*
		 * System.out.println("Lock based"); ConcurrentUnboundedQueueInterface
		 * concurrentUnboundedQueueInterface = new
		 * ConcurrentUnboundedQueueLockBased();
		 * concurrentUnboundedQueueInterface.enque(1, 100);
		 * System.out.println(concurrentUnboundedQueueInterface.deque().getKey()
		 * );
		 * 
		 * System.out.println("Lock free"); concurrentUnboundedQueueInterface =
		 * new ConcurrentUnboundedQueueLockFree();
		 * concurrentUnboundedQueueInterface.enque(1, 100);
		 * System.out.println(concurrentUnboundedQueueInterface.deque().getKey()
		 * );
		 */

		int algo = ALGO_ITER;

		while (algo <= 2) {

			for (int tIter = 2; tIter <= maxThreadLimit; tIter *= 2) {

				Thread threads[] = new Thread[tIter];
				ConcurrentUnboundedQueueInterface concurrentQueue = null;

				if (algo == 1) {
					System.out.println("Concurrent Queue Lock based Synchronization");
					concurrentQueue = new ConcurrentUnboundedQueueLockBased();
				} else if (algo == 2) {
					System.out.println("Concurrent Queue Lock free Synchronization");
					concurrentQueue = new ConcurrentUnboundedQueueLockFree();
				}

				for (int i = 0; i < tIter; i++) {
					threads[i] = new Thread(new Application(concurrentQueue, testFlavor, noOfOperations, i + 1, print));
				}

				// start the timer

				System.out.println("Number of Threads: " + tIter);
				System.out.println("Number of Operations: " + noOfOperations);

				timer();
				for (int i = 0; i < tIter; i++) {
					threads[i].start();
				}

				for (int i = 0; i < tIter; i++) {
					threads[i].join();
				}
				timer();
				System.out.println("*********************************************");
			}
				algo += 1;
				System.out.println("**********************************************");
			}
	}
}
