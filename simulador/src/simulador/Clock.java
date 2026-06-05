package simulador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Clock {

    public static ResultadoAlgoritmo executar(int[] referencias, int numQuadros) {
        int[] quadros   = new int[numQuadros];
        boolean[] bits  = new boolean[numQuadros]; // bits de referência
        Arrays.fill(quadros, -1); // -1 = vazio

        int ponteiro = 0;
        int ocupados = 0;
        int faltas   = 0;
        List<int[]> snapshots = new ArrayList<>();

        for (int pagina : referencias) {
            // Verifica se página já está na memória
            boolean hit = false;
            for (int i = 0; i < numQuadros; i++) {
                if (quadros[i] == pagina) {
                    bits[i] = true; // marca como referenciada
                    hit = true;
                    break;
                }
            }

            if (!hit) {
                faltas++;

                if (ocupados < numQuadros) {
                    // Ainda há quadros vazios
                    quadros[ponteiro] = pagina;
                    bits[ponteiro]    = true;
                    ponteiro = (ponteiro + 1) % numQuadros;
                    ocupados++;
                } else {
                    // Procura quadro para substituir — segunda chance
                    while (bits[ponteiro]) {
                        bits[ponteiro] = false;
                        ponteiro = (ponteiro + 1) % numQuadros;
                    }
                    quadros[ponteiro] = pagina;
                    bits[ponteiro]    = true;
                    ponteiro = (ponteiro + 1) % numQuadros;
                }
            }

            // Snapshot apenas com páginas válidas
            int[] estado = new int[ocupados < numQuadros ? ocupados : numQuadros];
            for (int i = 0; i < estado.length; i++) estado[i] = quadros[i];
            snapshots.add(estado.clone());
        }

        return new ResultadoAlgoritmo("Clock", faltas, snapshots);
    }
}
