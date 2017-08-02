#!/bin/sh
mvn -q test
mvn -q -Pdagger test
mvn -q -Pdagger2 test
mvn -q -Phk2 test
mvn -q -Pspring test
