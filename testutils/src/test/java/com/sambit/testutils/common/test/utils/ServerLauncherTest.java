package com.sambit.testutils.common.test.utils;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.sambit.testutils.common.AbstractClientServerTest;
import com.sambit.testutils.common.test.utils.ServerLauncher;

public class ServerLauncherTest extends AbstractClientServerTest {

	ServerLauncher launcher = new ServerLauncher(SampleUtilServer.class.getName());

	@Test
	public void testServerLaunch() {
		assertTrue(launchServer(SampleUtilServer.class, true),
				"server did not launch correctly");
	}
	
	@Test
	public void testLauncherOps() throws Exception
	{
		try {
			launcher.notifyServerFailed();
		} catch (Exception e) {
			fail("Server fail notification falied with error message:"+e.getMessage());
		}
		
		try {
			launcher.notifyServerIsReady();
		} catch (Exception e) {
			fail("Server ready notification falied with error message:"+e.getMessage());
		}
		
		try {
			launcher.notifyServerIsStopped();
		} catch (Exception e) {
			fail("Server stopped notification falied with error message:"+e.getMessage());
		}
		
		try {
			launcher.signalStop();
		} catch (Exception e) {
			fail("Server stop signal falied with error message:"+e.getMessage());
		}
		
		try {
			launcher.stopServer();
		} catch (Exception e) {
			fail("Server stop server falied with error message:"+e.getMessage());
		}
	}

	@AfterClass
	public void tearDown() throws Exception {
		stopAllServers();
	}
}
