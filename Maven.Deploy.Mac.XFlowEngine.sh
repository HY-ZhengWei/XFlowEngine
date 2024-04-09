#!/bin/sh

rm ./target/classes/org/hy/xflow/engine/config/sys.DB.Config.xml

mvn install:install-file -Dfile=hy.xflow.engine.jar                              -DpomFile=./src/main/resources/META-INF/maven/cn.openapis/hy.xflow.engine/pom.xml
mvn install:install-file -Dfile=hy.xflow.engine-sources.jar -Dclassifier=sources -DpomFile=./src/main/resources/META-INF/maven/cn.openapis/hy.xflow.engine/pom.xml

mvn deploy:deploy-file -Dfile=hy.xflow.engine.jar                                -DpomFile=./src/main/resources/META-INF/maven/cn.openapis/hy.xflow.engine/pom.xml -DrepositoryId=thirdparty -Durl=http://HY-ZhengWei:8081/repository/thirdparty
mvn deploy:deploy-file -Dfile=hy.xflow.engine-sources.jar   -Dclassifier=sources -DpomFile=./src/main/resources/META-INF/maven/cn.openapis/hy.xflow.engine/pom.xml -DrepositoryId=thirdparty -Durl=http://HY-ZhengWei:8081/repository/thirdparty
