package com.haibin.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codelogger.utils.exceptions.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

	protected static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	private static PoolingHttpClientConnectionManager connectionManager;
	
	public static CloseableHttpClient httpclient=null;
	
	private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 800;
	/**
	 * 获取连接的最大等待时间
	 */
	public final static int WAIT_TIMEOUT = 60000;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 250;
	/**
	 * 连接超时时间
	 */
	public final static int CONNECT_TIMEOUT = 5000;
	/**
	 * 读取超时时间
	 */
	public final static int READ_TIMEOUT = 8000;
	
	public final static int BUFFER_SIZE = 4096;
	
	public final static int SO_TIMEOUT = 60000;
	
	//默认重新尝试次数
	public final static int DEFAULT_RETRY_TIME = 3;

    //字符编码
    public static final String CHARSET = "UTF-8";


	private static SSLConnectionSocketFactory sslsf;

	private static SSLContext sslcontext;
	
	static {
		SocketConfig socketConfig  = SocketConfig.custom()
				.setSoKeepAlive(true)
				.setSoTimeout(SO_TIMEOUT)
				.setTcpNoDelay(false)
				.setSoReuseAddress(true)
				.setSoKeepAlive(true)
				.build();
		
		ConnectionConfig connectionConfig =  ConnectionConfig.custom()
				.setBufferSize(BUFFER_SIZE)
				.build();

		try {
			sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
			sslsf = new SSLConnectionSocketFactory(sslcontext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (Exception e) {
			e.printStackTrace();
		}

		connectionManager = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build());
		connectionManager.setDefaultSocketConfig(socketConfig);
		connectionManager.setDefaultConnectionConfig(connectionConfig);
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		
		httpclient = HttpClients.custom()
				.addInterceptorFirst(new GZIPRequestInterceptor())
				.addInterceptorFirst(new GZIPResponseInterceptor())
				.setConnectionManager(connectionManager)
				.setDefaultConnectionConfig(connectionConfig)
//				.setRedirectStrategy(r)
				.build();
	}

	public static void printHeader(HttpResponse response) {
		Header headers[] = response.getAllHeaders();
		StringBuilder cookie = new StringBuilder();
		int ii = 0;
		while (ii < headers.length) {
			System.out.println(headers[ii].getName() + ": " + headers[ii].getValue());
			if (StringUtils.equalsIgnoreCase(headers[ii].getName(), "Set-Cookie")) {
				cookie.append(headers[ii].getValue()).append(";");
			}
			++ii;
		}
		System.out.println(cookie.toString());
	}
	
	public static String doPost(String url, JSONObject jsonObject,int retryTime) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
				if(entry.getKey() != null && entry.getValue() != null) {
					map.put(entry.getKey(), entry.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doPost(url, map,retryTime);
	}

	public static String doPost(String url, JSONObject jsonObject) throws Exception {
		return doPost(url, jsonObject,DEFAULT_RETRY_TIME);
	}
	
	public static String doPost(String url, Map<String, Object> map,int retryTime ) throws Exception{
		try {
			return doPost(url, map);
		} catch (Exception e) {
			if ((e instanceof NoHttpResponseException || e instanceof HttpException) && retryTime > 1) {
				retryTime--;
                return doPost(url, map, retryTime);
            } else {
                throw e;
            }
		}
	}

	public static String doPost(String url, Map<String, Object> map) throws Exception {
		logger.debug("HttpClientUtil doPost  url:{},param:{}",url,map);
		HttpClientContext context = HttpClientContext.create();
		HttpEntity entity = null;
		HttpPost httpPost = new HttpPost(url);
		
		RequestConfig requestConfig = RequestConfig.custom()
	          .setConnectionRequestTimeout(CONNECT_TIMEOUT)
	          .setConnectTimeout(CONNECT_TIMEOUT)
	          .setSocketTimeout(CONNECT_TIMEOUT)
	          .build();
		httpPost.setConfig(requestConfig);
      
		CloseableHttpResponse response = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			response = httpclient.execute(httpPost, context);
			entity = response.getEntity();
			context.getCookieStore().clear();
			return EntityUtils.toString(entity, "utf-8");
		}catch (ConnectionPoolTimeoutException e) {
            log.error("Exception: 连接池超时.");
            throw e;
        } catch (ConnectTimeoutException e) {
            log.error("Exception: 连接超时");
            throw e;
        } catch (SocketTimeoutException e) {
            log.error("Exception: Socket超时.");
            throw e;
        } catch (ConnectException e){
            log.error("Exception: 连接被拒绝.");
            throw e;
        } catch (Exception e) {
            log.error("HTTP Exception", e);
            throw e;
        } finally {
        	if(httpPost!=null)
        		httpPost.abort();
        	if(response!=null)
        		response.close();
        }
	}

    public static String doPost(String url, String content) throws Exception {
        log.info("请求地址[{}], POST过去的数据是：{}", url, content);
        HttpPost httpPost = new HttpPost(url);
        StringEntity postEntity = new StringEntity(content, CHARSET);
        httpPost.addHeader("Content-type", "application/json");
        httpPost.setEntity(postEntity);

        log.info("执行请求 " + httpPost.getRequestLine());

        String result = null;
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity,CHARSET);
        } catch (ConnectionPoolTimeoutException e) {
            log.error("Exception: 连接池超时.");
            throw new Exception(e);
        } catch (ConnectTimeoutException e) {
            log.error("Exception: 连接超时");
            throw new Exception(e);
        } catch (SocketTimeoutException e) {
            log.error("Exception: Socket超时.");
            throw new Exception(e);
        } catch (ConnectException e){
            log.error("Exception: 连接被拒绝.");
            throw new Exception(e);
        }
        catch (Exception e) {
            log.error("HTTP Exception", e);
            throw e;
        } finally {
        	if(httpPost!=null)
        		httpPost.abort();
            if(response!=null)
        		response.close();
        }
        return result;
    }

    public static String doPost(String url, String content, int retryTimes) throws Exception {
        try {
            return doPostByLoop(url, content, retryTimes);
        } catch (Exception e) {
            throw new HttpException(String.format(
                "获取返回值失败, URL: \'%s\' . 尝试 \'%s\' 次",
                new Object[] {url, Integer.valueOf(Math.max(retryTimes, 1))}));
        }
    }

    private static String doPostByLoop(String url, String content, int retryTimes) throws Exception {
        try {
            return doPost(url, content);
        } catch (Exception e) {
            if ((e instanceof NoHttpResponseException || e instanceof HttpException) && retryTimes > 1) {
				retryTimes--;
                return doPost(url, content, retryTimes);
            } else {
                throw e;
            }
        }
    }
	
}
