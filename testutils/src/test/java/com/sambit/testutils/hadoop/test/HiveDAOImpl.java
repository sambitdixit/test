package com.sambit.testutils.hadoop.test;

import java.util.List;
import java.util.Map;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.hadoop.hive.HiveScript;
import org.springframework.data.hadoop.hive.HiveTemplate;


public class HiveDAOImpl implements HiveDAO, ResourceLoaderAware {

	private ResourceLoader resourceLoader;
	private HiveTemplate hiveTemplate;

	
	public Long getRecordCount(String tableName) {
		return hiveTemplate.queryForLong("select count(*) from " + tableName);
	}

	public List<String> executeScript(String scriptResourcePath, Map parameters) {
		Resource res = resourceLoader.getResource(scriptResourcePath);
		List<String> result = hiveTemplate.executeScript(new HiveScript(res,parameters));
		return result;
	}

	public List<String> executeQuery(String query, Map parameters) {
		List<String> result = hiveTemplate.query(query, parameters);
		return result;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setHiveTemplate(HiveTemplate hiveTemplate) {
		this.hiveTemplate = hiveTemplate;
	}

}
