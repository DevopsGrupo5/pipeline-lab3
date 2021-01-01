package org.cl

def getData() {
	try {
		def request = libraryResource 'org/cl/data.json'
		def json    = readJSON text: request

		return json
	} catch (e) {
		println (e)
 		return {}
	}
}

def cleanRepo(String url) {
	return url.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
}

return this;