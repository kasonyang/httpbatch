package com.kasonyang.httprequest

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.util.EntityUtils;
/**
 *
 * @author KasonYang im@kasonyang.com
 *
 */
class HttpResponse {

	private CloseableHttpResponse response;
	
	int statusCode
	String text
	
	public HttpResponse(CloseableHttpResponse response) {
		this.response = response
		this.statusCode = response.getStatusLine().getStatusCode()
		this.text = EntityUtils.toString(response.getEntity())
	}
	
}
