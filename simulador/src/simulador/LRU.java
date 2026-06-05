package simulador;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LRU {

    public static ResultadoAlgoritmo executar(int[] referencias, int numQuadros) {
        // LinkedHashMap com accessOrder=true mantém ordem de uso (mais antigo no início)
        LinkedHashMap<Integer, Boolean> memoria = new LinkedHashMap<>(numQuadros, 0.75f, true);
        List<int[]> snapshots = new ArrayList<>();
        int faltas = 0;

        for (int pagina : referencias) {
            if (!memoria.containsKey(pagina)) {
                faltas++;
                if (memoria.size() == numQuadros) {
                    // Remove o primeiro (menos recentemente usado)
                    int lru = memoria.keySet().iterator().next();
                    memoria.remove(lru);
                }
                memoria.put(pagina, true);
            } else {
                // Acesso já existente — o get() do LinkedHashMap com accessOrder
                // move automaticamente a página para o final (mais recente)
                memoria.get(pagina);
            }
            snapshots.add(memoria.keySet().stream().mapToInt(Integer::intValue).toArray());
        }

        return new ResultadoAlgoritmo("LRU", faltas, snapshots);
    }
}
