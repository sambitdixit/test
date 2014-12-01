package com.sambit.testutils.hadoop.test;

import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:sample-hive-context.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class HiveDAOImplTest {

	@Autowired
	private HiveDAOImpl hiveDao;
	
	@Test
	public void testCreateTable() {
		//Long count = hiveDao.getRecordCount("sample_07");
		List<String> results = hiveDao.executeQuery("create table temp(name string)", new HashMap());
		results = hiveDao.executeQuery("show tables", new HashMap());
		System.out.println("Results == " + results);
		Assert.assertNotNull(results);
		
		
		
	}

	

}
