package domini;
import excepcions.*;
import persistencia.CtrlPersistencia;
import java.io.IOException;
import java.util.*;

/**
 * Aquesta classe actua com a controlador principal de la lògica del domini 
 * en l'aplicació Kenken. S'encarrega de gestionar la creació, manipulació i 
 * resolució dels puzles Kenken, així com la gestió de perfils d'usuari, partides 
 * i interacció amb la capa de persistència. Implementa el patró Singleton per 
 * assegurar que només hi ha una instància d'aquesta classe en tota l'aplicació.
 */
public class CtrlDomini {
    /**
     * Objecte Kenken que representa l'ultim Kenken carregat.
     */
    private Kenken kenkenactual;

    /**
     * Objecte Kenken que representa la solució del Kenken actual que esta carregat.
     */
    private Kenken kenkensolucio;

    /**
     * Map d'operacions. Integer es  l'identificador de l'operació.
     */
    private static Map<Integer, Operacio> operacions;

    /**
     * Objecte Partida que representa l'última partida que s'ha o s'està jugant.
     */
    private Partida partidaactual;

    /**
     * Objecte perfil que indica el perfil que té iniciada la sessió.
     */
    private Perfil usuariactiu;

    /**
     * Instancia de CtrlDomini.
     */
    private static CtrlDomini singletonObject;

    /**
     * Controlador de la capa de Persistencia.
     */
    private CtrlPersistencia ctrlPersistencia;

    /**
     * Constructora de la classe.
     */
    public CtrlDomini() {
        ctrlPersistencia = CtrlPersistencia.getInstance(this);
        operacions = new HashMap<Integer, Operacio>();
        inicialitzaOps();
    }

    /**
     * Mètode que retorna la instancia de CtrlDomini, si no existeix cap es crea seguint la estratègia del patró Singleton.
     */
    public static CtrlDomini getInstance()  {
        if (singletonObject == null) {
            singletonObject = new CtrlDomini();
        }
        return singletonObject;
    }

    ////////////////////////////KENKENS////////////////////////////

    //CREANDO DESDE LA INTERFICIE MANUALMENTE
    /**
     * Mètode per crear un kenken buit només amb el tamany.
     * @param N nombre de files i columnes del Kenken.
     */
    public void crearKK (int N) {
        kenkenactual = new Kenken(N);
    }

    // CREAR KENKEN A PARTIR DE TODA LA INFO

    // OBTIENE EL STRING CON INFO DE UN KENKEN DE LA BASE DE DATOS
    /**
     * Mètode que obte la informacio d'un Kenken a partir d'un filepath.
     * @param filepath Ruta relativa de l'arxiu amb la informacio del Kenken.
     * @throws IOException En cas de no trobar cap Kenken amb les caracteristiques indicades.
     * @throws ExcepcioTamany En cas de que el Kenken tingui un tamany no vàlid.
     * @return Retorna un string amb la informacio del Kenken.
     */
    public String getKenkenRuta(String filepath) throws IOException {
        return ctrlPersistencia.getKenken(filepath);
    }

    public boolean validaGuions() {
        return kenkenactual.validaGuions();
    }

    // CREAS UN KENKEN CON TODA LA INFO DEL STRING

    /**
     * Mètode que crea un kenken a partir d'un string amb la seva informacio i passa a ser el Kenken actual.
     * @param info string amb la informació del Kenken.
     * @throws ExcepcioTamany En cas de que el Kenken tingui un tamany no vàlid.
     */
    public void setKenkenActual(String info) throws ExcepcioTamany, IllegalArgumentException {
        kenkenactual = new Kenken(info, operacions);
    }

    /**
     * Mètode que es guarda la informacio d'un Kenken.
     * @param info String amb la informacio d'un Kenken.
     * @throws ExcepcioTamany En cas de que el Kenken tingui un tamany no vàlid.
     */
    public void guardaKenken(String info) throws ExcepcioTamany, IOException {
        setKenkenActual(info);
        if (ctrlPersistencia.guardaKenken(info, kenkenactual.getTam(), kenkenactual.getDificultat()) != -1) {
            System.out.println("Kenken guardat");
            usuariactiu.getEstadistiques().incNumKenkensCreats();
            ctrlPersistencia.guardarPerfil();
        }
        else System.out.println("Kenken no guardat");
    }

