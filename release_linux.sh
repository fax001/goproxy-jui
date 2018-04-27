#!/bin/bash
rm -rf goproxy-jui-linux.7z goproxy-jui-linux

mkdir goproxy-jui-linux
mkdir goproxy-jui-linux/conf
cp conf/autostart goproxy-jui-linux/conf/
cp -R dist/lib dist/GoProxy.jar  goproxy-jui-linux/
cp start.sh goproxy-jui-linux/
cp -R jre_linux goproxy-jui-linux/jre
7z a  -t7z goproxy-jui-linux.7z goproxy-jui-linux  -mx=9 -ms=200m -mf -mhc -mhcf -mmt -r

rm -rf goproxy-jui-linux
