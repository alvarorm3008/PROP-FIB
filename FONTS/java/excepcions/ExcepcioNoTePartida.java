package excepcions;

/**
 * Excepció que es llença quan l'usuari no té cap partida guardada.
 */
public class ExcepcioNoTePartida extends Exception {
    public ExcepcioNoTePartida() {
        super("Aquest usuari no té cap partida guardada.");
    }
    public ExcepcioNoTePartida(String m) {
        super(m);
    }
}
