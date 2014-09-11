package com.sambit.testutils.common.test.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.sambit.testutils.common.test.utils.PortUtil;

public class PortUtilTest {

  @Test
  public void getAllTypesOfPorts() {
    Assert.assertNotNull(PortUtil.getAllPorts());
    Assert.assertNotNull(PortUtil.getPortNumber(PortUtilTest.class));
    Assert.assertNotNull(PortUtil.getPortNumber("test"));
    Assert.assertNotNull(PortUtil.getPortNumber(PortUtilTest.class,900));
  }
}
