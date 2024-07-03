package domini;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;
import java.util.Map;

/**
* Classe que s'encarrega de generar un Kenken amb solució. Conté els mètodes per a generar nombres de manera random, les regions del Kenken i les solucions d'aquestes regions.
*/
public class GenerarKenken {
    /**
     * Kenken a generar.
    */
    private Kenken KK;

    /**
     * Kenken .
    */
    private Kenken KKsol;

    /**
     * Objecte Random per a generar nombres aleatoris.
    */
    private Random rand;

    /**
     * Constructora de la classe.
     * @param kk Kenken a generar.
     */
    public GenerarKenken(Kenken kk) {
        KK = kk; //hacer que en el controlador del dominio se cree un kenken con el tamaño que se quiera
        rand = new Random();
    }

    public Kenken getKenken() {
        return KK;
    }

    /**
     * Mètode basat en backtracking que genera els números de cada casella del Kenken.
     * @param x Enter que indica la fila de la casella inicial.
     * @param y Enter que indica la columna de la casella inicial.
     * @return Booleà que indica si s'ha generat el Kenken correctament.
     */
    public boolean generarNums (int x, int y) {
        if (x == KK.getTam()+1) { //si ya hemos llegado a la ultima fila hemos acabado
            return true;
        }
        if (y == KK.getTam()+1) { //si ya hemos llegado a la ultima columna pasamos a la siguiente fila
            return generarNums(x+1, 1);
        }
        ArrayList<Integer> numeros = new ArrayList<>();
        for (int i = 1; i <= KK.getTam(); i++) {
            numeros.add(i); //añadimos los numeros del 1 al tamaño del kenken al vector de numeros
        }
        Collections.shuffle(numeros);

        for (int val : numeros) { //por cada numero
            if (KK.comprovaFila(x, y, val) && KK.comprovaColumna(x, y, val)) { //si el numero no esta en la fila ni en la columna
                KK.setNumeroCasella(x, y, val); //lo ponemos en la casilla
                if (generarNums(x, y+1)) return true;  //hacemos backtracking con la siguiente casilla
                KK.borraCasella(x, y); //si no se puede poner en la siguiente casilla lo borramos
            }
        }
        return false; //si no se puede poner en ninguna casilla devolvemos false
    }

    /**
     * Mètode que genera les regions del Kenken on els paràmetres indiquen si s'han de generar un nombre de regions aleatòris o no.
     * @param nRegs Enter que indica el nombre de regions a generar.
     * @param params Booleà que indica si s'han de generar un nombre determinat de regions o no.
     */
    public void generarRegions() {
        int idRegio = 0;
        boolean[][] visited = new boolean[KK.getTam()][KK.getTam()];   
        for (int i = 0; i < KK.getTam(); ++i) {
            for (int j = 0; j < KK.getTam(); ++j) {
                if (!visited[i][j]) { //si la casilla no ha sido visitada
                    List<Casella> casellesReg = new ArrayList<>();
                    dfs(i, j, visited, casellesReg, idRegio); //generamos la region
                    Regio reg = new Regio (idRegio, casellesReg, casellesReg.size()); 
                    ++idRegio;
                    KK.afegirRegio(reg); //añadimos la region al kenken
                }
            }    
        }        
    }

    /**
     * Algoritme dfs que genera les regions del Kenken i assigna les caselles a cada regió.
     * @param x Enter que indica la fila de la casella inicial.
     * @param y Enter que indica la columna de la casella inicial.
     * @param visited Matriu de booleans que indica si una casella ha estat visitada o no.
     * @param casellesReg Llista de caselles que formen una regió.
     * @param idRegio Enter que identifica la regió.
     */
    private void dfs(int x, int y, boolean[][] visited, List<Casella> casellesReg, int idRegio) {
        visited[x][y] = true; //marcamos la casilla como visitada
        casellesReg.add(KK.getCasellaI(x + 1, y + 1)); //añadimos la casilla a la region
        KK.getCasellaI(x + 1, y + 1).setIdRegio(idRegio); //asignamos la region a la casilla
    
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
    
        while (rand.nextDouble() > probabilitat(casellesReg.size()) && casellesReg.size() < 5){ //mientras la probabilidad de seguir añadiendo casillas sea mayor que un numero aleatorio y la region no tenga mas de 5 casillas
            List<Integer> dirs = new ArrayList<>();
            for (int k = 0; k < 4; ++k) { //por cada direccion
                int nx = x + dx[k];
                int ny = y + dy[k];
                //si eres contiguo y no te pasas de los limites y no estas visitado
                if (nx >= 0 && nx < KK.getTam() && ny >= 0 && ny < KK.getTam() && !visited[nx][ny]) {
                    dirs.add(k);
                }
            }
    
            if (dirs.isEmpty()) break;
    
            int dir = dirs.get(rand.nextInt(dirs.size()));
            x += dx[dir];
            y += dy[dir];
            visited[x][y] = true;
            casellesReg.add(KK.getCasellaI(x + 1, y + 1));
            KK.getCasellaI(x + 1, y + 1).setIdRegio(idRegio);//se hace lo mismo que al inicio
    
            // Mayor probabilidad de detenerse después de 2 casillas (para que no se hagan regiones muy grandes)
            if (casellesReg.size() == 2 && rand.nextDouble() < 0.9) {
                break;
            }
        }
    }

    /**
     * Mètode que calcula la probabilitat de continuar afegint caselles a una regió.
     * @param size Enter que indica la mida de la regió.
     * @return Double que indica la probabilitat de continuar afegint caselles.
     */
    public double probabilitat(int size) {
        double dif = 0.1;
        return 1 - Math.exp(-dif * size); 
    }
    
