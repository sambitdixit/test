package com.sambit.testutils.hadoop.test;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Factory bean building Hadoop mini clusters.
 * 
 */
public class QuantumClusterFactoryBean implements InitializingBean,
		DisposableBean, FactoryBean<QuantumCluster> {

	/** Instance returned from this factory */
	private QuantumCluster cluster;

	/** Unique running cluster name */
	private String clusterName;

	private String clusterType;

	/** Flag starting cluster from this factory */
	private boolean autoStart;

	/** Number of nodes */
	private int noOfNodes = 1;

	private String hadoopLogDir;
	private String testBuildDataDir;

	public QuantumCluster getObject() throws Exception {
		return cluster;
	}

	public Class<QuantumCluster> getObjectType() {
		return QuantumCluster.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(clusterName, "Cluster name must be set");
		QuantumClusterInfo clusterInfo = new QuantumClusterInfo();
		clusterInfo.setClusterName(clusterName);
		clusterInfo.setClusterType(clusterType);
		clusterInfo.setNoOfNodes(noOfNodes);
		clusterInfo.setHadoopLogDir(hadoopLogDir);
		clusterInfo.setTestBuildDataDir(testBuildDataDir);
		cluster = QuantumClusterManager.getInstance(true).getCluster(
				clusterInfo);
		if (autoStart) {
			cluster.start();
		}
	}

	public void destroy() throws Exception {
		if (cluster != null) {
			cluster.stop();
		}
	}

	/**
	 * Set whether cluster should be started automatically by this factory
	 * instance. Default setting is false.
	 * 
	 * @param autoStart
	 *            the flag if cluster should be started automatically
	 */
	public void setAutoStart(boolean autoStart) {
		this.autoStart = autoStart;
	}

	/**
	 * @param clusterName
	 */
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	/**
	 * @param noOfNodes
	 */
	public void setNoOfNodes(int noOfNodes) {
		this.noOfNodes = noOfNodes;
	}

	/**
	 * @param clusterType
	 */
	public void setClusterType(String clusterType) {
		this.clusterType = clusterType;
	}

	public void setHadoopLogDir(String hadoopLogDir) {
		this.hadoopLogDir = hadoopLogDir;
	}

	public void setTestBuildDataDir(String testBuildDataDir) {
		this.testBuildDataDir = testBuildDataDir;
	}

}
