package com.vettoritech.bybitapi.config;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vettoritech.bybitapi.exception.BybitApiException;
import com.vettoritech.bybitapi.util.BybitProperties;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class RetrofitAPIConfig {
	private final Converter.Factory converterFactory = createConverterFactory();

	protected final Retrofit retrofit;

	@SuppressWarnings("unchecked")
	protected final Converter<ResponseBody, BybitApiError> errorBodyConverter = (Converter<ResponseBody, BybitApiError>) converterFactory
			.responseBodyConverter(BybitApiError.class, new Annotation[0], null);

	public RetrofitAPIConfig(BybitProperties bybitProperties) {
		final OkHttpClient sharedClient = new OkHttpClient.Builder() //
				.pingInterval(20, TimeUnit.SECONDS) //
				.hostnameVerifier((s, sslSession) -> true)//
				.build();

		this.retrofit = new Retrofit.Builder() //
				.baseUrl(bybitProperties.getUrl())//
				.addConverterFactory(converterFactory) //
				.client(sharedClient) //
				.build();
	}

	protected <T> T executeSync(Call<T> call) throws Exception {
		Response<T> response = call.execute();
		if (response.isSuccessful()) {
			return response.body();
		} else {
			try {
				BybitApiError error = getBybitApiError(response);
				throw new BybitApiException(error);
			} catch (JsonParseException jpe) {
				throw new BybitApiException("code: '%d' | result: '%s'", response.code(), response);
			}
		}
	}

	protected BybitApiError getBybitApiError(Response<?> response) throws IOException {
		try (ResponseBody body = response.errorBody()) {
			assert body != null;
			return Objects.requireNonNull(errorBodyConverter).convert(body);
		}
	}

	private JacksonConverterFactory createConverterFactory() {
		return JacksonConverterFactory.create(new ObjectMapper() //
				.enable(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature())
				.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
				.registerModule(new JavaTimeModule()));
	}
}
