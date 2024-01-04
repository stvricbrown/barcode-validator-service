# barcode-validator-service
Programming challenge for Royal Mail
# Overview
This challenge was performed on Windows 10, running Eclipse JEE 2023-12, Maven 3.9.5, and JDK-17.

The Maven build runs successfully. Both builds include tests.

The spring boot tests fail in the Gradle build - I can't work out why: I'm not very familiar with Gradle.

The Gradle command line is 

```
.\gradlew.bat -Dorg.gradle.java.home=c:\Tools\jdk-17 --console plain --no-build-cache --no-configuration-cache --no-daemon --refresh-dependencies --rerun-tasks --warning-mode all clean build
```

Maven must be running under JDK-17 because the Spring Boot plugin requires it. The Maven wrapper works as well.

I've implemented the service as a REST service with JSON request and response payloads.

This is how I set the Windows environment for Maven:

```
@echo off 
set JAVA_HOME=C:\Tools\jdk-17
set Path=%JAVA_HOME%\bin;^
C:\Tools\apache-maven-3.9.5\bin;^
C:\WINDOWS;^
C:\WINDOWS\system32;^
C:\WINDOWS\System32\Wbem;^
C:\WINDOWS\System32\WindowsPowerShell\v1.0\;^
C:\Program Files\Git\cmd;^
%APPDATA%\..\Local\Microsoft\WindowsApps
mvn -version
```

All the tests use JUnit 5, and most of them are parameterised tests.

You can also use curl in the Git for Bash application for ad-hoc requests.

# Known Issues

1. The spring boot tests fail in the Gradle build - I can't work out why: I'm not very familiar with Gradle.

    The Gradle command line is:
    
    ```
    .\gradlew.bat -Dorg.gradle.java.home=c:\Tools\jdk-17 --console plain --no-build-cache --no-configuration-cache --no-daemon --refresh-dependencies --rerun-tasks --warning-mode all clean build
    ```
2. had a problem making Spring "see" my controller class. In then end, I had to directly import that class.

3. The Web service uses HTTP, not HTTPS. I figured that's OK for a demonstration, but it's no good for production use.
