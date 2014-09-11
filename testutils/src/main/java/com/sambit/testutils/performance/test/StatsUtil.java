package com.sambit.testutils.performance.test;

import static java.lang.Math.floor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to capture performance stats like heap usage, cpu usage and
 * response time.
 * 
 * @author sambitdikshit
 * 
 */
public final class StatsUtil {
	
	private static Logger logger = LoggerFactory.getLogger("performance");
	
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private static Map<String, List<Double>> timeStats = new ConcurrentHashMap<String, List<Double>>();
	private static Map<String, List<Double>> heapStats = new ConcurrentHashMap<String, List<Double>>();
	private static Map<String, List<Double>> cpuStats = new ConcurrentHashMap<String, List<Double>>();
	private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("##.###");

	private StatsUtil() {
	}

	
	/**
	 * @param statsMap
	 * @param sprint
	 * @param userStory
	 * @param tag
	 * @param delta
	 */
	public static void addStats(Map<String, List<Double>> statsMap, String tag,
			double delta) {
		String key = getKey(tag);
		List<Double> stats = statsMap.get(key);
		if (stats != null) {
			stats.add(delta);
		} else {
			stats = Collections.synchronizedList(new ArrayList<Double>());
			stats.add(delta);
			statsMap.put(key, stats);
		}
	}

	/**
	 * @param sprint
	 * @param userStory
	 * @param tag
	 * @return
	 */
	private static String getKey(String tag) {
		StringBuilder sb = new StringBuilder();
		sb.append(tag);
		return sb.toString();
	}

	/**
	 * @param header
	 * @param statsMap
	 * @param unit
	 * @return
	 */
	public static String getStats(String header,
			Map<String, List<Double>> statsMap, String unit) {
		StringBuilder sb = new StringBuilder();

		if (statsMap != null && !statsMap.isEmpty()) {
			String uf = "ms";
			if (unit != null && unit.equals("D")) {
				uf = "KB";
			}
			sb.append("Performance Statistics-" + header + "-" + NEWLINE);
			sb.append(String.format("%-68s%12s%12s%12s%12s%12s%12s%12s%12s%n", "Tag",
					"Mean(" + uf + ")", "Min(" + uf + ")", "Max(" + uf + ")",
					"Std Dev","Median","75%","95%","Count"));
			for (Map.Entry<String, List<Double>> entry : statsMap.entrySet()) {
				String tag = entry.getKey();
				List<Double> origStats = entry.getValue();
				List<Double> stats = new ArrayList<Double>(origStats);
				if(stats!=null){
					Collections.sort(stats);
				}
				int count = stats.size();
				double mean = getMean(stats);
				double min = getMin(stats);
				double max = getMax(stats);
				double stddev = getStdDev(stats);
				double median = getMedian(stats);
				double sevenfivePercentile=get75thPercentile(stats);
				double ninefivePercentile = get95thPercentile(stats);
				sb.append(String.format("%-68s%12s%12s%12s%12s%12s%12s%12s%12s%n", tag,
						NUMBER_FORMAT.format(mean), NUMBER_FORMAT.format(min),
						NUMBER_FORMAT.format(max),
						NUMBER_FORMAT.format(stddev),
						NUMBER_FORMAT.format(median),
						NUMBER_FORMAT.format(sevenfivePercentile),
						NUMBER_FORMAT.format(ninefivePercentile),
						NUMBER_FORMAT.format(count)));
			}
		}
		return sb.toString();
	}

	/**
	 * @param tag
	 * @param d
	 */
	public static void addTimeStats(String tag, double d) {
		addStats(timeStats, tag, d);
	}

	/**
	 * @param tag
	 * @param d
	 */
	public static void addHeapStats(String tag, double d) {
		addStats(heapStats, tag, d);
	}

	/**
	 * @param tag
	 * @param d
	 */
	public static void addCpuStats(String tag, double d) {
		addStats(cpuStats, tag, d);
	}

	/**
	 * 
	 */
	public static void logTimeStats() {
		log(getStats("Execution Time", timeStats, "T"));
		timeStats.clear();
	}

