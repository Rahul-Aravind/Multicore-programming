import java.util.concurrent.atomic.AtomicBoolean;

public class TestAndSet implements Mutex {
	
	private AtomicBoolean lock;
	private int counter;
	
	public TestAndSet() {
		lock = new AtomicBoolean(false);
		counter = 0;
	}
	
	public void lock(ThreadEngine threadEngine) {
		while(lock.getAndSet(true));
	}
	
	public void executeCS() {
		counter++;
	}
	
	public void unlock(ThreadEngine threadEngine) {
		lock.set(false);
	}
	
	public int getCounter() {
		return counter;
	}
}
