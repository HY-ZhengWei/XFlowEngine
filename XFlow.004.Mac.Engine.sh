#!/bin/sh

cd ./bin


rm -R ./org/hy/xflow/engine/junit


jar cvfm hy.xflow.engine.jar MANIFEST.MF META-INF org

cp hy.xflow.engine.jar ..
rm hy.xflow.engine.jar
cd ..





cd ./src
jar cvfm hy.xflow.engine-sources.jar MANIFEST.MF META-INF org 
cp hy.xflow.engine-sources.jar ..
rm hy.xflow.engine-sources.jar
cd ..
