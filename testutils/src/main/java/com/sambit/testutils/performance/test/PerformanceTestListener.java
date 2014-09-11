package com.sambit.testutils.performance.test;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.sambit.testutils.common.test.utils.RandomDataUtil;
import com.sambit.testutils.performance.test.annotation.PerformanceTest;
import com.sun.management.OperatingSystemMXBean;

/**
 * Custom lister for testng to capture performance metrics.
 * 
 * @author sambitdikshit
 * 
 */
public class PerformanceTestListener implements ITestListener {
	private Runtime runtime = Runtime.getRuntime();
	private OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	private RuntimeMXBean runbean = ManagementFactory.getRuntimeMXBean();
	private int nCPUs = osbean.getAvailableProcessors();
	private static final long NANOS_IN_A_MILLI = 1000000L;

	/*
	 * @Override public void beforeInvocation(IInvokedMethod method, ITestResult
	 * testResult) { PerformanceTest p = method.getTestMethod().getMethod()
	 * .getAnnotation(PerformanceTest.class); if (p != null &&
	 * p.measureExecutionTime().equals("Y")) { sprintName = p.sprintName();
	 * userStory = p.userStory(); prevTime = System.nanoTime(); doEval = true; }
	 * 
	 * }
	 * 
	 * @Override public void afterInvocation(IInvokedMethod method, ITestResult
	 * testResult) { if (doEval) { long currTime = System.nanoTime(); double
	 * diff = (currTime - prevTime)/NANOS_IN_A_MILLI; //double diff = currTime -
	 * prevTime; String tag = method.getTestMethod().getMethodName();
	 * StatsUtil.addTimeStats(sprintName,userStory,tag, diff); prevTime = 0;
	 * doEval = false; }
	 * 
	 * }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.ITestListener#onTestStart(org.testng.ITestResult)
	 */
	public void onTestStart(ITestResult result) {

		PerformanceTest p = result.getMethod().getConstructorOrMethod()
				.getMethod().getAnnotation(PerformanceTest.class);
		if (p != null) {
			if (p.measureExecutionTime().equals("Y")) {
				long prevTime = System.nanoTime();
				result.setAttribute("prevTime", prevTime);
				result.setAttribute("doTimeEval", true);
			}
			if (p.measureHeapUsage().equals("Y")) {
				double prevHeapSize = ((double) (runtime.totalMemory() - runtime
						.freeMemory()) / (double) (RandomDataUtil.KB * RandomDataUtil.KB));
				result.setAttribute("prevHeapSize", prevHeapSize);
				result.setAttribute("doHeapEval", true);
			}
			if (p.measureCPUUsage().equals("Y")) {
				long prevCpuUpTime = runbean.getUptime();
				long prevProcessCpuTime = osbean.getProcessCpuTime();
				result.setAttribute("prevCpuUpTime", prevCpuUpTime);
				result.setAttribute("prevProcessCpuTime", prevProcessCpuTime);
				result.setAttribute("doCpuEval", true);
			}
		}

	}

