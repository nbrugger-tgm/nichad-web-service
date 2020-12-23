package com.niton.jchad.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestLimiter implements HandlerInterceptor {

	public static int DAILY_REQUESTS               = 10000;
	public static int REQUEST_DELAY_MS             = 10;
	public static int REQUESTS_PER_INTERVAL        = 40;
	public static int REQUEST_INTERVAL_DURATION_MS = 1000 * 60;


	private final Map<String, Long>    lastRequest           = new HashMap<>();
	private final Map<String, Long>    intervalEnd           = new HashMap<>();
	private final Map<String, Integer> intervallRequestCount = new HashMap<>();
	private final Map<String, Long>    todayRequests         = new HashMap<>();

	@Override
	public boolean preHandle(HttpServletRequest request,
	                         HttpServletResponse response,
	                         Object handler) throws Exception {
		String ip        = request.getRemoteAddr();
		long   timeStamp = System.currentTimeMillis();
		if (timeStamp - lastRequest.getOrDefault(ip, 0L) < REQUEST_DELAY_MS) {
			throw new RateLimitException("Request to fast");
		}
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
