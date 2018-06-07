package com.vmware.assignment.wordfrequency;

import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentMap;

public class WordFrequencyCalculator implements Runnable {

	private final String buffer;
	private final ConcurrentMap<String, Integer> map;

	public WordFrequencyCalculator(String eachLine, ConcurrentMap<String, Integer> m) {
		this.map = m;
		this.buffer = eachLine;
	}

	@Override
	public void run() {
		StringTokenizer st = new StringTokenizer(buffer, " ");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();	
			updateWordCount(token);
		}

	}

	private void updateWordCount(String word) {
		Integer oldVal, newVal;
		Integer count = map.get(word);		
		if (count == null) {			
			oldVal = map.put(word, 1);
			if (oldVal == null)
				return;
		}		
		do {
			oldVal = map.get(word);
			newVal = (oldVal == null) ? 1 : (oldVal + 1);
		} while (!map.replace(word, oldVal, newVal));
	}

}
