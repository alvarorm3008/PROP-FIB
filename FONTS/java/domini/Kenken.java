package domini;
import java.io.Serializable;
import java.util.*;
import java.util.List;

import excepcions.*;

/**
 * Classe que representa un taulell de Kenken i tot allò del qual està format. Conté mètodes per a modificar-lo, consultar-lo i per comprovar el seu bon funcionament.
 */
public class Kenken implements Serializable {
    /**
     * Tamany del kenken (numero de caselles de les files i columnes).
     */
    private int tam;
    /**
     * Llista de regions que composen el Kenken.
     */
    private List<Regio> regionsKK;
    /**
     * Vector de caselles que composen el Kenken.
     */
    private Casella[] casellesKK;

    /**
     * Atribut que identifica la dificultat del Kenken que pot ser facil, mitja o dificil en funcio de quines operacions conte el Kenken.
     */
    private String dificultat;

    /**
     * Constructora que permet crear un Kenken amb tots els atributs inicialitzats.
     *
     * @param tam Tamany del Kenken a assignar.
     */
    public Kenken(int tam) {
        this.tam = tam;
        regionsKK = new ArrayList<>();
        casellesKK = new Casella[tam * tam];
        dificultat = "facil";
        for (int i = 1; i <= tam; ++i) {
            for (int j = 1; j <= tam; ++j) {
                Casella c = new Casella(i, j, -1);
                casellesKK[calculaPos(i, j)] = c;
            }
        }
    }

