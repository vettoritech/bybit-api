package com.vettoritech.bybitapi.api.config;

import static com.vettoritech.bybitapi.util.TestUtils.loadResourceTestFile;

import java.io.IOException;
import java.math.BigDecimal;

import com.vettoritech.bybitapi.config.BybitSpotApi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.vettoritech.bybitapi.dto.CommonResult;
import com.vettoritech.bybitapi.util.BybitProperties;
import com.vettoritech.bybitapi.util.BybitProperties.Environment;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class BybitSpotApiTest {
	private static MockWebServer mockServer;

	private static BybitSpotApi bybitSpotApi;

	@BeforeAll
	static void setUp() throws IOException {
		mockServer = new MockWebServer();
		mockServer.start();

		var mockUrl = mockServer.url("").url().toString();

		BybitProperties bybitProperties = BybitProperties.getInstance() //
				.environment(Environment.TEST) //
				.url(mockUrl);

		bybitSpotApi = new BybitSpotApi(bybitProperties);
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockServer.shutdown();
	}

	@Test
	void getTicker() throws Exception {
		mockServer.enqueue(new MockResponse() //
				.setBody(loadResourceTestFile("api/tiketPrice.json")) //
				.addHeader("Content-Type", "application/json"));

		final CommonResult commonResult = bybitSpotApi.getTicker("ICPETH");

		Assertions.assertEquals(0, commonResult.getRetCode());
		Assertions.assertFalse(commonResult.getResult().isEmpty());
		Assertions.assertEquals(new BigDecimal("5.7"), commonResult.getResult().get(0).getPrice());
		Assertions.assertEquals("ICPETH", commonResult.getResult().get(0).getSymbol());
	}

	@Test
	void getAllTicker() throws Exception {
		mockServer.enqueue(new MockResponse() //
				.setBody(loadResourceTestFile("api/allTiketPrice.json")) //
				.addHeader("Content-Type", "application/json"));

		final CommonResult commonResult = bybitSpotApi.getAllTicker();

		Assertions.assertEquals(0, commonResult.getRetCode());
		Assertions.assertFalse(commonResult.getResult().isEmpty());
		Assertions.assertEquals(new BigDecimal("0.86511"), commonResult.getResult().get(0).getPrice());
		Assertions.assertEquals("PMTEST16USDT", commonResult.getResult().get(0).getSymbol());
	}
}