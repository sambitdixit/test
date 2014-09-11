package com.sambit.testutils.hadoop.test;

import java.util.List;
import java.util.Map;


public interface HiveDAO {

	Long getRecordCount(String table);
	List<String> executeQuery(String query,Map parameters);
	List<String> executeScript(String scriptFile,Map parameters);

}