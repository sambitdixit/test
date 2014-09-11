package com.sambit.testutils.common.test.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

/**
 * Utility class to allocate ports dynamically. Helpful during integration
 * testing with randomly allocated ports.
 * 
 * @author sambitdikshit
 * 
 */
public final class PortUtil {

	private static final String TEST_UTIL_PORT = "testutil.ports.";
	private static boolean useRandomPorts = Boolean.getBoolean("useRandomPorts");
	private static int portNum = 63000; // Default port
	private static Properties ports = new Properties();

	private PortUtil() {
		// Complete
	}

	/**
	 * @return
	 */
	public static Properties getAllPorts() {
		return ports;
	}

	/**
	 * Returns a port for specified class. 
	 * 
	 * @param cls
	 * @return
	 */
	public static String getPortNumber(Class<?> cls) {
		return getPortNumber(cls.getSimpleName());
	}

	/**
	 * @param cls
	 * @param count
	 * @return
	 */
	public static String getPortNumber(Class<?> cls, int count) {
		return getPortNumber(cls.getSimpleName() + "." + count);
	}

	/**
	 * Gets a port number for specified name. 
	 * 
	 * @param name
	 * @return
	 */
	public static String getPortNumber(String name) {
		String p = ports.getProperty(TEST_UTIL_PORT + name);
		if (p == null) {
			p = System.getProperty(TEST_UTIL_PORT + name);
			if (p != null) {
				ports.setProperty(TEST_UTIL_PORT + name, p);
			}
		}
		if (p == null) {
			if (useRandomPorts) {
				try {
					ServerSocket sock = new ServerSocket(0);
					p = Integer.toString(sock.getLocalPort());
					sock.close();
				} catch (IOException ex) {
					//
				}
			} else {
				p = Integer.toString(portNum++);
			}
			ports.put(TEST_UTIL_PORT + name, p);
			System.setProperty(TEST_UTIL_PORT + name, p);
		}
		return p;
	}
}
