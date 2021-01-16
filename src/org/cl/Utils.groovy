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

def getVersion() {
	def pom = readMavenPom file: 'pom.xml'
	return pom.version
}

def getCleanVersion() {
	def pom = readMavenPom file: 'pom.xml'
	def version = pom.version.replace("alpha","").replace("rc","").replace("-","").replace("v","")
	println "Current $pom.version"
	println "Clean version $version"
	return version
}

def upVersionRC() {
	def temp_version = getCleanVersion()
	def version = "rc-$temp_version"
	def pom = readMavenPom file: 'pom.xml'
	println "Current version $pom.version"
	println "New version $version"
	pom.version = version
	writeMavenPom model: pom
	return version
}

def upVersionDev(BranchTypeEnum type) {
	// def patternBranchDev = ~/^([a-z])*(-)*([a-z])*(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/
	def patternBranchDev = ~/^(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/
	int upPatch = 0
	int upMinor = 1
	int upMajor = 0
	def temp_version = getCleanVersion()
	def version = temp_version.replaceFirst(patternBranchDev) { 
		return "alpha-${(it[1] as int) + upMajor}.${(it[2] as int) + upMinor}.${(it[3] as int) + upPatch}"
	}

	def pom = readMavenPom file: 'pom.xml'
	println "Current version $pom.version"
	println "New version $version"
	pom.version = version
	writeMavenPom model: pom
	return version
}

String generateRow(String text, int size = 60, padding = 1, separator = ' ') {
	String content = "|${"".padLeft(padding, separator)}$text"
	int currentSize = content.length()
	return "${content.padRight(size-1, separator)}|"
}

return this