    /**
     * Constructora que crea un Kenken a partir d'un format passat per paràmetre.
     * @param dades      String amb la informacio del Kenken.
     * @param operacions Map amb totes les operacions que pot tenir un Kenken.
     * @throws ExcepcioTamany           Si el tamany del Kenken no es correcte.
     * @throws IllegalArgumentException Si hi ha algun error en les dades del Kenken.
     */
    public Kenken(String dades, Map<Integer, Operacio> operacions) throws ExcepcioTamany, IllegalArgumentException {
        //Dividem el string d'entrada per linees que es representen amb els alts de linia.
        String[] lines = dades.split("\n");
        //De la primera linea separem el tamany del Kenken del nombre de regions.
        String[] taulell = lines[0].split(" ");
        if(taulell.length != 2) throw new IllegalArgumentException("Format Incorrecte");
        regionsKK = new ArrayList<>();
        setDificultat("facil");
        tam = Integer.parseInt(taulell[0]);
        //System.out.println("Tamany Kenken: " + getTam());
        casellesKK = new Casella[tam * tam];
        try {
            int R = Integer.parseInt(taulell[1]);
            if(lines.length != R+1) throw new IllegalArgumentException("Format Incorrecte");
            //System.out.println("num regions: " + R);
            if (R > tam * tam  || R < 1) throw new IllegalArgumentException("El nombre de regions es incorrecte");
            //Recorrem el nombre de regions per crear-les.
            for (int i = 0; i < R; ++i) {
                //System.out.println("Regio: " + i);
                //Dividim cada element de cada linia que representa una regió separat per un espai.
                String[] regions = lines[i+1].split(" ");
                int op = Integer.parseInt(regions[0]);
                //System.out.println("Operacio: " + op);
                if (op > 6 || op < 0) throw new IllegalArgumentException("L'operacio introduida es incorrecta");
                int result = Integer.parseInt(regions[1]);
                //System.out.println("result: "+ result);
                if (result < 0) throw new IllegalArgumentException("El resultat introduit es incorrecte");
                int numCaselles = Integer.parseInt(regions[2]);
                if(regions.length > (3 + numCaselles*3)) throw new IllegalArgumentException("Format Incorrecte");
                //System.out.println("num caselles: "+ numCaselles);
                // Revisar esta condicion para hacerla mas restrictiva.
                if (numCaselles > (tam*tam) || numCaselles < 1)
                    throw new IllegalArgumentException("El numero de caselles introduit es incorrecte");
                if(op != 1 && op != 3 && op != 0 && numCaselles != 2) throw new IllegalArgumentException("Nombre de caselles invalid pel tipus d'operacio");
                if (op == 0 && numCaselles != 1) throw new IllegalArgumentException("Nombre de caselles invalid pel tipus d'operacio");
                Regio r;
                if (op == 0) r = new Regio(i, result);
                else r = new Regio(i, operacions.get(op), result, numCaselles);
                //Comprovem si la nova operació modifica la dificultat del Kenken.
                comprovadificultat(op);
                //System.out.println("Tamany string regions: " + regions.length);
                for (int j = 3; j < regions.length; j += 2) {
                    int x = Integer.parseInt(regions[j]);
                    //System.out.println("pos x: "+ x);
                    if (x < 1 || x > tam) throw new IllegalArgumentException("La fila introduida no es valida");
                    int y = Integer.parseInt(regions[j+1]);
                    //System.out.println("pos y: "+ y);
                    if (y < 1 || y > tam) throw new IllegalArgumentException("La columna introduida no es valida");
                    int val = -1;           // Valor por defecte si no hi ha número a la casella.
                    Casella c = new Casella(x, y, val, i);
                    afegirCasellaKK(c);
                    if ((j + 2 < regions.length) && (regions[j+2].matches("\\[\\d+\\]"))) {
                        val = Integer.parseInt(regions[j + 2].substring(1, regions[j + 2].length() - 1));
                        if (val < 1 || val > tam)
                            throw new IllegalArgumentException("El valor introduit es incorrecte");
                        c.setNumero(val);
                        c.bloquejar();
                        ++j;
                    }
                    else if ((j + 2 < regions.length) && (regions[j+2].matches("\\-\\d+\\-"))) {
                        val = Integer.parseInt(regions[j + 2].substring(1, regions[j + 2].length() - 1));
                        if (val < 1 || val > tam)
                            throw new IllegalArgumentException("El valor introduit es incorrecte");
                        c.setNumero(val);
                        c.setIntroduit(true);
                        ++j;
                    }
                    r.afegirCasella(c);
                }
                afegirRegio(r);
            }
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        catch (NoSuchElementException | NullPointerException | IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Format d'entrada incorrecte");
        }

    }



    /**
     * Mèode que transforma el Kenken en un string per tal de poder-lo manipular més fàcilment.
     *
     * @return Retorna un string amb tota la informació del Kenken.
     */
    //@Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        String line;
        line = tam + " " + regionsKK.size();
        info.append(line).append("\n");
        for (int i = 0; i < regionsKK.size(); ++i) {
            Regio r = regionsKK.get(i);
            Operacio op = r.getOperacio();
            if (op == null) info.append(0);
            else {
                int oper = r.getOperacio().getId();
                info.append(oper);
            }
            int res = r.getResultat();
            int numcas = r.getnum_caselles();
            info.append(" ").append(res).append(" ").append(numcas).append(" ");
            for (int j = 0; j < numcas; ++j) {
                Casella c = r.getCasellaDeRegio(j);
                int x = c.getFila();
                int y = c.getColumna();
                info.append(x).append(" ").append(y);
                if (!c.casellaBuida()) {
                    int num = c.getNumero();
                    if(c.estaBloquejada()) {
                        info.append(" [").append(num).append("]");
                    }
                    //Aquest cas vol dir que la casella té un valor introduit per l'usuari.
                    else {
                        info.append(" -").append(num).append("-");
                    }
                }
                if (j != numcas - 1) info.append(" ");
            }
            info.append("\n");
        }
        String p = info.toString();
        System.out.println(p);
        return p;
    }

    public boolean validaGuions() {
        for (int i = 0; i < casellesKK.length; ++i) {
            if (casellesKK[i].estaIntroduit()) return false;
        }
        return true;
    }

