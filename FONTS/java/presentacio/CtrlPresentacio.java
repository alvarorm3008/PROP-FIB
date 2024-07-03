package presentacio;

import domini.CtrlDomini;
import domini.Partida;
import domini.Ranquing;
import excepcions.*;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Aquesta classe és responsable de gestionar la interacció entre la capa de presentació i la capa de domini,
 * així com de mostrar les diferents vistes segons les accions de l'usuari del joc del Kenken.
 */
public class CtrlPresentacio {
    /**
     * Objecte ctrl domini.
     */
    private CtrlDomini ctrlDomini;

    /**
     * Objecte vista principal.
     */
    private VistaPrincipal vistaPrincipal;

    /**
     * Constructora del controlador de presentació.
     */
    public CtrlPresentacio() {
        ctrlDomini = CtrlDomini.getInstance();
        vistaPrincipal = new VistaPrincipal(this);
    }

    //VISTES

    /**
     * Mostra en pantalla la vistaPrincipal.
     */
    public void vistaPantallaPrincipal() { VistaPrincipal vistaP = new VistaPrincipal(this);}

    /**
     * Mostra en pantalla la vistaIniciSessio.
     */
    public void vistaIniciaSessio() { VistaIniciSessio vistaIS = new VistaIniciSessio(this);}

    /**
     * Mostra en pantalla la vistaRegistre.
     */
    public void vistaRegistrarse() { VistaRegistre vistaR = new VistaRegistre(this);}

    /**
     * Mostra en pantalla la vistaOpcions.
     */
    public void vistaOpcions() { VistaOpcions vistaO = new VistaOpcions(this);}

    /**
     * Mostra en pantalla la vistaPerfil.
     */
    public void vistaPerfil() { VistaPerfil vistaP = new VistaPerfil(this);} //he quitado los static

    /**
     * Mostra en pantalla la vistaEstadistiques.
     */
    public void vistaEstadistiques() { VistaEstadistiques vistaE = new VistaEstadistiques(this);}

    /**
     * Mostra en pantalla la vistaCanviContra.
     */
    public void vistaCanviContrasenya() { VistaCanviContra vistaCC = new VistaCanviContra(this);}

    /**
     * Mostra en pantalla la vistaTipusPartida.
     */
    public void vistaTipusPartida() { VistaTipusPartida vistaNCP = new VistaTipusPartida(this);}

    /**
     * Mostra en pantalla la vistaConfiguracioPartida.
     */
    public void vistaConfiguracioPartida() { VistaConfiguracioPartida vistaCP = new VistaConfiguracioPartida(this);}

    /**
     * Mostra en pantalla la vistaJugarPartida.
     * @param mat matriu del kenken.
     * @param ops vector d'operacions del kenken.
     * @param res vector de resultats del kenken.
     * @param valCas vector amb els valors de les caselles del kenken.
     * @param segons enter amb el temps de la partida.
     */
    public void vistaJugarPartida(int[][] mat, String[] ops, Integer[] res, int[] valCas, int segons) { VistaJugarPartida vistaCP = new VistaJugarPartida(this, mat, ops, res, valCas, segons);}

    /**
     * Mostra en pantalla la vistaConfigCreacioKK.
     */
    public void vistaConfigCreacio() { VistaConfigCreacioKK vistaCCK = new VistaConfigCreacioKK(this);}

    /**
     * Mostra en pantalla la vistaCreaManualRegions.
     * @param N mida del kenken a crear.
     */
    public void vistaCreacioRegions(int N) { VistaCreaManualRegions vistaCMR = new VistaCreaManualRegions(this, N);}
    /**
     * Mostra en pantalla la vistaCreaManualCaselles.
     * @param mat matriu del kenken.
     * @param ops vector d'operacions del kenken.
     * @param res vector de resultats del kenken.
     * @param numRegions nombre de regions del kenken.
     */
    public void vistaCreacioCaselles(int[][] mat, String[] ops, Integer[] res, int numRegions) { VistaCreaManualCaselles vistaCMC = new VistaCreaManualCaselles(this, mat, ops, res, numRegions);}
    /**
     * Mostra en pantalla la vistaCreaAuto.
     * @param N mida del kenken a crear.
     */
    public void vistaCreaAuto(int N) { VistaCreaAuto vistaCA = new VistaCreaAuto(this, N);}
    /**
     * Mostra en pantalla la vistaKenken.
     * @param mat matriu del kenken.
     * @param ops vector d'operacions del kenken.
     * @param res vector de resultats del kenken.
     * @param valCas vector dels valors de les caselles del kenken.
     */
    public void vistaMostraKenken(int[][] mat, String[] ops, Integer[] res, int[] valCas) { VistaMostraKenken vistaKK = new VistaMostraKenken(this, mat, ops, res, valCas);}

