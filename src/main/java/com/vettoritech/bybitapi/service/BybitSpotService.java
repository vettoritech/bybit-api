package com.vettoritech.bybitapi.service;

import com.vettoritech.bybitapi.dto.CommonResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BybitSpotService {
	@GET("/spot/quote/v1/ticker/price")
	Call<CommonResult> getTicker(@Query("symbol") String symbol);

	@GET("/spot/quote/v1/ticker/price")
	Call<CommonResult> getAllTicker();

}
