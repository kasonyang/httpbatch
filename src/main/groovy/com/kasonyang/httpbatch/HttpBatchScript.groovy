package com.kasonyang.httpbatch

import com.kasonyang.httpbatch.test.TestException
import com.kasonyang.httprequest.HttpRequest
import com.kasonyang.httprequest.HttpResponse
import groovy.lang.Binding
import groovy.lang.Script;
/**
 *
 * @author KasonYang im@kasonyang.com
 *
 */
abstract class HttpBatchScript extends Script {
	
	private http = new HttpRequest();
	HttpResponseWrapper $$
	
	Closure beforeGo,beforePost,afterGo,afterPost
	
	Closure testFail
	
	private String base = ''
	private String path = ''
	
	private String trimPath(String path,boolean left=true,boolean right=true){
		int start =(left && path.startsWith('/')) ? 1 : 0;
		int end = (right && path.endsWith('/')) ? path.length()-1 : path.length();
		path.substring(start,end)
	}
	
	private def getUri(uri){
		this.base + '/' + this.path + '/' + trimPath(uri,true,false)
	}
	
	def base(){
		this.base = ''
	}
	
	def base(String path){
		this.base = trimPath(path)
	}
	
	def enter(){
		this.path = ''
	}
	
	def enter(String path){
		this.path = trimPath(path)
	}
	HttpResponseWrapper go(String uri,Map params,Closure callback){
		def httpGet = http.createGet(getUri(uri),params)
		this.beforeGo?.call(httpGet)
		def response = http.execute(httpGet)
		this.$$ = new HttpResponseWrapper(response)
		if(callback) callback.call()
		this.afterGo?.call(response)
		return this.$$
	}
	HttpResponseWrapper  go(String uri,Closure callback){
		return go(uri,[:],callback)
	}
	HttpResponseWrapper  go(String uri,Map params){
		return go(uri,params,null)
	}
	HttpResponseWrapper  go(String uri){
		return this.go(uri,null)
	}
	
	HttpResponseWrapper  post(String uri,Map params,Closure callback){
		def httpPost = http.createPost(getUri(uri),params)
		this.beforePost?.call(httpPost)
		def response = http.execute(httpPost)
		this.$$ = new HttpResponseWrapper(response)
		if(callback) callback.call()
		this.afterPost?.call(response)
		return this.$$
	}
	HttpResponseWrapper  post(String uri,Closure callback){
		return post(uri,[:],callback)
	}
	HttpResponseWrapper  post(String uri,Map params){
		return post(uri,params,null)
	}
	HttpResponseWrapper  post(String uri){
		return this.post(uri,null)
	}
	
	void beforeGo(Closure callback){
		this.beforeGo = callback
	}
	
	void beforePost(Closure callback){
		this.beforePost = callback
	}
	
	void testFail(Closure cb){
		this.testFail = cb
	}
	
	void afterGo(Closure callback){
		this.afterGo = callback
	}
	
	void afterPost(Closure callback){
		this.afterPost = callback
	}

	
	void testTrue(Object value){
		testEquals(true,value)
	}
	
	void testEquals(Object excepted,Object actual){
		if(excepted != actual){
			def ex = new TestException(excepted,actual)
			if(this.testFail){
				testFail(ex)
			}else{
				throw ex
			}
		}
	}
	
	void testNull(Object value){
		testEquals(null,value)
	}
	
}
