#!/bin/sh

mvn deploy:deploy-file -Dfile=hy.xflow.engine.jar                              -DpomFile=./src/META-INF/maven/org/hy/xflow/engine/pom.xml -DrepositoryId=thirdparty -Durl=http://HY-ZhengWei:1481/repository/thirdparty
mvn deploy:deploy-file -Dfile=hy.xflow.engine-sources.jar -Dclassifier=sources -DpomFile=./src/META-INF/maven/org/hy/xflow/engine/pom.xml -DrepositoryId=thirdparty -Durl=http://HY-ZhengWei:1481/repository/thirdparty
