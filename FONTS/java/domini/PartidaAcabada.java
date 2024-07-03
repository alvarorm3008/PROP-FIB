package domini;

/**
 * Classe que representa una partida que ja ha estat acabada i que pot entrar al ranquing.
 */
public class PartidaAcabada {
    /**
     * Atribut que representa el nom d'usuari d'un perfil.
     */
    private String nomusuari;
    /**
     * Atribut que representa el temps en segons utlitzats per resoldre la partida representada per la classe Pair.
     */
    private int temps;

    /**
     * Constructora d'una Partida Acabada amb el nom del perfil que ha jugat la partida i el temps utilitzat per resoldre el Kenken.
     * @param nomUS nom del perfil que ha jugat la partida acabada.
     * @param temps temps utilitzat per resoldre el Kenken de la partida acabada.
     */
    public PartidaAcabada(String nomUS, int temps) {
        this.nomusuari = nomUS;
        this.temps = temps;
    }

    public String getNomUsuari() {
        return nomusuari;
    }

    public void setNomUsuari(String us) {
        this.nomusuari = us;
    }

    public int getTemps() {
        return temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }
}