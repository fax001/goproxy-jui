#!/bin/bash
rm -rf goproxy-jui-linux.7z goproxy-jui-linux

mkdir goproxy-jui-linux
mkdir goproxy-jui-linux/conf
cp conf/autostart goproxy-jui-linux/conf/
cp -R dist/lib dist/GoProxy.jar  goproxy-jui-linux/
cp start.sh goproxy-jui-linux/
cp -R jre_linux goproxy-jui-linux/jre
7z a  goproxy-jui-linux.7z goproxy-jui-linux

rm -rf goproxy-jui-linux
