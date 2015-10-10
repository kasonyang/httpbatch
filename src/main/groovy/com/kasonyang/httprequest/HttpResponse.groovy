package com.kasonyang.httprequest

import groovy.json.JsonException
import groovy.json.JsonSlurper
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
	
	def getJson(){
		if(!json){
			def jsonSlurper = new JsonSlurper()
			try{
				json = jsonSlurper.parseText(text)
			}catch(JsonException ex){
				json = null
			}
		}
		return json
	}
	
	def methodMissing(String name,Object args){
		this.response.invokeMethod(name,args)
	}
	
	def propertyMissing(String name){
		this.response."$name"
	}
	
	def propertyMissing(String name,Object val){
		this.response."$name" = val
	}
	
}
