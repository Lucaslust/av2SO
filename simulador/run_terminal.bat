@echo off
echo Compilando...
if not exist "out" mkdir out
javac -d out src\simulador\*.java
echo.
echo Executando modo terminal...
java -cp out simulador.Main --terminal
pause
