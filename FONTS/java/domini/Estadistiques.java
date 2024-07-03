package domini;

/**
* Classe que representa les estadístiques pròpies d'un jugador del joc Kenken. Conté mètodes per a la modificació i consulta dels records del jugador i el nombre de Kenkens creats i jugats.
*/
public class Estadistiques {
    /**
     * Enter que indica la quantitat de kenkens jugats.
     */
    private int KenkensJugats;

    /**
     * Vector d'enters que indica el temps més baix jugat a una partida fàcil en segons.
     */
    private int[] Recordfacil;

    /**
     * Vector d'enters que indica el temps més baix jugat a una partida fàcil en segons.
     */
    private int[] Recordmig;

    /**
     * Vector d'enters que indica el temps més baix jugat a una partida fàcil en segons.
     */
    private int[] Recorddificil;

    /**
     * Vector d'enters que indica la quantitat de kenkens creats.
     */
    private int KenkensCreats;

    /**
     * Constructora de la classe.
     */
    public Estadistiques() {
        this.KenkensJugats = 0;
        this.KenkensCreats = 0;
        this.Recordfacil = new int[10];
        this.Recordmig = new int[10];
        this.Recorddificil = new int[10];
        for (int i = 2; i < 10; ++i) {
            this.Recordfacil[i] = -1;
            this.Recordmig[i] = -1;
            this.Recorddificil[i] = -1;
        }
    }

    /**
     * Constructora de la classe.
     * @param kkJugats Enter que indica la quantitat de kenkens jugats.
     * @param tmp Enter que indica el temps més baix jugat a una partida.
     * @param kkCreats Enter que indica la quantitat de kenkens creats.
     * TODAVIA NO SE UTILIZA
     */
    public Estadistiques(int kkJugats, int tmp, int kkCreats) {
        this.KenkensJugats = kkJugats;
        this.Recordfacil = new int[10];
        this.Recordmig = new int[10];
        this.Recorddificil = new int[10];
        for (int i = 2; i < 10; ++i) {
            this.Recordfacil[i] = -1;
            this.Recordmig[i] = -1;
            this.Recorddificil[i] = -1;
        }
        this.KenkensCreats = kkCreats;
    }

    /**
     * Constructora de la classe.
     * @param e Estadistiques a copiar.
     * TODAVIA NO SE UTILIZA
     */
    public Estadistiques(Estadistiques e) {
        this.KenkensJugats = e.getNumKenkensJugats();
        for (int i = 0; i < 10; ++i) {
            this.Recordfacil[i] = e.getRecordfacil(i);
            this.Recordmig[i] = e.getRecordmig(i);
            this.Recorddificil[i] = e.getRecorddificil(i);
        }
        this.KenkensCreats = e.getNumKenkensCreats();
    }

    public int getNumKenkensJugats() {
        return KenkensJugats;
    }

    public int getRecordfacil(int i) {
        return Recordfacil[i];
    }

    public int getRecordmig(int i) {
        return Recordmig[i];
    }

    public int getRecorddificil(int i) {
        return this.Recorddificil[i];
    }

    public int getNumKenkensCreats() {
        return KenkensCreats;
    }

    public void setNumKenkensJugats(int num) {
        KenkensJugats = num;
    }

    public void setNumKenkensCreats(int num) {
        KenkensCreats = num;
    }

    /**
     * Mètode que actualitza el record de la dificultat i el tamany passats per paràmetres si el temps és millor.
     * @param dificultat String que indica la dificultat de la partida.
     * @param tam Enter que indica el tamany del kenken.
     * @param temps Enter que indica el temps de la partida.
     */
    public void setRecord(String dificultat, int tam, int temps) {
        switch (dificultat) {
            case "facil": {
                if (Recordfacil[tam] == -1) Recordfacil[tam] = temps;
                else {
                    if (Recordfacil[tam] > temps) {
                        Recordfacil[tam] = temps;
                    }
                }
                break;
            }
            case "mitja": {
                if (Recordmig[tam] == -1) Recordmig[tam] = temps;
                else {
                    if (Recordmig[tam] > temps) {
                        Recordmig[tam] = temps;
                    }
                }
                break;
            }
            case "dificil": {
                if (Recorddificil[tam] == -1) Recorddificil[tam] = temps;
                else {
                    if (Recorddificil[tam] > temps) {
                        Recorddificil[tam] = temps;
                    }
                }
                break;
            }
            default: {
                //throw new IOException();
                break;
            }
        }
    }

    /**
     * Mètode que incrementa el número de kenkens jugats del jugador.
     */
    public void incNumKenkensJugats() {
        KenkensJugats = KenkensJugats + 1;

    }

    /**
     * Mètode que incrementa el número de kenkens creats del jugador.
     */
    public void incNumKenkensCreats() {
        KenkensCreats = KenkensCreats + 1;
    }

    /**
     * Mètode que mostra per termnial les estadístiques del jugador.
     */
    public void pintaEstadistiques() {
        System.out.println("Els teus millor temps son:\n");
        System.out.println("Dificultat facil:\n");
        for (int i = 3; i < 10; ++i) {
            System.out.println("Tamany " + i + ":");
            if (this.Recordfacil[i] == -1) System.out.println("-");
            else System.out.println(this.Recordfacil[i]);
            System.out.println("\n");
        }

        System.out.println("Dificultat mitja:\n");
        for (int i = 3; i < 10; ++i) {
            System.out.println("Tamany " + i + ":");
            if (this.Recordmig[i] == -1) System.out.println("-");
            else System.out.println(this.Recordmig[i]);
            System.out.println("\n");
        }
        for (int i = 3; i < 10; ++i) {
            System.out.println("Tamany " + i + ":");
            if (this.Recorddificil[i] == -1) System.out.println("-");
            else System.out.println(this.Recorddificil[i]);
            System.out.println("\n");
        }
    }
}