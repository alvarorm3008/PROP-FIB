package domini;

/**
* Classe que representa el perfil d'un jugador del joc Kenken. Proporciona mètodes per a la seva gestió i la consulta de les seves dades i estadístiques.
*/
public class Perfil {
    /**
     * String que representa el nom d'usuari del perfil.
     */
    private String Usuari;

    /**
     * String que representa la contrasenya associada al perfil.
     */
    private String Password;

    /**
     * Objecte de la classe Estadistiques, que representa les estadístiques del perfil.
     */
    private Estadistiques stats;

    /**
     * Constructora de la classe perfil a partir de un nom d'usuari i una contrasenya.
     * @param nomUS String que representa el nom d'usuari del perfil.
     * @param nomPASS String que representa la contrasenya del perfil.
     */
    public Perfil(String nomUS, String nomPASS) {
        this.Usuari = nomUS;
        this.Password = nomPASS;
        this.stats = new Estadistiques();
    }

    public String getUsuari() {return this.Usuari;}

    public String getPassword() {return this.Password;}

    public Estadistiques getEstadistiques() {
        return stats;
    }

    public void setUsuari(String US){this.Usuari = US;}

    public void setPassword(String PASS){this.Password = PASS;}

    public void setEstadistiques(Estadistiques estadistiques) {
        stats = estadistiques;
    }

    public int getKenkenscreats() {
        return getEstadistiques().getNumKenkensCreats();
    }
    
    public int getKenkensjugats() {
        return getEstadistiques().getNumKenkensJugats();
    }
}
