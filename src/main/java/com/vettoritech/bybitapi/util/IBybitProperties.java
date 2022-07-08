package com.vettoritech.bybitapi.util;

import org.jetbrains.annotations.NotNull;

import com.vettoritech.bybitapi.util.BybitProperties.Environment;

public interface IBybitProperties {
	interface IEnvironment {
		BybitProperties environment(@NotNull Environment environment);
	}
}
