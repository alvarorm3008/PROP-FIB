package domini;

/**
* Subclasse de la classe Operacio que representa l'operació de divisió. Implementa els mètodes per incialitzar-la i realitzar el càlcul de l'operació.
*/
public class Divisio extends Operacio {
    /**
    * Constructora de la classe.
    * @param id Enter que identifica l'operació.
    * @param simbol String que representa l'operació.
    */
    public Divisio(int id, String simbol) {
        super(id, simbol);
    }

    /**
    * Mètode que calcula l'operació divisió dels enters indicats.
    * @param operands Vector d'enters per a dur a terme el càlcul.
    * @return Enter que indica el resultat de l'operació.
    */
    @Override
    public int calcula(Integer[] operands) {
         if (operands.length != 2) {
            throw new IllegalArgumentException("Només pot haver dos operands");
        }
        if (operands[0] > operands[1]) {
            if (operands[0]%operands[1] != 0) return -1;
            return operands[0] / operands[1];
        }
        else {
            if (operands[1]%operands[0] != 0) return -1;
            return operands[1] / operands[0];
        }
    }
}
