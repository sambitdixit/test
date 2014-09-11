package com.sambit.testutils.common.test.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class RandomDataUtil.
 */
public final class RandomDataUtil {
	
	public static final int TWO_MB = 1000000;
	public static final int TWENTY_KB = 10220;
	public static final int ONE_KB = 512;
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	public static final int EIGHT = 8;
	public static final int TEN = 10;
	public static final int TWELVE = 12;
	public static final int FIFTEEN = 15;
	public static final int TWENTY = 20;
	public static final int TWENTY_EIGHT = 28;
	public static final int THIRTY_THREE = 33;
	public static final int FOURTY_FIVE = 45;
	public static final int SIXTY_FIVE = 65;
	public static final int NINETY = 90;
	public static final int NINETY_THREE = 93;
	public static final int ONE_TWENTY_EIGHT = 128;
	public static final int TWO_FIFTY_FIVE = 255;
	public static final int FIVE_HUNDRED = 500;
	public static final int THOUSAND = 1000;
	public static final int KB = 1024;
	public static final int NINETEEN_SEVENTY = 1970;
	public static final int TWO_THOUSAND_NINE = 2009;
	
	private RandomDataUtil(){}
	/**
	 * @return
	 */
	public static String get2MBRandomString() { 
		return getRandomString(TWO_MB);
	}
	
	public static String get1MBRandomString() {
		return getRandomString((TWO_MB/TWO));
	}
	/**
	 * @return
	 */
	public static String get6MBRandomString() { 
		return getRandomString((TWO_MB*THREE));
	}
	
	/**
	 * @return
	 */
	public static String get10MBRandomString() { 
		return getRandomString((TWO_MB*FIVE));
	}
	
	/**
	 * @return
	 */
	public static String get1KBRandomString() { 
		return getRandomString((ONE_KB));
	}
	
	/**
	 * @return
	 */
	public static String get2KBRandomString() { 
		return getRandomString((TWENTY_KB/TEN));
	}
	
	/**
	 * @return
	 */
	public static String get10KBRandomString() { 
		return getRandomString((ONE_KB*TEN));
	}
	
	/**
	 * @return
	 */
	public static String get20KBRandomString() { 
		return getRandomString((TWENTY_KB));
	}
	
	/**
	 * @return
	 */
	public static String get60KBRandomString() { 
		return getRandomString((TWENTY_KB*THREE));
	}
	
	/**
	 * @return
	 */
	public static String get100KBRandomString() { 
		return getRandomString((ONE_KB*TWENTY*FIVE));
	}
	
	public static String get500KBRandomString() {
		return getRandomString((ONE_KB*TWENTY*FIVE*FIVE));
	}
	
	/**
	 * @param d
	 * @return
	 */
	public static double getDataSizeInMB(String d) {
		int dataLength = d.length();
		return (EIGHT * (double) ((double)(((dataLength) * 2) + FOURTY_FIVE) / EIGHT))/(double)(KB*THOUSAND);
		
	}

    /**
     *
     * @param d
     * @return
     */
	public static double getDataSizeInKB(String d) {
		int dataLength = d.length();
		return (EIGHT * (double) ((double)(((dataLength) * 2) + FOURTY_FIVE) / EIGHT))/(double)(KB);
	}
	
	/**
	 * Gets the random long.
	 * 
	 * @return the random long
	 */
	public static long getRandomLong() {
		return getRandomLong(1, Long.MAX_VALUE);
	}

	/**
	 * Gets the random int.
	 * 
	 * @return the random int
	 */
	public static int getRandomInt() {
		return getRandomInt(1, Integer.MAX_VALUE);
	}

	/**
	 * Gets the random string.
	 * 
	 * @return the random string
	 */
	public static String getRandomString() {
		int stringLength = (int) (Math.random() * ONE_TWENTY_EIGHT);
		return getRandomString(stringLength);
	}

