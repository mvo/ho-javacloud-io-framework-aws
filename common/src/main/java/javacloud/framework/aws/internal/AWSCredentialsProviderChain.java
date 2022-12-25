package javacloud.framework.aws.internal;

import javax.inject.Singleton;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

import javacloud.framework.aws.config.AWSCredentialsSettings;
import javacloud.framework.cdi.ServiceRegistry;
import javacloud.framework.config.ConfigManager;
import javacloud.framework.util.Objects;

/**
 * Using ServiceRegistry.get().getInstance(com.amazonaws.auth.AWSCredentialsProvider.class)
 */
@Singleton
public final class AWSCredentialsProviderChain extends com.amazonaws.auth.AWSCredentialsProviderChain {
	public AWSCredentialsProviderChain() {
		super(new EnvironmentVariableCredentialsProvider(),
			CONFIG_PROVIDER,
            WebIdentityTokenCredentialsProvider.create(),
            new ProfileCredentialsProvider(),
            new EC2ContainerCredentialsProviderWrapper());
	}

	private static final AWSCredentialsProvider CONFIG_PROVIDER = new AWSCredentialsProvider() {
		private AWSCredentialsSettings cacheSettings;
		
		@Override
		public AWSCredentials getCredentials() {
			if (cacheSettings == null) {
				ConfigManager configManager = ServiceRegistry.get().getInstance(ConfigManager.class);
				cacheSettings = configManager.getConfig(AWSCredentialsSettings.class);
			}
			
			String accessKey = cacheSettings.accessKey();
	        if (Objects.isEmpty(accessKey)) {
	        	throw new SdkClientException("Not found AWS credentials from ConfigManager");
	        }
	        
	        String sessionToken = cacheSettings.sessionToken();
	        if (Objects.isEmpty(sessionToken)) {
	        	return new BasicAWSCredentials(accessKey, cacheSettings.secretKey());
	        }
	        return  new BasicSessionCredentials(accessKey, cacheSettings.secretKey(), sessionToken);
		}
		
		@Override
		public void refresh() {
			cacheSettings = null;
		}
	};
}
