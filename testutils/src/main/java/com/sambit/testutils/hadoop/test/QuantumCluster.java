package com.sambit.testutils.hadoop.test;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.data.hadoop.test.context.HadoopCluster;

public interface QuantumCluster {

	/**
	 * Gets the {@link Configuration} for the cluster.
	 * As most of the configuration parameters are not
	 * known until after cluster has been started, this
	 * configuration should be configured by the
	 * cluster itself.
	 *
	 * @return the Cluster configured {@link Configuration}
	 */
	Configuration getConfiguration();

	/**
	 * Starts the cluster.
	 *
	 * @throws Exception if cluster failed to start
	 */
	void start() throws Exception;

	/**
	 * Stops the cluster.
	 */
	void stop();

	/**
	 * Gets the configured {@link FileSystem} managed
	 * by {@link HadoopCluster}.
	 *
	 * @return file system managed by cluster
	 * @throws IOException if error occured
	 */
	FileSystem getFileSystem() throws IOException;
	
}
