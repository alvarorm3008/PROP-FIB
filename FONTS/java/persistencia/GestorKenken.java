package persistencia;

import java.io.*;
import java.util.Random;

/**
 * Aquesta classe és el Gestor de disc de Kenkens. Conté metodes que permeten accedir a les dades del Kenkens que hi han a la base a partir d'una ruta o a partir d'uns parametres i permet afegir-ne nous.
 */
public class GestorKenken {

    /**
     * Constructora del GestorKenken.
     */
    public GestorKenken() {
    }

    /**
     * Metode que retorna un String amb les dades d'un Kenken a partir d'una ruta relativa.
     * @param filepath Ruta relativa de l'arxiu amb el Kenken.
     * @return Retorna un String amb les dades del Kenken.
     * @throws IOException si no es pot llegir el fitxer.
     */
    public String getKenken(String filepath) throws IOException {
        StringBuilder content = new StringBuilder();
        try {
            File file = new File(filepath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
        catch (IOException e) {
            throw new IOException();
        }

    }

    /**
     * Metode que retorna la informacio d'un Kenken aleatori donat el seu tamany i la seva dificultat.
     * @param tamany indica el nombre de files i columnes del Kenken.
     * @param dificultat indica la dificultat del Kenken.
     * @return Retorna la informacio en un string d'un kenken amb les caracteristiques indicades.
     * @throws IOException si no hi ha cap kenken amb les caracteristiques indicades.
     */
    public String buscaKenkenRandom (int tamany, String dificultat) throws IOException {
        Random random = new Random();
        int numFile;
        try {
            numFile = getNumKenkens(tamany, dificultat);
        } catch (NullPointerException e) {
            throw new IOException();
        }
        int id = random.nextInt(numFile);
        StringBuilder content = new StringBuilder();
        String filepath = "data/kenkens/" + tamany + "x" + tamany + "/" + dificultat + "/" + id + ".txt";
        content.append(getKenken(filepath));
        String info = content.toString();
        System.out.println(info);
        return info;
    }

    /**
     * Metode que retorna el nombre de kenkens que existeixen a la base de dades donat un tamany i una dificultat.
     * @param tamany nombre de files i columnes dels kenkens.
     * @param dificultat string que identifica la dificultat dels Kenkens.
     * @return retorna un enter amb el nombre de kenkens existents amb aquest tamany i dificultat en el seu directori.
     * @throws NullPointerException si no hi ha cap kenken al directori seleccionat pel tamany i la dificultat.
     */
    public int getNumKenkens(int tamany, String dificultat) throws NullPointerException {
        File folder = new File("data/kenkens/" + tamany + "x" + tamany + "/" + dificultat);;
        File[] listFiles = folder.listFiles();
        return listFiles.length;
    }

    /**
     * Metode per guardar un Kenken en un fitxer.
     * @param info String que conté tota la informació a guardar del Kenken.
     * @param tamany Enter que representa el tamany del Kenken.
     * @param dificultat String que indica la dificultat del Kenken.
     * @return retorna un enter amb l'identificador del Kenken creat o -1 en cas de no haver-se pogut guardar.
     */
    public int guardaKenken(String info, int tamany, String dificultat) {
        FileWriter file = null;
        try {
            int number;
            try {
                number = getNumKenkens(tamany, dificultat);
            } catch (NullPointerException e) {
                number = 0;
                File folder = new File("data/kenkens/" + tamany + "x" + tamany + "/" + dificultat);
                folder.mkdir();
            }
            file = new FileWriter("data/kenkens/" + tamany + "x" + tamany + "/" + dificultat + "/"+ number + ".txt");
            PrintWriter pw = new PrintWriter(file);
            pw.print(info);
            pw.close();
            return number;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return -1;
    }
}
