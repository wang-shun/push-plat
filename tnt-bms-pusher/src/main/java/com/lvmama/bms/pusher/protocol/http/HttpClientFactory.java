package com.lvmama.bms.pusher.protocol.http;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HttpClientFactory {

	public static CloseableHttpClient httpClient;

	static {
		//保持连接时长预估时长3000ms=DefaultKeepAliveDuration+closeExpiredConnection检测间隔+数据传输耗时
		final long DefaultKeepAliveDuration = 1500;
		final int DefaultConnectionTimeout = 5*1000;
		final int DefaultSocketTimeout = 5*1000;

		final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setDefaultMaxPerRoute(300);
		connectionManager.setMaxTotal(Integer.MAX_VALUE);

		ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {

			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				//Connection:keep-alive
				//Keep-Alive:timeout=60
				final HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
				while (it.hasNext()) {
					final HeaderElement he = it.nextElement();
					final String param = he.getName();
					final String value = he.getValue();
					if (value != null && param.equalsIgnoreCase("timeout")) {
						try {
							return Long.parseLong(value) * 1000;
						} catch(final NumberFormatException ignore) {
						}
					}
				}

				return DefaultKeepAliveDuration;

			}

		};

		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				connectionManager.closeExpiredConnections();
			}
		}, 500, 500, TimeUnit.MILLISECONDS);

		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setConnectTimeout(DefaultConnectionTimeout)
				.setSocketTimeout(DefaultSocketTimeout)
				.build();

		httpClient = HttpClients.custom()
				.setUserAgent("lvmama-agent")
				.setDefaultRequestConfig(defaultRequestConfig)
				.setConnectionManager(connectionManager)
				.setKeepAliveStrategy(connectionKeepAliveStrategy)
				.setConnectionReuseStrategy(new DefaultClientConnectionReuseStrategy())
				.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
				.build();

	}
	
	private HttpClientFactory() {
	}
	
	public static CloseableHttpClient getHttpClient() {
		return httpClient;
	}
	
}
