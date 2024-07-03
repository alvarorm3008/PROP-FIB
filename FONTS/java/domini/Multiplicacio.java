package domini;

/**
* Subclasse de la classe Operacio que representa l'operació de multiplicació. Implementa els mètodes per incialitzar-la i realitzar el càlcul de l'operació.
*/
public class Multiplicacio extends Operacio {
    /**
    * Constructora de la classe.
    * @param id Enter que identifica l'operació.
    * @param simbol String que representa l'operació.
    */
    public Multiplicacio(int id, String simbol) {
        super(id, simbol);
    }

    /**
    * Mètode que calcula l'operació multiplicació dels enters indicats.
    * @param operands Vector d'enters per a dur a terme el càlcul.
    * @return Enter que indica el resultat de l'operació.
    */
    @Override
    public int calcula(Integer[] operands) {
        int res = 1;
        for (int operand : operands) {
            res *= operand;
        }
        return res;
    }
}    
