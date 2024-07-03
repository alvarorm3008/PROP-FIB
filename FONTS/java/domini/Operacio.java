package domini;

import java.io.Serializable;

/**
* Classe abstracta que identifica i conté les diverses operacions que una regió pot contenir i que poden ser del tipus suma, resta, multiplicació, divisió, mòdul i màxim. Conté els mètodes per tal de realitzar els càlculs associats a cada tipus d'operació.
*/
public abstract class Operacio implements Serializable {
    /**
     * Enter (entre 1 i 6) que indica l'identificador de l'operació.
     */
    int id; 

    /**
     * String que representa el símbol de l'operació.
     */
    String simbol;

    /**
     * Constructora de la classe.
     * @param id Enter que identifica l'operació.
     * @param simbol String que representa l'operació.
     */
    public Operacio(int id, String simbol) {
        this.id = id;
        this.simbol = simbol;
    }

    public int getId() {
        return id;
    }

    public String getSimbol() { 
        return simbol;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSimbol(String simbol) {
        this.simbol = simbol;
    }

    /**
     * Mètode abstracte que calcula un resultat segons l'operació.
     * @param operands Vector d'enters per a dur a terme el càlcul de l'operació.
     * @return Retorna un enter que és el resultat de l'operació.
     */
    public abstract int calcula(Integer[] operands);
}