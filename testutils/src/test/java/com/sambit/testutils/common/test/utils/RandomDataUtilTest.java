package com.sambit.testutils.common.test.utils;

import java.io.IOException;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.sambit.testutils.common.test.utils.RandomDataUtil;

public class RandomDataUtilTest {


  @Test
  public void get10MBRandomString() {
	 Assert.assertEquals(RandomDataUtil.TWO_MB*RandomDataUtil.FIVE,RandomDataUtil.get10MBRandomString().length());
  }

  @Test
  public void get1KBRandomString() {
	  Assert.assertEquals(RandomDataUtil.ONE_KB,RandomDataUtil.get1KBRandomString().length());
  }

  @Test
  public void get20KBRandomString() {
	  Assert.assertEquals(RandomDataUtil.TWENTY_KB,RandomDataUtil.get20KBRandomString().length());
  }

  @Test
  public void get2KBRandomString() {
	  Assert.assertEquals(RandomDataUtil.TWENTY_KB/RandomDataUtil.TEN,RandomDataUtil.get2KBRandomString().length());
  }

  @Test
  public void get2MBRandomString() {
	  Assert.assertEquals(RandomDataUtil.TWO_MB,RandomDataUtil.get2MBRandomString().length());
  }

  @Test
  public void get60KBRandomString() {
	  Assert.assertEquals(RandomDataUtil.TWENTY_KB*RandomDataUtil.THREE,RandomDataUtil.get60KBRandomString().length());
  }

  @Test
  public void get6MBRandomString() {
	  Assert.assertEquals(RandomDataUtil.TWO_MB*RandomDataUtil.THREE,RandomDataUtil.get6MBRandomString().length());
  }

  @Test
  public void getDataSizeInKB() {
	  Assert.assertNotNull(RandomDataUtil.getDataSizeInKB("1"));
  }

  @Test
  public void getDataSizeInMB() {
	  Assert.assertNotNull(RandomDataUtil.getDataSizeInMB("1"));
  }

  @Test
  public void getRandomAlphaString() {
	  Assert.assertNotNull(RandomDataUtil.getRandomAlphaString(1));
  }

  @Test
  public void getRandomDate() {
	  Assert.assertTrue(RandomDataUtil.getRandomDate() instanceof java.util.Date);
  }

  @Test
  public void getRandomEmail() {
	  Assert.assertTrue(RandomDataUtil.getRandomEmail().contains("@"));
  }

  @Test
  public void getRandomFile() {
	  try {
		Assert.assertNotNull(RandomDataUtil.getRandomFile());
	} catch (IOException e) {
		Assert.fail("getRandomFile failed with error message:"+e.getMessage());
	}
  }

  @Test
  public void getRandomInt() {
	  Assert.assertNotNull(RandomDataUtil.getRandomInt());
  }

  @Test
  public void getRandomIpAddress() {
	  Assert.assertNotNull(RandomDataUtil.getRandomIpAddress());
  }

  @Test
  public void getRandomLong() {
	  Assert.assertNotNull(RandomDataUtil.getRandomLong());
  }


  @Test
  public void getRandomString() {
	  Assert.assertNotNull(RandomDataUtil.getRandomString());
  }


  @Test
  public void getRandomURL() {
	  Assert.assertNotNull(RandomDataUtil.getRandomURL());
  }


  @Test
  public void getUniqueRandomNumbers() {
	  Assert.assertTrue(RandomDataUtil.getUniqueRandomNumbers(0, 100, 50).size() == 50);
  }
}
