package com.sambit.testutils.jetty.test;

import java.net.URISyntaxException;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sambit.testutils.common.AbstractServerTest;

/**
 * This class is an abstraction on top of embedded jetty to help testing from
 * within unit test case container like junit or testng. This class extends
 * AbstractServerTest class. Developers need to extend this class and create an
 * embedded jetty server instance by passing the path to the folder where
 * WEB-INF and its associated classes, web.xml deployment descriptors are kept.
 * 
 * @author sambitdikshit
 * 
 */
public abstract class AbstractJettyServer extends AbstractServerTest {
	public static final String PORT = allocatePort(AbstractJettyServer.class);
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJettyServer.class);

	private org.eclipse.jetty.server.Server server;
	private String resourcePath;
	private String contextPath;
	private int port;

	/**
	 * @param path
	 */
	protected AbstractJettyServer(String path) {
		this(path, "/", Integer.parseInt(PORT));
	}

	/**
	 * @param path
	 * @param portNumber
	 */
	protected AbstractJettyServer(String path, int portNumber) {
		this(path, "/", portNumber);
	}

	/**
	 * @param path
	 * @param cPath
	 */
	protected AbstractJettyServer(String path, String cPath) {
		this(path, cPath, Integer.parseInt(PORT));
	}

	/**
	 * @param path
	 * @param cPath
	 * @param portNumber
	 */
	protected AbstractJettyServer(String path, String cPath, int portNumber) {
		resourcePath = path;
		contextPath = cPath;
		port = portNumber;
	}

	/* (non-Javadoc)
	 * @see com.sambit.testutils.jetty.test.AbstractServerTest#run()
	 */
	protected void run() {
		server = new org.eclipse.jetty.server.Server();

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		server.setConnectors(new Connector[] { connector });

		WebAppContext webappcontext = new WebAppContext();
		webappcontext.setContextPath(contextPath);

		String warPath = null;
		try {
			warPath = getClass().getResource(resourcePath).toURI().getPath();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("WarPath--" + warPath);
			}
		} catch (URISyntaxException e1) {
			if(LOGGER.isWarnEnabled()) { LOGGER.warn(e1.getMessage()); }
		}

		webappcontext.setWar(warPath);

		HandlerCollection handlers = new HandlerCollection();
		handlers.setHandlers(new Handler[] { webappcontext,
				new DefaultHandler() });

		server.setHandler(handlers);

		try {
			configureServer(server);
			server.start();
			server.setStopAtShutdown(true);
		} catch (Exception e) {
			
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Error", e);
			}
		}
	}

	/**
	 * Any specific configuration for jetty can be done here. This needs to extended in future. 
	 * @param theserver
	 * @throws Exception
	 */
	protected void configureServer(org.eclipse.jetty.server.Server theserver)
			throws Exception {

	}

    @Override
	public void tearDown() throws Exception {
		super.tearDown();
		if (server != null) {
			server.stop();
			server.destroy();
			server = null;
		}
	}
}
