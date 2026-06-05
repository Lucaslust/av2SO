@echo off
echo ================================================
echo  Simulador de Substituicao de Paginas - UNIFOR
echo ================================================
echo.

echo [1/2] Compilando...
if not exist "out" mkdir out
javac -d out src\simulador\*.java
if %errorlevel% neq 0 (
    echo ERRO: Falha na compilacao. Verifique se o JDK esta instalado.
    pause
    exit /b 1
)
echo Compilacao concluida!
echo.

echo [2/2] Iniciando interface grafica...
java -cp out simulador.Main
