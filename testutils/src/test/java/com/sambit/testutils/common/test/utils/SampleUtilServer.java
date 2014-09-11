package com.sambit.testutils.common.test.utils;

import com.sambit.testutils.common.test.utils.PortUtil;
import com.sambit.testutils.jetty.test.AbstractJettyServer;

public class SampleUtilServer extends AbstractJettyServer {
	private static int PORT = Integer.valueOf(PortUtil
			.getPortNumber("utilServer"));

	protected SampleUtilServer() {
		super("", PORT);
	}

	public static void main(String[] args) {
		try {
			SampleUtilServer s = new SampleUtilServer();
			s.start();
		} catch (Exception ex) {
			fail("Server launcher test failed with message:" + ex.getMessage());
		} finally {
			System.out.println("done!");
		}
	}
}
