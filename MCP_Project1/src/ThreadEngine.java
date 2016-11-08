
public class ThreadEngine implements Runnable {
	
	private int nRequests;
	private Mutex mutex;
	public int threadId;
	public int petersonThreadId[];
	
	public ThreadEngine(int nRequests, Mutex mutex, int threadId, int nThreads) {
		this.nRequests = nRequests;
		this.mutex = mutex;
		this.threadId = threadId;
		
		if(mutex instanceof Tournament) {
			int maxLevel = (int)Math.ceil((Math.log(nThreads) / Math.log(2)));
			petersonThreadId = new int[maxLevel + 1];
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		for(int i = 0; i < nRequests; i++) {
			mutex.lock(this);
			mutex.executeCS();
			mutex.unlock(this);
		}
	}

}
