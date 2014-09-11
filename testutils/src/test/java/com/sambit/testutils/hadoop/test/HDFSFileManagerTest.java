package com.sambit.testutils.hadoop.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:sample-hadoop-context.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class HDFSFileManagerTest {
	
	private static Logger log = LoggerFactory.getLogger(HDFSFileManagerTest.class);

	@Autowired
	private FileManager fileManager;
	
	
	@Test
	public void testCopyAndReplaceFile() {
		boolean isCopied = fileManager.copyAndReplaceFile("books", "csv", "/Users/sambitdixit/git/spring-hadoop/spring-hadoop-build-tests/src/test/resources/data", "testing/hive/warehouse/books", false);
		log.debug("IsFileCopied = {}",isCopied);
		Assert.assertEquals(true, isCopied);
		//fileManager.checkAndRemoveFile("Batting", "csv","/user/hive/warehouse", false);
	}

	/*@Test
	public void testCheckAndRemoveFile() {
		boolean isCopied = fileManager.copyAndReplaceFile("Batting", "csv", "/Users/sambitdixit/workspace/jupiter/ingestion/data", "/user/hive/warehouse", false);
		log.debug("IsFileCopied = {}",isCopied);
		Assert.assertEquals(true, isCopied);
		boolean isRemoved = fileManager.checkAndRemoveFile("Batting", "csv","/user/hive/warehouse", false);
		log.debug("IsFileRemoved = {}",isRemoved);
		Assert.assertEquals(true, isRemoved);
	}

	@Test
	public void testTestFileExist() {
		boolean isCopied = fileManager.copyAndReplaceFile("Batting", "csv", "/Users/sambitdixit/workspace/jupiter/ingestion/data", "/user/hive/warehouse", false);
		log.debug("IsFileCopied = {}",isCopied);
		Assert.assertEquals(true, isCopied);
		boolean fileExist = fileManager.testFileExist("Batting", "csv","/user/hive/warehouse", false);
		log.debug("fileExist = {}",fileExist);
		Assert.assertEquals(true, fileExist);
	}

	@Test
	public void testTestDirectoryExist() {
		boolean directoryExist = fileManager.testDirectoryExist("/user/hive/warehouse");
		log.debug("directoryExist = {}",directoryExist);
		Assert.assertEquals(true, directoryExist);
	}

	@Test
	public void testCreateDirectoryIfNotExist() {
		boolean directoryCreated = fileManager.createDirectoryIfNotExist("/user/hive/warehouse/test123");
		log.debug("directoryCreated = {}",directoryCreated);
		Assert.assertEquals(true, directoryCreated);
		boolean directoryExist = fileManager.testDirectoryExist("/user/hive/warehouse/test123");
		log.debug("directoryExist = {}",directoryExist);
		Assert.assertEquals(true, directoryExist);
	}*/

}
