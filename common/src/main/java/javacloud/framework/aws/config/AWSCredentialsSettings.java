package javacloud.framework.aws.config;

import javacloud.framework.config.ConfigProperty;

public interface AWSCredentialsSettings {
	@ConfigProperty(name = "aws.accessKeyId")
	String accessKey();
	
	@ConfigProperty(name = "aws.secretKey")
	String secretKey();
	
	@ConfigProperty(name = "aws.sessionToken")
	String sessionToken();
}
