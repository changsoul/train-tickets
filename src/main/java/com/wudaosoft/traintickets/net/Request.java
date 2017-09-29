/**
 *    Copyright 2009-2017 Wudao Software Studio(wudaosoft.com)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        https://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.wudaosoft.traintickets.net;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 保存上下文的12306网络请求工具，可作模拟IE浏览器请求
 * 
 * 使用12306官网自签的证书进行https加密通信
 * 
 * @author Changsoul Wu
 * 
 */
public class Request {

	private HostConfig hostConfig;
	private CloseableHttpClient httpClient;
	private PoolingHttpClientConnectionManager connManager;

	public Request(HostConfig hc) {
		this.hostConfig = hc;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void init() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
			CertificateException, IOException {

		SSLConnectionSocketFactory sslConnectionSocketFactory = null;

		if (hostConfig.getCA() != null) {
			// Trust root CA and all self-signed certs
			SSLContext sslcontext = SSLContexts.custom()
					.loadTrustMaterial(hostConfig.getCA(), hostConfig.getCAPassword(), new TrustSelfSignedStrategy())
					.build();

			// Allow TLSv1 protocol only
			sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		} else {
			sslConnectionSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
		}

		ConnectionKeepAliveStrategy myKeepAliveStrategy = new ConnectionKeepAliveStrategy() {

			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				// Honor 'keep-alive' header
				HeaderElementIterator it = new BasicHeaderElementIterator(
						response.headerIterator(HTTP.CONN_KEEP_ALIVE));
				while (it.hasNext()) {
					HeaderElement he = it.nextElement();
					String param = he.getName();
					String value = he.getValue();
					if (value != null && param.equalsIgnoreCase("timeout")) {
						try {
							return Long.parseLong(value) * 1000;
						} catch (NumberFormatException ignore) {
						}
					}
				}
				// HttpHost target = (HttpHost)
				// context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
				// if ("kyfw.12306.cn".equalsIgnoreCase(target.getHostName())) {
				// // Keep alive for 5 seconds only
				// return 3 * 1000;
				// } else {
				// // otherwise keep alive for 30 seconds
				// return 30 * 1000;
				// }

				return 30 * 1000;
			}

		};

		/*HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 3) {
					// 如果已经重试了3次，就放弃
					return false;
				}
				if (exception instanceof InterruptedIOException) {
					// 超时
					return false;
				}
				if (exception instanceof UnknownHostException) {
					// 目标服务器不可达
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {
					// 连接被拒绝
					return false;
				}
				if (exception instanceof SSLException) {
					// ssl握手异常
					return false;
				}
				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// 如果请求是幂等的，就再次尝试
					return true;
				}
				return false;
			}

		};*/
		
		//不重试
		HttpRequestRetryHandler myRetryHandler = new DefaultHttpRequestRetryHandler(0, false);

		connManager = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", sslConnectionSocketFactory).build());

		connManager.setMaxTotal(400);
		connManager.setDefaultMaxPerRoute(50);
		connManager.setMaxPerRoute(new HttpRoute(hostConfig.getHost(), null, !HttpHost.DEFAULT_SCHEME_NAME.equals(hostConfig.getHost().getSchemeName())), 300);
		// connManager.setValidateAfterInactivity(2000);

		// Create socket configuration
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).setSoKeepAlive(true).build();
		connManager.setDefaultSocketConfig(socketConfig);

