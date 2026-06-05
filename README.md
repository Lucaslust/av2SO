# Simulador de Algoritmos de Substituição de Páginas

> Trabalho da disciplina de **Sistemas Operacionais** — Ciência da Computação, UNIFOR  
> Implementa e compara quatro algoritmos de substituição de páginas em memória virtual.

---

## 📋 Descrição

O simulador recebe uma cadeia de referências de páginas e o número de quadros de memória disponíveis, executando os quatro algoritmos e exibindo o número de faltas de página de cada um.

**Algoritmos implementados:**

| # | Algoritmo | Descrição |
|---|-----------|-----------|
| 1 | **FIFO** | First In, First Out — remove a página mais antiga |
| 2 | **LRU** | Least Recently Used — remove a menos recentemente usada |
| 3 | **Clock** | Segunda Chance — ponteiro circular com bit de referência |
| 4 | **Ótimo** | Remove a página cujo próximo uso é mais distante |

---

## 🖥️ Pré-requisitos

- **Java JDK 11 ou superior**  
  Download: https://adoptium.net

Verifique a instalação:
```bash
java -version
javac -version
```

---

## ▶️ Como executar

### Windows

**Interface gráfica (recomendado):**
```
run.bat
```

**Modo terminal:**
```
run_terminal.bat
```

### Linux / Mac

```bash
chmod +x run.sh
./run.sh
```

**Modo terminal:**
```bash
mkdir -p out
javac -d out src/simulador/*.java
java -cp out simulador.Main --terminal
```

---

## 🗂️ Estrutura do projeto

```
simulador/
│
├── src/simulador/
│   ├── Main.java               # Interface Swing + ponto de entrada
│   ├── FIFO.java               # Algoritmo FIFO
│   ├── LRU.java                # Algoritmo LRU
│   ├── Clock.java              # Algoritmo do Relógio (Segunda Chance)
│   ├── Otimo.java              # Algoritmo Ótimo (Bélády)
│   └── ResultadoAlgoritmo.java # DTO com resultado + snapshots
│
├── out/                        # Bytecode compilado (gerado pelo script)
├── run.bat                     # Execução Windows (GUI)
├── run_terminal.bat            # Execução Windows (terminal)
├── run.sh                      # Execução Linux/Mac
└── README.md
```

---

## 🎮 Como usar a interface gráfica

1. Execute `run.bat` (Windows) ou `./run.sh` (Linux/Mac)
2. No campo **"Cadeia de páginas"**, insira os números separados por espaço
   - Exemplo: `7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1`
3. Defina o número de **quadros de memória** (padrão: 3)
4. Clique em **▶ Simular**
5. Visualize:
   - Cards com o número de faltas de cada algoritmo
   - Gráfico de barras comparativo
   - Tabela passo a passo do estado da memória

---

## 📊 Resultado esperado (sequência padrão)

Sequência: `7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1` | Quadros: `3`

```
  Método 1 - 15 faltas de página  (FIFO)
  Método 2 - 12 faltas de página  (LRU)
  Método 3 - 14 faltas de página  (Clock)
  Método 4 -  9 faltas de página  (Ótimo)
```

O **Algoritmo Ótimo** serve como referência teórica (menor número possível de faltas).  
O **FIFO** apresenta o pior desempenho por não considerar o histórico de acessos.

---

## 👥 Autores

- Lucas Lustosa  
- João Pedro Alexandrino Brasil

**Disciplina:** Sistemas Operacionais  
**Professor:** [Nome do Professor]  
**Universidade de Fortaleza — UNIFOR, 2026**
