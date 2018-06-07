package com.vmware.assignment.wordfrequency;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordFrequencyExecutor {

	public static void main(String[] args) {

		// Step 1: get the files from arguments. perform validations
		// Step 2 : Assign each file to a thread. perform validations
		// >> Each thread has to perform below
		// Step 3 : Read File and fetch the lines.
		//>> Each Line to Thread.
		// Step 4 : convert to lower case and strip off the punctuation.
		// Step 5 : Update Concurrent Hash Map with word as key and count as value.		
		if(args.length == 0) 
		{
			System.err.println("Please Input the files");
			return;
		}
		ExecutorService executor = Executors.newFixedThreadPool(args.length);
		
		//Execute
		ConcurrentMap<String, Integer> wordCountMap = execute(args, executor);	
		
		// Shutdown
		shutdownExecutor(executor);
		
		// Print the Result
		printResult(wordCountMap);
	}

	

	public static ConcurrentMap<String, Integer> execute(String[] args, ExecutorService executor) {
		ConcurrentMap<String, Integer> wordCountMap = new ConcurrentHashMap<String, Integer>();
		for (String eachFile : args) {
			if(Files.exists(Paths.get(eachFile.trim())))
			{
				executor.submit(new FileReader(eachFile.trim(), wordCountMap));	
			}
			else
			{
				System.err.println("File Name" + eachFile + "does not exist");
			}
			
		}
		return wordCountMap;
	}

	private static void shutdownExecutor(ExecutorService executor) {
		try {
			executor.shutdown();
			// The wait time can be increased.
			executor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("tasks interrupted");
			System.exit(1);
		}
		finally {
		    if (!executor.isTerminated()) {
		        System.err.println("cancel non-finished tasks");
		    }
		    executor.shutdownNow();		    
		}
	}
	
	public static void printResult(ConcurrentMap<String, Integer> wordCountMap) {
		for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
			int count = entry.getValue();
			
			System.out.format("%-10s %d\n", entry.getKey(), count);

		}
	}

}
