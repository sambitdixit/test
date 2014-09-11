package com.sambit.testutils.performance.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sambit.testutils.performance.test.StatsUtil;

public class StatsUtilTest {
	List<Double> l;

	@BeforeClass
	public void setup() throws Exception {
		l = new ArrayList<Double>();
		l.add(new Double(1.0));
		l.add(new Double(2.1));
		l.add(new Double(3.0));
		l.add(new Double(5.0));
		l.add(1.10);
		Collections.sort(l);
	}

	@Test
	public void getMax() {
		Assert.assertEquals(new Double(5.0), StatsUtil.getMax(l));
	}

	@Test
	public void getMean() {
		Assert.assertEquals(new Double(2.44), StatsUtil.getMean(l));
	}

	@Test
	public void getMin() {
		Assert.assertEquals(new Double(1.0), StatsUtil.getMin(l));
	}

	@Test
	public void getStdDev() {
		Assert.assertEquals(new Double(1.6471186963907611),StatsUtil.getStdDev(l));
	}
}
