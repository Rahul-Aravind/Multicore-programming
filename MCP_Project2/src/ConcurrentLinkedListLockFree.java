import java.util.Random;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class ConcurrentLinkedListLockFree implements ConcurrentLinkedListInterface {

	class Node {

		int key;
		AtomicMarkableReference<Node> next;

		Node(int val) {
			key = val;
			next = new AtomicMarkableReference<>(null, false);
		}
	}

	class FindResult {

		Node pred, cur;

		FindResult(Node p, Node c) {
			pred = p;
			cur = c;
		}
	}

	private Node head;
	private Node tail;

	public ConcurrentLinkedListLockFree() {
		head = new Node(Integer.MIN_VALUE);
		tail = new Node(Integer.MAX_VALUE);
		head.next.set(tail, false);
	}

	public Node getHead() {
		return head;
	}

	public Node getTail() {
		return tail;
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
		Node temp = head.next.getReference();
		Node pre = null;
		while (temp != tail) {
			pre = temp;
			temp = temp.next.getReference();
			if (temp != tail) {
				if (pre.key > temp.key) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void printList() {
		Node temp = head.next.getReference();
		while (temp != tail) {
			System.out.print(temp.key);
			temp = temp.next.getReference();
			if (temp != tail) {
				System.out.print(" ---> ");
			}
		}
		System.out.println();
	}

	public FindResult find(int key) {
		Node pred = null;
		Node curr = null;
		Node succ = null;

		boolean[] marked = { false };
		boolean flag;

		retry: while (true) {
			pred = head;
			curr = pred.next.getReference();

			while (true) {
				succ = curr.next.get(marked);
				while (marked[0]) {
					flag = pred.next.compareAndSet(curr, succ, false, false);
					if (!flag) {
						continue retry;
					}
					curr = succ;
					curr.next.get(marked);
				}

				if (curr.key >= key) {
					return new FindResult(pred, curr);
				}
				pred = curr;
				curr = succ;
			}
		}
	}

	@Override
	public boolean search(int key) {
		FindResult res = find(key);
		if (res.cur.key == key) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean insert(int key) {
		while (true) {
			FindResult findResult = find(key);
			Node pred = findResult.pred;
			Node curr = findResult.cur;

			if (curr.key == key) {
				return false;
			} else {
				Node node = new Node(key);
				node.next = new AtomicMarkableReference<Node>(curr, false);
				if (pred.next.compareAndSet(curr, node, false, false)) {
					return true;
				}
			}
		}
	}

	@Override
	public boolean delete(int key) {
		boolean flag;

		while (true) {
			FindResult findResult = find(key);
			Node pred = findResult.pred;
			Node curr = findResult.cur;

			if (curr.key != key) {
				return false;
			} else {
				Node succ = curr.next.getReference();
				flag = curr.next.attemptMark(succ, true);
				if (!flag) {
					continue;
				}

				pred.next.compareAndSet(curr, succ, false, false);
				return true;
			}
		}
	}

}
