package com.kasonyang.httprequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author KasonYang im@kasonyang.com
 *
 */
class HttpRequest {

	private CloseableHttpClient httpclient;
	
	int statusCode;
	String responseText;
	String uri;
	
	private CloseableHttpClient getHttpClient(){
		if(!httpclient){
			httpclient = HttpClients.createDefault();
		}
		return httpclient;
	}
	
	HttpResponse execute(def method) throws IOException{
		CloseableHttpClient client = getHttpClient();
		CloseableHttpResponse response;
		try {
			response = client.execute(method);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		}
		return new HttpResponse(response)
	}
	
	public HttpResponse get(String uri,Map params = [:]) throws IOException{
		def httpGet = createGet(uri,params)
		return this.execute(httpGet)
	}
	public HttpGet createGet(String uri,Map params = [:]) throws IOException{
		if(params){
			URIBuilder uriBuilder;
			try {
				uriBuilder = new URIBuilder(uri);
			} catch (URISyntaxException e2) {
				throw new RuntimeException(e2);
			}
			for(p in params){
				uriBuilder.setParameter(p.key.toString(),p.value.toString());
			}
			try {
				uri = uriBuilder.build().toString();
			} catch (URISyntaxException e1) {
				throw new RuntimeException(e1);
			}
		}
		HttpGet httpGet = new HttpGet(uri);
		return httpGet
	}
	
	public HttpResponse post(String uri,Map params=[:])  throws IOException{
		def httpPost = createPost(uri,params)
		return this.execute(httpPost)
	}
	
	public HttpPost createPost(String uri,Map params=[:]){
		HttpEntity entity = null;
		if(params){
			ArrayList<NameValuePair> paramsNvp = new ArrayList();
			for(p in params){
				paramsNvp.add(new BasicNameValuePair(p.key.toString(),p.value.toString()));
			}
			try {
				entity = new UrlEncodedFormEntity(paramsNvp);
			} catch (UnsupportedEncodingException e1) {
				throw new RuntimeException(e1);
			}
		}
		HttpPost httpPost = new HttpPost(uri);
		if(entity != null){
			httpPost.setEntity(entity);
		}
		return httpPost
	}
}
