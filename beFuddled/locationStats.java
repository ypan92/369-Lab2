import java.util.*;

class locationStats {

	public HashMap<String, Integer> locationFrequencies;

	public locationStats() {
		locationFrequencies = new HashMap<String, Integer>();
	}

	public HashMap<String, Integer> getSortedLocations() {

		return sortByValues(locationFrequencies);

	}

	public HashMap sortByValues(HashMap map) {
		List list = new LinkedList(map.entrySet());

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
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