    /**
     * Mostra en pantalla la vistaNormes.
     */
    public void vistaNormes() { VistaNormes vistaN = new VistaNormes();}

    /**
     * Mostra en pantalla la vistaRanquing.
     */
    public void vistaRanquing() { VistaRanquing vistaR = new VistaRanquing(this);}

    /**
     * Mostra un missatge d'avis amb el missatge passat per paràmetre.
     * @param missatge El missatge a mostrar.
     */
    public static void mostraAvis(String missatge) {
        JOptionPane.showMessageDialog(null, missatge, "Avis", JOptionPane.INFORMATION_MESSAGE);
    }

    //PERFILS

    /**
     * Mètode que crida a la funció inciarSessio() del controlador de domini, amb els paràmetres passats.
     * @param nomUs nom del perfil a crear i amb el que s'inicia la instancia.
     * @param contra contrasenya del perfil a crear i amb el que s'inicia la instancia.
     * @throws ExcepcioContrasenyaIncorrecta si la contrasenya no és correcta.
     * @throws ExcepcioNoExisteixPerfil si el perfil no existeix.
     * @throws IOException si l'entrada no és correcta
     */
    public void iniciaSessio(String nomUs, String contra) throws ExcepcioContrasenyaIncorrecta, ExcepcioNoExisteixPerfil, IOException {
        ctrlDomini.iniciarSessio(nomUs, contra);
    }

    /**
     * Mètode que crida a la funció tancarSessio() del controlador de domini.
     * @throws ExcepcioNoHiHaSessio si no hi ha cap sessió iniciada.
     */
    public void tancaSessio() throws ExcepcioNoHiHaSessio {
        ctrlDomini.tancarSessio();
    }

    /**
     * Mètode que crida a la funció creaPerfil() del controlador de domini, amb els paràmetres passats.
     * @param nomUs nom del perfil a crear i amb el que s'inicia la instancia.
     * @param contra contrasenya del perfil a crear i amb el que s'inicia la instancia.
     * @throws ExcepcioJaExisteixPerfil si el perfil ja existeix.
     * @throws IOException si l'entrada no és correcta
     */
    public void creaPerfil(String nomUs, String contra) throws ExcepcioJaExisteixPerfil, IOException {
        ctrlDomini.crearPerfil(nomUs, contra);
    }

    /**
     * Mètode que crida a la funció getUsuariActiu() del controlador de domini.
     * @return el nom de l'usuari actiu.
     */
    public String getNomUsuariActiu() {
        return ctrlDomini.getUsuariActiu().getUsuari();
    }

    /**
     * Mètode que crida a la funció modificarPassword() del controlador de domini.
     * @param contra contrasenya nova.
     * @throws ExcepcioNoHiHaSessio si no hi ha cap sessió iniciada.
     * @throws ExcepcioMateixaContrasenya si la contrasenya nova és igual a la antiga.
     */
    public void novaContrasenya(String contra) throws ExcepcioNoHiHaSessio, ExcepcioMateixaContrasenya {
        ctrlDomini.modificarPassword(contra);
    }

    /**
     * Mètode que crida a la funció eliminarPerfil() del controlador de domini.
     * @throws ExcepcioNoExisteixPerfil si el perfil no existeix.
     * @throws ExcepcioNoHiHaSessio si no hi ha cap sessió iniciada.
     * @throws ExcepcioNoTePartida si el perfil té una partida en curs.
     */
    public void eliminaPerfil() throws ExcepcioNoExisteixPerfil, ExcepcioNoHiHaSessio, ExcepcioNoTePartida {
        ctrlDomini.eliminarPerfil();
    }

    //PARTIDES

    /**
     * Mètode que crida a la funció crearPartida() del controlador de domini.
     */
    public void crearPartida () {
        ctrlDomini.crearPartida();
    }

