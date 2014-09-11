package com.sambit.testutils.performance.test.annotation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author sambitdikshit
 * 
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ METHOD, TYPE, CONSTRUCTOR })
public @interface PerformanceTest {


	/**
	 * Do you want to capture execution time, if YES, set the value to "Y"
	 */
	String measureExecutionTime() default "N";

	/**
	 * Do you want to capture heap usage, if YES, set the value to "Y"
	 */
	String measureHeapUsage() default "N";

	/**
	 * Do you want to capture cpu usage for the process, if YES, set the value
	 * to "Y"
	 */
	String measureCPUUsage() default "N";

}