	/**
	 * Gets the random alpha string.
	 * 
	 * @param length
	 *            the length
	 * @return the random alpha string
	 */
	public static String getRandomAlphaString(int length) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < length; i++) {
			// ascii 60 to 90
			char str = (char) (SIXTY_FIVE + (int) (Math.random() * (NINETY - SIXTY_FIVE)));
			b.append(str);
		}

		return b.toString();
	}

	/**
	 * Gets the random string.
	 * 
	 * @param length
	 *            the length
	 * @return the random string
	 */
	public static String getRandomString(int length) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < length; i++) {
			// ascii 33 (!) to 126 (~)
			char str = (char) (THIRTY_THREE + (int) (Math.random() * NINETY_THREE));
			b.append(str);
		}

		return b.toString();
	}

	/**
	 * Gets the random date.
	 * 
	 * @return the random date
	 */
	public static Date getRandomDate() {
		// month
		int month = getRandomInt(1, TWELVE);
		// day
		int day = getRandomInt(1, TWENTY_EIGHT);

		// year
		int year = getRandomInt(NINETEEN_SEVENTY, TWO_THOUSAND_NINE);

		String dateString = year + "-" + month + "-" + day;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(dateString);
		} catch (ParseException ex) {
			return new Date();
		}
	}

	/**
	 * Gets the random int.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 * @return the random int
	 */
	public static int getRandomInt(int min, int max) {
		return min + (int) ((Math.random() * (max - min)));
	}

	/**
	 * Gets the random long.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 * @return the random long
	 */
	public static long getRandomLong(long min, long max) {
		return min + (long) ((Math.random() * (max - min)));
	}

	/**
	 * Gets the unique random numbers.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 * @param totalCount
	 *            the total count
	 * @return the unique random numbers
	 */
	public static Set<Integer> getUniqueRandomNumbers(int min, int max,
			int totalCount) {
		int spread = max - min;
		if ((max - min) < totalCount) {
			totalCount = spread;
		}
		Set<Integer> output = new HashSet<Integer>();
		while (true) {
			int id = min + (int) Math.floor(((double) spread) * Math.random());
			if (!output.contains(id)) {
				output.add(Integer.valueOf(id));
			}
			if (output.size() >= totalCount) {
				break;
			}
		}
		return output;
	}

	/**
	 * Gets the random email.
	 * 
	 * @return the random email
	 */
	public static String getRandomEmail() {
		return RandomDataUtil.getRandomAlphaString(TEN) + "@"
				+ RandomDataUtil.getRandomAlphaString(FIFTEEN) + ".com";
	}

	/**
	 * Gets the random url.
	 * 
	 * @return the random url
	 */
	public static String getRandomURL() {
		return "http://" + getRandomAlphaString(THREE) + "."
				+ getRandomAlphaString(TEN) + ".com/"
				+ getRandomAlphaString(getRandomInt(THREE, TEN));
	}

	/**
	 * Gets the random words.
	 * 
	 * @param wordCount
	 *            the word count
	 * @return the random words
	 */
	public static String getRandomWords(int wordCount) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < wordCount; i++) {
			if (i > 0) {
				buf.append(" ");
			}
			String word = RandomDataUtil.getRandomAlphaString(RandomDataUtil
					.getRandomInt(FIVE, FIFTEEN));
			if (i == 0) {
				word = Character.toUpperCase(word.charAt(0))
						+ word.substring(1).toLowerCase();
			} else {
				word = word.toLowerCase();
			}
			buf.append(word);
		}
		return buf.toString();
	}

	/**
	 * Gets the random words.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 * @return the random words
	 */
	public static String getRandomWords(int min, int max) {
		int wordsToCreate = getRandomInt(min, max);
		return getRandomWords(wordsToCreate);
	}

	/**
	 * Gets the random ip address.
	 * 
	 * @return the random ip address
	 */
	public static String getRandomIpAddress() {
		return getRandomInt(0, TWO_FIFTY_FIVE) + "." + getRandomInt(0, TWO_FIFTY_FIVE) + "."
				+ getRandomInt(0, TWO_FIFTY_FIVE) + "." + getRandomInt(0, TWO_FIFTY_FIVE);
	}

	/**
	 * Gets the random file.
	 * 
	 * @return the random file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File getRandomFile() throws IOException {
		File file = File.createTempFile("randomDataUtil", "tmp");
		file.deleteOnExit();

		OutputStream out = new FileOutputStream(file);
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(out, "UTF-8");
			writer.write(getRandomWords(RandomDataUtil.getRandomInt(TEN, FIVE_HUNDRED)));
			return file;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
}