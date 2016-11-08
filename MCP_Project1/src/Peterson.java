
public class Peterson {
	private volatile boolean[] flag;
	private volatile int victim;
	
	public Peterson() {
		flag = new boolean[2];
	}
	
	public void lock(int threadId) {
		flag[threadId] = true;
		victim = threadId;
		
		while(flag[1 - threadId] && victim == threadId);
	}
	
	public void unlock(int threadId) {
		flag[threadId] = false;
	}
}