    public int getTam() {
        return tam;
    }

    public List<Regio> getRegionsKK() {
        return regionsKK;
    }

    /**
     * Mètode que obté la regió del Kenken identificada pel paràmetre.
     *
     * @param n Identificador de la regió del Kenken.
     * @return Retorna un objecte de Regio del Kenken identificada pel paràmetre.
     */
    public Regio getRegioI(int n) {
        return regionsKK.get(n);
    }

    /**
     * Mètode que obté la casella del Kenken identificada pels paràmetres.
     *
     * @param x Enter que indica la fila de la casella.
     * @param y Enter que indica la columna de la casella.
     * @return Retorna un objecte de Casella del Kenken identificada pels paràmetres.
     */
    public Casella getCasellaI(int x, int y) {
        return casellesKK[calculaPos(x, y)];
    }

    public String getDificultat() {
        return dificultat;
    }

    public void setTam(int t) throws ExcepcioTamany {
        if (t > 9 || t < 2) throw new ExcepcioTamany("El tamany ha d'estar entre 2 i 9");
        tam = t;
    }

    /**
     * Mètode que afegeix una regió a la llista de regions del Kenken.
     *
     * @param r Objecte de la classe Regio.
     */
    public void afegirRegio(Regio r) {
        regionsKK.add(r);
    }

    /**
     * Mètode que afegeix una casella al vector de caselles del Kenken.
     *
     * @param c Objecte de la classe Casella.
     */
    public void afegirCasellaKK(Casella c) {
        int pos = calculaPos(c.getFila(), c.getColumna());
        casellesKK[pos] = c;
    }

    /**
     * Mètode que canvia el valor del numero de la casella identificada pels paràmetres.
     *
     * @param x   Enter que indica la fila de la casella.
     * @param y   Enter que indica la columna de la casella.
     * @param val Enter que indica el numero del valor de la casella.
     * @throws IllegalArgumentException Si les coordenades donades es pasen de rang.
     */
    public void setNumeroCasella(int x, int y, int val) throws IllegalArgumentException {
        if (x < 1 || x > tam || y < 1 || y > tam) {
            throw new IllegalArgumentException("La posició està fora de rang.");
        }
        casellesKK[calculaPos(x, y)].setNumero(val);
    }

    public void setDificultat(String dif) {
        dificultat = dif;
    }

    /**
     * Mètode que càlcu la posició del vector on s'ha de col·locar la casella (identificada pels paràmetres) perquè estigui ordenada.
     *
     * @param a Enter que indica la fila de la casella.
     * @param b Enter que indica la columna de la casella.
     * @return Retorna un enter amb la posició de la casella.
     * @throws IllegalArgumentException Si les coordenades donades es pasen de rang.
     */
    public int calculaPos(int a, int b) throws IllegalArgumentException {
        if (a < 1 || b < 1 || a > tam || b > tam) throw new IllegalArgumentException("Posicions invalides");
        --a;
        --b;
        return a * tam + b;
    }

