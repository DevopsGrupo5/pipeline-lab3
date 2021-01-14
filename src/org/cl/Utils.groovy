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

	int upPatch = type == BranchTypeEnum.HOTFIX ? 1 : 0
	int upMinor = type == BranchTypeEnum.FEATURE ? 1 : 0
	int upMajor = type == BranchTypeEnum.CORE ? 1 : 0

	def version = pom.version.replaceFirst(patternBranchRelease) { _, major, minor, patch ->
		"release-v${(major as int) + upMajor}.${(minor as int) + upMinor}.${(patch as int) + upPatch}"
	}
	pom.version = version
	writeMavenPom model: pom
}

def upVersionDev(BranchTypeEnum type) {
	def patternBranchDev = ~/^(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/
	def pom = readMavenPom file: 'pom.xml'
	int upPatch = type == BranchTypeEnum.HOTFIX ? 1 : 0
	int upMinor = type == BranchTypeEnum.FEATURE ? 1 : 0
	int upMajor = type == BranchTypeEnum.CORE ? 1 : 0

	def version = pom.version.replaceFirst(patternBranchDev) { 
		return "${(it[1] as int) + upMajor}.${(it[2] as int) + upMinor}.${(it[3] as int) + upPatch}"
	}

	return version
	// pom.version = version
	// writeMavenPom model: pom
}

String typeof(variable)
 {
	if (variable instanceof Integer) return ("Integer");
    else if(variable instanceof Double) return ("Double");
    else if(variable instanceof Float) return ("Float");
    else if(variable instanceof String) return ("String");
    else if(variable.getClass().isArray()) return ("Array");
    else return ("Unsure");
 }
String generateRow(String text, int size = 60, padding = 1, separator = ' ') {
	String content = "|${"".padLeft(padding, separator)}$text"
	int currentSize = content.length()
	return "${content.padRight(size-1, separator)}|"
}

return this