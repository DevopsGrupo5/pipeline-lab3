package org.cl

def getData() {
	try{
		def request = libraryResource 'org/cl/data.json'
		def json = readJSON text: request
		return json
	} catch (e){
		println(e)
		return readJSON text: "{}"
	}

}

def cleanRepo(String url) {
	try{
		println("cleanRepo $url")
		return url.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
	} catch (e) {
		println(e)
		return "ms-iclab"
	}
}

return this;