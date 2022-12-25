package javacloud.framework.aws.config;

import java.util.Map;

import javacloud.framework.config.internal.StandardConfigSource;

/**
 * Auto translate dot to hyphen when lookup environment, for example:
 * aws.region will be passed as AWS_REGION
 * 
 */
public class AWSConfigSource extends StandardConfigSource {
	public AWSConfigSource() {
		this(System.getenv());
	}
	
	AWSConfigSource(Map<String, String> properties) {
		super(properties);
	}
	
	@Override
	public String getProperty(String name) {
		String value = super.getProperty(name);
		if (value == null && name.contains(".")) {
			name = name.replaceAll("\\.", "_").toUpperCase();
			value = super.getProperty(name);
		}
		return value;
	}
}
