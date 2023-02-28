

del /Q hy.xflow.engine.jar
del /Q hy.xflow.engine-sources.jar


call mvn clean package
cd .\target\classes


rd /s/q .\org\hy\xflow\engine\junit


jar cvfm hy.xflow.engine.jar META-INF/MANIFEST.MF META-INF org

copy hy.xflow.engine.jar ..\..
del /q hy.xflow.engine.jar
cd ..\..





cd .\src\main\java
xcopy /S ..\resources\* .
jar cvfm hy.xflow.engine-sources.jar META-INF\MANIFEST.MF META-INF org 
copy hy.xflow.engine-sources.jar ..\..\..
del /Q hy.xflow.engine-sources.jar
rd /s/q META-INF
cd ..\..\..
