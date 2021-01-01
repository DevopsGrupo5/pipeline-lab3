package org.cl

def getData() {
	def request = libraryResource 'org/cl/data.json'
	return readJSON text: request
}

def cleanRepo(String url) {
	println("cleanRepo $url")
	return url.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
}

return this;