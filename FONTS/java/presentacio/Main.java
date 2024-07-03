package presentacio;

/**
* Clase que s'encarrega d'executar l'aplicació.
*/
public class
Main {
    public static void main (String[] args) {
        javax.swing.SwingUtilities.invokeLater (
            new Runnable() {
                public void run() {
                    CtrlPresentacio ctrlPresentacio = new CtrlPresentacio();
                }});
    }

}
