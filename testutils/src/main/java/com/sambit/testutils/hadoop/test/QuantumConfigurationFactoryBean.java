package com.sambit.testutils.hadoop.test;

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Simple factory bean delegating retrieval of
 * hadoop {@link Configuration} from a {@link QuantumCluster}.
 *
 * 
 *
 */
public class QuantumConfigurationFactoryBean implements InitializingBean, FactoryBean<Configuration> {

	private QuantumCluster cluster;

	public Configuration getObject() throws Exception {
		return cluster.getConfiguration();
	}

	public Class<Configuration> getObjectType() {
		return Configuration.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(cluster, "QuantumCluster must be set");
	}

	/**
	 * Sets the {@link QuantumCluster}
	 *
	 * @param cluster the {@link QuantumCluster}
	 */
	public void setCluster(QuantumCluster cluster) {
		this.cluster = cluster;
	}

}
