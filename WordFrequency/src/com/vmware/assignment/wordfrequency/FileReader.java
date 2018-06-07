package com.vmware.assignment.wordfrequency;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileReader implements Runnable{

	private final String filePath;
	private final ConcurrentMap<String, Integer> map;
	ExecutorService pool = Executors.newFixedThreadPool(4);
	public FileReader(String fileName, ConcurrentMap<String, Integer> map)
	{
		this.map = map;
		this.filePath = fileName;
	}
	
	@Override
	public void run() {
		List<String> lines = new ArrayList<>();
		try {
			lines = Files.readAllLines(Paths.get(filePath),Charset.forName("ISO-8859-1"));
		} catch (IOException e) {			
			e.printStackTrace();
		}
		for (String eachLine : lines) {
			//Strip off Punctuation and convert to lower case
			eachLine = eachLine.replaceAll("\\p{Punct}", "").toLowerCase();
			pool.submit(new WordFrequencyCalculator(eachLine, map));
		}
		
		pool.shutdown();
		try {
			pool.awaitTermination(4, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println("Pool interrupted!");
			System.exit(1);
		}
	}

}
