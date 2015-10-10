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
			for(stack in ex.stackTrace){
				if(stack.className == scriptClass){
					def fileName = stack.fileName
					def lineNumber = stack.lineNumber.toString()
					println("\tFile:${fileName}(${lineNumber})")
				}
			}
			println "\texcepted:${ex.excepted}\n\tactual:${ex.actual}"
		}catch(Exception ex){	
			ex.printStackTrace()
		}
	}
}
