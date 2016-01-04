#!/bin/sh
mvn clean test
mvn -pl hello-world-lib clean test -Pdagger
mvn -pl hello-world-lib clean test -Pdagger2
mvn -pl hello-world-lib clean test -Phk2
mvn -pl hello-world-lib clean test -Pspring
gradle -p gradlep test
gradle -p gradlep :hello-world-lib:clean :hello-world-lib:test -Pdagger
gradle -p gradlep :hello-world-lib:clean :hello-world-lib:test -Pdagger2
gradle -p gradlep :hello-world-lib:clean :hello-world-lib:test -Phk2
gradle -p gradlep :hello-world-lib:clean :hello-world-lib:test -Pspring