    /**
     * Mètode que comprova si un cert valor es troba ja a una fila del Kenken.
     *
     * @param y   Enter que indica la fila de la casella.
     * @param val Enter que indica el numero del valor d'una casella.
     * @return Retorna true si no hi ha cap numero a la fila que coincideixi amb val, false en cas contrari.
     * @throws IllegalArgumentException Si la fila es pasa de rang.
     */
    public boolean comprovaFila(int x, int y, int val) throws IllegalArgumentException {
        if (x < 1 || x > tam) {
            throw new IllegalArgumentException("La fila està fora de rang.");
        }
        for (int i = 1; i <= getTam(); ++i) {
            //System.out.println("valor: " + val);
            //System.out.println("num: " + getCasellaI(y, i).getNumero());
            // Si estem comparant els valors de la mateixa casella no es comprova.
            if (y != i) {
                if (val == getCasellaI(x, i).getNumero() && val != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Mètode que comprova si un cert valor es troba ja a una columna del Kenken.
     *
     * @param x     Enter que indica la fila de la casella.
     * @param valor Enter que indica el numero del valor d'una casella.
     * @return Retorna true si no hi ha cap numero a la columna que coincideixi amb val, false en cas contrari.
     * @throws IllegalArgumentException Si la columna es pasa de rang.
     */
    public boolean comprovaColumna(int x, int y, int valor) throws IllegalArgumentException {
        if (y < 1 || y > tam) {
            throw new IllegalArgumentException("La columna està fora de rang.");
        }
        for (int j = 1; j <= getTam(); ++j) {
            if (x != j) {
                if (valor == getCasellaI(j, y).getNumero() && valor != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Metode que valida la solucio d'un Kenken es correcta comprovant els valors de les caselles i els resultats de les regions.
     * @return Retorna cert si la solució del Kenken es correcta i fals en cas contrari.
     */
    public boolean validarSolucio() {
        //Comprovem per totes les caselles del kenken que no hi hagi valors repetits ni a les files ni a les columnes.
        for (int i = 0; i < casellesKK.length; ++i) {
            int fila = casellesKK[i].getFila();
            //System.out.println(fila);
            int columna = casellesKK[i].getColumna();
            //System.out.println(columna);
            int valor = casellesKK[i].getNumero();
            //System.out.println(valor);
            if (!comprovaFila(fila, columna, valor)) {
                //System.out.println("fila incorrecta: " + fila + " " + valor);
                return false;
            }
            if (!comprovaColumna(fila, columna, valor)) {
                //System.out.println("columna incorrecta: " + columna + " " + valor);
                return false;
            }
        }
        //System.out.println("he llegado");
        //Recorro les regions del kenken per comprovar els resultats.
        for (int i = 0; i < regionsKK.size(); ++i) {
            Regio r = regionsKK.get(i);
            if(r.regCompleta()) {
                if (r.getnum_caselles() == 1) {
                    //System.out.println();
                    if (r.getResultat() != r.getCasellaDeRegio(0).getNumero()) return false;
                } else {
                    if (!r.comprovaResultat()) return false;
                }
            }
        }
        return true;
    }


    /**
     * Mètode que retorna l'identificador de la regió on està la casella identificada pels paràmetres.
     *
     * @param x Enter que indica la fila de la casella.
     * @param y Enter que indica la columna de la casella.
     * @return Enter de l'identificador de la regió.
     * @throws IllegalArgumentException Si les coordenades es pasen de rang.
     */
    public int idRegio(int x, int y) throws IllegalArgumentException {
        if (x < 1 || x > tam || y < 1 || y > tam) {
            throw new IllegalArgumentException("La posició està fora de rang.");
        }
        for (int i = 0; i < regionsKK.size(); ++i) {
            for (int j = 0; j < regionsKK.get(i).getnum_caselles(); ++j) {
                Casella c = regionsKK.get(i).getCasellaDeRegio(j);
                if (c.getFila() == getCasellaI(x, y).getFila() && c.getColumna() == getCasellaI(x, y).getColumna())
                    return regionsKK.get(i).getId();
            }
        }
        return -1;
    }

    /**
     * Mètode que calcula el número de caselles que no tenen un valor assignat al Kenken.
     *
     * @return Enter del numero de caselles restants sense valor assignat.
     */
    public int calcularCasellesRestants() {
        int n = 0;
        for (int i = 0; i < tam * tam; ++i) {
            if (casellesKK[i].casellaBuida()) n++;
        }
        return n;
    }

    /**
     * Mètode que borra el valor d'una casella identificada pels paràmetres.
     *
     * @param x Enter que indica la fila de la casella.
     * @param y Enter que indica la columna de la casella.
     */
    public void borraCasella(int x, int y) {
        casellesKK[calculaPos(x, y)].borra();
    }

    /**
     * Mètode que treu per la terminal el Kenken (Només per debuggar).
     */
    public void imprimirKK() {
        for (int i = 1; i <= getTam(); ++i) {
            for (int j = 1; j <= getTam(); ++j) {
                Casella c = getCasellaI(i, j);
                if (c.casellaBuida()) {
                    System.out.print("X ");
                } else if (c.estaBloquejada()){
                    System.out.print("B ");
                } else {
                    System.out.print(c.getNumero() + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Mètode que imprimeix les regions del Kenken per terminal (Només per debuggar).
     */
    public void imprimirRegions() {
        int suma = 0;
        for (int i = 0; i < regionsKK.size(); ++i) {
            System.out.println("Regio " + i + ": " + "numero de caselles " + regionsKK.get(i).getnum_caselles());
            Regio r = regionsKK.get(i);
            if (r.getOperacio() == null) System.out.println("Operacio: null");
            else System.out.println("Operacio: " + r.getOperacio().getId());
            for (int j = 0; j < r.getnum_caselles(); ++j) {
                Casella c = r.getCasellaDeRegio(j);
                System.out.println("Casella " + j + ": " + c.getFila() + " " + c.getColumna() + " resultat: " + r.getResultat());
            }
            suma += r.getCaselles().size();
        }
        System.out.println("casillas añadidas " + suma);
    }

    /**
     * Mètode que transforma el Kenken en una matriu d'enters amb els identificadors de la regions de cada casella del Kenken per ser gestionats a la capa de Presentació.
     *
     * @return Retorna una matriu d'enters amb els identificadors de les regions de cada casella del Kenken.
     */
    public int[][] transformaMatriu() {
        // Recorrem el vector de caselles i guardem l'identificador de regio de cascuna en una matriu.
        int vec = 0;
        int[][] mat = new int[tam][tam];
        for (int i = 0; i < tam; ++i) {
            for (int j = 0; j < tam; ++j) {
                mat[i][j] = casellesKK[vec].getIdRegio();
                ++vec;
            }
        }
        return mat;
    }

    /**
     * Mètode que afegeix a una llista d'Strings les operacions de les regions del Kenken per ser gestionades a la capa de Presentació.
     *
     * @return Retorna una llista d'Strings amb les operacions de totes les regions del Kenken.
     */
    public String[] transformaOps() {
        String[] ops = new String[regionsKK.size()];
        for (int i = 0; i < regionsKK.size(); ++i) {
            if (regionsKK.get(i).getOperacio() == null) ops[i] = "=";
            else ops[i] = regionsKK.get(i).getOperacio().getSimbol();
        }
        return ops;
    }

    /**
     * Mètode que afegeix a una llista d'enters els resultats de totes les regions del Kenken per ser gestionades a la capa de Presentació.
     *
     * @return Retorna una llista d'enters amb els resultats de totes les regions del Kenken.
     */
    public Integer[] transformaResultats() {
        Integer[] resultats = new Integer[tam * tam];
        for (int i = 0; i < regionsKK.size(); ++i) {
            resultats[i] = regionsKK.get(i).getResultat();
        }
        return resultats;
    }

    /**
     * Mètode que retorna un vector d'enters amb tots els valors de les caselles del Kenken.
     *
     * @return Retorna un vector d'enters amb tots els valors de les caselles del Kenken.
     */
    public int[] transformaValors() {
        // Recorrem el vector de caselles i guardem el el valor de cadascuna en un vector d'enters.
        int[] valorcaselles = new int[tam * tam];
        for (int i = 0; i < tam * tam; ++i) {
            valorcaselles[i] = casellesKK[i].getNumero();
        }
        return valorcaselles;
    }

    /**
     * Mètode que assigna les regions del Kenken que han estat creades manualment per l'usuari.
     *
     * @param mat        Matriu amb totes les caselles del Kenken que indica l'identificador de la regió de cadascuna.
     * @param ops        Vector amb les operacions de totes les regions ordenades per l'identificador de regió.
     * @param resultats  Vector amb els resultats de totes les regions ordenades per l'identificador de regió.
     * @param numRegions Nombre de regions del kenken.
     * @param operacions Map que conté tots els objectes de les operacions que pot tenir un Kenken.
     */
    public void completaRegions(int[][] mat, Integer[] ops, Integer[] resultats, int numRegions, Map<Integer, Operacio> operacions) {
        //Recorro la matriu del taulell que té indicada a cada posició l'identificador de la regió a la qual pertany.
        boolean[] visitats = new boolean[numRegions];
        //Aquest vector de regions les té ordenades per identificador.
        Regio[] aux = new Regio[numRegions];
        for (int i = 0; i < tam; ++i) {
            for (int j = 0; j < tam; ++j) {
                int idregio = mat[i][j];
                System.out.println("Identificador de la regio: " + idregio);
                System.out.println("Tamaño lista de regiones: " + regionsKK.size());
                Regio r;
                //Si es la primera vegada que visito la regió, llavors s'ha de crear.
                if (!visitats[idregio]) {
                    visitats[idregio] = true;
                    r = new Regio(idregio);
                    System.out.println("Operacio de la regio: " + ops[idregio]);
                    System.out.println("Resultat de la regio: " + resultats[idregio]);
                    int resultat = resultats[idregio];
                    r.setResultat(resultat);
                    //Si existeix una operació s'ho indiquem a la regió.
                    if (!Objects.equals(ops[idregio], "=")) {
                        comprovadificultat(ops[idregio]);
                        Operacio operacio = operacions.get(ops[idregio]);
                        r.setOperacio(operacio);
                    }
                    regionsKK.add(r);
                    aux[idregio] = r;
                }
                //En cas de no ser la primera vegada, obtenim la regió existent.
                else {
                    System.out.println("Entro " + idregio);
                    System.out.println(idregio);
                    r = aux[idregio];
                }
                //Afegim la casella a la llista de caselles de la regio.
                Casella c = getCasellaI(i + 1, j + 1);
                r.afegirCasella(c);
                r.setnum_caselles(r.getnum_caselles() + 1);
                c.setIdRegio(idregio);
            }
        }
    }

    /**
     * Mètode que col·loca els valors de les caselles del Kenken que ha indicat manualment per l'usuari.
     * @param vals Vector amb els valors de totes les caselles ordenades per la fila i columna de cada casella.
     */
    public void completaValorsBloquejats(int[] vals) {
        for (int i = 0; i < vals.length; ++i) {
            if (!casellesKK[i].estaIntroduit()) {
                casellesKK[i].setNumero(vals[i]);
                if (casellesKK[i].getNumero() != -1) casellesKK[i].bloquejar();
            }
        }
    }

    /**
     * Mètode que col·loca els valors de les caselles del Kenken que ha indicat manualment per l'usuari.
     * @param vals Vector amb els valors de totes les caselles ordenades per la fila i columna de cada casella.
     */
    public void completaValorsFixats(int[] vals) {
        for (int i = 0; i < vals.length; ++i) {
            casellesKK[i].setNumero(vals[i]);
            if (casellesKK[i].getNumero() != -1) casellesKK[i].setIntroduit(true);
        }
    }

    /**
     * Mètode que comprova si l'operació nova introduida al Kenken provoca que es modifiqui la dificultat que aquest tenia.
     * Si el Kenken té alguna operació de Mòdul o Màxim la seva dificultat és dificil, si no en té però té alguna Multiplicació o Divisió llavors és de dificultat mitja, en altre cas es fàcil.
     *
     * @param op Enter que representa una operació.
     */
    public void comprovadificultat(int op) {
        if (dificultat != "dificil" && (op == 5 || op == 6)) {
            setDificultat("dificil");
        } else if (dificultat == "facil" && (op == 3 || op == 4)) {
            setDificultat("mitja");
        }
    }

    /**
     * Metode que comprova si el nombre de caselles que queden sense valor es superior o igual a la meitat del total de caselles.
     *
     * @return Retorna fals en cas que el nombre de caselles resoltes sigui superior a la meitat del total de caselles del Kenken i cert en cas contrari.
     */
    public boolean comprovaMaximCasellesResoltes() {
        int casellessenseresoldre = calcularCasellesRestants();
        System.out.println(casellessenseresoldre);
        if (casellessenseresoldre >= casellesKK.length / 2) return true;
        return false;
    }

    /**
     * Metode que esborra els valors passats a la capa de presentació.
     * @param cas Vector amb els valors de les caselles del Kenken.
     */
    public void esborraValorsBloquejats(int[] cas) {
        for(int i = 0; i < cas.length; ++i) {
            if (cas[i] != -1) {
                casellesKK[i].setNumero(-1);
                casellesKK[i].desbloquejar();
            }
        }
    }

    /**
     * Mètode que esborra els valors introduits per l'usuari a la capa de presentació.
     * @param cas Vector amb els valors de les caselles del Kenken.
     */
    public void esborraValorsIntroduits(int[] cas) {
        for(int i = 0; i < cas.length; ++i) {
            if (cas[i] != -1) {
                casellesKK[i].setNumero(-1);
                casellesKK[i].setIntroduit(false);
            }
        }
    }

    /**
     * Mètode que consulta el nombre de caselles del Kenken que tenen un valor assignat (bloquejat).
     * @return Retorna un enter amb el nombre de caselles bloquejades.
     */
    public int[] valorsBloquejats() {
        int[] valsBloquejats = new int[tam*tam];
        for (int i = 0; i < tam*tam; ++i) {
            valsBloquejats[i] = -1;
        }
        for (int i = 0; i < tam*tam; ++i) {
            if (casellesKK[i].estaBloquejada()) {
                valsBloquejats[i] = casellesKK[i].getNumero();
            }
        }
        return valsBloquejats;
    }

    /**
     * Mètode que comprova si el Kenken es correcte.
     * @param nregs Enter amb el nombre de regions del Kenken
     * @return Retorna cert si el Kenken es correcte i fals en cas contrari.
     */
    public boolean comprovarEstatCorrecte (int nregs) {
        boolean b = true;
        for (int i = 0; i < tam; ++i) {
            for (int j = 0; j < tam; ++j) {
                if (getCasellaI(i+1, j+1).getIdRegio() == -1) {
                    System.out.println("Casella " + i + " " + j + " " + getCasellaI(i+1, j+1).getIdRegio());
                    b = false;
                }
            }
        }
        for (int i = 0; i < regionsKK.size(); ++i) {
            if (regionsKK.get(i).getnum_caselles() != regionsKK.get(i).getCaselles().size()) {
                System.out.println("Regio " + i + " no te el mateix nombre de caselles que el seu atribut num_caselles");
                b = false;
            }

            if (regionsKK.get(i).getnum_caselles() == 2 && regionsKK.get(i).getOperacio() == null) {
                System.out.println("Regio " + i + " no pot tenir 2 caselles i operacio =");
                b = false;
            }
        }
        if (regionsKK.size() != nregs) {
            System.out.println("El nombre de regions no es correcte");
            b = false;
        }
        return b;
    }

    /**
     * Mètode que bloqueja un cert nombre de caselles del Kenken.
     * @param n Enter amb el nombre de caselles a bloquejar.
     */
    public void bloquejarCaselles (int n) {
        for (int i = 0; i < n; ++i) {
            Random r = new Random();
            int x = r.nextInt(getTam()) + 1;
            int y = r.nextInt(getTam()) + 1;
            casellesKK[calculaPos(x, y)].bloquejar();
        }
    }
}


