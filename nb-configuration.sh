#!/bin/sh
find . -mindepth 2 -name pom.xml -execdir pwd \; -execdir cp -v ../nb-configuration.xml . \;
