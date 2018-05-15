

cd .\bin


rd /s/q .\org\hy\xflow\engine\junit


jar cvfm hy.xflow.engine.jar MANIFEST.MF META-INF org

copy hy.xflow.engine.jar ..
del /q hy.xflow.engine.jar
cd ..





cd .\src
jar cvfm hy.xflow.engine-sources.jar MANIFEST.MF META-INF org 
copy hy.xflow.engine-sources.jar ..
del /q hy.xflow.engine-sources.jar
cd ..
