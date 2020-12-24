package org.cl

def getData() {
	def request = libraryResource 'org/cl/data.json'
	return readJSON text: request
}

return this;