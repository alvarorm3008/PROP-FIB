package domini;
import excepcions.ExcepcioPistesMax;
/**
 * Classe que representa la simulació d’una partida en la qual un usuari resol un Kenken determinat. Conté els mètodes per tal que aquesta pugui ser iniciada, pausada o finalitzada pel jugador.
 */
public class Partida {

    /**
     * Enter que representa el temps transcorregut en segons.
     */
    int segons;

    /**
     * Booleà que indica si la partida esta actualment en pausa o no.
     */
    private boolean pausada;

    /**
     * Booleà que indica si s'ha utilitzat l'assistencia de la maquina durant la partida.
     */
    private boolean assistencia;

    /**
     * Booleà que indica si l'usuari ha resolt o no el Kenken.
     */
    private boolean resolt;

    /**
     * Enter que indica el nombre de pistes utilitzades.
     */
    private int pistes;

    /**
     * Objecte de la classe kenken, que representarà el kenken de la partida.
     */
    //private Kenken kenken;


    private String taulell;

    /**
     * Objecte de la classe perfil, que representarà el perfil del jugador.
     */
    private Perfil perfil;

    /**
     * Constructora de la classe.
     * @param kenken Objecte de la classe Kenken que representa el Kenken de la partida.
     * @param perfil Objecte de la classe Perfil que representa el perfil del jugador.
     */
    public Partida(String kenken, Perfil perfil) {
        this.pausada = true;
        this.assistencia = false;
        this.resolt = false;
        taulell = kenken;
        this.perfil = perfil;
        this.pistes = 0;
    }

    /*
    public Partida(int segons, boolean pausada, boolean assistencia, boolean resolt, int pistes, String kenken) {
        kenken = new Kenken(kenken);

    }*/

    /**
     * Constructora de la classe (partida ja iniciada).
     * @param kenken Objecte de la classe Kenken que representa el Kenken de la partida.
     * @param segons Enter que representa el temps transcorregut en segons.
     * @param pausada Booleà que indica si la partida esta actualment en pausa o no.
     * @param assistencia Booleà que indica si s'ha utilitzat l'assistencia de la maquina durant la partida.
     * @param resolt Booleà que indica si l'usuari ha resolt o no el Kenken.
     * @param pistes Enter que indica el nombre de pistes utilitzades.
     */
    public Partida (String kenken, int segons, boolean pausada, boolean assistencia, boolean resolt, int pistes) {
        taulell = kenken;
        this.perfil = null;
        this.segons = segons;
        this.pausada = pausada;
        this.assistencia = assistencia;
        this.resolt = resolt;
        this.pistes = pistes;
    }

    public Perfil getPerfil () { 
        return perfil; 
    }

    public int getTemps () {
        return segons;
    }

    public boolean isPausada () {
        return pausada;
    }

    public boolean isAssistencia () {
        return assistencia;
    }

    public int getPistes() {
        return pistes;
    }

    public String getTaulell() { return taulell; }

    public boolean isResolt () {
        return resolt;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public void setTemps (int segons) {
        this.segons = segons; 
    }

    public void setPausada (boolean pausada) {
        this.pausada = pausada;
    }

    public void setAssistencia (boolean assistencia) {
        this.assistencia = assistencia;
    }

    public void setPistes(int pistes) {
        this.pistes = pistes;
    }

    public void setResolt (boolean resolt) {
        this.resolt = resolt;
    }

    public void setTaulell(String info) { taulell = info; }

    /**
     * Mètode que s'encarrega de finalitzar la partida pausant el contador i establint la partida com a resolta.
     * @param segons Enter que representa el temps transcorregut en segons.
     */
    public void acabarPartida (int segons) {
        resolt = true;
        setTemps(segons);
        perfil.getEstadistiques().incNumKenkensJugats();
    }

    /**
     * Mètode que s'encarrega de demanar una pista a la màquina, que incrementa el nombre de pistes demanades i posa a true l'atribut assistència.
     * @throws ExcepcioPistesMax Es llança si s'han demanat 3 pistes.
     */
    public void demanarPista() throws ExcepcioPistesMax {
        if (pistes < 3) {
            pistes++;
            assistencia = true;
        } else throw new ExcepcioPistesMax("Has esgotat les assitències.");
    }
 }


