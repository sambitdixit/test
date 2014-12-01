package com.sambit.testutils.hadoop.test;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hive.conf.HiveConf.ConfVars;
import org.springframework.data.hadoop.test.support.compat.MiniMRClusterCompat;

/**
 * Standalone simple mini cluster having MR
 * and Hdfs nodes.
 *
 * 
 *
 */
public class QuantumHadoopCluster implements QuantumCluster {

	private final static Log log = LogFactory.getLog(QuantumHadoopCluster.class);

	/** MR specific mini cluster object via reflection. */
	private Object mrClusterObject;

	/** Hdfs specific mini cluster */
	private MiniDFSCluster dfsCluster = null;

	/** Unique cluster name */
	private final String clusterName;

	/** Configuration build at runtime */
	private Configuration configuration;

	/** Monitor sync for start and stop */
	private final Object startupShutdownMonitor = new Object();

	/** Flag for cluster state */
	private boolean started;

	/** Number of nodes for dfs */
	private int noOfNodes = 1;

	private String hadoopLogDir;
	private String testBuildDataDir="build/test/data/";
	
	/**
	 * Instantiates a mini cluster with default
	 * cluster node count.
	 *
	 * @param clusterName the unique cluster name
	 */
	public QuantumHadoopCluster(String clusterName) {
		this.clusterName = clusterName;
	}

	/**
	 * Instantiates a mini cluster with given
	 * cluster node count.
	 *
	 * @param clusterName the unique cluster name
	 * @param noOfNodes the node count
	 */
	public QuantumHadoopCluster(String clusterName, int noOfNodes) {
		this.clusterName = clusterName;
		this.noOfNodes = noOfNodes;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	@SuppressWarnings("deprecation")
	public void start() throws IOException {
		log.info("Checking if cluster=" + clusterName + " needs to be started");
		synchronized (this.startupShutdownMonitor) {
			if (started) {
				return;
			}

			// TODO: fix for MAPREDUCE-2785
			// I don't like it at all to do it like this, but
			// until we find a better way for the fix,
			// just set the system property
			// who knows what kind of problems this will cause!!!
			// keeping this here as reminder for the next guy who
			// clean up the mess
			String tmpDir = (hadoopLogDir!=null && hadoopLogDir.trim().length()>0)?hadoopLogDir:QuantumClusterInfo.getTmpDir();
			System.setProperty("hadoop.log.dir", tmpDir);

			// need to get unique dir per cluster
			System.setProperty("test.build.data", testBuildDataDir + clusterName);

			log.info("Starting cluster=" + clusterName);

			Configuration config = new Configuration();
			
			// umask trick
			String umask = getCurrentUmask(tmpDir, config);
			if (umask != null) {
				log.info("Setting expected umask to " + umask);
				config.set("dfs.datanode.data.dir.perm", umask);
			}

			// dfs cluster is updating config
			// newer dfs cluster are using builder pattern
			// but we need to support older versions in
			// a same code. there are some ramifications if
			// deprecated methods are removed in a future
			dfsCluster = new MiniDFSCluster(config, noOfNodes, true, null);

			// we need to ask the config from mr cluster after init
			// returns. for this case it is not guaranteed that passed config
			// is updated.
			// we do all this via compatibility class which uses
			// reflection because at this point we don't know
			// the exact runtime implementation

			FileSystem fs = dfsCluster.getFileSystem();
			log.info("Dfs cluster uri= " + fs.getUri());

			mrClusterObject = MiniMRClusterCompat.instantiateCluster(this.getClass(),
					noOfNodes, config, fs, this.getClass().getClassLoader());

			configuration = MiniMRClusterCompat.getConfiguration(mrClusterObject);
			
			System.setProperty(ConfVars.METASTOREWAREHOUSE.toString(), testBuildDataDir  +
		            "hive-test/target/hive/warehouse");
			
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

			if (mrClusterObject != null) {
				MiniMRClusterCompat.stopCluster(mrClusterObject);
				mrClusterObject = null;
			}

			if (dfsCluster != null) {
				dfsCluster.shutdown();
				dfsCluster = null;
			}
			log.info("Stopped cluster=" + clusterName);
			started = false;
		}
	}

	public FileSystem getFileSystem() throws IOException {
		return dfsCluster != null ? dfsCluster.getFileSystem() : null;
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

	public int getNoOfNodes() {
		return noOfNodes;
	}

	public void setNoOfNodes(int noOfNodes) {
		this.noOfNodes = noOfNodes;
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

	public String getClusterName() {
		return clusterName;
	}

}
