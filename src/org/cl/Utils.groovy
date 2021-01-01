package org.cl

def getData() {
	def String json
	try {
		def request = libraryResource 'org/cl/data.json'
		json = readJSON text: request
		return json
	} catch (e){
		println(e)
		json = readJSON text: "{}"
		return json
	}

}

def cleanRepo(String url) {
	try {
		println("cleanRepo $url")
		// def repo = url.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
		def repo = url.replace('https://github.com/DevopsGrupo5/','')
		// return repo
		return repo

	} catch (e) {
		println(e)
		return "ms-iclab"
	}
}

return this;