    /**
     * Comprova si les caselles resoltes al Kenken no supera la meitat per ser guardat a la base de dades.
     * @return Retorna true si és vàlid per guardar a la base de dades, false altrament.
     */
    public boolean kenkenValidBD () {
        if (!kenkenactual.comprovaMaximCasellesResoltes()) return false;
        return true;
    }

    /**
     * Metode que busca un Kenken aleatori a la base de dades a partir del seu tamany i la seva dificultat.
     * @param tam Enter que representa el nombre de files i columnes del Kenken.
     * @param dificultat String que representa la dificultat desitjada pel Kenken que pot ser facil, mitja o dificil.
     * @return Retorna un string amb la informacio del Kenken.
     * @throws IOException En cas de no trobar cap Kenken amb les caracteristiques indicades.
     */
    public String buscaKenkenRandom(int tam, String dificultat) throws IOException {
        return ctrlPersistencia.buscaKenkenRandom(tam, dificultat);
    }

    /**
     * Metode que retorna el Kenken actual.
     * @return Retorna el Kenken actual.
     */
    public Kenken getKenkenActual() {
        return kenkenactual;
    }

    /**
     * Mètode que retorna el Kenken actual en forma de matriu.
     * @return Retorna el Kenken actual en forma de matriu.
     */
    public int[][] transformaMatriu() {
        return kenkenactual.transformaMatriu();
    }

    /**
     * Mètode que retorna una llista amb les operacions del Kenken actual.
     * @return Retorna una llista amb les operacions del Kenken actual.
     */
    public String[] transformaOps() {
        return kenkenactual.transformaOps();
    }

    /**
     * Mètode que retorna una llista amb els resultats del Kenken actual.
     * @return Retorna una llista amb els resultats del Kenken actual.
     */
    public Integer[] transformaResultats() {
        return kenkenactual.transformaResultats();
    }

    /**
     * Mètode que retorna un vector amb els valors del Kenken actual.
     * @return Retorna un vector amb els valors del Kenken actual.
     */
    public int[] transformaValors() {
        return kenkenactual.transformaValors();
    }

    /**
     * Mètode que completa les regions del Kenken actual.
     * @param mat Matriu amb les regions.
     * @param ops Llista amb les operacions.
     * @param resultats Llista amb els resultats.
     * @param numRegions Enter amb el nombre de regions.
     */
    public void completaRegions(int[][] mat, Integer[] ops, Integer[] resultats, int numRegions) {
        kenkenactual.completaRegions(mat, ops, resultats, numRegions, operacions);
    }

    /**
     * Mètode que col·loca els valors de les caselles del Kenken actual que esta creant l'usuari desde la presentació.
     * @param vals Vector amb els valors de totes les caselles.
     */
    public void completaValorsBloquejats(int[] vals) {
        kenkenactual.completaValorsBloquejats(vals);
    }

    /**
     * Mètode que col·loca els valors de les caselles del Kenken actual que esta creant l'usuari desde la presentació.
     */
    public boolean esCasellaBloquejada(int x, int y) {
        return kenkenactual.getCasellaI(x,y).estaBloquejada();
    }


    /**
     * Mètode que col·loca els valors de les caselles del Kenken actual que esta jugant l'usuari desde la presentació.
     * @param vals Vector amb els valors de totes les caselles.
     */
    public void completaValorsFixats(int[] vals) {
        kenkenactual.completaValorsFixats(vals);
    }

    /**
     * Metode que esborra els Valors que es passen per la presentació per netejar el kenken actual.
     * @param vals Valors introduits a la capa de presentació.
     */
    public void esborraValorsCreacio(int[] vals) {
        kenkenactual.esborraValorsBloquejats(vals);
    }

    /**
     * Metode que esborra els Valors que es passen per la presentació per netejar el kenken actual.
     * @param vals Valors introduits a la capa de presentació.
     */
    public void esborraValorsPartida(int[] vals) {
        kenkenactual.esborraValorsIntroduits(vals);
    }

    /**
     * Mètode que permet a l'usuari afegir un valor a la casella indicada del Kenken.
     * @param x Enter de la fila de la casella.
     * @param y Enter de la columna de la casella.
     * @param val Valor de la casella a introduir.
     */
    public void afegirValorCreacio (int x, int y, int val) {
        kenkenactual.setNumeroCasella(x, y, val);
        //Es marca que la casella té un valor introduit per l'usuari perquè l'algoritme de resoldre la tingui en compte.
        kenkenactual.getCasellaI(x, y).bloquejar();
    }

