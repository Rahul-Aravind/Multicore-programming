
public class Result {
	
	int threadId;
	Integer key;
	
	public Result(int tId, Integer k) {
		threadId = tId;
		key = k;
	}
	
	/**
	 * @return the threadId
	 */
	public int getThreadId() {
		return threadId;
	}
	
	/**
	 * @return the key
	 */
	public Integer getKey() {
		return key;
	}
	
	/**
	 * @param threadId the threadId to set
	 */
	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
	
	/**
	 * @param key the key to set
	 */
	public void setKey(Integer key) {
		this.key = key;
	}

}
