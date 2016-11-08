
public class ThreadDriver {
	
	private static int nRequests;
	private static int nThreads;
	private static String algoConstant;
	private static Mutex mutex;
	
	public static void main(String args[]) {
		if(args.length < 2) {
			System.out.println("Usage: <number of threads> <number of requests>");
		}
		
		nThreads = Integer.parseInt(args[0]);
		nRequests = Integer.parseInt(args[1]);
		
		System.out.println("***********************************");
		System.out.println("Test And Set Algorithm");
		System.out.println("N threads = " + nThreads + " N requests = " + nRequests);
		System.out.println("***********************************");
		
		mutex = new TestAndSet();
		
		Thread thread_arr[];
		thread_arr = new Thread[nThreads];
		
		Long start = System.currentTimeMillis();
		
		for(int i = 0; i < nThreads; i++) {
			thread_arr[i] = new Thread(new ThreadEngine(nRequests, mutex, i + 1, nThreads));
			thread_arr[i].start();
		}
		
		
		
		for(Thread thread : thread_arr) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Long end = System.currentTimeMillis();
		
		System.out.println("Counter: " + mutex.getCounter());
		System.out.println("Total time taken: " + (end - start));
		
		System.out.println("***********************************");
		System.out.println("Test Test And Set Algorithm");
		System.out.println("N threads = " + nThreads + " N requests = " + nRequests);
		System.out.println("***********************************");
		
		mutex = new TestTestAndSet();
		
		thread_arr = new Thread[nThreads];
		
		start = System.currentTimeMillis();
		
		for(int i = 0; i < nThreads; i++) {
			thread_arr[i] = new Thread(new ThreadEngine(nRequests, mutex, i + 1, nThreads));
			thread_arr[i].start();
		}
		
		for(Thread thread : thread_arr) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		end = System.currentTimeMillis();
		
		System.out.println("Counter: " + mutex.getCounter());
		System.out.println("Total time taken: " + (end - start));
		
		System.out.println("***********************************");
		System.out.println("Tournament Selection Algorithm");
		System.out.println("N threads = " + nThreads + " N requests = " + nRequests);
		System.out.println("***********************************");
		
		mutex = new Tournament(nThreads);
		
		thread_arr = new Thread[nThreads];
		start = System.currentTimeMillis();
		
		for(int i = 0; i < nThreads; i++) {
			thread_arr[i] = new Thread(new ThreadEngine(nRequests, mutex, i + 1, nThreads));
			thread_arr[i].start();
		}
		
		for(Thread thread : thread_arr) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		end = System.currentTimeMillis();
		
		System.out.println("Counter: " + mutex.getCounter());
		System.out.println("Total time taken: " + (end - start));
		
	}

}
