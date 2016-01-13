import java.util.*;

class locationStats {

	public HashMap<Point, Integer> locationFrequencies;

	public locationStats() {
		locationFrequencies = new HashMap<Point, Integer>();
	}

	public HashMap<Point, Integer> getSortedLocations() {

		return sortByValues(locationFrequencies);

	}

	private HashMap sortByValues(HashMap map) {
		List list = new LinkedList(map.entrySet());

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}

		return sortedHashMap;
	}

}