    /**
     * Mètode que permet a l'usuari afegir un valor a la casella indicada del Kenken.
     * @param x Enter de la fila de la casella.
     * @param y Enter de la columna de la casella.
     * @param val Valor de la casella a introduir.
     */
    public void afegirValorPartida (int x, int y, int val) {
        kenkenactual.setNumeroCasella(x, y, val);
        //Es marca que la casella té un valor introduit per l'usuari perquè l'algoritme de resoldre la tingui en compte.
        // tener en cuenta que el algoritmo de resoldre no lo tiene en cuenta.
        kenkenactual.getCasellaI(x, y).setIntroduit(true);
    }

    /**
     * Metode que comprova si els valors introduits per un usuari compleixen les normes de joc del Kenken.
     * @return retorna cert si no hi ha numeros repetits a les files i columnes i si les regions completes compleixen els resultats de les operacions i fals en cas contrari.
     */
    public boolean validarSolucio() {
        return kenkenactual.validarSolucio();
    }

    ////////////////////////////ALGORITMES////////////////////////////

    /**
     * Mètode que resol el Kenken actual.
     * @return Retorna true si el kenken es pot resoldre, false altrament.
     */
    public boolean resoldreKenken () {
        kenkensolucio = Util.deepCopy(kenkenactual);
        ResolverKenKen resoldre = new ResolverKenKen(kenkensolucio);
        //Resol caselles que pertanyen a una regió amb només una casella.
        if (!resoldre.casellesuniques()) {
            return false;
        }
        //Resol la resta
        if (resoldre.resoldre(1, 1)) {
            return true;
        }
        return false;
    }

    /**
     * Mètode que genera un Kenken amb els paràmetres passats per paràmetre.
     * @param tam Enter amb el tamany del Kenken.
     * @param ncasRes Enter amb el nombre de caselles amb resultats.
     * @param llistaOps Llista amb les operacions que es poden utilitzar al generar el Kenken.
     */
    public void generarKenkenParametres (int tam, int ncasRes, List<Integer> llistaOps) {
        crearKK(tam);
        GenerarKenken generar = new GenerarKenken(kenkenactual);
        kenkenactual = generar.generarKKParametres(tam, ncasRes, llistaOps, operacions);
    }


    ////////////////////////////OPERACIONS////////////////////////////

    /**
     * Mètode que crea una única instancia de cada operació.
     */
    public void inicialitzaOps() {
        operacions.put(1, new Suma(1, "+"));
        operacions.put(2, new Resta(2, "-"));
        operacions.put(3, new Multiplicacio(3, "x"));
        operacions.put(4, new Divisio(4, "/"));
        operacions.put(5, new Modul(5, "%"));
        operacions.put(6, new Maxim(6, "Max"));
    }

    /**
     * Mètode que obté l'objecte de l'operació a partir de l'enter que la representa.
     * @param op Enter de l'identificador de la operació.
     * @return Objecte Operació corresponent a l'identificador.
     */
    public Operacio getOp(int op) {
        return operacions.get(op);
    }

    ////////////////////////////PARTIDES////////////////////////////

    /**
     * Mètode que crea una partida amb el perfil de l'usuari actiu i el Kenken actual, a més també incrementa el nombre de Kenkens jugats a les estadístiques de l'usuari actiu.
     */
    public void crearPartida() {
        partidaactual = new Partida(kenkenactual.toString(), usuariactiu);
    }

    /**
     * Mètode que permet restaurar la partida amb el perfil de l'usuari actiu i el Kenken actual.
     */
    public void restauraPartida() throws ExcepcioNoHiHaPartidaGuardada, ExcepcioTamany {
        partidaactual = ctrlPersistencia.getPartida(usuariactiu.getUsuari());
        kenkenactual = new Kenken(partidaactual.getTaulell(), operacions);
        partidaactual.setPerfil(usuariactiu);
    }

    /**
     * Metode que retorna la partida actual.
     * @return Retorna l'objecte partida de la partida actual.
     */
    public Partida getPartida() {
        return partidaactual;
    }

