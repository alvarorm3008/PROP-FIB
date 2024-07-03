package presentacio;

import javax.swing.*;
import java.awt.*;

/**
* Classe que representa una vista específica de la presentació per a mostrar les normes del joc del Kenken.
* Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
*/
public class VistaNormes {

    /**
     * Constructora de la vista.
     */
    public VistaNormes() {
        hacerVisible();
    }

    /**
     * Fa visible la vista.
     */
    public void hacerVisible() {
        String strTitulo = "NORMES";
        String[] strBotones = {"D'acord"};
        String strTexto =
                "Normes del Joc KenKen\n" +
                        "\n" +
                        "KenKen és un trencaclosques matemàtic similar al Sudoku, però amb un enfocament addicional en les operacions aritmètiques bàsiques i algunes operacions especials. L'objectiu és omplir una quadrícula de n x  n amb números de l'1 al n de manera que es compleixin les següents regles:\n" +
                        "\n" +
                        "1. Números Únics per Fila i Columna:\n" +
                        "Cada fila i cada columna han de contenir números únics, és a dir, no es pot repetir cap número a la mateixa fila o columna.\n" +
                        "\n" +
                        "2. Regions i Objectius:\n" +
                        "La quadrícula està dividida en diverses regions delimitades per línies gruixudes, conegudes com \"regions\".\n" +
                        "Cada regió té un objectiu numèric i una operació aritmètica (suma, resta, multiplicació, divisió, mòdul o màxim) indicada en una cantonada de la regió.\n" +
                        "Els números dins d'una regió s'han de combinar utilitzant l'operació especificada per obtenir l'objectiu numèric de la regió.\n" +
                        "\n" +
                        "3. Operacions Aritmètiques:\n" +
                        "Suma (+): Els números dins de la regió s'han de sumar per igualar l'objectiu de la regió.\n" +
                        "Resta (-): La diferència entre els números dins de la regió ha d'igualar l'objectiu de la regió. Aquest tipus de regions normalment tenen dues cel·les.\n" +
                        "Multiplicació (×): Els números dins de la regió s'han de multiplicar per igualar l'objectiu de la regió.\n" +
                        "Divisió (÷): El quocient entre els números dins de la regió ha d'igualar l'objectiu de la regió. Aquest tipus de regions normalment tenen dues cel·les.\n" +
                        "Mòdul (%): El residu de la divisió del número més gran entre el número més petit ha d'igualar l'objectiu de la regió. Aquest tipus de regions normalment tenen dues cel·les.\n" +
                        "Màxim (Max): El número més gran entre els números dins de la regió ha d'igualar l'objectiu de la regió. Aquest tipus de regions normalment tenen dues cel·les.\n" +
                        "\n" +
                        "4. Consistència amb les Regles Bàsiques:\n" +
                        "Tot i que s'han de complir les operacions dins de les regions, els números també han de respectar la regla de ser únics a cada fila i columna.\n" +
                        "\n" +
                        "Exemple de Joc KenKen\n" +
                        "\n" +
                        "Suposem que tenim una quadrícula de 4x4. Algunes regions i els seus objectius poden ser:\n" +
                        "\n" +
                        "- Una regió de dues cel·les amb l'objectiu \"3+\" (suma 3).\n" +
                        "- Una regió d'una cel·la amb l'objectiu \"4\" (el número és 4).\n" +
                        "- Una regió de dues cel·les amb l'objectiu \"1%\" (mòdul 1).\n" +
                        "- Una regió de dues cel·les amb l'objectiu \"3Max\" (màxim 3).\n" +
                        "\n" +
                        "El jugador ha de col·locar els números de l'1 al 4 a la quadrícula de manera que:\n" +
                        "- Cada fila i cada columna continguin els números 1, 2, 3 i 4 sense repetir.\n" +
                        "- Els números dins de les regions respectin l'operació i l'objectiu de la regió.\n" +
                        "\n" +
                        "Estratègies per Resoldre un KenKen\n" +
                        "\n" +
                        "1. Comença per les regions més petites: Les regions d'una sola cel·la o aquelles amb una operació simple (com suma o resta en regions de dues cel·les) solen ser més fàcils de resoldre.\n" +
                        "2. Utilitza el procés d'eliminació: Descarrega números que ja estan presents a la mateixa fila o columna.\n" +
                        "3. Busca combinacions possibles: Per regions amb operacions més complexes (multiplicació, divisió, mòdul o màxim), identifica totes les combinacions possibles de números que compleixen amb l'objectiu i ajusta segons els números disponibles a la fila o columna.\n" +
                        "4. Revisa constantment: Assegura't que les files i columnes continuïn complint la regla de números únics mentre treballes a les regions.\n" +
                        "\n" +
                        "KenKen és un joc desafiant i divertit que no només posa a prova les teves habilitats aritmètiques, sinó també la teva capacitat de lògica i deducció. Gaudeix resolent i creant cada Kenken amb la nostra aplicació!\n";

        // Crear un JTextArea para el texto
        JTextArea textArea = new JTextArea(strTexto);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        // Cambiar el fondo del JTextArea
        textArea.setBackground(Color.decode("#E9EDEF")); // Por ejemplo, cambiar el color de fondo a amarillo


        // Crear un JScrollPane que contenga el JTextArea
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Tamaño fijo

        // Crear un JPanel para contener el JScrollPane y agregarle un borde amarillo
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.setBackground(Color.decode("#E9EDEF"));
        scrollPane.setBackground(Color.decode("#E9EDEF"));

        // Crear el JOptionPane y agregar el JPanel
        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE);
        optionPane.setOptions(strBotones);

        optionPane.setBackground(Color.decode("#E9EDEF"));

        // Crear el JDialog a partir del JOptionPane
        JDialog dialogOptionPane = optionPane.createDialog(new JFrame(), strTitulo);
        dialogOptionPane.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogOptionPane.setVisible(true);
    }
}