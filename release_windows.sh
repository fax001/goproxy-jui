#!/bin/bash
rm -rf goproxy-jui-windows goproxy-jui-windows.tar.gz

mkdir goproxy-jui-windows
mkdir goproxy-jui-windows/conf
cp conf/autostart.exe conf/proxysetting.exe conf/wininet.dll goproxy-jui-windows/conf/
cp -R dist/lib dist/GoProxy.jar  goproxy-jui-windows/
cp start.vbs bootstrap.bat goproxy-jui-windows/
cp -R jre_win goproxy-jui-windows/jre
tar zcfv goproxy-jui-windows.tar.gz goproxy-jui-windows

rm -rf goproxy-jui-windows

