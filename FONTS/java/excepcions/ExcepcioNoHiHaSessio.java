package excepcions;

/**
 * Excepció que es llença quan no hi ha cap sessió iniciada.
 */
public class ExcepcioNoHiHaSessio extends Exception {
    public ExcepcioNoHiHaSessio(String m) {
        super(m);
    }
}
