
public class PerformanceDriver {

	/**
	 * Timer to calculate the running time
	 */

	private static int phase = 0;
	private static long startTime, endTime, elapsedTime;

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

		if (args.length < 3) {
			System.out.println("Please enter the input in proper format");
			System.out.println("format: <test-flavor> <number of threads> <no-of-operations>");
			System.out.println("<test-falvor> takes values 1, 2, 3");
			System.out.println("1: Read Dominated");
			System.out.println("2: Mixed Dominated");
			System.out.println("3: Write Dominated");
			System.exit(0);
		}

		int testFlavor = Integer.parseInt(args[0]);
		int noOfThreads = Integer.parseInt(args[1]);
		int noOfOperations = Integer.parseInt(args[2]);

		if (testFlavor == 1) {
			System.out.println("*******Test Results************");
			System.out.println("Read Dominated");
		} else if (testFlavor == 2) {
			System.out.println("******Test Results*********");
			System.out.println("Mixed");
		} else if (testFlavor == 3) {
			System.out.println("******Test Results*********");
			System.out.println("Write Dominated");
		}

		System.out.println("Number of Threads: " + noOfThreads);
		System.out.println("Number of Operations: " + noOfOperations);
		int algo = 1;

		while (algo <= 3) {
			Thread threads[] = new Thread[noOfThreads];
			ConcurrentLinkedListInterface concurrentLinkedList = null;

			if (algo == 1) {
				System.out.println("Concurrent Linked List Coarse Grained Locking");
				concurrentLinkedList = new ConcurrentLinkedListCG();
				concurrentLinkedList.preCookList();
			} else if (algo == 2) {
				System.out.println("Concurrent Linked list Fine Grained Locking");
				concurrentLinkedList = new ConcurrentLinkedListFG();
				concurrentLinkedList.preCookList();
			} else if (algo == 3) {
				System.out.println("Concurrent Linked List lock free");
				concurrentLinkedList = new ConcurrentLinkedListLockFree();
				concurrentLinkedList.preCookList();
			}

			for (int i = 0; i < noOfThreads; i++) {
				threads[i] = new Thread(new Application(concurrentLinkedList, testFlavor, noOfOperations));
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
			concurrentLinkedList.printList();
			boolean chk = concurrentLinkedList.testCorrectness();

			if (chk == true) {
				System.out.println("Concurrency correctness is ensured by the Algorithm....");
			}
			System.out.println("*********************************************");
			algo += 1;
		}
	}
}
