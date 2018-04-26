#!/bin/bash
SHELL_FOLDER=$(dirname $(readlink -f "$0"))
cd $SHELL_FOLDER
jre/bin/java -jar GoProxy.jar >/dev/null 2>&1 &

