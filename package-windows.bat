@echo off

set APP_NAME=DEEVENwinCalculator
set MAIN_JAR=demo-2.jar
set MAIN_CLASS=com.example.CalculatriceApp
set INPUT_DIR=target
set OUTPUT_DIR=dist

jpackage ^
  --type exe ^
  --input %INPUT_DIR% ^
  --dest %OUTPUT_DIR% ^
  --name %APP_NAME% ^
  --main-jar %MAIN_JAR% ^
  --main-class %MAIN_CLASS% ^
  --win-shortcut ^
  --win-menu ^
  --java-options "--add-opens javafx.base/com.sun.javafx.runtime=ALL-UNNAMED" ^
  --verbose
