package domini;

/**
* Subclasse de la classe Operacio que representa l'operació de màxim. Implementa els mètodes per incialitzar-la i realitzar el càlcul de l'operació.
*/
public class Maxim extends Operacio {
    /**
    * Constructora de la classe.
    * @param id Enter que identifica l'operació.
    * @param simbol String que representa l'operació.
    */
    public Maxim(int id, String simbol) {
        super(id, simbol);
    }

    /**
    * Mètode que calcula l'operació màxim dels enters indicats.
    * @param operands Vector d'enters per a dur a terme el càlcul.
    * @return Enter que indica el resultat de l'operació.
    */
    @Override
    public int calcula(Integer[] operands) {
        if (operands.length != 2) {
            throw new IllegalArgumentException("Només pot haver dos operands");
        }
        return Math.max(operands[0], operands[1]);
    }
}
