package simulador;

import java.util.List;

public class ResultadoAlgoritmo {

    private final String nomeAlgoritmo;
    private final int faltasDePagina;
    private final List<int[]> snapshots; // estado da memória a cada referência

    public ResultadoAlgoritmo(String nomeAlgoritmo, int faltasDePagina, List<int[]> snapshots) {
        this.nomeAlgoritmo  = nomeAlgoritmo;
        this.faltasDePagina = faltasDePagina;
        this.snapshots      = snapshots;
    }

    public String getNomeAlgoritmo()      { return nomeAlgoritmo; }
    public int getFaltasDePagina()        { return faltasDePagina; }
    public List<int[]> getSnapshots()     { return snapshots; }

    @Override
    public String toString() {
        return String.format("%-8s → %d faltas de página", nomeAlgoritmo, faltasDePagina);
    }
}
