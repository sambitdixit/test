package com.sambit.testutils.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

import com.sambit.testutils.common.test.utils.PortUtil;
import com.sambit.testutils.common.test.utils.ServerLauncher;

/**
 * AbstractClientServerTest class to be used from unit test cases. All unit test
 * cases where the requirement is to start and manage the server and client at
 * the same test case level, they can extend this class.
 * 
 * @author sambitdikshit
 * 
 */

public abstract class AbstractClientServerTest extends Assert {
	static {
		System.setProperty("com.sambit.platform.runOnEnv","default");
		System.setProperty("unittest","true");

	}

	private static final Logger logger = LoggerFactory.getLogger(AbstractClientServerTest.class);
	private static List<ServerLauncher> launchers = new ArrayList<ServerLauncher>();

	/**
	 * Stops all running server instances.
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public static void stopAllServers() throws Exception {

		boolean passed = true;
		for (ServerLauncher sl : launchers) {
			try {
				sl.signalStop();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		for (ServerLauncher sl : launchers) {
			try {
				passed = passed && sl.stopServer();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		launchers.clear();
		System.gc();
		assertTrue(passed);
	}

	/**
	 * Starts the server instance inprocess.
	 * 
	 * @param clz
	 * @return
	 */
	public static boolean launchServer(Class<?> clz) {
		boolean ok = false;
		try {
			ServerLauncher sl = new ServerLauncher(clz.getName());
			ok = sl.launchServer();
			assertTrue(ok, "server failed to launch");
			launchers.add(sl);
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("failed to launch server " + clz);
		}

		return ok;
	}

	/**
	 * Starts process with inprocess or outprocess option.
	 * 
	 * @param clz
	 * @param inProcess
	 * @return
	 */
	public static boolean launchServer(Class<?> clz, boolean inProcess) {
		boolean ok = false;
		try {
			ServerLauncher sl = new ServerLauncher(clz.getName(), inProcess);
			ok = sl.launchServer();
			assertTrue(ok, "server failed to launch");
			launchers.add(sl);
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("failed to launch server " + clz);
		}

		return ok;
	}

	/**
	 * Starts the server instance with JVM properties set in map.
	 * 
	 * @param clz
	 * @param props
	 * @param args
	 * @return
	 */
	public static boolean launchServer(Class<?> clz, Map<String, String> props,
			String[] args) {
		return launchServer(clz, props, args, false);
	}

	/**
	 * Starts the server instance with jvm arguments and properties set.
	 * 
	 * @param clz
	 * @param props
	 * @param args
	 * @param inProcess
	 * @return
	 */
	public static boolean launchServer(Class<?> clz, Map<String, String> props,
			String[] args, boolean inProcess) {
		boolean ok = false;
		try {
			ServerLauncher sl = new ServerLauncher(clz.getName(), props, args,
					inProcess);
			ok = sl.launchServer();
			assertTrue(ok, "server failed to launch");
			launchers.add(sl);
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("failed to launch server " + clz);
		}

		return ok;
	}

	/**
	 * Gets random port for specified string.
	 * 
	 * @param s
	 * @return
	 */
	protected static String allocatePort(String s) {
		return PortUtil.getPortNumber(s);
	}

	/**
	 * Gets random port for specified class.
	 * 
	 * @param cls
	 * @return
	 */
	protected static String allocatePort(Class<?> cls) {
		return PortUtil.getPortNumber(cls);
	}

	/**
	 * Gets port for specified class and counter to increase the port number in
	 * next invocation.
	 * 
	 * @param cls
	 * @param count
	 * @return
	 */
	protected static String allocatePort(Class<?> cls, int count) {
		return PortUtil.getPortNumber(cls, count);
	}

}
