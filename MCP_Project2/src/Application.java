import java.util.Random;

public class Application implements Runnable {

	ConcurrentLinkedListInterface cll;
	int testFlavor;
	int noOfOperations;

	public Application(ConcurrentLinkedListInterface cll, int testFlavor, int noOfOperations) {
		this.cll = cll;
		this.testFlavor = testFlavor;
		this.noOfOperations = noOfOperations;
	}

	public void run() {
		Random random = new Random();

		for (int i = 0; i < noOfOperations; i++) {
			int number = random.nextInt(1001);
			int key = random.nextInt(1001);

			if (testFlavor == 1) {
				if (number >= 0 && number < 900) {
					cll.search(key);
				} else if (number >= 900 && number < 990) {
					cll.insert(key);
				} else {
					cll.delete(key);
				}
			} else if (testFlavor == 2) {
				if (number >= 0 && number < 700) {
					cll.search(key);
				} else if (number >= 700 && number < 900) {
					cll.insert(key);
				} else {
					cll.delete(key);
				}
			}

			else if (testFlavor == 3) {
				if (number >= 0 && number < 500) {
					// System.out.println("Insert: " + key);
					cll.insert(key);
				} else {
					// System.out.println("Delete: " + key);
					cll.delete(key);
				}
			}
		}
	}
}
