package fr.ensisa.hassenforder.tp.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class Differ<X, Y, K> {

	private Collection<X> toDelete = new ArrayList<>();
	private Collection<Y> toAdd = new ArrayList<>();
	private Collection<Pair<X,Y>> toUpdate = new ArrayList<>();

	static public interface Helper<X, Y, K> {
		public K getXKey(X x);
		public K getYKey(Y y);
		public boolean same(X x, Y y);
	}

	public void compute (Collection<X> left, Collection<Y> right, Helper<X, Y, K> helper) {
		Map<K, X> sortedLeft = new TreeMap<>();
		for (X x : left) sortedLeft.put(helper.getXKey(x), x);
		Map<K, Y> sortedRight = new TreeMap<>();
		for (Y y : right) sortedRight.put(helper.getYKey(y), y);
		for (K key : sortedLeft.keySet()) {
			if (! sortedRight.containsKey(key))
				toDelete.add(sortedLeft.get(key));
		}
		for (K key : sortedRight.keySet()) {
			if (! sortedLeft.containsKey(key))
				toAdd.add(sortedRight.get(key));
		}
		for (K key : sortedLeft.keySet()) {
			if (sortedRight.containsKey(key)) {
				X x = sortedLeft.get(key);
				Y y = sortedRight.get(key);
				if (! helper.same(x, y))
					toUpdate.add(new Pair<>(x, y));
			}
		}
	}

	public Collection<X> getToDelete() {
		return toDelete;
	}

	public Collection<Y> getToAdd() {
		return toAdd;
	}

	public Collection<Pair<X, Y>> getToUpdate() {
		return toUpdate;
	}

}