    /**
 * Mètode que genera les solucions de cada regió del Kenken, en base a unes operacions donades.
 * @param llistaOps Llista d'enters que indica les operacions a utilitzar.
 * @param operacions Map que relaciona els enters amb les operacions possibles d'un Kenken.
 */
    public void generarSolucio(List<Integer> llistaOps, Map<Integer, Operacio> operacions) {
        for (int i = 0; i < KK.getRegionsKK().size(); ++i) {
            Regio r = KK.getRegionsKK().get(i);
            int tamReg = r.getnum_caselles();

            switch (tamReg) {
                case 1: //si la region solo tiene una casilla se le asigna la operacio = null (=) y el resultado = el numero de la casilla
                    r.setOperacio(operacions.get(null));
                    r.setResultat(r.getCaselles().get(0).getNumero());
                    break;
                case 2:
                    int num1 = r.getCasellaDeRegio(0).getNumero();
                    int num2 = r.getCasellaDeRegio(1).getNumero();

                    double prob = rand.nextDouble();

                    double probDiv = 0.60;
                    double probResta = 0.125;
                    double probMax = 0.125;
                    double probMod = 0.125;

                    //en base a la probabilidad anterior vamos probando las operaciones posibles y si no se puede hacer una operacion se hace otra, exceptuando la division que tiene prioridad
                    //la resta se hace en caso de que la division no se pueda hacer y la multiplicacion en caso de que la division y la resta no se puedan hacer
                    //si la multiplicación no se puede hacer se hace la suma
                    int max = Math.max(num1, num2);
                    int min = Math.min(num1, num2);
                    if (prob < probDiv && llistaOps.contains(4) && max % min == 0) {
                        r.setOperacio(operacions.get(4));
                        r.setResultat(max / min);
                    }
                    else if (prob < probDiv + probResta && llistaOps.contains(2)) {
                        r.setOperacio(operacions.get(2));
                        r.setResultat(Math.abs(num1 - num2));
                    } else if (prob < probDiv + probResta + probMod && llistaOps.contains(5)) {
                        r.setOperacio(operacions.get(5));
                        r.setResultat(Math.max(num1, num2) % Math.min(num1, num2));
                    } else if (prob < probDiv + probResta + probMod + probMax && llistaOps.contains(6)) {
                        r.setOperacio(operacions.get(6));
                        r.setResultat(Math.max(num1, num2));
                    }
                    else {
                        int num = rand.nextInt(2);
                        if (num == 0 && llistaOps.contains(3)) {
                            r.setOperacio(operacions.get(3));
                            r.setResultat(num1 * num2);
                        } else {
                            r.setOperacio(operacions.get(1));
                            r.setResultat(num1 + num2);
                        }
                    }
                    break;
                default:
                    int numero = rand.nextInt(2);
                    numero = (numero == 0) ? 1 : 3;
                    r.setOperacio(operacions.get(numero));
                    System.out.println(operacions.get(numero).getSimbol());
                    r.setResultat(r.calcularRegio());
                    if (r.getResultat() > 2000 || !llistaOps.contains(3)) { //añadimos la condicion de que si el resultado > 2000 no se haga la multiplicación por reducir la dificultad
                        r.setOperacio(operacions.get(1));
                        r.setResultat(r.calcularRegio());
                    }
                    break;
            }
        }
    }

    /**
     * Mètode que fa desaparèixer els números del Kenken (KK) i els guarda en un altre Kenken auxiliar (KKsol).
     */
    public void desapareixerNums () {
        for (int i=1; i<=KK.getTam(); ++i) {
			for (int j=1; j<=KK.getTam(); ++j) {
                    KKsol.setNumeroCasella(i, j, KK.getCasellaI(i, j).getNumero());
                    KK.borraCasella(i,j);
			}
		}
    }

    /**
     * Mètode que resol un nombre determinat de caselles del Kenken.
     * @param ncasellesRes Enter que indica el nombre de caselles a resoldre.
     */
    public void resoldreCaselles (int ncasellesRes) {
        System.out.println(ncasellesRes);
        int i = 0;
        while (i < ncasellesRes) {
            int x = rand.nextInt(KK.getTam()) + 1;
            int y = rand.nextInt(KK.getTam()) + 1;
            if (!KK.getCasellaI(x,y).estaBloquejada()) { 
                System.out.println(x + " " + y);
                KK.setNumeroCasella(x, y, KKsol.getCasellaI(x, y).getNumero());
                System.out.println(KKsol.getCasellaI(x, y).getNumero());
                KK.getCasellaI(x, y).bloquejar();
                ++i;
            }
        }
    }

    /**
     * Mètode que genera un Kenken a partir d'un tamany, un nombre de regions, un nombre de caselles resoltes i un llistat d'operacions possibles.
     * @param tam Enter que indica la mida del Kenken.
     * @param nCasellesRes Enter que indica el nombre de caselles que el Kenken ha de tenir resoltes.
     * @param llistaOps Llista d'enters que indica les operacions a utilitzar.
     * @param operacions Map que relaciona els enters amb les operacions possibles d'un Kenken.
     * @return Kenken generat.
     */
    public Kenken generarKKParametres (int tam, int nCasellesRes, List<Integer> llistaOps, Map<Integer, Operacio> operacions) {
        KKsol = new Kenken(tam);
        generarNums (1, 1);
        generarRegions();
        generarSolucio(llistaOps, operacions);
        desapareixerNums();
        resoldreCaselles(nCasellesRes);
        return KK;
    }
}