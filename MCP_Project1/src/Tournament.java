
public class Tournament implements Mutex {
	
	private int maxLevel;
	private int totalNodes;
	private int incThreadIdFactor;
	private Peterson petersonLock[];
	private int counter;

	public Tournament(int nThreads) {
		maxLevel = (int)Math.ceil((Math.log(nThreads) / Math.log(2)));
		
		//System.out.println("Max Level " + maxLevel);
		totalNodes = (int)Math.pow(2, maxLevel);
		
		//System.out.println("DEBUG total nodes " + totalNodes);
		
		petersonLock = new Peterson[totalNodes];
		
		for(int i = 0; i < totalNodes; i++) {
			petersonLock[i] = new Peterson();
		}
		
		incThreadIdFactor = totalNodes - 1;
		
		counter = 0;
	}
	
	public void lock(ThreadEngine threadEngine) {
		int mappedThreadId = threadEngine.threadId + incThreadIdFactor;
		
		// start from leaf level
		for(int i = 1; i <= maxLevel ; i++) {
			threadEngine.petersonThreadId[i] = mappedThreadId % 2;
			//System.out.println("DEBUG LOCK level " + i + " thread " + mappedThreadId + " PID " + threadEngine.petersonThreadId[i]);
			mappedThreadId /= 2;
			petersonLock[mappedThreadId].lock(threadEngine.petersonThreadId[i]);
		}
		
	}
	
	public void unlock(ThreadEngine threadEngine) {
		int NodeThreadId = 1;
		
		// start from root level and release log n locks 
		for(int i = maxLevel; i > 0; i--) {
			//System.out.println("DEBUG UNLOCK level " + i + " Thread Id " + NodeThreadId + " PID " + threadEngine.petersonThreadId[i]);
			petersonLock[NodeThreadId].unlock(threadEngine.petersonThreadId[i]);
			NodeThreadId = 2 * NodeThreadId + threadEngine.petersonThreadId[i];
		}
		
	}
	
	public void executeCS() {
		counter++;
	}

	public int getCounter() {
		return counter;
	}
	

}
