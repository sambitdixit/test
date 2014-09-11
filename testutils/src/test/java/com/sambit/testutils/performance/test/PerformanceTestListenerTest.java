package com.sambit.testutils.performance.test;

import java.lang.reflect.Method;

import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;
import org.testng.Assert;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.ConstructorOrMethod;

import com.sambit.testutils.performance.test.PerformanceTestListener;
import com.sambit.testutils.performance.test.annotation.PerformanceTest;

public class PerformanceTestListenerTest {
	ITestResult result;
	ITestNGMethod testMethod;
	ConstructorOrMethod itc;
	
	@BeforeClass
	public void setup() throws Exception {
		result = Mockito.mock(ITestResult.class);
		testMethod = Mockito.mock(ITestNGMethod.class);
		itc = Mockito.mock(ConstructorOrMethod.class);
	}

	@Test
	public void testPerformanceTestAnnotationListener() {
		Method m = ReflectionUtils.findMethod(Print.class, "print");
		Mockito.when(result.getMethod()).thenReturn(testMethod);
		Mockito.when(testMethod.getConstructorOrMethod()).thenReturn(new ConstructorOrMethod(m));
		Mockito.when(itc.getMethod()).thenReturn(m);
		try {
			PerformanceTestListener ptl = new PerformanceTestListener();
			ptl.onTestStart(result);
			ptl.onTestSuccess(result);
			ptl.onFinish(null);
		} catch (Exception e) {
			Assert.fail("PerformanceTestAnnotationListener Failed", e);
		}

	}

	static class Print {
		
		@PerformanceTest(measureExecutionTime = "Y", measureHeapUsage = "Y", measureCPUUsage = "Y")
		@Test
		public void print() {
			System.out.println("Print hello");
		}

		
	}
}
