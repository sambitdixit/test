package com.sambit.testutils.hadoop.test;

/**
 * @author sambitdixit
 *
 */
public interface FileManager {

	/**
	 * @param fileName
	 * @param fileSuffix
	 * @param sourceDirectory
	 * @param targetDirectory
	 * @param separatePathByDate
	 * @return
	 */
	public boolean copyAndReplaceFile(String fileName, String fileSuffix,
			String sourceDirectory, String targetDirectory,
			boolean separatePathByDate);

	
	/**
	 * @param fileName
	 * @param fileSuffix
	 * @param directoryPath
	 * @param dirSeparatedByDate
	 * @return
	 */
	public boolean checkAndRemoveFile(String fileName, String fileSuffix,
			String directoryPath, boolean dirSeparatedByDate);

	/**
	 * @param fileName
	 * @param fileSuffix
	 * @param directoryPath
	 * @param dirSeparatedByDate
	 * @return
	 */
	public boolean testFileExist(String fileName, String fileSuffix,
			String directoryPath, boolean dirSeparatedByDate);

	/**
	 * @param directoryName
	 * @return
	 */
	public boolean testDirectoryExist(String directoryName);

	/**
	 * @param directoryName
	 * @return
	 */
	public boolean createDirectoryIfNotExist(String directoryName);

}
