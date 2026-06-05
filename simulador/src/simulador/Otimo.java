package simulador;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Otimo {

    public static ResultadoAlgoritmo executar(int[] referencias, int numQuadros) {
        Set<Integer> memoria = new HashSet<>();
        List<Integer> listaOrdenada = new ArrayList<>(); // para manter ordem de inserção p/ snapshot
        List<int[]> snapshots = new ArrayList<>();
        int faltas = 0;

        for (int idx = 0; idx < referencias.length; idx++) {
            int pagina = referencias[idx];

            if (!memoria.contains(pagina)) {
                faltas++;
                if (memoria.size() == numQuadros) {
                    // Encontra a página que será usada mais tarde (ou nunca mais)
                    int paginaSubstituir = encontrarMaisDistante(listaOrdenada, referencias, idx);
                    memoria.remove(paginaSubstituir);
                    listaOrdenada.remove(Integer.valueOf(paginaSubstituir));
                }
                memoria.add(pagina);
                listaOrdenada.add(pagina);
            }

            snapshots.add(listaOrdenada.stream().mapToInt(Integer::intValue).toArray());
        }

        return new ResultadoAlgoritmo("Ótimo", faltas, snapshots);
    }

    /**
     * Dentre as páginas na memória, retorna aquela cujo próximo uso
     * está mais distante no futuro (ou que nunca será usada novamente).
     */
    private static int encontrarMaisDistante(List<Integer> memoria, int[] referencias, int posAtual) {
        int paginaVitima = -1;
        int maiorDistancia = -1;

        for (int pagina : memoria) {
            int proximoUso = Integer.MAX_VALUE; // assume que não será usada novamente
            for (int i = posAtual + 1; i < referencias.length; i++) {
                if (referencias[i] == pagina) {
                    proximoUso = i;
                    break;
                }
            }
            if (proximoUso > maiorDistancia) {
                maiorDistancia = proximoUso;
                paginaVitima   = pagina;
            }
        }

        return paginaVitima;
    }
}
