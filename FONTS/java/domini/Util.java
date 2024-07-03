package domini;
import java.io.*;

/**
 * Classe que conté mètodes útils per a la resta de classes del paquet domini i que permet fer una copia d'objectes del domini.
 */
public class Util {
    /**
     * Metode que permet fer una còpia d'un objecte del Domini.
     * @param object Objecte el qual es vol copiar.
     * @return Retorna una copia de l'objecte que es passa per paràmetre per copiar.
     * @param <T>
     */
    public static <T> T deepCopy(T object) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);
                out.writeObject(object);
                out.flush();
                byte[] byteData = bos.toByteArray();
                out.close();
                bos.close();

                ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
                ObjectInputStream in = new ObjectInputStream(bis);
                T copiedObject = (T) in.readObject();
                in.close();
                bis.close();

                return copiedObject;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
    }

}
