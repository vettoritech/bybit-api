package com.vettoritech.bybitapi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import com.vettoritech.bybitapi.util.IBybitProperties.IEnvironment;

public class BybitProperties extends Properties implements IEnvironment {
	private Environment environment;
	private String url;

	private BybitProperties() {
		try (InputStream input = BybitProperties.class.getClassLoader().getResourceAsStream("application.properties")) {
			load(input);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static IEnvironment getInstance() {
		return new BybitProperties();
	}

	@Override
	public BybitProperties environment(@NotNull Environment environment) {
		this.environment = environment;
		return this;
	}

	public BybitProperties url(String url) {
		this.url = url;
		return this;
	}

	public String getUrl() {
		if (StringUtils.isNotBlank(url))
			return url;

		return Objects.equals(Environment.PROD, this.environment) //
				? getProperty("bybit.api.mainnet") //
				: getProperty("bybit.api.testnet");
	}

	public enum Environment {
		PROD, TEST
	}
}
