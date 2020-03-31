package tabu_search;

import java.util.HashMap;
import java.util.Map;

public class TabuList<T> {
	private Map<T, Integer> tabulist;

	/**
	 * Tabu List Constructor
	 */
	public TabuList() {
		tabulist = new HashMap<>();
	}

	/**
	 * Adds element to tabu list for a specific duration
	 *
	 * @param element
	 * @param duration
	 */
	public void add(T element, int duration) {
		if (duration > 0) {
			tabulist.put(element, duration);
		}
	}

	/**
	 * Updates tabu list
	 */
	public void update() {
		tabulist.entrySet().removeIf(entry -> entry.getValue().equals(0));
		tabulist.entrySet().forEach(entry -> tabulist.replace(entry.getKey(), entry.getValue() - 1));	
	}

	/**
	 * Cheks if tabu list contains an element
	 *
	 * @param element
	 * @return true if yes, false otherwise
	 */
	public boolean contains(T element) {
		return tabulist.containsKey(element);
	}

	/**
	 * @return size of tabu list
	 */
	public int size() {
		return tabulist.size();
	}

	/**
	 * Clears tabu list
	 *
	 * @return list size before being cleared
	 */
	public int clear() {
		int size = size();
		tabulist.clear();
		return size;
	}

}