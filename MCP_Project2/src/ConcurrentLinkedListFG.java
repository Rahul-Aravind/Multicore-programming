
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentLinkedListFG implements ConcurrentLinkedListInterface {

	@Override
	public void printList() {
		NodeFG temp = head.next;
		while (temp != tail) {
			System.out.print(temp.key);
			temp = temp.next;
			if (temp != tail) {
				System.out.print(" ---> ");
			}
		}
		System.out.println();
	}

	class FindResult {

		NodeFG pred, cur;

		FindResult(NodeFG p, NodeFG c) {
			pred = p;
			cur = c;
		}
	}

	class NodeFG {

		int key;
		NodeFG next;
		boolean marked;
		Lock lock;

		NodeFG(int val) {
			key = val;
			lock = new ReentrantLock();
			marked = false;
		}
	}

	private NodeFG head;
	private NodeFG tail;

	public NodeFG getHead() {
		return head;
	}

	public NodeFG getTail() {
		return tail;
	}

	public ConcurrentLinkedListFG() {
		head = new NodeFG(Integer.MIN_VALUE);
		tail = new NodeFG(Integer.MAX_VALUE);
		head.next = tail;
		tail.next = null;
	}

	boolean validateOperation(NodeFG pred, NodeFG cur) {
		return !pred.marked && !cur.marked && pred.next == cur;
	}

	public FindResult find(int key) {
		NodeFG cur = head.next;
		NodeFG pred = head;
		while (pred != tail && cur.key < key) {
			cur = cur.next;
			pred = pred.next;
		}
		return new FindResult(pred, cur);
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
	public boolean testCorrectness() {
		NodeFG temp = head.next;
		NodeFG pre = null;
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
		while (true) {
			FindResult findR = find(key);
			findR.pred.lock.lock();
			findR.cur.lock.lock();
			try {
				if (validateOperation(findR.pred, findR.cur)) {

					if (findR.cur.key == key) {
						return false;
					} else {
						NodeFG newNode = new NodeFG(key);
						newNode.next = findR.cur;
						findR.pred.next = newNode;
						return true;
					}
				}
			} finally {
				findR.cur.lock.unlock();
				findR.pred.lock.unlock();
			}
		}
	}

	@Override
	public boolean delete(int key) {
		while (true) {
			FindResult findR = find(key);
			findR.pred.lock.lock();
			findR.cur.lock.lock();

			try {
				if (validateOperation(findR.pred, findR.cur)) {

					if (findR.cur.key == key) {
						findR.cur.marked = true;
						findR.pred.next = findR.cur.next;
						return true;
					} else {
						return false;
					}
				}
			} finally {
				findR.cur.lock.unlock();
				findR.pred.lock.unlock();
			}
		}
	}
}
