package com.vettoritech.bybitapi.exception;

import com.vettoritech.bybitapi.config.BybitApiError;

public class BybitApiException extends Exception {

	private BybitApiError error;

	public BybitApiException(String string) {
		super(string);
	}

	public BybitApiException(String format, Object... args) {
		super(String.format(format, args));
	}

	public BybitApiException(BybitApiError error) {
		this.error = error;
	}

}
