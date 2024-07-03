package persistencia;

import domini.*;
import excepcions.*;
import java.io.IOException;

/**
 * Aquesta classe s'encarrega de connectar la capa de Domini amb la capa de Persistència. Conté metodes que es relacionen amb els gestors de disc que emmagatzemanen les dades dels perfils, les partides a mitges, els ranquings i els Kenkens que formen la base de dades de Kenkens.
 */
public class CtrlPersistencia {
    /**
     * ctrlDomini es la instancia del CtrlDomini
     */
    private final CtrlDomini ctrlDomini;
    /**
     * gestorPerfil es la instancia del GestorPerfil
     */
    private GestorPerfil gestorPerfil;
    /**
     * gestorkenken es la instancia del GestorKenken
     */
    private GestorKenken gestorkenken;
    /**
     * gestorPartida es la instancia del GestorPartida
     */
    private GestorPartida gestorPartida;
    /**
     * gestorRanquing es la instancia del GestorRanquing
     */
    private GestorRanquing gestorRanquing;

    /**
     * singletonObject es la instancia del CtrlPersistencia per aplicar el patró Singleton.
     */
    private static CtrlPersistencia singletonObject;

    //Constructora

    /**
     * Constructora per defecte del Controlador Persistencia.
     * @param cd Es la referencia del Controlador Domini.
     */
    public CtrlPersistencia(CtrlDomini cd)  {
        ctrlDomini = cd;
        gestorPerfil = new GestorPerfil();
        gestorkenken = new GestorKenken();
        gestorPartida = new GestorPartida();
        gestorRanquing = new GestorRanquing();
    }

    /**
     * Retorna la instancia de CtrlPersistencia.
     * @param cd Controlador Domini.
     * @return Instancia de CtrlPersistencia.
     */
    public static CtrlPersistencia getInstance(CtrlDomini cd) {
        if (singletonObject == null) {
            singletonObject = new CtrlPersistencia(cd);
        }
        return singletonObject;
    }

    /**
     * Metode que carrega la informacio dels Kenkens.
     */
    public void carregarperfils() {
        gestorPerfil.carregar();
    }

    /**
     * Metode que carrega la informacio dels Kenkens.
     */
    public void carregarpartides() {
        gestorPartida.carregarPartides();
    }

    /**
     * Mètode que guarda la informacio del perfil.
     */
    public void guardarPerfil() {
        gestorPerfil.guardar();
    }

    /**
     * Metode que obté l'objecte perfil amb el nom d'usuari indicat.
     * @param nomUS nom d'usuari del perfil que volem obtenir
     * @return Objecte perfil associat al nom d'usuari d'aquest.
     * @throws ExcepcioNoExisteixPerfil en cas de que el perfil no existeixi
     */
    public Perfil getPerfil(String nomUS) throws ExcepcioNoExisteixPerfil {
        return gestorPerfil.getPerfil(nomUS);
    }

    /**
     * Metode que indica si existeix el perfil amb el nom d'usuari nomUS
     * @param nomUS nom d'usuari del perfil que volem saber si existeix a la capa de persistencia.
     * @return Booleà que retorna cert si el perfil existeix i fals en cas contrari.
     */
    public boolean existeixPerfil(String nomUS) {
        return gestorPerfil.existeixPerfil(nomUS);
    }

    /**
     * Metode que registra la informacio d'un perfil en la seva creació.
     * @param nomUS nom d'usuari del perfil que es vol registrar
     * @param nomPASS contrasenya del perfil que es vol registrar
     * @throws ExcepcioJaExisteixPerfil en cas de que el perfil amb aquest nom d'usuari ja existeixi
     * @return Retorna un perfil amb el nom d'usuari i contrasenya indicats
     */
    public Perfil creaPerfil(String nomUS, String nomPASS) throws ExcepcioJaExisteixPerfil{
        return gestorPerfil.crearPerfil(nomUS, nomPASS);
    }

    /**
     * Metode que elimina el perfil amb aquest nom d'usuari
     * @param nomUS nom d'usuari del perfil que es vol eliminar
     * @throws ExcepcioNoExisteixPerfil en cas de que el perfil no existeixi
     */
    public void eliminaPerfil(String nomUS) throws ExcepcioNoExisteixPerfil {
        gestorPerfil.eliminaPerfil(nomUS);
    }

