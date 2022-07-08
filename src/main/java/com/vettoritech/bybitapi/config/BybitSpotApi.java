package com.vettoritech.bybitapi.config;

import com.vettoritech.bybitapi.dto.CommonResult;
import com.vettoritech.bybitapi.service.BybitSpotService;
import com.vettoritech.bybitapi.util.BybitProperties;

public class BybitSpotApi extends RetrofitAPIConfig {
	private BybitSpotService spotService;

	public BybitSpotApi(BybitProperties bybitProperties) {
		super(bybitProperties);
		this.spotService = retrofit.create(BybitSpotService.class);
	}

	public CommonResult getTicker(String symbol) throws Exception {
		return executeSync(spotService.getTicker(symbol));
	}

	public CommonResult getAllTicker() throws Exception {
		return executeSync(spotService.getAllTicker());
	}
}