    /**
     * Mètode que comprova si existeix una partida de l'usuari actiu.
     * @return Retorna true si existeix la partida de l'usuari actiu, false altrament.
     */
    public boolean existeixPartida() {
        return ctrlPersistencia.existeixPartida(usuariactiu.getUsuari());
    }

    public int getSegonsPartida() {
        return partidaactual.getTemps();
    }

    public void actualitzaTemps(int segons) {
        partidaactual.setTemps(segons);
    }

    public void demanaPista() throws ExcepcioPistesMax {
        partidaactual.demanarPista();
    }

    /**
     * Mètode que guarda la partida actual a la capa de persistencia.
     * @throws ExcepcioNoTePartida En cas de que l'usuari actiu no tingui cap partida.
     */
    public void guardarPartida() throws ExcepcioNoTePartida {
        partidaactual.setTaulell(kenkenactual.toString());
        ctrlPersistencia.guardarPartida(usuariactiu.getUsuari(), partidaactual);
    }

    /**
     * Metode que col·loca la partida actual a null quan s'abandona una pantalla de jugar Partida desde la Presentació.
     */
    public void sortirPartida() {
        partidaactual = null;
        kenkenactual = null;
    }

    /**
     * Metode que retorna un vector amb els valors de les caselles d'un Kenken afegint una casella resolta amb l'algorisme de resoldre.
     * afegint el valor d'una nova casella aleatoria resolta amb l'algorisme de resolució de Kenkens.
     * @param caselles Valors de les caselles d'un Kenken.
     * @param bloquejats Valors de les caselles bloquejades.
     * @param fil Enter amb la fila de la casella a resoldre.
     * @param col Enter amb la columna de la casella a resoldre.
     * @return Retorna el vector de valors de caselles amb un nou valor en cas de tenir solució amb els valors ja introduits per l'usuari o el mateix vector en cas de no tenir solució.
     */
    public int resolCasella(int[] caselles, int[] bloquejats, int fil, int col) throws ExcepcioPistesMax{
        int num = -1;
        partidaactual.demanarPista();
        kenkenactual.completaValorsBloquejats(caselles);
        // Comrpova que els valors bloquejats siguin correctes
        if (kenkenactual.validarSolucio()) {
            //Si hi ha solució s'envia el vector amb el nou valor.
            if (resoldreKenken()) {
                num = kenkensolucio.getCasellaI(fil, col).getNumero();
                kenkenactual.getCasellaI(fil, col).bloquejar();
                return num;
            }
        }
        // Si els valors introduits no son correctes, es col·loca un numero del resoldre sense tenir en compte valors introduits
        kenkenactual.esborraValorsBloquejats(caselles);
        kenkenactual.completaValorsBloquejats(bloquejats);
        if(resoldreKenken()) {
            num = kenkensolucio.getCasellaI(fil, col).getNumero();
            kenkenactual.getCasellaI(fil, col).bloquejar();
            return num;
        }
        return num;
    }

    /**
     * Mètode que elimina la partida actual de la capa de persistencia.
     * @throws ExcepcioNoTePartida En cas de que l'usuari actiu no tingui cap partida.
     */
    public void eliminaPartida() throws ExcepcioNoTePartida {
        if(existeixPartida()) {
            ctrlPersistencia.eliminaPartida(usuariactiu.getUsuari());
            partidaactual = null;
        }
    }

    /**
     * Metode que retorna les pistes que porta utilitzades l'usuari durant la partida actual.
     * @return Retorna les pistes que porta utilitzades l'usuari durant la partida actual
     */
    public int getPistes() {
        return partidaactual.getPistes();
    }

    /**
     * Metode que envia la solució a la capa de presentació.
     * @return Retorna un vector d'enters amb els valors de les caselles del kenken solucionat.
     */
    public int[] mostrarSolucio() {
        return kenkensolucio.transformaValors();
    }

    /**
     * Mètode que finalitza una partida, comprova si el Kenken esta ben resolt i, si ho està i no ha utilitzat cap assitència,
     * afegeix aquesta al rànquing corresponent.
     */
    public boolean finalitzaPartida(int segons) throws ExcepcioNoTePartida, IOException {
        if(kenkenactual.calcularCasellesRestants() == 0 && validarSolucio()) {
            partidaactual.acabarPartida(segons);
            //Nomes entren al ranquing les partides que no utilitzen assitencia.
            if (!partidaactual.isAssistencia()) {
                actualitzaRanquing();
            }
            usuariactiu.getEstadistiques().setRecord(kenkenactual.getDificultat(), kenkenactual.getTam(), segons);
            ctrlPersistencia.guardarPerfil();
            eliminaPartida();
            kenkenactual = null;
            return true;
        }
        return false;
    }

