import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentUnboundedQueueLockBased implements ConcurrentUnboundedQueueInterface {

	class Node {
		
		int threadId;
		Integer key;
		Node next;
		
		Node(Integer k) {
			key = k;
			next = null;
		}

		Node(int tId, Integer k) {
			threadId = tId;
			key = k;
			next = null;
		}
	}

	Node head, tail;
	ReentrantLock enqLock, deqLock;
	AtomicInteger size;

	public ConcurrentUnboundedQueueLockBased() {
		head = new Node(null);
		tail = head;
		enqLock = new ReentrantLock();
		deqLock = new ReentrantLock();
		size = new AtomicInteger(0);
	}

	@Override
	public void enque(int threadId, int key) {
		enqLock.lock();
		try {
			Node newN = new Node(threadId, key);
			tail.next = newN;
			tail = newN;
			size.getAndIncrement();
		} finally {
			enqLock.unlock();
		}
	}

	@Override
	public Result deque() {
		deqLock.lock();
		try {
			if (head.next == null) {
				return null;
			} else {
				int tId = head.next.threadId;
				Integer x = head.next.key;
				Result res = new Result(tId, x);
				head = head.next;
				return res;
			}
		} finally {
			deqLock.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		return head.next == null;
	}

}
