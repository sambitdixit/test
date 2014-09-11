package com.sambit.testutils.hadoop.test;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.util.Assert;

/**
 * @author sambitdixit
 * 
 */
public class HDFSFileManager implements FileManager {

	private static Logger log = LoggerFactory.getLogger(HDFSFileManager.class);

	private FsShell fsShell;

	public HDFSFileManager(Configuration configuration) {
		Assert.notNull(configuration, "Hadoop Configuration must not be null.");
		fsShell = new FsShell(configuration);
	}

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
			boolean separatePathByDate) {
		boolean copied = false;
		long st = System.currentTimeMillis();
		try {
			String sourceFile = HDFSPathUtil.getFilePath(fileName, fileSuffix,
					sourceDirectory, false);
			log.debug("sourceFile to be copied = {}", sourceFile);
			String targetFile = HDFSPathUtil.getFilePath(fileName, fileSuffix,
					targetDirectory, separatePathByDate);
			log.debug("destFile to be copied = {}", targetFile);
			// checkAndRemoveFile(destFile.toUri().toString());
			createDirectoryIfNotExist(targetDirectory);
			fsShell.copyFromLocal(sourceFile, targetFile);
			copied = true;
			log.debug("destFile {} copied to hdfs", targetFile);
		} catch (Exception e) {
			log.error("error in copyAndReplaceFile", e);
		} finally {
			long et = System.currentTimeMillis();
			log.info("Time taken for copyAndReplaceFile = {} ms", (et - st));
		}
		return copied;
	}

	/**
	 * @param fileName
	 * @param fileSuffix
	 * @param directoryPath
	 * @param dirSeparatedByDate
	 * @return
	 */
	public boolean checkAndRemoveFile(String fileName, String fileSuffix,
			String directoryPath, boolean dirSeparatedByDate) {
		boolean isRemoved = false;
		long st = System.currentTimeMillis();
		try {
			boolean destFileExists = testFileExist(fileName, fileSuffix,
					directoryPath, dirSeparatedByDate);
			if (destFileExists) {
				fsShell.rm(fileName);
				isRemoved = true;
			}
		} catch (Exception e) {
			log.error("error in checkAndRemoveFile", e);
		} finally {
			long et = System.currentTimeMillis();
			log.info("Time taken for checkAndRemoveFile = {} ms", (et - st));
		}
		return isRemoved;
	}

	/**
	 * @param fileName
	 * @param fileSuffix
	 * @param directoryPath
	 * @param dirSeparatedByDate
	 * @return
	 */
	public boolean testFileExist(String fileName, String fileSuffix,
			String directoryPath, boolean dirSeparatedByDate) {
		boolean exist = false;
		long st = System.currentTimeMillis();
		try {
			boolean directoryExist = testDirectoryExist(directoryPath);
			if (directoryExist) {
				String completeFileName = HDFSPathUtil.getFilePath(fileName,
						fileSuffix, directoryPath, dirSeparatedByDate);
				exist = fsShell.test(completeFileName);
			}
		} catch (Exception e) {
			log.error("error in testFileExist", e);
		} finally {
			long et = System.currentTimeMillis();
			log.info("Time taken for testFileExist = {} ms", (et - st));
		}
		return exist;
	}

	/**
	 * @param directoryName
	 * @return
	 */
	public boolean testDirectoryExist(String directoryName) {
		boolean exist = false;
		long st = System.currentTimeMillis();
		try {
			exist = fsShell.test(true, false, true, directoryName);
		} catch (Exception e) {
			log.error("error in testDirectoryExist", e);
		} finally {
			long et = System.currentTimeMillis();
			log.info("Time taken for testDirectoryExist = {} ms", (et - st));
		}
		return exist;
	}

	/**
	 * @param directoryName
	 * @return
	 */
	public boolean createDirectoryIfNotExist(String directoryName) {
		boolean created = false;
		long st = System.currentTimeMillis();
		try {
			boolean exist = testDirectoryExist(directoryName);
			if (!exist) {
				fsShell.mkdir(directoryName);
				created = true;
			}
		} catch (Exception e) {
			log.error("error in createDirectoryIfNotExist", e);
		} finally {
			long et = System.currentTimeMillis();
			log.info("Time taken for createDirectoryIfNotExist = {} ms",
					(et - st));
		}
		return created;
	}
}
