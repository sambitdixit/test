package com.sambit.testutils.common;

import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A custom GUID generator used to geenrate guid based on macaddress + process
 * id + random number + current time + counter. This has been moved from
 * soari-wrapper module to testutils. Usage of this class should be limited to
 * testing sake only.
 * 
 * 
 * @author sambit dikshit
 * 
 */
@Deprecated
public final class GuidGenerator {

	private static final Logger logger = LoggerFactory
			.getLogger(GuidGenerator.class);
	private static final String UTF8 = "UTF-8";

	/** singleton instance. */
	private static GuidGenerator instance = new GuidGenerator();

	/** The mac address. */
	private static String macAddress = null;

	/** The process id. */
	private static String processId = null;

	/** The counter. */
	private AtomicLong counter = new AtomicLong(0);

	/** The random. */
	private static SecureRandom random = null;

	static {

		try {
			NetworkInterface ni = getNetworkInterface();
			byte[] mac = ni.getHardwareAddress();
			macAddress = (mac != null) ? new String(mac, UTF8)
					: "AA:BB:CC:DD:EE"; // This is hard coded to avoid cases
										// where it didnt find any mac address
										// like wifi n/w.
			processId = ManagementFactory.getRuntimeMXBean().getName();
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		}

	}

	private GuidGenerator() {

	}

	/**
	 * Returns available NetworkInterface
	 * 
	 * @return
	 */
	private static NetworkInterface getNetworkInterface() {
		NetworkInterface ni = null;
		Enumeration<NetworkInterface> nis = null;
		try {
			nis = NetworkInterface.getNetworkInterfaces();
			while (nis.hasMoreElements()) {
				NetworkInterface n = nis.nextElement();
				if (n != null) {
					ni = n;
					break;
				} else {
					continue;
				}
			}
		} catch (SocketException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		}

		return ni;
	}

	/**
	 * Returns GuidGenerator instance.
	 * 
	 * @return
	 */
	public static GuidGenerator getInstance() {
		return instance;
	}

	/**
	 * Gets the uuid.
	 * 
	 * @return the uuid
	 */
	public String getRawUUID() {
		StringBuilder sb = new StringBuilder();
		sb.append(macAddress).append("-").append(processId).append("-")
				.append(random.nextDouble()).append("-")
				.append(System.currentTimeMillis()).append("-")
				.append(counter.incrementAndGet());
		return sb.toString();
	}

	/**
	 * 
	 * @return
	 */
	public String getDefaultJDKUUID() {
		return UUID.randomUUID().toString();
	}

}