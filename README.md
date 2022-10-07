## Version policy extension for maven-release-plugin

Standalone [boring](https://issues.apache.org/jira/browse/MRELEASE-797) `maven-release-plugin`:

```shell
% mvn help:evaluate  -Dexpression=project.version
0.1.2-SNAPSHOT
% mvn release:clean release:prepare
What is the release version for "version-policy-extension"? (version-policy-extension) 0.1.2: :
```

after enabling version policy extension:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-release-plugin</artifactId>
    <configuration>
        <projectVersionPolicyId>FlexibleSemVerPolicy</projectVersionPolicyId>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>tel.panfilov.maven</groupId>
            <artifactId>version-policy-extension</artifactId>
            <version>0.1.3</version>
        </dependency>
    </dependencies>
</plugin>
```

```shell
% mvn release:clean release:prepare
What is the release version for "version-policy-extension"? (version-policy-extension) 0.1.2: : 

% FSVPR=INC_MAJOR mvn release:clean release:prepare
What is the release version for "version-policy-extension"? (version-policy-extension) 1.0.0: : 

% FSVPR=INC_MINOR mvn release:clean release:prepare
What is the release version for "version-policy-extension"? (version-policy-extension) 0.2.0: : 

% FSVPR=INC_PATCH mvn release:clean release:prepare
What is the release version for "version-policy-extension"? (version-policy-extension) 0.1.3: : 

% FSVPR=PRERELEASE_M.1 mvn release:clean release:prepare
What is the release version for "version-policy-extension"? (version-policy-extension) 0.1.2-M.1: : 

% FSVPR=BUILD_0.1 mvn release:clean release:prepare
What is the release version for "version-policy-extension"? (version-policy-extension) 0.1.2+0.1: :

% FSVPR=VERSION_10.0.1 mvn release:clean release:prepare
What is the release version for "version-policy-extension"? (version-policy-extension) 10.0.1: : 

% FSVPR=INC_MINOR FSVPD=INC_MAJOR  mvn release:clean release:prepare
What is the release version for "version-policy-extension"? (version-policy-extension) 0.2.0: : 
What is the new development version for "version-policy-extension"? (version-policy-extension) 1.0.0-SNAPSHOT: : 

```

Focus down and do only what you need to do, do not waste your time on scripting and solving `maven` puzzles :)