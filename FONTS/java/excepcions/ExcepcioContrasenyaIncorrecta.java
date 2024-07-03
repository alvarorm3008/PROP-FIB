package excepcions;

/**
 * Excepció que es llença quan la contrasenya introduïda no és correcta.
 */
public class ExcepcioContrasenyaIncorrecta extends Exception{
    public ExcepcioContrasenyaIncorrecta(String m) {
        super(m);
    }
}