	/**
	 * 
	 */
	public static void logHeapStats() {
		log(getStats("Heap Usage", heapStats, "D"));
		heapStats.clear();
	}

	/**
	 * 
	 */
	public static void logCpuStats() {
		log(getStats("CPU Time Usage", cpuStats, "T"));
		cpuStats.clear();
	}

    /**
     *
     */
	public static void logMetrics() {
		logTimeStats();
		logHeapStats();
		logCpuStats();
	}

	 /**
     * Returns the value at the given quantile.
     *
     * @param quantile    a given quantile, in {@code [0..1]}
     * @return the value in the distribution at {@code quantile}
     */
	private static double getValue(List<Double> values, double quantile) {
		
		if (quantile < 0.0 || quantile > 1.0) {
			throw new IllegalArgumentException(quantile + " is not in [0..1]");
		}
		int size = values.size();

		if (size == 0) {
			return 0.0;
		}

		final double pos = quantile * (size + 1);

		if (pos < 1) {
			return values.get(0);
		}

		if (pos >= size) {
			return values.get(size - 1);
		}
		final double lower = values.get((int) pos - 1);
		final double upper = values.get((int) pos);
		return lower + (pos - floor(pos)) * (upper - lower);
	}
	
	  /**
     * Returns the median value in the distribution.
     *
     * @return the median value
     */
    public static double getMedian(List<Double> values) {
        return getValue(values,0.5);
    }

    /**
     * Returns the value at the 75th percentile in the distribution.
     *
     * @return the value at the 75th percentile
     */
    public static double get75thPercentile(List<Double> values) {
        return getValue(values,0.75);
    }

    /**
     * Returns the value at the 95th percentile in the distribution.
     *
     * @return the value at the 95th percentile
     */
    public static double get95thPercentile(List<Double> values) {
        return getValue(values,0.95);
    }

    /**
     * Returns the value at the 98th percentile in the distribution.
     *
     * @return the value at the 98th percentile
     */
    public static double get98thPercentile(List<Double> values) {
        return getValue(values,0.98);
    }

    /**
     * Returns the value at the 99th percentile in the distribution.
     *
     * @return the value at the 99th percentile
     */
    public static double get99thPercentile(List<Double> values) {
        return getValue(values,0.99);
    }

    /**
     * Returns the value at the 99.9th percentile in the distribution.
     *
     * @return the value at the 99.9th percentile
     */
    public static double get999thPercentile(List<Double> values) {
        return getValue(values,0.999);
    }
    
	 /**
     * Returns the highest value in the snapshot.
     *
     * @return the highest value
     */
    public static double getMax(List<Double> values) {
        int size = values.size();
    	if (size == 0) {
            return 0;
        }
        return values.get(size-1);
    }

    /**
     * Returns the lowest value in the snapshot.
     *
     * @return the lowest value
     */
    public static double getMin(List<Double> values) {
        int size = values.size();
    	if (size == 0) {
            return 0;
        }
        return values.get(0);
    }

    /**
     * Returns the arithmetic mean of the values in the snapshot.
     *
     * @return the arithmetic mean
     */
    public static double getMean(List<Double> values) {
       int size = values.size();
    	if (size == 0) {
            return 0;
        }
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / size;
    }

    /**
     * Returns the standard deviation of the values in the snapshot.
     *
     * @return the standard value
     */
    public static double getStdDev(List<Double> values) {
        // two-pass algorithm for variance, avoids numeric overflow
    	int size = values.size();
        if (size <= 1) {
            return 0;
        }
        final double mean = getMean(values);
        double sum = 0;

        for (double value : values) {
            final double diff = value - mean;
            sum += diff * diff;
        }
        final double variance = sum / (size - 1);
        return Math.sqrt(variance);
    }
    
    
	/**
	 * prints log message. 
	 * 
	 * @param msg
	 */
	private static void log(String msg) {
		if(logger.isDebugEnabled()) {logger.debug(msg);}
		//System.out.println(msg);
	}
}
