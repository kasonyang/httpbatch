package com.kasonyang.httpbatch.test
/**
 *
 * @author KasonYang im@kasonyang.com
 *
 */
class TestException extends Exception {
	
	Object excepted
	
	Object actual
	
	TestException(Object excepted,Object actual){
		this.excepted = excepted
		this.actual = actual
	}
}
