package excepcions;

/**
 * Excepció que es llença quan no hi ha cap partida guardada per carregar.
 */
public class ExcepcioNoHiHaPartidaGuardada extends Exception {
    public ExcepcioNoHiHaPartidaGuardada(String m) {
        super(m);
    }
    public ExcepcioNoHiHaPartidaGuardada() {
        super("No existeix cap partida guardada per carregar");
    }
}
