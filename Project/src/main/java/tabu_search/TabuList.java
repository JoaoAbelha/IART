package tabu_search;

import java.util.HashMap;
import java.util.Map;

public class TabuList<T> {
	private Map<T, Integer> tabulist;
	
	public TabuList() {
		tabulist = new HashMap<>();
	}

	public void add(T element, int duration) {
		if (duration > 0) {
			tabulist.put(element, duration);
		}
	}

	public void update() {
		tabulist.entrySet().removeIf(entry -> entry.getValue().equals(0));
		tabulist.entrySet().forEach(entry -> tabulist.replace(entry.getKey(), entry.getValue() - 1));	
	}
	
	public boolean contains(T element) {
		return tabulist.containsKey(element);
	}
	
	public int size() {
		return tabulist.size();
	}
	
	public int clear() {
		int size = size();
		tabulist.clear();
		return size;
	}

}