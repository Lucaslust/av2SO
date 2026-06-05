package simulador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FIFO {

    public static ResultadoAlgoritmo executar(int[] referencias, int numQuadros) {
        Queue<Integer> memoria = new LinkedList<>();
        List<int[]> snapshots = new ArrayList<>();
        int faltas = 0;

        for (int pagina : referencias) {
            if (!memoria.contains(pagina)) {
                faltas++;
                if (memoria.size() == numQuadros) {
                    memoria.poll(); // remove o mais antigo
                }
                memoria.add(pagina);
            }
            snapshots.add(memoria.stream().mapToInt(Integer::intValue).toArray());
        }

        return new ResultadoAlgoritmo("FIFO", faltas, snapshots);
    }
}
