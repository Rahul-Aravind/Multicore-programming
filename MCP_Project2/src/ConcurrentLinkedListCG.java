
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentLinkedListCG implements ConcurrentLinkedListInterface {

	@Override
	public void printList() {
		NodeCG temp = head.next;
		while (temp != tail) {
			System.out.print(temp.key);
			temp = temp.next;
			if (temp != tail) {
				System.out.print(" ---> ");
			}
		}
		System.out.println();
	}

	@Override
	public boolean testCorrectness() {
		NodeCG temp = head.next;
		NodeCG pre = null;
		while (temp != tail) {
			pre = temp;
			temp = temp.next;
			if (temp != tail) {
				if (pre.key > temp.key) {
					return false;
				}
			}
		}
		return true;
	}

	class FindResult {

		NodeCG pred, cur;

		FindResult(NodeCG p, NodeCG c) {
			pred = p;
			cur = c;
		}
	}

	class NodeCG {

		int key;
		NodeCG next;

		NodeCG(int val) {
			key = val;
		}
	}

	private NodeCG head;
	private NodeCG tail;
	private Lock lock;

	public NodeCG getHead() {
		return head;
	}

	public NodeCG getTail() {
		return tail;
	}

	public ConcurrentLinkedListCG() {
		head = new NodeCG(Integer.MIN_VALUE);
		tail = new NodeCG(Integer.MAX_VALUE);
		lock = new ReentrantLock();
		head.next = tail;
		tail.next = null;
	}

	public FindResult find(int key) {
		NodeCG cur = head.next;
		NodeCG pred = head;
		while (pred != tail && cur.key < key) {
			cur = cur.next;
			pred = pred.next;
		}
		return new FindResult(pred, cur);
	}

	@Override
	public boolean search(int key) {
		FindResult findR = find(key);
		if (findR.cur.key == key) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean insert(int key) {
		lock.lock();
		try {
			FindResult findR = find(key);
			if (findR.cur.key == key) {
				return false;
			} else {
				NodeCG newNode = new NodeCG(key);
				newNode.next = findR.cur;
				findR.pred.next = newNode;
				return true;
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void preCookList() {
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			int key = random.nextInt(501);
			insert(key);
		}
	}

	@Override
	public boolean delete(int key) {
		lock.lock();
		try {
			FindResult findR = find(key);
			if (findR.cur.key == key) {
				findR.pred.next = findR.cur.next;
				return true;
			} else {
				return false;
			}
		} finally {
			lock.unlock();
		}
	}

}
