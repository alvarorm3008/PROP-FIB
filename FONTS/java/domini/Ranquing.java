package domini;
import java.util.*;

/**
 * Classe que representa el rànquing de partides acabades de Kenkens, organitzant-les segons la seva dificultat i tamany.
 */
public class Ranquing {
   /**
     * Identificador del rànquing. Representa el tamany del Kenkens que apareixen a les llistes del ranquing.
     */
    private int tam;

    /**
     * Llista de partides acabades de dificultat facil del ranquing.
     */
    private ArrayList<PartidaAcabada> topfacil;

    /**
     * Llista de partides acabades de dificultat mitja del ranquing.
     */
    private ArrayList<PartidaAcabada> topmitja;

    /**
     * Llista de partides acabades de dificultat dificil del ranquing.
     */
    private ArrayList<PartidaAcabada> topdificil;

    /**
     * Constructora per defecte dels ranquings amb les diferents dificultats d'un tamany de Kenken determinat.
     * @param tamany Tamany dels kenkens utilitzats a les partides del ranquing.
     */
    public Ranquing(int tamany) {
        this.tam = tamany;
        this.topfacil = new ArrayList<>();
        this.topmitja = new ArrayList<>();
        this.topdificil = new ArrayList<>();
    }

    public int getTam() {
        return tam;
    }

    public ArrayList<PartidaAcabada> getTopfacil() {
        return topfacil;
    }

    public ArrayList<PartidaAcabada> getTopmitja() {
        return topmitja;
    }

    public ArrayList<PartidaAcabada> getTopdificil() {
        return topdificil;
    }

    public void setTam(int tamany) {
        this.tam = tamany;
    }

    /**
     * Mètode per afegir una partida al ranquing.
     * @param partida Objecte Pair que conté la informació d'una partida acabada.
     * @param dificultat String que identifica la dificultat de la nova partida a introduir al ranquing.
     */
    public void afegirPartida(PartidaAcabada partida, String dificultat) {
        switch (dificultat) {
            case "facil": {
                topfacil.add(partida);
                break;
            }
            case "mitja": {
                topmitja.add(partida);
                break;
            }
            case "dificil": {
                topdificil.add(partida);
                break;
            }
            default: {
                //Aqui seguramente iria la excepcion.
                System.out.println("Dificultat no valida");
            }
        }
        ordenarPartides();
    }

    /**
     * Mètode que elimina totes les partides que existeixen en les llistes de partides segons les dificultats d'un mateix usuari.
     * @param nomUS String que identifica el nom del perfil del qual es volen eliminar les seves partides.
     */
    public void eliminarPartidesUsuari(String nomUS) {
        // Revisem el ranquing de les partides fàcils
        if (!topfacil.isEmpty()) {
            Iterator<PartidaAcabada> itFacil = topfacil.iterator();
            while (itFacil.hasNext()) {
                PartidaAcabada p = itFacil.next();
                // Si coincideix el nom del perfil d'una partida amb el perfil a eliminar s'esborra la partida del ranquing.
                if (Objects.equals(p.getNomUsuari(), nomUS)) itFacil.remove();
            }
        }

        // Revisem el ranquing de les partides de dificultat mitja
        if (!topmitja.isEmpty()) {
            Iterator<PartidaAcabada> itMitja = topmitja.iterator();
            while (itMitja.hasNext()) {
                PartidaAcabada p = itMitja.next();
                // Si coincideix el nom del perfil d'una partida amb el perfil a eliminar s'esborra la partida del ranquing.
                if (Objects.equals(p.getNomUsuari(), nomUS))
                    itMitja.remove();
            }
        }

        // Revisem el ranquing de les partides dificils
        if (!topdificil.isEmpty()) {
            Iterator<PartidaAcabada> itDificil = topdificil.iterator();
            while (itDificil.hasNext()) {
                PartidaAcabada p = itDificil.next();
                // Si coincideix el nom del perfil d'una partida amb el perfil a eliminar s'esborra la partida del ranquing.
                if (Objects.equals(p.getNomUsuari(), nomUS))
                    itDificil.remove();
            }
        }
    }

    /**
     * Mètode que retorna el número de partides que té el ranquing amb tamany N de dificultat fàcil.
     * @return Size de la llista de partides acabades de dificultat facil.
     */
    public int numPartidesFacil() {
        return topfacil.size();
    }

    /**
     * Mètode que retorna el número de partides que té el ranquing amb tamany N de dificultat mitja.
     * @return Size de la llista de partides acabades de dificultat mitja.
     */
    public int numPartidesMitja() {
        return topmitja.size();
    }

