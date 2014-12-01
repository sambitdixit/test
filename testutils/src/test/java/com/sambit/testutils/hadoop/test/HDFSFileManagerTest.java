package com.sambit.testutils.hadoop.test;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.junit.Assert;
import org.junit.BeforeClass;
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

	@BeforeClass
	public static void setup(){
		System.setProperty("HADOOP_USER_HOME","sambitdixit");
		try {
			UserGroupInformation.createProxyUserForTesting("sambitd", UserGroupInformation.getCurrentUser(), new String[]{"test"});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Autowired
	private FileManager fileManager;
	
	@Autowired
	Configuration hadoopConfiguration;
	
	@Test
	public void testCopyAndReplaceFile() {
		
		System.out.println("HadoopConfiguration == " + hadoopConfiguration);
		try{
		System.out.println("UserGroupInformation == " + UserGroupInformation.getCurrentUser() + ", LoginUser == " + UserGroupInformation.getLoginUser());
		}catch(Exception e){
			e.printStackTrace();
		}
		boolean isCopied = fileManager.copyAndReplaceFile("books", "csv", "/Users/sambitdixit/git/spring-hadoop/spring-hadoop-build-tests/src/test/resources/data", "sambitdixit", false);
		log.debug("IsFileCopied = {}",isCopied);
		Assert.assertEquals(true, isCopied);
		try {
			Thread.sleep(3 * 3L * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean fileExist = fileManager.testFileExist("books", "csv","sambitdixit", false);
		log.debug("fileExist = {}",fileExist);
		Assert.assertEquals(true, fileExist);
		boolean isRemoved = fileManager.checkAndRemoveFile("books", "csv","sambitdixit", false);
		log.debug("IsFileRemoved = {}",isRemoved);
		Assert.assertEquals(true, isRemoved);
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
