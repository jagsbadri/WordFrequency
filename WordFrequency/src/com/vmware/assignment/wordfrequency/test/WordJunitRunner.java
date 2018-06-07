package com.vmware.assignment.wordfrequency.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class WordJunitRunner {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(WordTS.class);

	      for (Failure failure : result.getFailures()) {
	         System.out.println(failure.toString());
	      }
			
	      System.out.println(result.wasSuccessful());
	}

}
