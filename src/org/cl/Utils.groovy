package org.cl

def getData() {
	def request = libraryResource 'org/cl/data.json'
	def json    = readJSON text: request

	return json
}

def cleanRepo(String url) {
	return url.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
}

return this;