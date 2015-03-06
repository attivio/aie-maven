# aie-maven
AIE Maven Repository

Maven is the de-facto method for Java dependency management and build automation.  
There is no publicly-available maven repository that contains AIE artifacts.
This project provides maven POMs and scripts that installs artifacts distributed with the AIE installation into your (private) maven repository.
We also provide sample mavenized projects.

Installation to the local repository
------------------------------------
You're probably most familiar with downloading artifacts from Maven central, or perhaps adding another repository to your POM to fetch artifacts.
Maven caches artifacts in your local repository, by default under %USERPROFILE%\.m2\repository (windows) or ${HOME}/.m2/repository (linux).
You can install artifacts directly in your local repository, and Maven will pick them up from there.
The scripts we provide simply add artifacts from your local AIE installation to your local repository using the [maven deploy-file plugin](http://maven.apache.org/plugins/maven-deploy-plugin/deploy-file-mojo.html).

Installation to a private repository
------------------------------------
Many organizations maintain their own private maven repositories/Nexus installations with a curated set of artifacts 
(e.g. to avoid inadvertent use of artifacts with toxic licenses).
This scripts we provide can install AIE artifacts to your private maven repository.
Just make sure you add the parameters needed, as documented in the [maven deploy-file plugin](http://maven.apache.org/plugins/maven-deploy-plugin/deploy-file-mojo.html).

Usage
====================
* Install AIE and all required modules
* Open a comand prompt/shell
* Ensure Java environment is set up (java and maven in path, JAVA_HOME set)
* Set the ATTIVIO_HOME environment variable
* Download and unzip aie-poms-[version].zip to a temporary directory
* Execute the deployment scripts.  The scripts pass all parameters to the maven command line.  You must specify at a minimum the url and repository parameters required by the deploy-file plugin.

The following examples demonstrate how to deploy AIE artifacts to the default local maven repository (obviously adjust the paths to match your environment).

Windows
-------
```bat
set JAVA_HOME=xxx
set M2_HOME=xxx
set ATTIVIO_HOME=xxx
set PATH=%JAVA_HOME%\bin;%M2_HOME%\bin;%PATH%
aie-mvn-deploy.bat "-Durl=file://%USERPROFILE%\.m2\repository" "-Drepository=localrepo"
aie-mvn-deploy-nonmc.bat "-Durl=file://%USERPROFILE%\.m2\repository" "-Drepository=localrepo"
```

Linux
-------
```sh
JAVA_HOME=xxx
M2_HOME=xxx
ATTIVIO_HOME=xxx
export JAVA_HOME
export ATTIVIO_HOME
PATH=${JAVA_HOME}/bin:${M2_HOME}/bin:${PATH}
chmod u+x aie-mvn-deploy*.sh
dos2unix aie-mvn-deploy*.sh
./aie-mvn-deploy.sh -Durl=file://${HOME}/.m2/repository -Drepository=localrepo
./aie-mvn-deploy-nonmc.sh -Durl=file://${HOME}/.m2/repository -Drepository=localrepo
```

Mavenizing your project
====================================

Adding Dependencies - 4.x and above
--------------------------------------
Starting with AIE 4.0, we have an [SDK](https://developer.attivio.com/display/extranet420/Developing+with+AIE).  Custom code should strive to include only AIE SDK artifacts in the compile scope.
For integration testing, we typically start AIE within JUnit tests; therefore, we add AIE runtime artifacts to the test scope.

```xml
<properties>
	<aieVersion>4.2.0</aieVersion>
</properties>
<dependencies>
	<!-- compile-scoped AIE dependencies -->
	<dependency>
		<groupId>com.attivio.platform.sdk</groupId>
		<artifactId>model</artifactId>
		<version>${aieVersion}</version>
		<scope>compile</scope>
	</dependency>
	<dependency>
		<groupId>com.attivio.platform.sdk</groupId>
		<artifactId>client</artifactId>
		<version>${aieVersion}</version>
		<scope>compile</scope>
	</dependency>
	<dependency>
		<groupId>com.attivio.platform.core</groupId>
		<artifactId>kernel</artifactId>
		<version>${aieVersion}</version>
		<scope>compile</scope>
	</dependency>
	<dependency>
		<groupId>com.attivio.platform.core</groupId>
		<artifactId>api</artifactId>
		<version>${aieVersion}</version>
		<scope>compile</scope>
	</dependency>
	<!-- add the server sdk only if you're developing server-side AIE components like transformers -->
	<dependency>
		<groupId>com.attivio.platform.sdk</groupId>
		<artifactId>server</artifactId>
		<version>${aieVersion}</version>
		<scope>compile</scope>
	</dependency>
	<!-- runtime AIE dependencies in test scope -->
	<!-- manually specify version of xml-apis used by AIE -->
	<dependency>
		<groupId>xml-apis</groupId>
		<artifactId>xml-apis</artifactId>
		<version>1.4.01</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>com.attivio.platform</groupId>
		<artifactId>app</artifactId>
		<version>${aieVersion}</version>
		<scope>test</scope>
		<!-- manually exclude transitive dependencies that conflict with AIE -->
		<exclusions>
			<exclusion>
				<artifactId>xml-apis</artifactId>
				<groupId>xml-apis</groupId>
			</exclusion>
			<exclusion>
				<artifactId>xerces</artifactId>
				<groupId>xerces</groupId>
			</exclusion>
			<exclusion>
				<artifactId>servlet-api</artifactId>
				<groupId>javax.servlet</groupId>
			</exclusion>
		</exclusions>
	</dependency>
	<!-- this artifact includes test support -->
	<dependency>
		<groupId>com.attivio.platform</groupId>
		<artifactId>app</artifactId>
		<classifier>tests</classifier>
		<version>${aieVersion}</version>
		<scope>test</scope>
	</dependency>
	<!-- add modules that your integration tests use below are examples -->
	<dependency>
		<groupId>com.attivio.platform.modules</groupId>
		<artifactId>textextraction</artifactId>
		<version>${aieVersion}</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>com.attivio.platform.modules</groupId>
		<artifactId>dbconnector</artifactId>
		<version>${aieVersion}</version>
		<scope>test</scope>
	</dependency>
</dependencies>
```

Adding Dependencies - 3.5.x
--------------------------------------
In AIE 3.5, we did not have an SDK.  We could pick and choose the artifacts we really need, but for simplicity we expose the entire belly of the beast in the provided scope.
```xml
<properties>
	<aieVersion>3.5.1</aieVersion>
</properties>
<dependencies>
	<!-- runtime AIE dependencies in provided scope -->
	<!-- manually specify version of xml-apis used by AIE -->
	<dependency>
		<groupId>xml-apis</groupId>
		<artifactId>xml-apis</artifactId>
		<version>1.4.01</version>
		<scope>provided</scope>
	</dependency>
	<dependency>
		<groupId>com.attivio.platform</groupId>
		<artifactId>app</artifactId>
		<version>${aieVersion}</version>
		<scope>provided</scope>
		<!-- manually exclude transitive dependencies that conflict with AIE -->
		<exclusions>
			<exclusion>
				<artifactId>xml-apis</artifactId>
				<groupId>xml-apis</groupId>
			</exclusion>
			<exclusion>
				<artifactId>xerces</artifactId>
				<groupId>xerces</groupId>
			</exclusion>
			<exclusion>
				<artifactId>servlet-api</artifactId>
				<groupId>javax.servlet</groupId>
			</exclusion>
		</exclusions>
	</dependency>
	<!-- this artifact includes test support -->
	<dependency>
		<groupId>com.attivio.platform</groupId>
		<artifactId>app</artifactId>
		<classifier>test</classifier>
		<version>${aieVersion}</version>
		<scope>test</scope>
	</dependency>
	<!-- add modules that your integration tests use below are examples -->
	<dependency>
		<groupId>com.attivio.platform.modules</groupId>
		<artifactId>textextraction</artifactId>
		<version>${aieVersion}</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>com.attivio.platform.modules</groupId>
		<artifactId>dbconnector</artifactId>
		<version>${aieVersion}</version>
		<scope>test</scope>
	</dependency>
</dependencies>
```

Mavenized AIE project directory layout
--------------------------------------
The [standard maven directory layout](http://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html) looks like this:
* src/main/java
* src/main/resources
* src/test/java
* src/test/resources

This is fine, but if you're developing with AIE, you probably want to run AIE for development and testing.  
To do this, simply add the standard AIE project directories to your project:
* conf - configuration files
* resources - used in AIE 4.x and above; for AIE resources (e.g. dictionaries).
* lib - java libraries 
* webapps - exploded web applications

If we're developing code that runs within AIE (e.g. transformers), then we need to add the jar created by the maven build to the lib directory so that AIE picks it up.
Add the following to your pom:
```xml
<build>
	<!-- the resources directory is in the AIE classpath - add it to the java classpath for development & testing -->
	<resources>
		<resource>
			<directory>resources</directory>
		</resource>
	</resources>
	<!-- copy the output jar to the lib directory -->
	<plugin>
		<artifactId>maven-antrun-plugin</artifactId>
		<executions>
			<execution>
				<phase>install</phase>
				<configuration>
					<echo>copy ${project.build.directory}/${project.build.finalName}.jar to lib</echo>
					<tasks>
						<!-- copy the jar file to the lib directory -->
						<copy
							file="${project.build.directory}/${project.build.finalName}.jar"
							todir="${basedir}/lib" overwrite="yes" />
					</tasks>
				</configuration>
				<goals>
					<goal>run</goal>
				</goals>
			</execution>
		</executions>
	</plugin>
</build>
```

Watch out for conflicts
-----------------------
AIE includes many standard open source artifacts, including jakarta commons, spring, guava, etc.
Make sure that you don't add conflicting dependencies, or inadvertently [include them via transitive dependencies](http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html).  

AIE includes patched versions of some standard, widely used libraries, including SL4J, Log4J, and commons-httpclient.
These patched libraries use different group/artifact IDs than the originals, so maven cannot automatically resolve conflicting versions.

To prevent maven from using conflicting versions of transitive dependencies, 
[configure maven to exclude them](http://maven.apache.org/guides/introduction/introduction-to-optional-and-excludes-dependencies.html).

License
========
This is distributed under the Apache 2.0 License, on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.  
Please don't blame us if your corn flakes get soggy.

The maven repository created by these scripts includes the most widely-used AIE artifacts. 
Nevertheless, if something is missing, please drop us a line and we'll do our best to fix things up.
