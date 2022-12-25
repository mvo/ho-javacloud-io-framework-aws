package javacloud.framework.aws.internal;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.auth.AWSCredentialsProvider;

import javacloud.framework.cdi.internal.IntegrationTest;

public class AWSConfigIT extends IntegrationTest {
	@Inject
	AWSCredentialsProvider credentialsProvider;
	
	@Test
	public void testCredentials() {
		Assert.assertTrue(credentialsProvider instanceof AWSCredentialsProviderChain);
		Assert.assertEquals("secretKey", credentialsProvider.getCredentials().getAWSSecretKey());
	}
}
