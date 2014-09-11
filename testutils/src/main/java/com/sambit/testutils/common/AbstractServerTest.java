package com.sambit.testutils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.sambit.testutils.common.test.utils.PortUtil;
import com.sambit.testutils.common.test.utils.ServerLauncher;


/**
 * @author sambitdikshit
 */
public abstract class AbstractServerTest extends Assert {
    private boolean inProcess;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServerTest.class);

    /**
     * method implemented by test servers.  Initialise
     * servants and publish endpoints etc.
     */
    protected abstract void run();

    protected Logger getLog() {
        return LoggerFactory.getLogger(this.getClass());
    }

    /**
     *
     * @throws Exception
     */
    public void startInProcess() throws Exception {
        inProcess = true;
        run();
        ready();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public boolean stopInProcess() throws Exception {
        boolean ret = true;
        tearDown();
        if (verify(getLog())) {
            if (!inProcess) {
            	System.out.println("server passed");
            }
        } else {
            ret = false;
        }
        return ret;
    }

    /**
     *
     */
    public void start() {
        try {
        	System.out.println("running server");
            run();
            System.out.println("signal ready");
            ready();
            // wait for a key press then shut
            // down the server
            //
            System.in.read();
            System.out.println("stopping bus");
            tearDown();
        } catch (Exception ex) {
            LOGGER.error("", ex);
            startFailed();
        } finally {
        	if (verify(getLog())) {
                System.out.println("server passed");
            } else {
                System.out.println(ServerLauncher.SERVER_FAILED);
            }
            System.out.println("server stopped");
            System.exit(0);
        }
    }

    /**
     *
     * @throws Exception
     */
    public void setUp() throws Exception {
        // emtpy
    }

    /**
     *
     * @throws Exception
     */
    public void tearDown() throws Exception {
        // empty
    }

    protected void ready() {
        if (!inProcess) {
        	 System.out.println("server ready");
        }
    }

    protected void startFailed() {
        System.out.println(ServerLauncher.SERVER_FAILED);
        System.exit(-1);
    }

    /**
     * Used to facilitate assertions on server-side behaviour.
     *
     * @param log logger to use for diagnostics if assertions fail
     * @return true if assertions hold
     */
    protected boolean verify(Logger log) {
        return true;
    }

    protected static int allocatePortAsInt(Class<?> cls) {
        return Integer.parseInt(PortUtil.getPortNumber(cls));
    }

    protected static String allocatePort(Class<?> cls) {
        return PortUtil.getPortNumber(cls);
    }

    protected static String allocatePort(Class<?> cls, int i) {
        return PortUtil.getPortNumber(cls, i);
    }


}