    /**
     * Mètode que crida a la funció buscaKenkenRandom() del controlador de domini.
     * @param N mida del kenken a crear.
     * @param dificultat dificultat del kenken a crear.
     */
    public void buscarKenkenRandom (int N, String dificultat) throws IOException, ExcepcioTamany {
        ctrlDomini.setKenkenActual(ctrlDomini.buscaKenkenRandom(N, dificultat));
    }

    /**
     * Mètode que crida a la funció resolCasella() del controlador de domini.
     * @param valsCaselles vector amb tots els valors col·locats a les caselles del kenken
     * @param bloquejats vector amb els valors bloquejats col·locats a les caselles del kenken
     * @param x posició de la fila de la casellla a resoldre
     * @param y posició de la columna de la casellla a resoldre
     * @return valor de la casella resolta
     * @throws ExcepcioTamany si el tamany no és correcte
     * @throws ExcepcioPistesMax si es supera el número de pistes utilitzades
     * @throws ExcepcioNoTeSolucio si el kenken no té solució
     */
    public int resolCasella(int[] valsCaselles, int[] bloquejats, int x, int y) throws ExcepcioTamany, ExcepcioPistesMax, ExcepcioNoTeSolucio {
        return ctrlDomini.resolCasella(valsCaselles, bloquejats, x, y);
    }

    /**
     * Mètode que crida a la funció mostrarSolucio() del controlador de domini.
     * @return un vector amb tots els valors del kenken solucionat
     */
    public int[] mostrarSolucio() {
        return ctrlDomini.mostrarSolucio();
    }

    /**
     * Mètode que crida a la funció finalitzaPartida() del controlador de domini.
     * @param segons enter que indica els segons que s'han emprat en jugar la partida
     * @return booleà que indica si el kenken està ben resolt
     * @throws ExcepcioNoTePartida si l'usuari no té cap partida guardada
     * @throws IOException si l'entrada és incorrecta
     */
    public boolean finalitzarPartida(int segons) throws ExcepcioNoTePartida, IOException {
        return ctrlDomini.finalitzaPartida(segons);
    }

    /**
     * Mètode que crida a la funció getKenkenscreats() del controlador de domini.
     * @return un enter amb el numero de kenkens creats per l'usuari actiu
     */
    public int getKenkenscreats() {
        return ctrlDomini.getKenkenscreats();
    }

    /**
     * Mètode que crida a la funció getKenkensjugats() del controlador de domini.
     * @return un enter amb el numero de kenkens jugats per l'usuari actiu
     */
    public int getKenkensjugats() {
        return ctrlDomini.getKenkensjugats();
    }

    /**
     * Mètode que crida a la funció recordFacil() del controlador de domini.
     * @param tam enter que indica el tamany del kenken
     * @return enter amb el record de dificultat fàcil de l'usuari actiu en segons
     */
    public int recordFacil(int tam) {
        return ctrlDomini.recordFacil(tam);
    }

    /**
     * Mètode que crida a la funció recordMitja() del controlador de domini.
     * @param tam enter que indica el tamany del kenken
     * @return enter amb el record de dificultat mitjana de l'usuari actiu en segons
     */
    public int recordMitja(int tam) {
        return ctrlDomini.recordMitja(tam);
    }

    /**
     * Mètode que crida a la funció recordDificil() del controlador de domini.
     * @param tam enter que indica el tamany del kenken
     * @return enter amb el record de dificultat dificil de l'usuari actiu en segons
     */
    public int recordDificil(int tam) {
        return ctrlDomini.recordDificil(tam);
    }

    //RANQUINGS

    /**
     * Mètode que crida a la funció existeixPerfil() del controlador de domini.
     * @param nom string del nom de l'usuari
     * @return booleà que retorna true si existeix un perfil amb aquell nom d'usuari, false altrament.
     */
    public boolean existeixPerfil(String nom) {
        return ctrlDomini.existeixPerfil(nom);
    }

    /**
     * Mètode que crida a la funció filtrarNom() del controlador de domini.
     * @param tam enter que indica el tamany del kenken
     * @param dificultat string que indica la dificultat del kenken
     * @param nomUs string que indica el nom d'usuari
     * @return un vector s'enters amb els millors temps de l'usuari nomUs en el tamany i dificultat indicades
     */
    public int[] filtarNom(int tam, String dificultat, String nomUs) {
        return ctrlDomini.getTopFiltre(tam, dificultat, nomUs);
    }

