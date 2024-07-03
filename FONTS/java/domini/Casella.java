package domini;

import java.io.Serializable;


/**
 * Classe que representa la casella d'un Kenken, que forma part d'una regió i que pot contenir un número o no. Conté mètodes per consultar els seus atributs i modificar-los segons sigui necessari.
 */
public class Casella implements Serializable {
    /**
     * Enter que indica la fila en la que es troba la casella dins del Kenken.
     */
    private int fila;

    /**
     * Enter que ndica la columna en la que es troba la casella dins del Kenken.
     */
    private int columna;

    /**
     * Enter que indica el valor que té la casella.
     */
    private int numero;

    /**
     * Booleà que indica si la casella està resolta correctament.
     */
    private boolean bloquejada;

    /**
     * Booleà que indica si la casella té un valor introduit per l'usuari durant la partida.
     */
    private boolean introduit;

    /**
     * Enter que indica l'identificador de la regio.
     */
    private int idRegio;

    /**
     * Constructora de la classe (a partir d'una fila, columna, valor i idR de la regió a la que pertany).
     * @param x Enter que indica la fila de la casella.
     * @param y Enter que indica la columna de la casella.
     * @param val Enter que indica el valor de la casella.
     * @param idR Enter que identifica la regió.
     */
    public Casella (int x, int y, int val, int idR) {
        fila = x;
        columna = y;
        numero = val;
        bloquejada = false;
        introduit = false;
        idRegio = idR;
    }

    /**
     * Constructora de la classe (a partir d'una fila, columna i valor).
     * @param x Enter que indica la fila de la casella.
     * @param y Enter que indica la columna de la casella.
     * @param val Enter que indica el valor de la casella.
     */
    public Casella (int x, int y, int val) {
        fila = x;
        columna = y;
        numero = val;
        bloquejada = false;
        introduit = false;
    }

    public int getFila () {
        return fila;
    }

    public int getColumna () {
        return columna;
    }

    public int getNumero () {
        return numero;
    }

    public int getIdRegio() {
        return idRegio;
    }

    public void setNumero (int n) {
        this.numero = n;
    }

    public void setIdRegio(int idR) {
        idRegio = idR;
    }

    public void setIntroduit(boolean introduit) { this.introduit = introduit; }

    /**
     * Mètode que comprova si una casella està buida.
     * @return true si la casella esta buida, false altrament.
     */
    public boolean casellaBuida () {
        return (numero == -1);
    }

    /**
     * Mètode que canvia el valor del numero de la casella a -1.
     */
    public void borra () {
        numero = -1;
    }

    /**
     * Mètode que canvia el valor de l'atribut bloquejada a true.
     */
    public void bloquejar () {
        bloquejada = true;
    }

    /**
     * Mètode que canvia el valor del booleà bloquejada a false.
     */
    public void desbloquejar () {
        bloquejada = false;
    }

    /**
     * Mètode que comprova si una casella es troba bloquejada o no.
     * @return true si la casella està bloquejada, false altrament.
     */
    public boolean estaBloquejada () {
        return bloquejada;
    }

    /**
     * Mètode que comprova si una casella té un valor introduit per l'usuari o no.
     * @return true si la casella té un valor introduit per l'usuari, false altrament.
     */
    public boolean estaIntroduit() { return introduit; }
}




