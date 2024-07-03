package domini;

/**
* Subclasse de la classe Operacio que representa l'operació de resta. Implementa els mètodes per incialitzar-la i realitzar el càlcul de l'operació.
*/
public class Resta extends Operacio {
    /**
    * Constructora de la classe.
    * @param id Enter que identifica l'operació.
    * @param simbol String que representa l'operació.
    */
    public Resta(int id, String simbol) {
        super(id, simbol);
    }

    /**
    * Mètode que calcula l'operació resta dels enters indicats.
    * @param operands Vector d'enters per a dur a terme el càlcul.
    * @return Enter que indica el resultat de l'operació.
    */
    @Override
    public int calcula(Integer[] operands) {
        if (operands.length != 2) {         //no estoy segura si las excepciones van asi
            throw new IllegalArgumentException("Només pot haver dos operands");
        }
        if (operands[0] > operands[1]) return operands[0] - operands[1];
        else return operands[1] - operands[0];
    }
}
