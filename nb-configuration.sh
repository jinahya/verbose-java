#!/bin/sh
find . -type d -name verbose\* -maxdepth 1 -exec cp -v ./nb-configuration.xml {} \;
