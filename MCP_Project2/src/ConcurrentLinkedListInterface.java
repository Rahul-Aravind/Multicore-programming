
public interface ConcurrentLinkedListInterface {

	void preCookList();

	boolean testCorrectness();

	boolean search(int key);

	boolean insert(int key);

	boolean delete(int key);

	void printList();

}