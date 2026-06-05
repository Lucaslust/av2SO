#!/bin/bash
echo "================================================"
echo " Simulador de Substituicao de Paginas - UNIFOR"
echo "================================================"
echo

echo "[1/2] Compilando..."
mkdir -p out
javac -d out src/simulador/*.java
if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilacao. Verifique se o JDK esta instalado."
    exit 1
fi
echo "Compilacao concluida!"
echo

echo "[2/2] Iniciando interface grafica..."
java -cp out simulador.Main
