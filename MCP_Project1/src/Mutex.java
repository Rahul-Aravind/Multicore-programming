
public interface Mutex {
	
	public void lock(ThreadEngine threadEngine);
	public void executeCS();
	public void unlock(ThreadEngine threadEngine);
	public int getCounter();

}
