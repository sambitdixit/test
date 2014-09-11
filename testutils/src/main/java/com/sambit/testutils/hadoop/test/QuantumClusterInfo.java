package com.sambit.testutils.hadoop.test;

import java.io.File;
import java.util.Random;

/**
 * Object keeping some testing cluster init info.
 * 
 * 
 * 
 */
public class QuantumClusterInfo {

	private int noOfNodes = 1;
	private String clusterName = "default";
	private String clusterType = "hadoop";
	private String hadoopLogDir;
	private String testBuildDataDir = "build/test/data/";

	public int getNoOfNodes() {
		return noOfNodes;
	}

	public void setNoOfNodes(int noOfNodes) {
		this.noOfNodes = noOfNodes;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getClusterType() {
		return (clusterType != null && clusterType.trim().length() > 0) ? clusterType
				: "hadoop";
	}

	public void setClusterType(String clusterType) {
		this.clusterType = clusterType;
	}

	public String getHadoopLogDir() {
		return (hadoopLogDir != null && hadoopLogDir.trim().length() > 0) ? hadoopLogDir
				: getTmpDir();
	}

	public void setHadoopLogDir(String hadoopLogDir) {
		this.hadoopLogDir = hadoopLogDir;
	}

	public String getTestBuildDataDir() {
		return (testBuildDataDir != null && testBuildDataDir.trim().length() > 0) ? testBuildDataDir
				: "build/test/data/";
	}

	public void setTestBuildDataDir(String testBuildDataDir) {
		this.testBuildDataDir = testBuildDataDir;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clusterName == null) ? 0 : clusterName.hashCode());
		result = prime * result
				+ ((clusterType == null) ? 0 : clusterType.hashCode());
		result = prime * result + noOfNodes;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuantumClusterInfo other = (QuantumClusterInfo) obj;
		if (clusterName == null) {
			if (other.clusterName != null)
				return false;
		} else if (!clusterName.equals(other.clusterName))
			return false;
		if (clusterType == null) {
			if (other.clusterType != null)
				return false;
		} else if (!clusterType.equals(other.clusterType))
			return false;
		if (noOfNodes != other.noOfNodes)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QuantumClusterInfo [noOfNodes=" + noOfNodes + ", clusterName="
				+ clusterName + ", clusterType=" + clusterType + "]";
	}

	public static String getTmpDir() {
		String propTmpPath = System.getProperty("java.io.tmpdir");
		Random random = new Random();
		int rand = 1 + random.nextInt();
		File tmpDir = new File(propTmpPath + File.separator + "hadoopTmpDir"
				+ rand);
		if (tmpDir.exists() == false) {
			tmpDir.mkdir();
		}
		tmpDir.deleteOnExit();
		return tmpDir.getAbsolutePath();
	}

}
