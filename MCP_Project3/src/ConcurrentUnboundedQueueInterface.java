
public interface ConcurrentUnboundedQueueInterface {

	void enque(int threadId, int key);

	Result deque();

	boolean isEmpty();

}
