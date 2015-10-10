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
