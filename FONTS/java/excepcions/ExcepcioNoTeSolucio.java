package excepcions;

/**
 * Excepció que es llença quan un Kenken no té solució.
 */
public class ExcepcioNoTeSolucio extends Exception {
    public ExcepcioNoTeSolucio() {
        super("El kenken no té solució");
    }
}
