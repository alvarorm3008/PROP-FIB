package persistencia;

import domini.PartidaAcabada;
import domini.Ranquing;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Aquesta classe és el Gestor de disc dels ranquings de cada tamany possible de Kenkens. Conté mètodes que permeten guardar els rànquings dividits pel tamany dels Kenkens i que contenen llistes ordenades per temps de menor a major per cadascuna de les possibles dificultats que tenen els Kenkens.
 */
public class GestorRanquing {

    /**
     * Map amb totes les instancies de Ranquing identificades pel tamany que contenen les llistes de partides amb el nom dels perfils i els seus temps.
     */
    Map<Integer, Ranquing> top = new HashMap<>();

    /**
     * Constructora del GestorPartida
     */
    public GestorRanquing() {
        crearArxius();
    }

    /**
     * Metode per crear un arxiu json amb la informacio dels ranquings de cada possible tamany.
     */
    private void crearArxius() {
        for (int i = 2; i < 10; ++i) {
            File file = new File("./data/arxius/Ranquings/Top" + i + "x" + i + ".json");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    inicialitzaRanquings();
                } catch (IOException e) {
                    System.err.println("Error al crear el archivo de partidas: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Metode que inicialitza tots els ranquings quan inicia l'execucio.
     */
    private void inicialitzaRanquings() {
        for (int i = 2; i < 10; ++i) {
            Ranquing ranquing = new Ranquing(i);
            top.put(i, ranquing);
        }
        guardar();
    }

    /**
     * Metode que comprova si hi ha alguna partida al ranquing d'aquell tamany.
     * @param tamany Enter que identifica el ranquing.
     * @return retorna cert si existeix alguna partida al ranquing i fals en cas contrari.
     */
    public boolean HiHaAlguAlRanquing(int tamany) {
        return top.containsKey(tamany);
    }

    /**
     * Metode que guarda tota la informació dels ranquings als arxius .json.
     */
    public void guardar() {
        System.out.println("Guardant ranquings");

        //Recorro tot el map amb tots els ranquings disponibles, un per cada tamany de Kenken possible.
        for(Map.Entry<Integer, Ranquing> entry : top.entrySet()) {
            JSONObject ranquing = new JSONObject();
            JSONArray CjtPartidesF = new JSONArray();
            //Recorro totes la llista de partides acabades d'aquell ranquing amb aquell tamany i de dificultat facil.
            for (PartidaAcabada partida : entry.getValue().getTopfacil()) {
                    JSONObject novapartida = partidaToJSONObject(partida);
                    CjtPartidesF.add(novapartida);
            }
            ranquing.put("Partides facils", CjtPartidesF);

            JSONArray CjtPartidesM = new JSONArray();
            //Recorro totes la llista de partides acabades d'aquell ranquing amb aquell tamany i de dificultat mitja.
            for (PartidaAcabada partida : entry.getValue().getTopmitja()) {
                JSONObject novapartida = partidaToJSONObject(partida);
                CjtPartidesM.add(novapartida);
            }
            ranquing.put("Partides mitjanes", CjtPartidesM);

            JSONArray CjtPartidesD = new JSONArray();
            //Recorro totes la llista de partides acabades d'aquell ranquing amb aquell tamany i de dificultat dificil.
            for (PartidaAcabada partida : entry.getValue().getTopdificil()) {
                JSONObject novapartida = partidaToJSONObject(partida);
                CjtPartidesD.add(novapartida);
            }
            ranquing.put("Partides dificils", CjtPartidesD);

            try (FileWriter fileWriter = new FileWriter(("./data/arxius/Ranquings/Top" + entry.getKey() + "x" + entry.getKey() + ".json"))) {
                fileWriter.write(ranquing.toJSONString());
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metode que carrega tots els ranquings de tots els tamanys i dificultats possibles.
     */
    public void carregarRanquings() {
        System.out.println("Carregant ranquings");
        top = new HashMap<>();
        for (int i = 2; i < 10; ++i) {
            File file = new File("./data/arxius/Ranquings/Top" + i + "x" + i + ".json");
            if (file.exists()) {
                try (FileReader reader = new FileReader(file)) {
                    JSONParser parser = new JSONParser();
                    JSONObject RanquingJson = (JSONObject) parser.parse(reader);
                    Ranquing r = new Ranquing(i);
                    // Cargar partides facils
                    JSONArray CjtPartidesF = (JSONArray) RanquingJson.get("Partides facils");
                    for (Object obj : CjtPartidesF) {
                        JSONObject next = (JSONObject) obj;
                        PartidaAcabada partida = partidaFromJSONObject(next);
                        r.afegirPartida(partida, "facil");
                    }

                    // Cargar partides mitjanes
                    JSONArray CjtPartidesM = (JSONArray) RanquingJson.get("Partides mitjanes");
                    for (Object obj : CjtPartidesM) {
                        JSONObject next = (JSONObject) obj;
                        PartidaAcabada partida = partidaFromJSONObject(next);
                        r.afegirPartida(partida, "mitja");
                    }

                    // Cargar partides dificils
                    JSONArray CjtPartidesD = (JSONArray) RanquingJson.get("Partides dificils");
                    for (Object obj : CjtPartidesD) {
                        JSONObject next = (JSONObject) obj;
                        PartidaAcabada partida = partidaFromJSONObject(next);
                        r.afegirPartida(partida, "dificil");
                    }

                    // Afegim el nou ranquing al map que conté tots els ranquings.
                    top.put(i, r);

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("El archivo JSON no existe.");
            }
        }
    }


    /**
     * Metode que actualitza la informacio del ranquing quan s'ha produit alguna modificació afegint-la al map i guardant la nova informació a l'arxiu json.
     * @param r ranquing amb la informació actualitzada.
     */
    public void actualitzaRanquing(Ranquing r)  {
        int tamany = r.getTam();
        top.put(tamany, r);
        guardar();
    }

    /**
     * Metode que obte un objecte ranquing a partir d'un tamany de Kenken.
     * @param tam tamany dels kenkens del ranquing.
     * @return retorna un objecte ranquing a partir del seu tamany.
     */
    public Ranquing getRanquing(int tam) {
        if (HiHaAlguAlRanquing(tam)) {
            return top.get(tam);
        }
        return new Ranquing(tam);

    }

    /**
     * Metode que transforma un objecte PartidaAcabada en un objecte JSOn amb la seva informació.
     * @param partida objecte partidaacabada que es vol registrar en un objecte json.
     * @return retorna l'objecte json amb tota la informacio de la partida acabada.
     */
    private JSONObject partidaToJSONObject(PartidaAcabada partida) {
        JSONObject novapartida = new JSONObject();
        novapartida.put("Usuari", partida.getNomUsuari());
        novapartida.put("Temps", partida.getTemps());
        return novapartida;
    }

    /**
     * Metode que transforma un objecte JSON amb la informació d'una partida acabada en un objecte Partida Acabada.
     * @param p objecte JSON que conté l'informacio de la partida acabada.
     * @return retorna l'objecte partidaacabada amb tota la informació.
     */
    private PartidaAcabada partidaFromJSONObject(JSONObject p) {
        String nomUS = (String)p.get("Usuari");
        int temps = ((Long) p.get("Temps")).intValue();
        PartidaAcabada partida = new PartidaAcabada(nomUS, temps);
        return partida;
    }

}

