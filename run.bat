@echo off
echo Compiling ShopEasy...
javac -cp "lib/mysql-connector-j-9.7.0.jar" src/dao/*.java src/model/*.java src/ui/*.java src/Main.java

echo Building JAR...
jar cvfm ShopEasy.jar manifest.txt -C src .

echo Launching...
java -cp "ShopEasy.jar;lib/mysql-connector-j-9.7.0.jar" Main
pause