package domini;

/**
* Subclasse de la classe Operacio que representa l'operació de suma. Implementa els mètodes per incialitzar-la i realitzar el càlcul de l'operació.
*/
public class Suma extends Operacio {

    /**
    * Constructora de la classe.
    * @param id Enter que identifica l'operació.
    * @param simbol String que representa l'operació.
    */
    public Suma(int id, String simbol) {
        super(id, simbol);
    }

    /**
    * Mètode que calcula l'operació suma dels enters indicats.
    * @param operands Vector d'enters per a dur a terme el càlcul.
    * @return Enter que indica el resultat de l'operació.
    */
    @Override
    public int calcula(Integer[] operands) {
        int res = 0;
        for (int operand : operands) {
            res += operand;
        }
        return res;
    }
}
