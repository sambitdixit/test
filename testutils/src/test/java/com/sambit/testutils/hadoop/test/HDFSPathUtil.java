package com.sambit.testutils.hadoop.test;

import java.io.File;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sambitdixit
 * 
 */
public abstract class HDFSPathUtil {

	private static Logger log = LoggerFactory.getLogger(HDFSPathUtil.class);

	public static String targetDirectoryFormat = "%1$tY/%1$tm/%1$td";

	/**
	 * @param pathFormat
	 * @return
	 */
	public static String formatByDate(String pathFormat) {
		long st = System.currentTimeMillis();
		if (pathFormat == null || pathFormat.length() == 0) {
			return "";
		}
		pathFormat = pathFormat.replace('/', File.separatorChar);
		StringBuilder strBuffer = new StringBuilder();
		Formatter formatter = new Formatter(strBuffer, Locale.US);
		Date date = new Date();
		formatter.format(pathFormat, date);
		if (!pathFormat.endsWith(File.separator)) {
			strBuffer.append(File.separator);
		}
		long et = System.currentTimeMillis();
		log.info("Time taken by formatByDate is {} ms", (et - st));
		return strBuffer.toString();
	}

	/**
	 * @param fileName
	 * @param fileSuffix
	 * @param directoryPath
	 * @param separateByDate
	 * @return
	 */
	public static String getFilePath(String fileName, String fileSuffix,
			String directoryPath, boolean separateByDate) {
		long st = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder(directoryPath);
		if (!sb.toString().endsWith(File.separator)) {
			sb.append(File.separator);
		}
		if (separateByDate) {
			sb.append(formatByDate(targetDirectoryFormat));
		}
		if (!sb.toString().endsWith(File.separator)) {
			sb.append(File.separator);
		}
		sb.append(fileName);
		sb.append(".");
		sb.append(fileSuffix);
		long et = System.currentTimeMillis();
		log.info("Time taken by formatByDate is {} ms", (et - st));
		return sb.toString();
	}

}