    /**
     * Mètode que actualitza el rànquing amb la nova partida finalitzada.
     */
    public void actualitzaRanquing() {
        String dificultat = kenkenactual.getDificultat();
        int tam = kenkenactual.getTam();
        Ranquing r = ctrlPersistencia.getRanquing(tam);
        PartidaAcabada p = new PartidaAcabada(usuariactiu.getUsuari(), partidaactual.getTemps());
        r.afegirPartida(p, dificultat);
        ctrlPersistencia.actualitzaRanquing(r);
    }

    /**
     * Mètode que retorna una llista ordenada per temps de menor a major amb els temps i els noms dels perfils del rènquing del tamany indicat de dificultat fècil.
     * @param tam Enter que identifica el tamany del Kenkens de les partides.
     * @param dificultat String que identifica la dificultat de les partides.
     * @return Retorna una llista ordenada amb les partides del rènquing del tamany indicat de dificultat fàcil.
     */
    public int[] getTopTemps(int tam, String dificultat) {
        Ranquing r = ctrlPersistencia.getRanquing(tam);
        return r.topTemps(dificultat);
    }

    /**
     * Mètode que retorna una llista ordenada per temps de menor a major amb els temps i els noms dels perfils del rènquing del tamany indicat de dificultat fècil.
     * @param tam Enter que identifica el tamany del Kenkens de les partides.
     * @param dificultat String que identifica la dificultat de les partides.
     * @return Retorna una llista ordenada amb les partides del rènquing del tamany indicat de dificultat fàcil.
     */
    public String[] getTopUsuaris(int tam, String dificultat) {
        Ranquing r = ctrlPersistencia.getRanquing(tam);
        return r.topUsuaris(dificultat);
    }

    /**
     * Mètode que retorna un vector d'enters amb els temps ordenats de menor a major.
     * @param tam Enter que identifica el tamany del Kenkens de les partides.
     * @param dificultat String que identifica la dificultat de les partides.
     * @param usuari String que identifica el nom de l'usuari.
     * @return Retorna un vector d'enters ordenat amb les partides del rènquing del tamany indicat de dificultat fàcil.
     */
    public int[] getTopFiltre (int tam, String dificultat, String usuari) {
        Ranquing r = ctrlPersistencia.getRanquing(tam);
        return r.topFiltre(dificultat, usuari);
    }



    ////////////////////////////PERFILS////////////////////////////

    /**
     * Mètode que crea un perfil amb el nom d'usuari i contrasenya passats per paràmetre.
     * @param nomUS String amb l'identificador del perfil.
     * @param nomPASS String amb la contrasenya del perfil.
     * @throws ExcepcioJaExisteixPerfil En cas de que ja existeixi un perfil amb el nom d'usuari indicat.
     */
    public void crearPerfil(String nomUS, String nomPASS) throws ExcepcioJaExisteixPerfil, IOException {
        ctrlPersistencia.carregarperfils();
        ctrlPersistencia.carregarranquings();
        usuariactiu = ctrlPersistencia.creaPerfil(nomUS, nomPASS);
    }

    public Perfil getUsuariActiu() {
        return usuariactiu;
    }

    /**
     * Mètode en el qual l'usuari introdueix el nom d'usuari i la contrasenya, i si la contrasenya coincideix amb la del perfil amb el nom d'usuari indicat s'incia la sessió.
     * @param nomUS String amb l'identificador del perfil.
     * @param password String amb la contrasenya del perfil.
     * @throws ExcepcioContrasenyaIncorrecta En cas de que la contrasenya sigui incorrecta.
     * @throws ExcepcioNoExisteixPerfil En cas de que no existeixi cap perfil amb el nom d'usuari indicat.
     */
    public void iniciarSessio(String nomUS, String password) throws ExcepcioContrasenyaIncorrecta, ExcepcioNoExisteixPerfil, IOException {
        ctrlPersistencia.carregarperfils();
        ctrlPersistencia.carregarpartides();
        ctrlPersistencia.carregarranquings();
        Perfil p = ctrlPersistencia.getPerfil(nomUS);
        if (p.getPassword().equals(password)) usuariactiu = p;
        else throw new ExcepcioContrasenyaIncorrecta("Contrasenya Incorrecta");
    }

