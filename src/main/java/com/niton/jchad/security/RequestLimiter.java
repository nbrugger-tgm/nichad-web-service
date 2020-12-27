package com.niton.jchad.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class RequestLimiter implements HandlerInterceptor {

	public final int DAILY_REQUESTS               ;
	public final int REQUEST_DELAY_MS             ;
	public final int REQUESTS_PER_INTERVAL        ;
	public final int REQUEST_INTERVAL_DURATION_MS ;


	private final Map<String, Long>    lastRequest           = new HashMap<>();
	private final Map<String, Long>    intervalEnd           = new HashMap<>();
	private final Map<String, Integer> intervallRequestCount = new HashMap<>();
	private final Map<String, Long>    todayRequests         = new HashMap<>();

	public RequestLimiter(int daily_requests,
	                      int request_delay_ms,
	                      int requests_per_interval,
	                      int request_interval_duration_ms) {
		DAILY_REQUESTS               = daily_requests;
		REQUEST_DELAY_MS             = request_delay_ms;
		REQUESTS_PER_INTERVAL        = requests_per_interval;
		REQUEST_INTERVAL_DURATION_MS = request_interval_duration_ms;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
	                         HttpServletResponse response,
	                         Object handler) throws Exception {
		String ip        = request.getRemoteAddr();
		long   timeStamp = System.currentTimeMillis();
//		if (timeStamp - lastRequest.getOrDefault(ip, 0L) < REQUEST_DELAY_MS) {
//			throw new RateLimitException("Request to fast");
//		}
		intervallRequestCount.put(ip, intervallRequestCount.getOrDefault(ip, 0) + 1);
		todayRequests.put(ip, todayRequests.getOrDefault(ip, 0L) + 1);
		lastRequest.put(ip, timeStamp);

		if (intervalEnd.getOrDefault(ip, 0L) <= timeStamp) {
			intervallRequestCount.remove(ip);
			intervalEnd.put(ip, timeStamp + REQUEST_INTERVAL_DURATION_MS);
		}
		if (intervallRequestCount.getOrDefault(ip, 0) > REQUESTS_PER_INTERVAL) {
			throw new RateLimitException("All requests are used. Wait for the next interval");
		}
		if (todayRequests.getOrDefault(ip, 0L) > DAILY_REQUESTS) {
			throw new RateLimitException("All daily requests are used. Wait for tomorrow");
		}
		return true;
	}
}
