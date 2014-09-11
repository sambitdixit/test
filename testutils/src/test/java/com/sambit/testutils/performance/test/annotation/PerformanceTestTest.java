package com.sambit.testutils.performance.test.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.sambit.testutils.performance.test.annotation.PerformanceTest;

public class PerformanceTestTest {

  @Test
  public void testDefaultValues() {
	    PerformanceTest p = Defaults.of(PerformanceTest.class);
	   
		Assert.assertNotNull(p);
		Assert.assertEquals(p.measureCPUUsage(), "N");
		Assert.assertEquals(p.measureExecutionTime(), "N");
		Assert.assertEquals(p.measureHeapUsage(), "N");
  }
  
   static class Defaults implements InvocationHandler {
		public static <A extends Annotation> A of(Class<A> annotation) {
			return (A) Proxy.newProxyInstance(annotation.getClassLoader(),
					new Class[] { annotation }, new Defaults());
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			return method.getDefaultValue();
		}
	}
}
