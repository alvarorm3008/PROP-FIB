package excepcions;

/**
 * Excepció que es llença quan el tamany del Kenken no és correcte.
 */
public class ExcepcioTamany extends Exception{
    public ExcepcioTamany(String m) {
        super(m);
    }
}