    /**
     * Mètode que crida a la funció getTopTemps() del controlador de domini.
     * @param tam enter que indica el tamany del kenken
     * @param dificultat string que indica la dificultat del kenken
     * @return vector d'enters amb els millors temps en el tamany i dificultat indicades
     */
    public int[] getTopTemps(int tam, String dificultat) {
        return ctrlDomini.getTopTemps(tam, dificultat);
    }

    /**
     * Mètode que crida a la funció getTopUsuaris() del controlador de domini.
     * @param tam enter que indica el tamany del kenken
     * @param dificultat string que indica la dificultat del kenken
     * @return vector de string amb els noms d'usuaris amb els millors temps en el tamany i dificultat indicades
     */
    public String[] getTopUsuaris(int tam, String dificultat) {
        return ctrlDomini.getTopUsuaris(tam, dificultat);
    }


    //KENKENS

    /**
     * Mètode que crida a la funció crearKenken() del controlador de domini.
     * @param N mida del kenken a crear.
     */
    public void crearKK(int N) {
        ctrlDomini.crearKK(N);
    }

    /**
     * Mètode que crida a les funcions getKenkenRuta() i setKenkenActual() del controlador de domini.
     * @param path path del kenken a carregar.
     * @throws ExcepcioTamany si el kenken no té la mida correcta.
     * @throws IOException si no es pot llegir el fitxer.
     * @throws IllegalArgumentException si algun argument és invàlid
     */
    public void creoKenkenPath(String path) throws ExcepcioTamany, IOException, IllegalArgumentException {
        String kk = ctrlDomini.getKenkenRuta(path);
        ctrlDomini.setKenkenActual(kk);
    }

    /**
     * Mètode que crida a la funció validaGuions() del controlador de domini.
     * @return booleà que retorna true si el kenken actual té valors introduit, false altrament
     */
    public boolean validaGuions() {
        return ctrlDomini.validaGuions();
    }

    /**
     * Mètode que crida a la funció trasnformaMatriu() del controlador de domini.
     * @return la matriu d'enters del kenken actual.
     */
    public int[][] matTauler() {
        return ctrlDomini.transformaMatriu();
    }

    /**
     * Mètode que crida a la funció trasnformaOps() del controlador de domini.
     * @return vector de les operacions de les regions del kenken actual.
     */
    public String[] vectorOps() {
        return ctrlDomini.transformaOps();
    }

    /**
     * Mètode que crida a la funció trasnformaResultats() del controlador de domini.
     * @return vector dels resultats de les regions del kenken actual.
     */
    public Integer[] vectorRes() {
        return ctrlDomini.transformaResultats();
    }

    /**
     * Mètode que crida a la funció transformaValors() del controlador de domini.
     * @return vector amb els valors de les caselles del kenken actual.
     */
    public int[] vectorVals() {
        return ctrlDomini.transformaValors();
    }

    /**
     * Mètode que crida a la funció guardarKenken() del controlador de domini.
     * @throws Exception si hi ha algun error en la inicialització.
     * @throws ExcepcioTamany si el tamany del kenekn no és correcte
     * @throws IOException si l'entrada no és correcta
     */
    public void guardarKenken() throws ExcepcioTamany, IOException {
        ctrlDomini.guardaKenken(ctrlDomini.getKenkenActual().toString());
    }

    /**
     * Mètode que crida a la funció kenkenValidBD() del controlador de domini.
     * @return booleà que retorna true si el numero de caselles resoltes no supera la meitat de caselles totals, false altrament
     */
    public boolean kenkenValidBD () {
        return ctrlDomini.kenkenValidBD();
    }

    /**
     * Mètode que crida a la funció completaRegions() del controlador de domini, amb els paràmetres passats.
     * @param mat matriu del kenken.
     * @param ops vector d'operacions del kenken.
     * @param res vector de resultats del kenken.
     * @param numRegions nombre de regions del kenken.
     */
    public void actualitzaRegions (int[][] mat, Integer[] ops, Integer[] res, int numRegions) {
        ctrlDomini.completaRegions(mat, ops, res, numRegions);
    }

    /**
     * Mètode que crida a la funció completaValorsBloquejats() del controlador de domini, amb els paràmetres passats.
     * @param vals valors de les caselles a completar com a bloquejats.
     */
    public void actualitzaValorsBloquejats (int[] vals) {
        ctrlDomini.completaValorsBloquejats(vals);
    }

