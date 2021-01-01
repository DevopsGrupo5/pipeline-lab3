package org.cl

def getData() {
	def request = libraryResource 'org/cl/data.json'
	def json = readJSON text: request
	return json
}

def cleanRepo(String url) {
	try {
		println("cleanRepo $url")
		// def repo = url.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
		String repo = url.replace('https://github.com/DevopsGrupo5/','')
		// return repo
		return repo
	} catch (e) {
		println(e)
		return "ms-iclab"
	}
}

def validateBranchRelease(String branch) {
	def patternBranchRelease = /release-v(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/
	String clean_branch = branch.replace('origin/','')
	return ( clean_branch ==~ patternBranchRelease) ? true : false
}

def upVersion(String type) {
	def patternBranchRelease = ~/^release-v(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/

	def pom = readMavenPom file: 'pom.xml'
	println pom.version

	int upPatch = type == Branch.HOTFIX ? 1 : 0
	int upMinor = type == Branch.FEATURE ? 1 : 0
	int upMajor = type == Branch.CORE ? 1 : 0

	def version = pom.version.replaceFirst(patternBranchRelease) { _, major, minor, patch ->
		"release-v${(major as int) + upMajor}.${(minor as int) + upMinor}.${(patch as int) + upPatch}"
	}
	pom.version = version
	writeMavenPom model: pom
}

String generateRow(String text, int size = 60, padding = 1, separator = ' ') {
	String content = "|${"".padLeft(padding, separator)}$text"
	int currentSize = content.length()
	return "${content.padRight(size-1, separator)}|"
}

return this;