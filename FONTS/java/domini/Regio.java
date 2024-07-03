package domini;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
* Classe que representa una regió del Kenken que està formada per un un conjunt de caselles, que conté una operació i el seu resultat. Implementa mètodes per tal de crear la regió, modificar-la i assegurar el seu bon funcionament. 

*/
public class Regio implements Serializable {

    /**
     * Enter que representa l'identificador de la regió.
     */
    private int identificador;

    /**
     * Objecte Operació, que indica l'operació assignada a la regió.
     */
    private Operacio operacio;

    /**
     * Enter que representa el resultat assignat a la regió.
     */
    private int resultat;

    /**
     * Enter que indica el nombre de caselles que té la regió.
     */
    private int num_caselles;

    /**
     * Llista de caselles que formen la regió.
     */
    private List<Casella> caselles;

    /**
     * Constructora d'una regió amb una única casella.
     * @param id Identificador de la regió.
     * @param result Resultat de la regió.
     */
    public Regio (int id, int result) {
        identificador = id;
        resultat = result;
        num_caselles = 1;
        caselles = new ArrayList<Casella>(num_caselles);
        operacio = null;                 //vol dir que no hi ha cap operacio
    }

    /**
     * Constructora d'una regió amb una operació, un resultat i un nombre de caselles assignades.
     * @param id Identificador de la regió.
     * @param op Objecte Operació que s'assignarà a la regió.
     * @param result Resultat que s'assignarà a la regió.
     * @param numc Nombre de caselles que contindrà la regió.
     */
    public Regio (int id, Operacio op, int result, int numc) {
        identificador = id;
        operacio = op;
        resultat = result;
        num_caselles = numc;
        caselles = new ArrayList<Casella>(num_caselles);
    }

    /**
     * Constructora que només indica l'identificador de la regió.
     * @param id Enter que representa l'identificador de la regio.
     */
    public Regio (int id) {
        identificador = id;
        caselles = new ArrayList<>();
        num_caselles = 0;
        operacio = null;
    }

    /**
     * Constructora d'una regió amb una llista de caselles i un nombre de caselles assignades.
     * @param id Identificador de la regió.
     * @param caselles Llista de caselles que formen la regió.
     * @param tam Nombre de caselles que contindrà la regió.
     */
    public Regio (int id, List<Casella> caselles, int tam) {
        identificador = id;
        operacio = null;
        num_caselles = tam;
        this.caselles = caselles;
    }

    public int getId() {
        return identificador;
    }

    public Operacio getOperacio() {
        return operacio;
    }

    public int getResultat() {
        return resultat;
    }

    public int getnum_caselles() {
        return num_caselles;
    }

    public List<Casella> getCaselles() { //no se usa
        return caselles;
    }

    public void setId(int identificador) {
         this.identificador = identificador;
    }

    public void setOperacio(Operacio operacio) {
        this.operacio = operacio;
    }

    public void setResultat(int resultat) { //no se usa
        this.resultat = resultat;
    }

    public void setnum_caselles(int num_caselles) { //no se usa
        this.num_caselles = num_caselles;
    }

    public void setCaselles (List<Casella> l) { //no se usa
        caselles = l;
    }

    //METODES

    /**
     * Mètode que afegeix una casella a la llista de caselles de la regió.
     * @param casella Objecte de casella a afegir.
     */
    public void afegirCasella(Casella casella) {
        caselles.add(casella);
    }

    /**
     * Mètode que comproba si una regió té alguna casella buida.
     * @return Retorna false si la regió té alguna casella buida, true en cas contrari.
     */
    public boolean regCompleta() {
        for (int i = 0; i < caselles.size(); ++i) {
            if (caselles.get(i).casellaBuida()) return false;
        }
        return true;
    }

    /**
     * Mètode que comproba si el resultat marcat a la regió és correcte.
     * @return Retorna true si el resultat de la regió és correcte, false en cas contrari.
     */
    public boolean comprovaResultat() {
        if (operacio == null) return true;
        List<Integer> valorscas = new ArrayList<>();
        for (Casella casella : caselles) {
            if (casella.getNumero() != -1) valorscas.add(casella.getNumero());
        }
        int res = operacio.calcula(valorscas.toArray(new Integer[0]));
        if (res == -1) return false;
        return res == resultat;
    }

    /**
     * Mètode que obté la casella de la llista de caselles de la regió que es troba a la posició passada per paràmetre.
     * @param n Enter que indica la posició de la casella dins de la llista.
     * @return Retorna la casella de la llista de caselles de la regió que es troba a la posició del paràmetre.
     */
    public Casella getCasellaDeRegio (int n) {
        return caselles.get(n);
    }

    /**
     * Mètode que comprova si el resultat marcat a la regió és correcte.
     * @return Retorna true si el resultat de la regió és major o igual que el que hauria de ser, false en cas contrari.
     */
    public boolean checkRegio() {
        if (operacio.getId() == 1 || operacio.getId() == 3) {
            int res = calcularRegio();
            if (res > resultat) return false;
        }
        return true;
    }
    
    /**
     * Mètode que calcula la suma o multiplicació dels valors que hi ha a les caselles de la regió.	
     * @return Enter que representa la suma o multiplicació dels valors que hi ha a les caselles de la regió.
     */
    public int calcularRegio () {
        List<Integer> valorscas = new ArrayList<>();
        for (Casella casella : caselles) {
            if (casella.getNumero() != -1) valorscas.add(casella.getNumero());
        }
        return operacio.calcula(valorscas.toArray(new Integer[0]));
    }
}