    /**
     * Mètode que crida a la funció completaValorsFixats() del controlador de domini, amb els paràmetres passats.
     * @param vals valors de les caselles a completar com a fixats.
     */
    public void actualitzaValorsFixats (int[] vals) {
        ctrlDomini.completaValorsFixats(vals);
    }

    /**
     * Mètode que crida a la funció esCasellaBloquejada() del controlador de domini.
     * @param x posició de la fila de la casella
     * @param y posició de la columna de la casella
     * @return booleà que retorna true si la casella està bloquejada, false altrament.
     */
    public boolean casellaBloquejada (int x, int y) {
        return ctrlDomini.esCasellaBloquejada(x, y);
    }

    /**
     * Mètode que crida a la funció resoldreKenken() del controlador de domini.
     * @return true si el kenken es pot resoldre, false altrament.
     */
    public boolean resoldreKenken() {
        return ctrlDomini.resoldreKenken();
    }

    /**
     * Mètode que crida a la funció validaSolucio() del controlador de domini.
     * @return retorna cert si no hi ha numeros repetits a les files i columnes i si les regions completes compleixen els resultats de les operacions i fals en cas contrari.
     */
    public boolean validaSolucio() {
        return ctrlDomini.validarSolucio();
    }

    /**
     * Mètode que crida a la funció getSegonsPartida() del controlador de domini.
     * @return enter que indica el temps en segons de la partida activa
     */
    public int getSegons() {
        return ctrlDomini.getSegonsPartida();
    }

    /**
     * Mètode que crida a la funció getPistes() del controlador de domini.
     * @return enter que indica el número de pistes utilitzades a la partida activa
     */
    public int getPistes() {
        return ctrlDomini.getPistes();
    }

    /**
     * Mètode que crida a la funció demanaPista() del controlador de domini.
     * @throws ExcepcioPistesMax si les pistes utilitzades son majors al màxim establert
     */
    public void demanaPista() throws ExcepcioPistesMax {
        ctrlDomini.demanaPista();
    }

    /**
     * Mètode que crida a la funció eliminaValorsCreacio() del controlador de domini.
     * @param valCaselles vector amb els valors de les caselles del kenken
     */
    public void eliminaValorsCreacio(int[] valCaselles) {
        ctrlDomini.esborraValorsCreacio(valCaselles);
    }

    /**
     * Mètode que crida a la funció esbooraValorsPartida() del controlador de domini.
     * @param valCaselles vector amb els valors de les caselles del kenken
     */
    public void eliminaValorsPartida(int[] valCaselles) {
        ctrlDomini.esborraValorsPartida(valCaselles);
    }

    /**
     * Mètode que crida a la funció restauraPartida() del controlador de domini.
     * @throws ExcepcioNoHiHaPartidaGuardada si no hi ha cap partida guardada
     * @throws ExcepcioTamany si el tamany de kenken és incorrecte
     */
    public void carregarPartida() throws ExcepcioNoHiHaPartidaGuardada, ExcepcioTamany {
        ctrlDomini.restauraPartida();
    }

    /**
     * Mètode que crida a la funció partidaGuardada() del controlador de domini.
     * @return booleà que retorna true si existeix una partida guardada, false altrament
     */
    public boolean partidaGuardada() {
        return ctrlDomini.existeixPartida();
    }

    /**
     * Mètode que crida a la funció actualitzaTemps() del controlador de domini.
     * @param segons enter que indica el temps en segons de la partida
     */
    public void actualitzaTemps(int segons) {
        ctrlDomini.actualitzaTemps(segons);
    }

    /**
     * Mètode que crida a la funció guardarPartida() del controlador de domini.
     * @throws ExcepcioNoTePartida si no hi ha cap partida
     */
    public void guardarPartida() throws ExcepcioNoTePartida {
        ctrlDomini.guardarPartida();
    }

    /**
     * Mètode que crida a la funció sortirPartida() del controlador de domini.
     */
    public void sortirPartida() {
        ctrlDomini.sortirPartida();
    }

    //GENERAR

    /**
     * Mètode que crida a la funció generaKenkenParams() del controlador de domini.
     * @param tam enter que indica el tamany del kenken
     * @param nCas enter que indica el numero de caselles que es volen resoldre
     * @param llistaOps llista amb les operacions que es volen tenir al kenken generat
     */
    public void generaKenkenParams(int tam, int nCas, List<Integer> llistaOps) {
        ctrlDomini.generarKenkenParametres(tam, nCas, llistaOps);
    }
}
