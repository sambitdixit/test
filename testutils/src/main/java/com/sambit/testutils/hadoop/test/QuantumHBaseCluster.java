package com.sambit.testutils.hadoop.test;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.mapred.JobConf;

/**
 * Standalone simple mini hbase cluster having MR and Hdfs nodes.
 *
 * 
 *
 */
public class QuantumHBaseCluster implements QuantumCluster {

	private final static Log log = LogFactory.getLog(QuantumHBaseCluster.class);

	private HBaseTestingUtility hbaseTestingUtility;
	
	/** Unique cluster name */
	private final String clusterName;

	/** Configuration build at runtime */
	private Configuration configuration;

	/** Monitor sync for start and stop */
	private final Object startupShutdownMonitor = new Object();

	/** Flag for cluster state */
	private boolean started;

	private int noOfNodes = 1;

	private String hadoopLogDir;
	private String testBuildDataDir="build/test/data/";
	
	/**
	 * Instantiates a mini hbase cluster with default
	 * cluster node count.
	 *
	 * @param clusterName the unique cluster name
	 */
	public QuantumHBaseCluster(String clusterName) {
		this.clusterName = clusterName;
		this.hbaseTestingUtility  = new HBaseTestingUtility();
	}

	/**
	 * Instantiates a mini cluster with given
	 * cluster node count.
	 *
	 * @param clusterName the unique cluster name
	 * @param noOfNodes the node count
	 */
	public QuantumHBaseCluster(String clusterName, int noOfNodes) {
		this.clusterName = clusterName;
		this.noOfNodes = noOfNodes;
		this.hbaseTestingUtility  = new HBaseTestingUtility();
	}
	

	public Configuration getConfiguration() {
		return configuration;
	}

	public void start() throws IOException {
		log.info("Checking if cluster=" + clusterName + " needs to be started");
		synchronized (this.startupShutdownMonitor) {
			if (started) {
				return;
			}
			String tmpDir = (hadoopLogDir!=null && hadoopLogDir.trim().length()>0)?hadoopLogDir:QuantumClusterInfo.getTmpDir();
			System.setProperty("hadoop.log.dir", tmpDir);

			// need to get unique dir per cluster
			System.setProperty("test.build.data", testBuildDataDir + clusterName);

			log.info("Starting cluster=" + clusterName);

			Configuration config = new JobConf();

			// umask trick
			String umask = getCurrentUmask(tmpDir, config);
			if (umask != null) {
				log.info("Setting expected umask to " + umask);
				config.set("dfs.datanode.data.dir.perm", umask);
			}

			try {
				
			   hbaseTestingUtility.startMiniCluster(noOfNodes);
			} catch (Exception e) {
				log.error(e);

			}
			

			FileSystem fs = hbaseTestingUtility.getTestFileSystem();
			log.info("Dfs cluster uri= " + fs.getUri());

			
			configuration = hbaseTestingUtility.getConfiguration();

			// set default uri again in case it wasn't updated
			FileSystem.setDefaultUri(configuration, fs.getUri());

			log.info("Started cluster=" + clusterName);
			started = true;
		}
	}

	public void stop() {
		log.info("Checking if cluster=" + clusterName + " needs to be stopped");
		synchronized (this.startupShutdownMonitor) {
			if (!started) {
				return;
			}

			if (started) {
				try {
					hbaseTestingUtility.shutdownMiniCluster();
				} catch (Exception e) {
					log.error(e);
				}
			}

			log.info("Stopped cluster=" + clusterName);
			started = false;
		}
	}

	public FileSystem getFileSystem() throws IOException {
		return (started)? hbaseTestingUtility.getTestFileSystem(): null;
	}

	
	/**
	 * Gets the current umask.
	 */
	private String getCurrentUmask(String tmpDir, Configuration config) throws IOException {
		try {
			LocalFileSystem localFS = FileSystem.getLocal(config);
			return Integer.toOctalString(localFS.getFileStatus(new Path(hadoopLogDir)).getPermission().toShort());
		} catch (Exception e) {
			return null;
		}
	}



	public String getHadoopLogDir() {
		return hadoopLogDir;
	}


	public void setHadoopLogDir(String hadoopLogDir) {
		this.hadoopLogDir = hadoopLogDir;
	}


	public String getTestBuildDataDir() {
		return testBuildDataDir;
	}


	public void setTestBuildDataDir(String testBuildDataDir) {
		this.testBuildDataDir = testBuildDataDir;
	}


	public int getNoOfNodes() {
		return noOfNodes;
	}


	public void setNoOfNodes(int noOfNodes) {
		this.noOfNodes = noOfNodes;
	}

}
