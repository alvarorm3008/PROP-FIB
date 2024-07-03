package excepcions;

/**
 * Excepció que es llença quan no existeix un perfil amb el nom introduït.
 */
public class ExcepcioNoExisteixPerfil extends Exception {
    public ExcepcioNoExisteixPerfil(String m) {
        super(m);
    }
}