    /**
     * Mètode que comprova si existeix un perfil amb el nom d'usuari indicat.
     * @param nom String amb l'identificador del perfil.
     * @return Retorna true si existeix un perfil amb el nom d'usuari indicat, false altrament.
     */
    public boolean existeixPerfil(String nom) {
        return ctrlPersistencia.existeixPerfil(nom);
    }

    /**
     * Mètode que elimina el perfil del perfil que representa l'usuari actiu, posa l'atribut usuari actiu a null i elimina totes les dades relacionades amb el perfil com les seves partides al ranquing o les que pugui tenir iniciades.
     * @throws ExcepcioNoExisteixPerfil En cas de que no existeixi cap perfil amb el nom d'usuari de l'usuari actiu.
     * @throws ExcepcioNoHiHaSessio En cas de que no hi hagi cap sessió iniciada.
     * @throws ExcepcioNoTePartida En cas de que el perfil tingui una partida a mitges.
     */
    public void eliminarPerfil() throws ExcepcioNoExisteixPerfil, ExcepcioNoHiHaSessio, ExcepcioNoTePartida {
        if (usuariactiu == null) throw new ExcepcioNoHiHaSessio("No hi ha cap sessió iniciada");
        eliminaDadesPerfilRanquing();
        eliminaPartida();
        ctrlPersistencia.eliminaPerfil(usuariactiu.getUsuari());
        usuariactiu = null;
        kenkenactual = null;
        kenkensolucio = null;
    }

    /**
     * Mètode que s'encarrega de tancar la sessió de l'usuari actiu, posant a null l'atribut usuariactiu.
     * @throws ExcepcioNoHiHaSessio En cas de que no hi hagi cap sessió iniciada.
     */
    public void tancarSessio() throws ExcepcioNoHiHaSessio {
        usuariactiu = null;
        partidaactual = null;
        kenkenactual = null;
        kenkensolucio = null;
    }

    /**
     * Mètode que modifica la contrasenya de l'usuari actiu.
     * @param novapwd String amb la nova contrasenya.
     * @throws ExcepcioNoHiHaSessio En cas de que no hi hagi cap sessió iniciada.
     * @throws ExcepcioMateixaContrasenya En cas de que la contrasenya sigui la mateixa que la que ja tenia.
     */
    public void modificarPassword(String novapwd) throws ExcepcioNoHiHaSessio, ExcepcioMateixaContrasenya {
        if (usuariactiu == null) throw new ExcepcioNoHiHaSessio("No hi ha cap sessió iniciada");
        if (usuariactiu.getPassword().equals(novapwd)) throw new ExcepcioMateixaContrasenya();
        usuariactiu.setPassword(novapwd);
        ctrlPersistencia.guardarPerfil();
    }

    public int getKenkenscreats() {
        return usuariactiu.getKenkenscreats();
    }

    public int getKenkensjugats() {
        return usuariactiu.getKenkensjugats();
    }

    public int recordFacil(int tam) {
        return usuariactiu.getEstadistiques().getRecordfacil(tam);
    }

    public int recordMitja(int tam) {
        return usuariactiu.getEstadistiques().getRecordmig(tam);
    }

    public int recordDificil(int tam) {
        return usuariactiu.getEstadistiques().getRecorddificil(tam);
    }

    /**
     * Mètode que comprova si el Kenken actual està ben resolt.
     * @param nregs Enter amb el nombre de regions del Kenken.
     * @return Retorna true si el Kenken està ben resolt, false altrament.
     */
    public boolean comprovarEstatCorrecte(int nregs) {
        return kenkenactual.comprovarEstatCorrecte(nregs);

    }

    /**
     * Metode que elimina de tots el ranquings les partides acabades del perfil amb la sessió iniciada i actualitza els nous ranquings.
     */
    public void eliminaDadesPerfilRanquing() {
        for (int i = 2; i < 10; ++i) {
            Ranquing ranquing = ctrlPersistencia.getRanquing(i);
            ranquing.eliminarPartidesUsuari(usuariactiu.getUsuari());
            ctrlPersistencia.actualitzaRanquing(ranquing);
        }
    }
}



