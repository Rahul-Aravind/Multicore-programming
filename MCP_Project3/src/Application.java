import java.util.Random;

public class Application implements Runnable {

	ConcurrentUnboundedQueueInterface concurrentUnboundedQueueInterface;
	int testFlavor;
	int noOfOperations;
	int threadId;
	int key;
	boolean print;

	public Application(ConcurrentUnboundedQueueInterface cq, int noOfOperations, int threadId, boolean p) {
		this.concurrentUnboundedQueueInterface = cq;
		this.testFlavor = 3;
		this.noOfOperations = noOfOperations;
		this.threadId = threadId;
		this.key = 1;
		this.print = p;
	}

	public Application(ConcurrentUnboundedQueueInterface cq, int testFlavor, int noOfOperations, int threadId,
			boolean p) {
		this.concurrentUnboundedQueueInterface = cq;
		this.testFlavor = testFlavor;
		this.noOfOperations = noOfOperations;
		this.threadId = threadId;
		this.key = 1;
		this.print = p;
	}

	public void run() {
		Random random = new Random();

		for (int i = 0; i < noOfOperations; i++) {
			int number = random.nextInt(1001);

			if (testFlavor == 1) {
				if (number >= 0 && number < 500) {

					if (print)
						System.out.println("Thread Id " + threadId + " Enqueue " + key);

					concurrentUnboundedQueueInterface.enque(threadId, key);
					key += 1;
				} else if (number >= 500 && number < 1000) {
					if (print)
						System.out.println("Thread Id " + threadId + " Dequeue Operation");
					Result res = concurrentUnboundedQueueInterface.deque();
					if (res != null) {
						if (print)
							System.out.println("Thread Id " + threadId + " Dequeue " + res.getKey());
					} else {
						if (print)
							System.out.println("Thread Id " + threadId + " Queue EMPTY!!");
					}
				}
			} else if (testFlavor == 2) {
				if (number >= 0 && number < 400) {

					if (print)
						System.out.println("Thread Id " + threadId + " Enqueue " + key);

					concurrentUnboundedQueueInterface.enque(threadId, key);
					key += 1;
				} else if (number >= 400 && number < 800) {

					if (print)
						System.out.println("Thread Id " + threadId + " Dequeue Operation");

					Result res = concurrentUnboundedQueueInterface.deque();
					if (res != null) {
						if (print)
							System.out.println("Thread Id " + threadId + " Dequeue " + res.getKey());
					} else {
						if (print)
							System.out.println("Thread Id " + threadId + " Queue EMPTY!!");
					}
				} else {
					boolean chk = concurrentUnboundedQueueInterface.isEmpty();

					if (print)
						System.out.println("Thread Id " + threadId + " Empty check " + chk);
				}
			} else if (testFlavor == 3) {

				if (print)
					System.out.println("Thread Id " + threadId + " Enqueue " + key);

				concurrentUnboundedQueueInterface.enque(threadId, key);
				key += 1;
			}
		}
	}
}
