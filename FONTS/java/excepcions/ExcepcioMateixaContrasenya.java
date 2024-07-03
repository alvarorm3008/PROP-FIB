package excepcions;

/**
 * Excepció que es llença quan la contrasenya introduïda és igual a la contrasenya anterior.
 */
public class ExcepcioMateixaContrasenya extends Exception {
    public ExcepcioMateixaContrasenya() {
        super("Aquesta contrasenya es igual a l'anterior");
    }
    public ExcepcioMateixaContrasenya(String m) {
        super(m);
    }
}
