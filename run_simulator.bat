@echo off
cd /d "%~dp0"

:: Compile all Java files with JDK 22
"C:\Program Files\Java\jdk-22\bin\javac.exe" ^
--module-path "C:\javafx-sdk-24.0.1\lib" ^
--add-modules javafx.controls,javafx.fxml ^
*.java

:: Run the Main class with JavaFX modules
"C:\Program Files\Java\jdk-22\bin\java.exe" ^
--module-path "C:\javafx-sdk-24.0.1\lib" ^
--add-modules javafx.controls,javafx.fxml ^
-cp ".;C:\javafx-sdk-24.0.1\lib\*" Main

pause