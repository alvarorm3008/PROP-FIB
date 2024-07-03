package persistencia;

import domini.Partida;
import excepcions.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.io.File;


/**
 * Aquesta classe és el Gestor de disc de les partides que estan sense acabar. Conté metodes que permeten guardar l'estat d'una partida que no estigui acabada, accedir a les dades de les partides no acabades per tornar a restaurar el seu estat i continuar la partida o per eliminar partides quan ja estan acabades o s'elimina un perfil.
 */
public class GestorPartida {

    /**
     * Map amb tot el contingut de les partides a mitjes
     */
    Map<String, Partida> partides = new HashMap<>();

    /**
     * Constructora del GestorPartida
     */
    public GestorPartida() {
        crearArxiuPartides();
    }

    /**
     * Metode que s'encarrega de crear un arxiu json amb les partides a mitges en cas de no existir.
     */
    private void crearArxiuPartides() {
        File file = new File("./data/arxius/PartidesPerfilsActius.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error al crear el archivo de partidas: " + e.getMessage());
            }
        }
    }

    /**
     * Metode que comprova si aquest usuari amb aquest nom d'usuaru te una partida guardada.
     * @param nomUS nom d'usuari del perfil que vol comprovar si existeix una partida guardada.
     * @return retorna cert si existeix una partida guardada d'aquest usuari i fals en cas contrari.
     */
    public boolean tePartida(String nomUS) {
        return partides.containsKey(nomUS);
    }

    /**
     * Metode que s'encarrega de guardar totes les partides a mitges guardades per tots els perfils a l'arxiu json amb les partides.
     */
    public void guardar() {
        System.out.println("Guardant partides");
        JSONArray CjtPartides = new JSONArray();
        for(Map.Entry<String, Partida> entry : partides.entrySet()) {
            JSONObject nouUsuari = new JSONObject();
            nouUsuari.put("Usuari", entry.getKey());
            Partida partidaGuardada = entry.getValue();
            JSONObject partidaObj = partidaToJSONObject(partidaGuardada);
            nouUsuari.put("Partida", partidaObj);
            CjtPartides.add(nouUsuari);
        }
        //Es passa tota la informació a l'arxiu
        try (FileWriter fileWriter = new FileWriter("./data/arxius/PartidesPerfilsActius.json")) {
            fileWriter.write(CjtPartides.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metode que s'encarrega de carregar totes les partides guardades per tots els perfils en el map de partides i que llegeix l'informacio de l'arxiu json.
     */
    public void carregarPartides() {
        System.out.println("Carregant partides");
        File file = new File("./data/arxius/PartidesPerfilsActius.json");
        partides = new HashMap<>();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                JSONParser parser = new JSONParser();
                JSONArray CjtPartides = (JSONArray) parser.parse(reader);
                for(int i = 0; i < CjtPartides.size(); ++i) {
                    JSONObject next = (JSONObject) CjtPartides.get(i);
                    String nomUS = (String)next.get("Usuari");
                    JSONObject partidaObj = (JSONObject) next.get("Partida");
                    if (nomUS != null) {
                        Partida partida = partidaFromJSONObject(partidaObj);
                        partides.put(nomUS, partida);
                    }
                }
            } catch (IOException | ParseException | ExcepcioTamany e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("El archivo JSON no existe.");
        }
    }

    public void afegirPartida(String nomUS, Partida p) throws ExcepcioNoTePartida {
        if (tePartida(nomUS)) {
            System.out.println("Eliminant partida anterior");
            eliminaPartida(nomUS);
        }
        System.out.println("Guardant partida");
        partides.put(nomUS, p);
        guardar();
    }

    public void eliminaPartida(String nomUS) throws ExcepcioNoTePartida {
        if (!tePartida(nomUS)) throw new ExcepcioNoTePartida();
        System.out.println("Eliminant partida");
        partides.remove(nomUS);
        guardar();
    }

    public boolean existeixPartida(String nomUS) {
        return partides.containsKey(nomUS);
    }

    public Partida getPartida(String nomUS) throws ExcepcioNoHiHaPartidaGuardada {
        if(!existeixPartida(nomUS)) throw new ExcepcioNoHiHaPartidaGuardada();
        return partides.get(nomUS);
    }


    /**
     * Metode que transforma un objecte partida en un objecte JSON.
     * @param partida Objecte partida que volem transformar.
     * @return retorna l'objecte JSON
     */
    private JSONObject partidaToJSONObject(Partida partida) {
        JSONObject partidaObj = new JSONObject();
        partidaObj.put("segons", partida.getTemps());
        partidaObj.put("pausada", partida.isPausada());
        partidaObj.put("assistencia", partida.isAssistencia());
        partidaObj.put("resolt", partida.isResolt());
        partidaObj.put("pistes", partida.getPistes());
        partidaObj.put("Kenken", partida.getTaulell());

        return partidaObj;
    }

    /**
     * Metode que transforma un objecte JSON en un objecte de tipus Partida.
     * @param partidaObj Objecte JSON que volem transformar.
     * @return retorna l'objecte Partida transformat.
     * @throws ExcepcioTamany es llença si el tamany del kenken no es valid.
     */
    private Partida partidaFromJSONObject(JSONObject partidaObj) throws ExcepcioTamany {
        int segundos = ((Long) partidaObj.get("segons")).intValue();
        boolean pausada = (boolean) partidaObj.get("pausada");
        boolean asistencia = (boolean) partidaObj.get("assistencia");
        boolean resuelta = (boolean) partidaObj.get("resolt");
        int pistes = ((Long) partidaObj.get("pistes")).intValue();
        String kenken = (String) partidaObj.get("Kenken");
        Partida partida = new Partida(kenken, segundos, pausada, asistencia, resuelta, pistes);
        return partida;
    }

}
