package excepcions;

/**
 * Excepció que es llença quan s'ha arribat al nombre màxim de pistes.
 */
public class ExcepcioPistesMax extends Exception{
    public ExcepcioPistesMax(String m) {
        super(m);
    }
}
