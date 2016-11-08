import java.util.concurrent.atomic.AtomicBoolean;

public class TestTestAndSet implements Mutex {
	
	private AtomicBoolean lock;
	public int counter;
	
	public TestTestAndSet() {
		lock = new AtomicBoolean(false);
		counter = 0;
	}
	
	public void lock(ThreadEngine threadEngine) {
		while(true) {
			while(lock.get());
			if(!lock.getAndSet(true))
				return;
		}
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
