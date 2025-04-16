@echo off
REM ------------------------------------------
REM Script pour packager l'application JavaFX
REM en un installateur Windows (.exe) avec jpackage
REM ------------------------------------------

set APP_NAME=calculatriceDEEVEN
set MAIN_JAR=demo-2.jar
set MAIN_CLASS=com.example.CalculatriceApp
set ICON=icon.png
set INPUT_DIR=target
set OUTPUT_DIR=dist

jpackage ^
  --type exe ^
  --name %APP_NAME% ^
  --input %INPUT_DIR% ^
  --main-jar %MAIN_JAR% ^
  --main-class %MAIN_CLASS% ^
  --dest %OUTPUT_DIR% ^
  --icon %ICON% ^
  --win-shortcut ^
  --win-menu ^
  --java-options "--add-opens javafx.base/com.sun.javafx.runtime=ALL-UNNAMED" ^
  --verbose

echo.
echo ✅ Packagé dans le dossier %OUTPUT_DIR%
pause