	public void onTestSuccess(ITestResult result) {

		Object te = result.getAttribute("doTimeEval");
		boolean doTimeEval = (te != null) ? true : false;

		Object he = result.getAttribute("doHeapEval");
		boolean doHeapEval = (he != null) ? true : false;

		Object ce = result.getAttribute("doCpuEval");
		boolean doCpuEval = (ce != null) ? true : false;

		String tag = result.getMethod().getConstructorOrMethod()
				.getDeclaringClass().getSimpleName()
				+ "." + result.getMethod().getMethodName();

		if (doTimeEval) {
			long currTime = System.nanoTime();
			long prevTime = (Long) result.getAttribute("prevTime");
			double diff = (double) (currTime - prevTime)
					/ (double) NANOS_IN_A_MILLI;
			// double diff = currTime - prevTime;
			StatsUtil.addTimeStats(tag, diff);
			prevTime = 0;
			doTimeEval = false;
		}
		if (doHeapEval) {
			double javaheap = 0.0;
			double prevHeapSize = (Double) result.getAttribute("prevHeapSize");
			double currentHeapSize = ((double) (runtime.totalMemory() - runtime
					.freeMemory()) / (double) (RandomDataUtil.KB * RandomDataUtil.KB));
			if (prevHeapSize > 0L && currentHeapSize > prevHeapSize) {
				javaheap = currentHeapSize - prevHeapSize;
			} else {
				javaheap = 0.0;
			}
			StatsUtil.addHeapStats(tag, javaheap);
			doHeapEval = false;
		}
		if (doCpuEval) {
			long upTime = runbean.getUptime();
			long processCpuTime = osbean.getProcessCpuTime();
			double javacpu = 0.0;
			long prevCpuUpTime = (Long) result.getAttribute("prevCpuUpTime");
			long prevProcessCpuTime = (Long) result
					.getAttribute("prevProcessCpuTime");
			if (prevCpuUpTime > 0L && upTime > prevCpuUpTime) {
				long elapsedCpu = processCpuTime - prevProcessCpuTime;
				long elapsedTime = upTime - prevCpuUpTime;
				javacpu = Math.min(99F, elapsedCpu
						/ (elapsedTime * 10000F * nCPUs));
			} else {
				javacpu = 0.001;
			}
			StatsUtil.addCpuStats(tag, javacpu);
			doCpuEval = false;
		}

	}

	public void onTestFailure(ITestResult result) {

		Object te = result.getAttribute("doTimeEval");
		boolean doTimeEval = (te != null) ? true : false;

		Object he = result.getAttribute("doHeapEval");
		boolean doHeapEval = (he != null) ? true : false;

		Object ce = result.getAttribute("doCpuEval");
		boolean doCpuEval = (ce != null) ? true : false;
		String tag = result.getMethod().getConstructorOrMethod()
				.getDeclaringClass().getSimpleName()
				+ "." + result.getMethod().getMethodName();

		if (doTimeEval) {
			long currTime = System.nanoTime();
			long prevTime = (Long) result.getAttribute("prevTime");
			double diff = (double) (currTime - prevTime)
					/ (double) NANOS_IN_A_MILLI;
			// double diff = currTime - prevTime;
			StatsUtil.addTimeStats(tag, diff);
			prevTime = 0;
			doTimeEval = false;
		}
		if (doHeapEval) {
			double javaheap = 0.0;
			double prevHeapSize = (Double) result.getAttribute("prevHeapSize");
			double currentHeapSize = ((double) (runtime.totalMemory() - runtime
					.freeMemory()) / (double) (RandomDataUtil.KB * RandomDataUtil.KB));
			if (prevHeapSize > 0L && currentHeapSize > prevHeapSize) {
				javaheap = currentHeapSize - prevHeapSize;
			} else {
				javaheap = 0.0;
			}
			StatsUtil.addHeapStats(tag, javaheap);
			doHeapEval = false;
		}
		if (doCpuEval) {
			long upTime = runbean.getUptime();
			long processCpuTime = osbean.getProcessCpuTime();
			double javacpu = 0.0;
			long prevCpuUpTime = (Long) result.getAttribute("prevCpuUpTime");
			long prevProcessCpuTime = (Long) result
					.getAttribute("prevProcessCpuTime");
			if (prevCpuUpTime > 0L && upTime > prevCpuUpTime) {
				long elapsedCpu = processCpuTime - prevProcessCpuTime;
				long elapsedTime = upTime - prevCpuUpTime;
				javacpu = Math.min(99F, elapsedCpu
						/ (elapsedTime * 10000F * nCPUs));
			} else {
				javacpu = 0.001;
			}
			StatsUtil.addCpuStats(tag, javacpu);
			doCpuEval = false;
		}

	}

	public void onTestSkipped(ITestResult result) {

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onStart(ITestContext context) {

	}

	public void onFinish(ITestContext context) {
		//StatsUtil.logTimeStats();
		//StatsUtil.logHeapStats();
		//StatsUtil.logCpuStats();
		StatsUtil.logMetrics();
	}

}
