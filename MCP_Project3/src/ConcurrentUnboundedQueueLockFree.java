import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentUnboundedQueueLockFree implements ConcurrentUnboundedQueueInterface {

	AtomicReference<Node> head;
	AtomicReference<Node> tail;

	class Node {
		int threadId;
		Integer key;
		AtomicReference<Node> next;
		
		Node(Integer k) {
			key = k;
			next = new AtomicReference<Node>(null);
		}

		Node(int tId, Integer k) {
			threadId = tId;
			key = k;
			next = new AtomicReference<Node>(null);
		}
	}

	public ConcurrentUnboundedQueueLockFree() {
		Node temp = new Node(null);
		head = new AtomicReference<Node>(temp);
		tail = new AtomicReference<Node>(temp);
	}

	@Override
	public void enque(int threadId, int key) {
		Node newN = new Node(threadId, key);
		while (true) {
			Node last = tail.get();
			Node next = last.next.get();
			if (last == tail.get()) {
				if (next == null) {
					boolean res = last.next.compareAndSet(next, newN);
					if (res) {
						tail.compareAndSet(last, newN);
						return;
					}
				} else {
					tail.compareAndSet(last, next);
				}
			}
		}
	}

	@Override
	public Result deque() {
		while (true) {
			Node first = head.get();
			Node last = tail.get();
			Node next = first.next.get();
			if (first == head.get()) {
				if (first == last) {
					if (next == null) {
						return null;
					}
					tail.compareAndSet(last, next);
				} else {
					int value = next.key;
					int tId = next.threadId;
					
					boolean res = head.compareAndSet(first, next);
					if (res) {
						Result resObj = new Result(tId, value);
						return resObj;
					}
				}
			}
		}
	}

	@Override
	public boolean isEmpty() {
		return head.get().next.get() == null; 
	}

}