		// Create connection configuration
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).build();
		connManager.setDefaultConnectionConfig(connectionConfig);

		httpClient = HttpClients.custom().setConnectionManager(connManager).setKeepAliveStrategy(myKeepAliveStrategy)
				.setDefaultRequestConfig(hostConfig.getRequestConfig()).setRetryHandler(myRetryHandler).build();
		
		new IdleConnectionMonitorThread(connManager).start();
	}

	public String get(final String hostUrl, String urlSuffix, HttpClientContext context) throws Exception {

		return get(hostUrl, urlSuffix, null, context);
	}

	public String get(final String hostUrl, String urlSuffix, Map<String, String> params, HttpClientContext context)
			throws Exception {
		String url = hostUrl + urlSuffix;
		url = buildReqUrl(url, params);

		HttpGet httpGet = new HttpGet(url);

		setHeader(httpGet, context);

		return getHttpClient().execute(httpGet, new StringResponseHandler(), context);
	}

	public JSONObject getAjax(final String hostUrl, String urlSuffix, HttpClientContext context) throws Exception {

		return getAjax(hostUrl, urlSuffix, null, context);
	}

	/**
	 * Ajax get请求，只请求一次，不做重试
	 */
	public JSONObject getAjax(final String hostUrl, String urlSuffix, Map<String, String> params,
			HttpClientContext context) throws Exception {

		String url = hostUrl + urlSuffix;
		url = buildReqUrl(url, params);

		HttpGet httpGet = new HttpGet(url);

		setAjaxHeader(httpGet, context);
		setHeader(httpGet, context);

		return getHttpClient().execute(httpGet, new JsonResponseHandler(), context);
	}

	/**
	 * post请求，只请求一次，不做重试
	 */
	public String post(final String hostUrl, String urlSuffix, Map<String, String> params, HttpClientContext context)
			throws Exception {

		String url = hostUrl + urlSuffix;
		url = buildReqUrl(url);

		HttpPost httpPost = new HttpPost(url);

		setHeader(httpPost, context);

		if (params != null) {
			UrlEncodedFormEntity postEntity = buildUrlEncodedFormEntity(params);
			httpPost.setEntity(postEntity);
		}

		return getHttpClient().execute(httpPost, new StringResponseHandler(), context);
	}

	/**
	 * Ajax post请求，只请求一次，不做重试
	 */
	public JSONObject postAjax(final String hostUrl, String urlSuffix, Map<String, String> params,
			HttpClientContext context) throws Exception {

		String url = hostUrl + urlSuffix;
		url = buildReqUrl(url);

		HttpPost httpPost = new HttpPost(url);

		setAjaxHeader(httpPost, context);
		setHeader(httpPost, context);

		if (params != null) {
			UrlEncodedFormEntity postEntity = buildUrlEncodedFormEntity(params);
			httpPost.setEntity(postEntity);
		}

		return getHttpClient().execute(httpPost, new JsonResponseHandler(), context);
	}

	public BufferedImage getImage(final String hostUrl, String urlSuffix, HttpClientContext context)
			throws Exception {
		String url = hostUrl + urlSuffix;
		url = buildReqUrl(url);

		HttpGet httpGet = new HttpGet(url);

		httpGet.addHeader("Accept", "image/webp,image/*,*/*;q=0.8");
		setHeader(httpGet, context);

		HttpResponse response = getHttpClient().execute(httpGet, context);
		int status = response.getStatusLine().getStatusCode();
		HttpEntity entity = response.getEntity();

		try {
			if (status != 200)
				throw new ClientProtocolException("Unexpected response status: " + status);

			BufferedImage buffImg = ImageIO.read(entity.getContent());
			return buffImg;
		} finally {
			EntityUtils.consumeQuietly(entity);
			httpGet.releaseConnection();
		}
	}

	public void sendHeartbeat(final String hostUrl, String urlSuffix, HttpClientContext context) throws Exception {
		String url = hostUrl + urlSuffix + "?d=" + new Date().toString();
		url = buildReqUrl(url);
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Accept", "image/webp,image/*,*/*;q=0.8");
		setHeader(httpGet, context);

		HttpClient httpclient = getHttpClient();
		httpclient.execute(httpGet, context);

		httpGet.releaseConnection();
	}

	public String getServerTime(final String hostUrl, String urlSuffix, HttpClientContext systemContext)
			throws Exception {
		String url = hostUrl + urlSuffix;

		HttpGet httpGet = new HttpGet(url);
		setHeader(httpGet, systemContext);

		HttpClient httpclient = getHttpClient();
		HttpResponse response = httpclient.execute(httpGet, systemContext);

		String date = response.getHeaders("Date")[0].getValue();
		httpGet.releaseConnection();
		return date;
	}

	public long testNetworkDaly(final String hostUrl, String urlSuffix, HttpClientContext systemContext)
			throws Exception {
		String url = hostUrl + urlSuffix + "?_r=" + UUID.randomUUID().toString();

		HttpGet httpGet = new HttpGet(url);
		setHeader(httpGet, systemContext);

		HttpClient httpclient = getHttpClient();

		long beginTimemillis = System.currentTimeMillis();

		httpclient.execute(httpGet, systemContext);

		long endTimemillis = System.currentTimeMillis();

		httpGet.releaseConnection();
		return endTimemillis - beginTimemillis;
	}

	public void setAjaxHeader(HttpUriRequest resquest, HttpClientContext context) {
		resquest.addHeader("x-requested-with", "XMLHttpRequest");
		resquest.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		resquest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		resquest.addHeader("Origin", hostConfig.getHostUrl());
		// resquest.addHeader("Referer",
		// "https://210.76.66.109:7006/gdweb/ggfw/web/pub/mainpage/mainpageldl!wsyw.do");
		// resquest.addHeader("Referer",
		// "https://210.76.66.109:7006/gdweb/ggfw/web/wsyw/app/ldlzy/gryw/grbtsb/btxx.do?MenuId=170201");
	}

	public void setHeader(HttpUriRequest resquest, HttpClientContext context) {
		if (!resquest.containsHeader("Accept")) {
			resquest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		}

		resquest.addHeader("Accept-Language", "zh-CN,zh;q=0.8,ja;q=0.6,en;q=0.4");
		resquest.addHeader("Cache-Control", "no-cache");
		resquest.addHeader("Pragma", "no-cache");
		resquest.addHeader("Origin", hostConfig.getHostUrl());

		// resquest.addHeader("Token", CookieUtil.getCookieValue("Token",
		// context.getCookieStore()));

		if (!resquest.containsHeader("Referer") && hostConfig.getReferer() != null) {
			resquest.addHeader("Referer", hostConfig.getReferer());
		}
		resquest.addHeader("User-Agent", hostConfig.getUserAgent());
	}

	/**
	 * 
	 * @param reqUrl
	 * @return
	 */
	public String buildReqUrl(String reqUrl) {

		return buildReqUrl(reqUrl, null);
	}

	/**
	 * 
	 * @param reqUrl
	 * @param params
	 * @return
	 */
	public String buildReqUrl(String reqUrl, Map<String, String> params) {
		if (reqUrl == null)
			return null;

		if (params == null) {

			if (reqUrl.indexOf("?") == -1)
				return reqUrl;

			params = new HashMap<String, String>();
		}

		String[] reqUrls = reqUrl.split("\\?");
		StringBuilder sp = new StringBuilder(reqUrls[0]);

		if (reqUrls.length == 2 && reqUrls[1].trim().length() != 0) {

			String[] kvs = reqUrls[1].split("&");
			for (String kv : kvs) {
				if (kv == null || kv.length() == 0)
					continue;

				String[] nv = kv.split("=");
				if (nv.length > 2)
					continue;

				if (nv.length == 2) {
					params.put(nv[0], nv[1]);
				} else {
					params.put(nv[0], "");
				}
			}
		}

		if (!params.isEmpty()) {
			sp.append("?");
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(params.size());
			for (Map.Entry<String, String> entry : params.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			sp.append(URLEncodedUtils.format(parameters, Consts.UTF_8));
		}
		return sp.toString();
	}

	/**
	 * 
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 */
	public UrlEncodedFormEntity buildUrlEncodedFormEntity(Map<String, String> params) throws ClientProtocolException {
		if (params == null)
			throw new ClientProtocolException("Params is null");

		List<NameValuePair> parameters = new ArrayList<NameValuePair>(params.size());

		for (Map.Entry<String, String> entry : params.entrySet()) {
			parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		return new UrlEncodedFormEntity(parameters, Consts.UTF_8);
	}

	/**
	 * @return
	 */
	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public void shutdown() {
		connManager.shutdown();
	}

	public static void main(String[] args) throws Exception {

		TicketsHostConfig config = new TicketsHostConfig();
		Request req = new Request(config);
		String pp = req.get(config.getHostUrl(), "/otn/login/init", HttpClientContext.create());
		System.out.println(pp);
		String pp1 = req.get(config.getHostUrl(), "/otn/login/init", HttpClientContext.create());
		System.out.println(pp1);

		req.shutdown();
	}

}
