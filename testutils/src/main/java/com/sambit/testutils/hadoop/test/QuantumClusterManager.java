package com.sambit.testutils.hadoop.test;

import java.util.Hashtable;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.hadoop.test.context.HadoopCluster;

/**
 * Manager handling running mini clusters for tests.
 * 
 * 
 * 
 */
public class QuantumClusterManager {

	private final static Log log = LogFactory
			.getLog(QuantumClusterManager.class);

	/** Singleton instance */
	private static volatile QuantumClusterManager instance = null;

	/** Synchronization monitor for the "refresh" and "destroy" */
	private final Object startupShutdownMonitor = new Object();

	/** Reference to the JVM shutdown hook, if registered */
	private Thread shutdownHook;

	/** Cached clusters */
	private Hashtable<QuantumClusterInfo, QuantumCluster> clusters = new Hashtable<QuantumClusterInfo, QuantumCluster>();

	/**
	 * Private initializer for singleton access.
	 */
	private QuantumClusterManager() {
	}

	/**
	 * Gets the singleton instance of {@link QuantumClusterManager}.
	 * 
	 * @return the singleton instance
	 */
	public static QuantumClusterManager getInstance() {
		if (instance == null) {
			synchronized (QuantumClusterManager.class) {
				if (instance == null) {
					instance = new QuantumClusterManager();
				}
			}
		}
		return instance;
	}

	/**
	 * Gets the singleton instance of {@link QuantumClusterManager}.
	 * 
	 * @param registerShutdownHook
	 *            if true register shutdown hook
	 * @return the singleton instance
	 */
	public static QuantumClusterManager getInstance(boolean registerShutdownHook) {
		QuantumClusterManager instance2 = getInstance();
		if (registerShutdownHook) {
			instance2.registerShutdownHook();
		}
		return instance2;
	}

	/**
	 * Gets and starts the mini cluster.
	 * 
	 * @param quantumClusterInfo
	 *            the info about the cluster
	 * @return the running mini cluster
	 */
	public QuantumCluster getCluster(QuantumClusterInfo quantumClusterInfo) {
		QuantumCluster cluster = clusters.get(quantumClusterInfo);
		if (cluster == null) {
			log.info("Building new cluster for QuantumClusterInfo="
					+ quantumClusterInfo);
			try {
				if (quantumClusterInfo.getClusterType().equals("hbase")) {
					cluster = new QuantumHBaseCluster(
							quantumClusterInfo.getClusterName(),
							quantumClusterInfo.getNoOfNodes());
					clusters.put(quantumClusterInfo, cluster);
				} else {
					cluster = new QuantumHadoopCluster(
							quantumClusterInfo.getClusterName(),
							quantumClusterInfo.getNoOfNodes());
					clusters.put(quantumClusterInfo, cluster);
				}

			} catch (Exception e) {
			}
		} else {
			log.info("Found cached cluster for QuantumClusterInfo="
					+ quantumClusterInfo);
		}
		return cluster;
	}

	/**
	 * Closes and remove {@link HadoopCluster} from a manager cache.
	 * 
	 * @param cluster
	 *            the Quantum cluster
	 * @return true if cluster was closed, false otherwise
	 */
	public boolean closeCluster(QuantumCluster cluster) {
		for (Entry<QuantumClusterInfo, QuantumCluster> entry : clusters
				.entrySet()) {
			if (entry.getValue().equals(cluster)) {
				entry.getValue().stop();
				clusters.remove(entry.getKey());
				return true;
			}
		}
		return false;
	}

	/**
	 * Close the manager, removes shutdown hook and closes all running clusters.
	 */
	public void close() {
		synchronized (this.startupShutdownMonitor) {
			doClose();
			// If we registered a JVM shutdown hook, we don't need it anymore:
			// We've already explicitly closed the context.
			if (this.shutdownHook != null) {
				try {
					Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
				} catch (IllegalStateException ex) {
					// ignore - VM is already shutting down
				}
			}
		}
	}

	/**
	 * Register a jvm shutdown hook allowing manager to gracefully shutdown
	 * clusters in case that hasn't already happened. This is usually the
	 * scenario in tests.
	 */
	public synchronized void registerShutdownHook() {
		if (this.shutdownHook == null) {
			// No shutdown hook registered yet.
			this.shutdownHook = new Thread() {
				@Override
				public void run() {
					log.info("Received shutdown hook, about to call doClose()");
					doClose();
				}
			};
			Runtime.getRuntime().addShutdownHook(this.shutdownHook);
		}
	}

	/**
	 * Bring down and un-register all the running clusters.
	 */
	protected void doClose() {
		log.info("Closing all clusters handled by this manager");
		for (Entry<QuantumClusterInfo, QuantumCluster> entry : clusters
				.entrySet()) {
			entry.getValue().stop();
		}
		clusters.clear();
	}

}