    /**
     * Mètode que retorna el número de partides que té el ranquing amb tamany N de dificultat difícil.
     * @return Size de la llista de partides acabades de dificultat dificil.
     */
    public int numPartidesDificil() {
        return topdificil.size();
    }

    /**
     * Mètode que ordena les llistes de partides per temps (de menor a major).
     */
    public void ordenarPartides() {
        Comparator<PartidaAcabada> comparator = Comparator.comparing(PartidaAcabada::getTemps);
        Collections.sort(topfacil, comparator);
        Collections.sort(topmitja, comparator);
        Collections.sort(topdificil, comparator);
    }

    /**
     * Mètode que ordena les llistes de partides per temps (de menor a major).
     * @param dificultat String que identifica la dificultat de les partides a ordenar.
     * @return Retorna un vector d'enters amb els temps de les partides acabades ordenades de menor a major temps.
     */
    public int[] topTemps(String dificultat) {
        int[] tempsTop = new int[10];
        for (int i = 0; i < tempsTop.length; i++) {
            tempsTop[i] = -1;
        }
        switch (dificultat) {
            case "facil": {
                for (int i = 0; i < Math.min(topfacil.size(), 10); ++i) {
                    tempsTop[i] = topfacil.get(i).getTemps();
                }
                break;
            }
            case "mitja": {
                for (int i = 0; i < Math.min(topmitja.size(), 10); ++i) {
                    tempsTop[i] = topmitja.get(i).getTemps();
                }
                break;
            }
            case "dificil": {
                for (int i = 0; i < Math.min(topdificil.size(), 10); ++i) {
                    tempsTop[i] = topdificil.get(i).getTemps();
                }
                break;
            }
            default: {
                //throw new IOException();
                break;
            }
        }
        return tempsTop;
    }

    /**
     * Mètode que ordena les llistes de partides per temps (de menor a major).
     * @param dificultat String que identifica la dificultat de les partides a ordenar.
     * @return Retorna un vector de strings amb els noms dels usuaris que han jugat les partides acabades ordenades de menor a major temps.
     */
    public String[] topUsuaris(String dificultat) {
        String[] usuarisTop = new String[10];
        for (int i = 0; i < usuarisTop.length; i++) {
            usuarisTop[i] = "-";
        }
        switch (dificultat) {
            case "facil": {
                for (int i = 0; i < Math.min(topfacil.size(), 10); ++i) {
                    usuarisTop[i] = topfacil.get(i).getNomUsuari();
                }
                break;
            }
            case "mitja": {
                for (int i = 0; i < Math.min(topmitja.size(), 10); ++i) {
                    usuarisTop[i] = topmitja.get(i).getNomUsuari();
                }
                break;
            }
            case "dificil": {
                for (int i = 0; i < Math.min(topdificil.size(), 10); ++i) {
                    usuarisTop[i] = topdificil.get(i).getNomUsuari();
                }
                break;
            }
            default: {
                //throw new IOException();
                break;
            }
        }
        return usuarisTop;
    }

    /**
     * Mètode que ordena les llistes de partides per temps (de menor a major) i filtra les partides per nom d'usuari.
     * @param dificultat String que identifica la dificultat de les partides a ordenar.
     * @param usuari String que identifica el nom de l'usuari del qual es volen filtrar les partides.
     * @return Retorna un vector d'enters amb els temps de les partides acabades ordenades de menor a major temps en la dificultat passada per paràmetre.
     */
    public int[] topFiltre(String dificultat, String usuari) {
        int[] tempsTop = new int[10];
        for (int i = 0; i < tempsTop.length; i++) {
            tempsTop[i] = -1;
        }
        int cont = 0;
        switch (dificultat) {
            case "facil": {
                for (int i = 0; i < Math.min(topfacil.size(), 10); ++i) {
                    if(Objects.equals(topfacil.get(i).getNomUsuari(), usuari)) {
                        tempsTop[cont] = topfacil.get(i).getTemps();
                        ++cont;
                    }
                }
                break;
            }
            case "mitja": {
                for (int i = 0; i < Math.min(topmitja.size(), 10); ++i) {
                    if(Objects.equals(topmitja.get(i).getNomUsuari(), usuari)) {
                        tempsTop[cont] = topmitja.get(i).getTemps();
                        ++cont;
                    }
                }
                break;
            }
            case "dificil": {
                for (int i = 0; i < Math.min(topdificil.size(), 10); ++i) {
                    if(Objects.equals(topdificil.get(i).getNomUsuari(), usuari)) {
                        tempsTop[cont] = topdificil.get(i).getTemps();
                        ++cont;
                    }
                }
                break;
            }
            default: {
                break;
            }
        }
        return tempsTop;
    }
}
