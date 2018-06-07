package com.vmware.assignment.wordfrequency.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.vmware.assignment.wordfrequency.WordFrequencyExecutor;

public class WordJunit {
	private CountDownLatch countdownLatch;
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testExecuteWithInvalidFilePath() {
		String[] args = {"Test Test1"};
		ExecutorService executor = Executors.newFixedThreadPool(args.length);
		ConcurrentMap<String, Integer> map = new WordFrequencyExecutor().execute(args, executor);		
		assertEquals(0, map.size());
	}
	
	@Test
	public void testExecuteWithValidSingleFilePath() {
		countdownLatch = new CountDownLatch(1);
		String absolutePath = "";
		try {
			File temp = File.createTempFile("temp", ".txt" );
			 absolutePath = temp.getAbsolutePath();
			 List<String> lines = Arrays.asList("I like dogs. Dogs are cute.");
				Path file = Paths.get(absolutePath);	
				Files.write(file, lines, Charset.forName("UTF-8"));
			 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		String[] args = {absolutePath};
		// hello.txt file contails "I like dogs. Dogs are cute."
		ExecutorService executor = Executors.newFixedThreadPool(args.length);
		
		WordFrequencyExecutor word = new WordFrequencyExecutor();
		ConcurrentMap<String, Integer> map = word.execute(args, executor);
		try {
			countdownLatch.await(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//word.printResult(map);
		assertEquals(5, map.size());
		assertEquals(2, (int)map.get("dogs"));
	}

	@Test
	public void testExecuteWithValidMultiFiles() {
		countdownLatch = new CountDownLatch(1);
		String absolutePath = "";
		File temp= null;
		File temp1= null;
		try {
			temp = File.createTempFile("temp", ".txt");
			absolutePath = temp.getAbsolutePath();
			List<String> lines = Arrays.asList("I like dogs. Dogs are cute.");
			Path file = Paths.get(absolutePath);
			Files.write(file, lines, Charset.forName("UTF-8"));

			temp1 = File.createTempFile("temp1", ".txt");
			absolutePath = temp1.getAbsolutePath();
			List<String> lines1 = Arrays.asList("Are these things like the others?");
			Path file1 = Paths.get(absolutePath);
			Files.write(file1, lines1, Charset.forName("UTF-8"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		String[] args = {temp.getAbsolutePath(),temp1.getAbsolutePath()};
		// hello.txt file contails "I like dogs. Dogs are cute."
		ExecutorService executor = Executors.newFixedThreadPool(args.length);
		
		WordFrequencyExecutor word = new WordFrequencyExecutor();
		ConcurrentMap<String, Integer> map = word.execute(args, executor);
		try {
			countdownLatch.await(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//word.printResult(map);
		assertEquals(9, map.size());
		assertEquals(2, (int)map.get("dogs"));
		assertEquals(2, (int)map.get("like"));
	}
}
