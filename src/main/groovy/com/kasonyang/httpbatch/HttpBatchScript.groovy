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
	HttpResponse $$
	
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
	
	/**
	 * set the base path of request
	 * @param path the base path
	 * @return
	 */
	def base(String path){
		this.base = trimPath(path)
	}
	
	def enter(){
		this.path = ''
	}
	
	/**
	 * enter a directory in base
	 * @param path
	 * @return
	 */
	def enter(String path){
		this.path = trimPath(path)
	}
	
	/**
	 * submit a get request
	 * @param uri the request uri
	 * @param params the query params
	 * @param callback call after request
	 * @return
	 */
	HttpResponse go(String uri,Map params,Closure callback){
		def httpGet = http.createGet(getUri(uri),params)
		this.beforeGo?.call(httpGet)
		def response = http.execute(httpGet)
		this.$$ = response
		if(callback) callback.call()
		this.afterGo?.call(response)
		return this.$$
	}
	HttpResponse  go(String uri,Closure callback){
		return go(uri,[:],callback)
	}
	HttpResponse  go(String uri,Map params){
		return go(uri,params,null)
	}
	HttpResponse  go(String uri){
		return this.go(uri,null)
	}
	
	/**
	 * submit a post request
	 * @param uri the request uri
	 * @param params the post params
	 * @param callback call after request
	 * @return
	 */
	HttpResponse  post(String uri,Map params,Closure callback){
		def httpPost = http.createPost(getUri(uri),params)
		this.beforePost?.call(httpPost)
		def response = http.execute(httpPost)
		this.$$ = response
		if(callback) callback.call()
		this.afterPost?.call(response)
		return this.$$
	}
	HttpResponse  post(String uri,Closure callback){
		return post(uri,[:],callback)
	}
	HttpResponse  post(String uri,Map params){
		return post(uri,params,null)
	}
	HttpResponse  post(String uri){
		return this.post(uri,null)
	}
	
	/**
	 * set the beforeGo callback,which whill be call before every get request 
	 * @param callback
	 */
	void beforeGo(Closure callback){
		this.beforeGo = callback
	}
	
	/**
	 * set the beforePost callback,which whill be call before every post request 
	 * @param callback
	 */
	void beforePost(Closure callback){
		this.beforePost = callback
	}
	
	/**
	 * set the callback,which whill be call when test fail 
	 * @param cb
	 */
	void testFail(Closure cb){
		this.testFail = cb
	}
	
	/**
	 * set the callback,which whill be call after every get request
	 * @param callback
	 */
	void afterGo(Closure callback){
		this.afterGo = callback
	}
	
	/**
	 * set the callback,which whill be call after every post request
	 * @param callback
	 */
	void afterPost(Closure callback){
		this.afterPost = callback
	}

	/**
	 * test whether it is true
	 * @param value
	 */
	void testTrue(Object value){
		testEquals(true,value)
	}
	
	/**
	 * test whether actual equals the excepted
	 * @param excepted
	 * @param actual
	 */
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
	
	/**
	 * test whether it is null
	 * @param value
	 */
	void testNull(Object value){
		testEquals(null,value)
	}
	
}
