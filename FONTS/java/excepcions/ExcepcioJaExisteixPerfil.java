package excepcions;

/**
 * Excepció que es llença quan ja existeix un perfil amb el nom introduït.
 */
public class ExcepcioJaExisteixPerfil extends Exception {
    public ExcepcioJaExisteixPerfil(String s) {
        super(s);
    }
}
