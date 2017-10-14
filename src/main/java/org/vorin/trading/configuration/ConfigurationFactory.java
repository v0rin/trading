package org.vorin.trading.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

/**
*
* @author vorin
*/
public enum ConfigurationFactory {

	INSTANCE;

	private IConfiguration configuration;

	public IConfiguration get() {
		checkNotNull(configuration, "Configuration not set. Call ConfigurationFactory.INSTANCE.set() method first");
		return configuration;
	}

	public void set(IConfiguration configuration) {
		if (configuration != null) {
			throw new UnsupportedOperationException("Configuration already set. It is not allowed to override it");
		}
		this.configuration = configuration;
	}
}
