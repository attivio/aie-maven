# aie-maven
AIE Maven Repository
====================

This project provides maven POMs and scripts that install artifacts from your AIE installation into your (private) maven repository,
thereby allowing you to use maven for build automation of your AIE-related projects. We also provide sample mavenized AIE projects.

Installation to the local repository
------------------------------------
You're probably most familiar with downloading artifacts from Maven central, or perhaps adding another repository to your POM 
to fetch artifacts. Maven caches artifacts in your local repository, by default under ``%USERPROFILE%\.m2\repository`` (windows) 
or ``${HOME}/.m2/repository`` (linux). The scripts we provide can install artifacts from your local AIE installation to your 
local repository using the [maven deploy-file plugin](http://maven.apache.org/plugins/maven-deploy-plugin/deploy-file-mojo.html).

Installation to a private repository
------------------------------------
Many organizations maintain their own private maven repositories/Nexus installations with a curated set of artifacts 
(e.g. to avoid inadvertent use of artifacts with copy-left licenses). The scripts we provide can install AIE artifacts to your private maven repository.
Just run the scripts with the parameters needed by the [maven deploy-file plugin](http://maven.apache.org/plugins/maven-deploy-plugin/deploy-file-mojo.html) to install artifacts in your private repository.

Usage
====================
* Install AIE and all required modules
* Open a command prompt/shell
* Ensure Java environment is set up (java and maven in path, JAVA_HOME set)
* Set the ATTIVIO_HOME environment variable
* Download from [./repo](https://github.com/attivio/aie-maven/tree/master/repo) and unzip aie-poms-[version].zip to a temporary directory
* Execute the deployment scripts.  The scripts pass all parameters to the maven command line.  You must specify at a minimum 
the url and repository parameters required by the deploy-file plugin.

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
```bash
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
Please refer to the [sample-maven-351](https://github.com/attivio/aie-maven/tree/master/sample-maven-351)
or [sample-maven-430](https://github.com/attivio/aie-maven/tree/master/sample-maven-430)
projects for sample mavenized AIE projects.  The sample POMs are applicable to AIE 3.5.0/3.5.1 and AIE 4.2.0/4.3.0/4.3.1
(just set the version of AIE dependencies accordingly).

Adding Dependencies - 4.x and above
--------------------------------------
Starting with AIE 4.0, we have an [SDK](https://developer.attivio.com/display/extranet420/Developing+with+AIE).  
Custom code should include only AIE SDK artifacts in the compile scope.
For integration testing, we typically start AIE within JUnit tests; therefore, we add AIE runtime artifacts to the test scope.

```xml
<properties>
	<aieVersion>4.3.0</aieVersion>
	<attivio.home>${env.ATTIVIO_HOME}</attivio.home>
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
	<dependency>
		<groupId>com.attivio.platform.platform.esb</groupId>
		<artifactId>api</artifactId>
		<version>${aieVersion}</version>
		<scope>compile</scope>
	</dependency>
	<dependency>
		<groupId>com.attivio.platform.platform.esb</groupId>
		<artifactId>model</artifactId>
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
In AIE 3.5, we did not have a clearly-defined SDK.  Nevertheless, you should limit the compile scope to the APIs you need:
add the API, Transformer, and Connector artifacts to the compile scope 
(based on your needs, see POM below).  Add runtime artifacts required testing to the test scope.

```xml
<properties>
	<aieVersion>3.5.1</aieVersion>
	<attivio.home>${env.ATTIVIO_HOME}</attivio.home>
</properties>
<dependencies>
	<!-- this includes query model and search api -->
	<dependency>
		<groupId>com.attivio.platform.core</groupId>
		<artifactId>api</artifactId>
		<version>${aieVersion}</version>
	</dependency>
	<!-- this is only needed for custom transformer development -->
	<dependency>
		<groupId>com.attivio.platform.platform.base</groupId>
		<artifactId>transformer</artifactId>
		<version>${aieVersion}</version>
	</dependency>
	<!-- this is only needed for custom connector development -->
	<dependency>
		<groupId>com.attivio.platform.platform.base</groupId>
		<artifactId>connector</artifactId>
		<version>${aieVersion}</version>
	</dependency>
	<!-- runtime AIE dependencies in provided scope -->
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
		<classifier>test</classifier>
		<version>${aieVersion}</version>
		<scope>test</scope>
	</dependency>
	<!-- add modules that your integration tests use; below are examples -->
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
* ``src/main/java``
* ``src/main/resources``
* ``src/test/java``
* ``src/test/resources``

This is fine, but if you're developing with AIE, you probably want to run AIE for development and testing.
To do this, simply add the standard AIE project directories to your project:
* ``conf`` - configuration files
* ``lib`` - java libraries 
* ``webapps`` - exploded web applications
* ``resources`` - used in AIE 4.x and above; for AIE resources (e.g. dictionaries).

If we're developing code that runs within AIE (e.g. transformers), then we need to add the jar created by the maven build to the lib directory so that AIE picks it up.
Add the following to your pom so that the jar gets added to the lib directory:
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

Running tests via Maven
-----------------------
Unit/Integration tests fire up an instance of AIE within JUnit.  They need to know where the AIE installation is.
They figure this out from either the ``ATTIVIO_HOME`` environment variable or the ``attivio.home`` java system property.
For portable maven scripts, we define the ``attivio.home`` variable, default it to the ``ATTIVIO_HOME`` environment variable,
and configure the surefire plugin to pass this to unit tests:

```xml
<properties>
...
	<attivio.home>${env.ATTIVIO_HOME}</attivio.home>
...
</properties>
<build>
...
	<plugins>
...
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-surefire-plugin</artifactId>
			<version>2.18.1</version>
			<configuration>
				<argLine>-Xmx4096m -Xms1024m</argLine>
				<forkCount>1</forkCount>
				<reuseForks>false</reuseForks>
				<useSystemClassLoader>true</useSystemClassLoader>
				<useManifestOnlyJar>false</useManifestOnlyJar>
				<systemProperties>
					<attivio.home>${attivio.home}</attivio.home>
				</systemProperties>
			</configuration>
		</plugin>
...
	</plugins>
...
</build>
```

We can then either set the ``ATTIVIO_HOME`` environment variable before calling maven, or pass it to maven via the ``-Dattivio.home=xxx`` option.


Watch out for conflicts
-----------------------
AIE includes many standard open source artifacts, including jakarta commons, spring, guava, etc. Make sure 
that you don't add conflicting dependencies, or inadvertently [include them via transitive dependencies](http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html).  

AIE includes patched versions of some standard, widely used libraries, including SL4J, Log4J, and httpclient. These patched 
libraries use different group/artifact IDs than the originals, so maven cannot automatically resolve conflicting versions.

To prevent maven from using conflicting versions of transitive dependencies, 
[configure maven to exclude them](http://maven.apache.org/guides/introduction/introduction-to-optional-and-excludes-dependencies.html).

License
========
This is distributed under the Apache 2.0 License, on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
ANY KIND.  Please don't blame us if your corn flakes get soggy.

The maven repository created by these scripts includes the most widely-used AIE artifacts. Nevertheless, 
if something is missing, please [add an issue](https://github.com/attivio/aie-maven/issues) and we'll do our best to fix things up.
