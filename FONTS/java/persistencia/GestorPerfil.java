package persistencia;

import domini.Estadistiques;
import domini.Perfil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import excepcions.*;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Aquesta classe és el Gestor de disc dels perfils de l'aplicació. Conté mètodes que permeten guardar la informació d'un perfil quan es crea o es modifica alguna dada i també permet eliminar les dades d'un perfil quan s'elimina.
 */
public class GestorPerfil {

    /**
     * Map amb tot el contingut dels perfils
     */
    Map<String, Perfil> perfils = new HashMap<>();

    /**
     * Creadora del GestorPerfil
     */
    public GestorPerfil() {
    }

    /**
     * Metode que comprova si un perfil existeix a la base de dades mirant si existeix al map de la classe.
     * @param nomUS nom d'usuari del perfil que es vol comprovar si existeix.
     * @return retorna cert en cas d'existir a la base de dades i fals en cas contrari.
     */
    public boolean existeixPerfil(String nomUS) {
        return perfils.containsKey(nomUS);
    }

    /**
     * Metode que guarda la informacio del map perfils que conte tots els perfils de l'aplicacio en un arxiu json.
     */
    public void guardar() {
        System.out.println("Guardant perfils");
        JSONArray ConjuntPerfils = new JSONArray();

        for (Map.Entry<String, Perfil> entry : perfils.entrySet()) { //Per cada perfil
            JSONObject nouUsuari = new JSONObject();
            nouUsuari.put("Usuari", entry.getValue().getUsuari());
            nouUsuari.put("Password", entry.getValue().getPassword());
            Estadistiques e = entry.getValue().getEstadistiques();
            JSONObject stats = statsToJSONObject(e);
            nouUsuari.put("Estadistiques", stats);
            ConjuntPerfils.add(nouUsuari);
        }
        try (FileWriter file = new FileWriter("./data/arxius/PerfilsActius.json")) {
            file.write(ConjuntPerfils.toJSONString()); //Escribim el conjunt d'usuaris al fitxer
            file.flush();
        } catch (IOException e) {
        }
    }

    /**
     * Metode que transforma tota la informacio dels perfils que conté l'arxiu json en perfils que afegeix al map de perfils.
     */
    public void carregar() {
        System.out.println("Carregant perfils");
        JSONParser jsP = new JSONParser();
        JSONArray ConjuntPerfils;
        perfils = new HashMap<>();
        try (FileReader rd = new FileReader("./data/arxius/PerfilsActius.json")){
            ConjuntPerfils = (JSONArray) jsP.parse(rd);
            for (int i = 0; i < ConjuntPerfils.size(); ++i){
                JSONObject next = (JSONObject) ConjuntPerfils.get(i); //Obtenim l'objecte de l'usuari
                String nomUsuari = ((String)next.get("Usuari"));  //Obtenim el nom d'usuari de l'usuari
                String pwd = ((String)next.get("Password"));
                JSONObject statsObj = (JSONObject) next.get("Estadistiques");

                if(nomUsuari != null){
                    Perfil nouPerfil = new Perfil(nomUsuari, pwd);
                    Estadistiques e = statsFromJSONObject(statsObj);
                    nouPerfil.setEstadistiques(e);
                    perfils.put(nomUsuari, nouPerfil);
                }
            }
        }
        catch (IOException e){
        }
        catch (ParseException e) {
        }
    }

    /**
     * Metode que crea un perfil a partir del seu nom d'usuari i la seva contrasenya.
     * @param nomUS nom d'usuari del perfil a crear.
     * @param nomPASS contrasenya del perfil a crear.
     * @return retorna l'objecte del perfil nou creat amb la informacio
     * @throws ExcepcioJaExisteixPerfil indica que el perfil amb aquest nom d'usuari ja existeix.
     */
    public Perfil crearPerfil(String nomUS, String nomPASS) throws ExcepcioJaExisteixPerfil {
        if (existeixPerfil(nomUS)) throw new ExcepcioJaExisteixPerfil("perfil ja existeix");
        Perfil p = new Perfil(nomUS, nomPASS);
        perfils.put(nomUS, p);
        guardar();
        return p;
    }

    /**
     * Metode que elimina el perfil que té el nom d'usuari indicat.
     * @param nom nom del perfil de l'usuari que es vol eliminar de la base de dades
     * @throws ExcepcioNoExisteixPerfil indica que el perfil que es vol eliminar no existeix
     */
    public void eliminaPerfil(String nom) throws ExcepcioNoExisteixPerfil {
        System.out.println("Eliminant perfil " + nom);
        if (!existeixPerfil(nom)) throw new ExcepcioNoExisteixPerfil("perfil no existeix");
        perfils.remove(nom);
        guardar();
    }

    /**
     * Metode que obte l'objecte del perfil identificat amb aquest nom d'usuari.
     * @param nomUS nom d'usuari del perfil que es vol obtenir.
     * @return retorna l'objecte del perfil que es vol obtenir.
     * @throws ExcepcioNoExisteixPerfil
     */
    public Perfil getPerfil(String nomUS) throws ExcepcioNoExisteixPerfil{
        if (!perfils.containsKey(nomUS)) throw new ExcepcioNoExisteixPerfil("no existeix perfil.");
        return perfils.get(nomUS);
    }

    /**
     * Metode que transforma l'objecte estadistiques en un objecte JSON.
     * @param e Objecte Estadistiques que es vol transformar.
     * @return retorna l'objecte JSON que conté la informació de les estadistiqyes.
     */
    private JSONObject statsToJSONObject(Estadistiques e) {
        JSONObject statsObj = new JSONObject();
        statsObj.put("NumKenkensCreats", e.getNumKenkensCreats());
        statsObj.put("NumKenkensJugats", e.getNumKenkensJugats());
        for (int i = 2; i < 10; ++i) {
            statsObj.put("Record facil " + i, e.getRecordfacil(i));
            statsObj.put("Record mig " + i, e.getRecordmig(i));
            statsObj.put("Record dificil " + i, e.getRecorddificil(i));
        }
        return statsObj;
    }

    /**
     * Metode que transforma l'objecte JSON en un objecte estadistiques
     * @param statsObj Objecte JSON que es vol transformar en un objecte estadistiques.
     * @return retorna l'objecte estadistiques amb la informacio que contenia l'objecte JSON.
     */
    private Estadistiques statsFromJSONObject(JSONObject statsObj) {
        Estadistiques e = new Estadistiques();
        Long auxLong = (Long) statsObj.get("NumKenkensCreats");  // Obtenim la edat de l'usuari iessim
        int aux = (auxLong != null) ? auxLong.intValue() : 0; // Convertim Long a int
        e.setNumKenkensCreats(aux);
        auxLong = (Long) statsObj.get("NumKenkensJugats");
        aux = (auxLong != null) ? auxLong.intValue() : 0;
        e.setNumKenkensJugats(aux);
        for (int j = 2; j < 10; ++j) {
            auxLong = (Long) statsObj.get("Record facil " + j);
            aux = (auxLong != null) ? auxLong.intValue() : 0;
            e.setRecord("facil", j, aux);

            auxLong = (Long) statsObj.get("Record mig " + j);
            aux = (auxLong != null) ? auxLong.intValue() : 0;
            e.setRecord("mitja", j, aux);

            auxLong = (Long) statsObj.get("Record dificil " + j);
            aux = (auxLong != null) ? auxLong.intValue() : 0;
            e.setRecord("dificil", j, aux);
        }
        return e;
    }

}