    /**
     * Metode que retorna un string amb la informació d'un Kenken a partir d'una ruta.
     * @param filepath String amb la ruta relativa de l'arxiu d'un Kenken.
     * @throws IOException en cas de no trobar cap Kenken amb la ruta indicada.
     * @return String amb la informació d'un Kenken.
     */
    public String getKenken(String filepath) throws IOException {
        return gestorkenken.getKenken(filepath);
    }

    /**
     * Metode que retorna la informacio d'un Kenken aleatori donat el seu tamany i la seva dificultat.
     * @param tamany indica el nombre de files i columnes del Kenken.
     * @param dificultat indica si el Kenken es facil, mitja o dificil en funcion de les operacions indicades.
     * @return Retorna la informacio en un string d'un kenken amb les caracteristiques indicades.
     * @throws IOException en cas de no trobar cap Kenken amb les caracteristiques indicades a la base de dades.
     */
    public String buscaKenkenRandom (int tamany, String dificultat) throws IOException {
        return gestorkenken.buscaKenkenRandom(tamany, dificultat);
    }

    /**
     * Metode que guarda la informacio d'un Kenken en un directori especific pel seu tamany i dificultat.
     * @param info String amb la informacio del Kenken.
     * @param tam Enter que indica el tamany del Kenken.
     * @param dificultat String que indica la dificultat del Kenken(facil, mitja, dificil).
     * @return Retorna un enter que indica en cas de ser -1 que el Kenken no ha estat guardat correctament i en altre cas el seu identificador.
     */
    public int guardaKenken(String info, int tam, String dificultat) {
        return gestorkenken.guardaKenken(info, tam, dificultat);
    }

    /**
     * Metode que guarda la informacio d'una partida amb tots els seus atributs com el Kenken o les pistes.
     * @param nomUS String amb el nom d'usuari del perfil que ha jugat la partida.
     * @param p Partida que es vol guardar.
     * @throws ExcepcioNoTePartida en cas de que el perfil no tingui cap partida a mitges guardada.
     */
    public void guardarPartida(String nomUS, Partida p) throws ExcepcioNoTePartida {
        gestorPartida.afegirPartida(nomUS, p);
    }

    /**
     * Metode que obté un objecte partida a partir del nom d'usuari d'un perfil.
     * @param nomUS nom d'usuari del perfil del que es vol obtenir una partida.
     * @return retorna la partida guardada del perfil amb el nom d'usuari indicat.
     * @throws ExcepcioNoHiHaPartidaGuardada
     */
    public Partida getPartida(String nomUS) throws ExcepcioNoHiHaPartidaGuardada {
        return gestorPartida.getPartida(nomUS);
    }

    /**
     * Metode que retorna la informacio d'una partida.
     * @param nomUS String amb el nom d'usuari del perfil que ha jugat la partida.
     * @return Retorna la partida a mitges del perfil indicat.
     */
    public boolean existeixPartida(String nomUS) {
        return gestorPartida.tePartida(nomUS);
    }

    /**
     * Metode que retorna la informacio d'una partida.
     * @param nomUS String amb el nom d'usuari del perfil que ha jugat la partida.
     * @return Retorna la partida a mitges del perfil indicat.
     * @throws ExcepcioNoTePartida en cas de que el perfil no tingui cap partida a mitges.
     */
    public void eliminaPartida(String nomUS) throws ExcepcioNoTePartida {
        gestorPartida.eliminaPartida(nomUS);
    }

    /**
     * Metode que obte els ranquings de totes les dificultats donat un tamany de Kenken.
     * @param tam Enter que identifica el tamany dels Kenkens de les partides del ranquing.
     * @return Retorna una instancia de ranquing que conté les llistes de partides de totes les dificultats.
     */
    public Ranquing getRanquing(int tam) {
        return gestorRanquing.getRanquing(tam);
    }

    /**
     * Metode que actualitza el ranquing.
     * @param r Ranquing que es vol actualitzar.
     */
    public void actualitzaRanquing(Ranquing r) {
        gestorRanquing.actualitzaRanquing(r);
    }

    /**
     * Metode que carrega els ranquings.
     */
    public void carregarranquings() {
        gestorRanquing.carregarRanquings();
    }
}
