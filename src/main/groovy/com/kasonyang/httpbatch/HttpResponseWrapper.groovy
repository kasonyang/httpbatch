package com.kasonyang.httpbatch

import com.kasonyang.httprequest.HttpResponse
import groovy.json.JsonException
import groovy.json.JsonSlurper
/**
 *
 * @author KasonYang im@kasonyang.com
 *
 */
class HttpResponseWrapper {

	private HttpResponse response
	private Object json = null
	
	public HttpResponseWrapper(HttpResponse response) {
		this.response = response
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
