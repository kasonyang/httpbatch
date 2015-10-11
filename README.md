# What's it？
HttpBatch is a script engine for http request,which is used for automated testing,crawler and so on.

# Features
* Base on groovy,flexible
* Convenient and efficient
* Needlessly to compile

# Usage
1. Installation

  Download the latest package(https://github.com/kasonyang/httpbatch/releases)
  unzip, then add the path `httpbatch/bin` to system environment `PATH`

1. Run
 1. Create a new file with the name "example.hb"
```groovy
go "http://www.baidu.com"
testEquals 200,$$.statusCode//Is the status code 200?
def text = $$.text //get the response as text
println text //output the response
```
 1. Execute the script file
```bash
$ httpbatch example.hb
```

# Examples

### Post form
```
post YOUR_URL ,[user:'test',password:'test'],{
    def statusCode = $$.statusCode
    def text = $$.text //get the text
    testEquals 200,statusCode
}
```
### RESTful test
```
post YOUR_URL,[paramName:'paramValue'],{
    def json = $$.json//get the result as json
    testEquals "Hello Word!",json.message
}
```
### Set request header
```
beforeGo{ req ->
    req.addHeader("Accept","application/json")
}
go YOUR_URL,{
    //do something
}
```

# Pre-defined variables and methods

1. variables
    1. `$$` the latest response.You can use it as an instance of [`org.apache.http.client.methods.CloseableHttpResponse`](http://hc.apache.org/httpcomponents-client-4.3.x/httpclient/apidocs/org/apache/http/client/methods/CloseableHttpResponse.html)
1. methods
    visit the [api](http://www.kasonyang.com/httpbatch/api/com/kasonyang/httpbatch/HttpBatchScript.html)
