package com.kasonyang.httpbatch

import com.kasonyang.httpbatch.test.TestException
import org.codehaus.groovy.control.CompilerConfiguration

/**
 * 
 * @author KasonYang im@kasonyang.com
 *
 */
class Application {

	static void printUsage(){
		println """
usage : httpbatch file #execute a file
     or httpbatch -s   #enter shell mode
"""		
	}
	
	static void main(String[] args) {
		if(args.length<1){
			printUsage()
			return 
		}
		def reader,scriptStr = ''
		switch(args[0]){
			case '-s':
				print ">"
				reader	= System.in
				reader.eachLine {it->
					scriptStr +=  it + '\n'
					if(it == ''){
						runScript(scriptStr)
						scriptStr = ''
					}//else{
					print '>'
					//}
				}
				break
			default:
				def file = new File(args[0])
				runScript(file)
				break
		}
		/*
		while(state = reader.ea){
			
		}
		*/
	}
	
	private static String getExceptionStack(Exception ex,String clsName){
		String msg = ""
		for(stack in ex.stackTrace){
			if(stack.className == clsName){
				def fileName = stack.fileName
				def lineNumber = stack.lineNumber.toString()
				msg += ("\tFile:${fileName}(${lineNumber})")
			}
		}
		msg
	}
	
	private static runScript(def it){
		def config = new CompilerConfiguration()
		config.scriptBaseClass = 'com.kasonyang.httpbatch.HttpBatchScript'
		def shell = new GroovyShell(config)
		def scriptClass
		try{
			//shell.evaluate(file)
			def script = shell.parse(it)
			scriptClass = script.class.name
			def stateReturn = script.run()
			//System.out.println(stateReturn)
		}catch(TestException ex){
			println "test fail:"
			println getExceptionStack(ex,scriptClass)
			println "\texcepted:${ex.excepted}\n\tactual:${ex.actual}"
		}catch(Exception ex){
			println ex.message
			println getExceptionStack(ex,scriptClass)
		}catch(RuntimeException ex){
			println ex.message
			println getExceptionStack(ex,scriptClass)
		}
	}
}
