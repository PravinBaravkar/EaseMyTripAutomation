package uitest.easymytrip.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Utility {

	public String getMonthFromdepartureDate(String departureDate) {
		String[] date = departureDate.split("/");

		if (date[1].length() != 2 || Integer.parseInt(date[1]) < 1 || Integer.parseInt(date[1]) > 12)
			throw new ArithmeticException("Invalid Month. Must be between 01 and 12");

		return date[1];

	}

	public String getYearFromdepartureDate(String departureDate) {
		String[] date = departureDate.split("/");

		if (date[2].length() != 4)
			throw new ArithmeticException("Invalid Year. Must be 4-digit value");
		return date[2];

	}

	public int getMaxDaysOfMonth(String month) {
		int maxDays = 0;

		switch (month) {
		case "01":
		case "03":
		case "05":
		case "07":
		case "08":
		case "10":
		case "12":
			maxDays = 31;
			break;
		case "04":
		case "06":
		case "09":
		case "11":
			maxDays = 30;
			break;
		case "02":
			maxDays = 28;
			break;

		}
		return maxDays;
	}

	public HashMap<String, Double> getSortedMapByValues(HashMap<String, Double> hashMap) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(hashMap.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> map1, Map.Entry<String, Double> map2) {
				return (map1.getValue()).compareTo(map2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> sortedEntry : list) {
			sortedMap.put(sortedEntry.getKey(), sortedEntry.getValue());
		}
		return sortedMap;
	}

	public void printMapContents(HashMap<String, Double> hashMap) {

		for (Map.Entry<String, Double> entry : hashMap.entrySet()) {
			System.out.println("Day " + entry.getKey() + " : Price " + entry.getValue());
		}
	}
}