package presentacio;

import excepcions.ExcepcioNoHiHaSessio;
import excepcions.ExcepcioTamany;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
* Classe que representa una vista específica de la presentació per a mostrar el ranquing del joc del Kenken.
* Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
*/
public class VistaRanquing extends JFrame{
        /**
         * Ctrl presentacio.
         */
        private CtrlPresentacio ctrlPresentacio;

        /**
         * Panell de continguts de la vista.
         */
        private JPanel mainPanel = new JPanel();

        /**
         * Panell de botons de la vista.
         */
        private JPanel buttonsPanel = new JPanel();

        /**
         * Taula que representa el ranquing.
         */
        private JTable ranquingTable;

        /**
        * Botó per tornar a la pagina anterior.
        */
        private JButton tornarEnrereButton = new JButton("Tornar enrere");

        /**
        * Etiqueta que indica s'ha d’introduir el nom d’usuari a la capsa de text corresponent.
        */
        private JLabel IntroduirNom = new JLabel("Introdueix un nom d'usuari: ");

        /**
         * Camp de text per entrar un nom d'usuari que buscar.
         */
        private JTextField buscadorNom = new JTextField(10);

        /**
         * Botó per començar a crear un Kenken.
         */
        private JComboBox<String> tamanyComboBox = new JComboBox<>(new String[]{"-","2", "3", "4", "5", "6", "7", "8", "9"});

        /**
         * Botó per seleccionar la dificultat fàcil.
         */
        private JRadioButton facil = new JRadioButton("Fàcil (+-)");

        /**
         * Botó per seleccionar la dificultat mitjana.
         */
        private JRadioButton medio = new JRadioButton("Mitjana (+-*/)");

        /**
         * Botó per seleccionar la dificultat dificil.
         */
        private JRadioButton dificil = new JRadioButton("Difícil (+-*/%Max)");

        /**
         * Etiqueta del titol de la pantalla
         */
        private JLabel titolBenvinguda = new JLabel("RANQUINGS");

        /**
         * Botó per consultar el ranquing.
         */
        private JButton consultarRanquingButton = new JButton("Consultar Rànquing");

        /**
         * Etiqueta que indica s'ha de seleccionar un tamany.
         */
        private JLabel labelTam = new JLabel("Selecciona un tamany:");

        /**
         * Constructora de la vista
         */
        public VistaRanquing (CtrlPresentacio ctrlPresentacio) {
            ferVisible();
            this.ctrlPresentacio = ctrlPresentacio;
            iniComponents();
        }

        /**
         * Fa visible la vista.
         */
        public void ferVisible() {
            pack();
            setVisible(true);
        }

        /**
         * Inicialitza els components de la vista i assigna els listeners corresponents.
         */
        private void iniComponents() {
            iniFrameVista();
            iniClose();
            iniRanquingTable();
            iniBotons();
            assignar_listenersComponents();
        }

        /**
         * Inicialitza el marc de la vista.
         */
        private void iniFrameVista() {
            // Tamany
            setMinimumSize(new Dimension(900,550));
            setPreferredSize(getMinimumSize());
            setResizable(false);
            // Posicio
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setTitle("Pantalla Rànquing");
        }

        /**
         * Inicialitza el botó per sortir del programa.
         */
        private void iniClose() {
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Evita el cierre automático

            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    int confirmed = JOptionPane.showConfirmDialog(null,
                            "Segur que vols sortir?", "Avís",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmed == JOptionPane.YES_OPTION) {
                        try {
                            ctrlPresentacio.tancaSessio();
                            dispose();
                            System.exit(0);
                        } catch (ExcepcioNoHiHaSessio ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }

        /**
         * Inicialitza els botons de la vista.
         */
        private void iniBotons() {
            // Configura l'aparença dels components
            configuraAparencia();

            // Inicialitza els botons de la part superior de la pantalla
            botonsSuperiors();

            // Configura el layout del panell principal
            mainPanel.setLayout(new BorderLayout());

            // Panell per a la taula de ranquing
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.add(new JScrollPane(ranquingTable));

            // Configura el layout del panell de botons
            buttonsPanel.setLayout(new GridBagLayout());
            GridBagConstraints constraintsBut = new GridBagConstraints();
            constraintsBut.fill = GridBagConstraints.HORIZONTAL;
            constraintsBut.anchor = GridBagConstraints.EAST;
            constraintsBut.insets = new Insets(10, 20, 10, 20);

            constraintsBut.gridx = 0;
            constraintsBut.gridy = 1;
            buttonsPanel.add(IntroduirNom, constraintsBut);
            constraintsBut.gridx = 0;
            constraintsBut.gridy = 2;
            buttonsPanel.add(buscadorNom, constraintsBut);

            constraintsBut.gridx = 0;
            constraintsBut.gridy = 3;
            buttonsPanel.add(labelTam, constraintsBut);
            constraintsBut.gridx = 0;
            constraintsBut.gridy = 4;
            buttonsPanel.add(tamanyComboBox, constraintsBut);

            ButtonGroup operacionsGrup = new ButtonGroup();
            JRadioButton[] opButtons = {facil, medio, dificil};
            for (int i = 0; i < opButtons.length; i++) {
                constraintsBut.gridx = 0;
                constraintsBut.gridy = 5 + i;
                buttonsPanel.add(opButtons[i], constraintsBut);
                operacionsGrup.add(opButtons[i]);
            }

            constraintsBut.gridx = 0;
            constraintsBut.gridy = 10;
            buttonsPanel.add(consultarRanquingButton, constraintsBut);

            buttonsPanel.setBackground(Color.decode("#E9EDEF"));
            tablePanel.setBackground(Color.decode("#E9EDEF"));

            // Afegeix els panells al panell principal
            mainPanel.add(buttonsPanel, BorderLayout.WEST);
            mainPanel.add(tablePanel, BorderLayout.CENTER);

            // Afegeix el panell principal al frame
            add(mainPanel, BorderLayout.CENTER);

            // Tooltips
            consultarRanquingButton.setToolTipText("Consultes el ranquing amb els paràmetres indicats");
        }

    /**
     * Configuració de la taula Rànquing
     */
    private static class CustomTableCellRenderer extends DefaultTableCellRenderer {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row == 0) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD, 14f));
                }
                return c;
            }
    }

    /**
     * Configura l'aparença dels components de la vista
     */
    private void configuraAparencia() {

        //BOTONES
        Dimension buttonSize = new Dimension(200, 50);
        consultarRanquingButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 40);
        tornarEnrereButton.setPreferredSize(buttonSize);

        //TEXTS
        Font font = new Font("Arial", Font.BOLD, 20);
        titolBenvinguda.setFont(font);

        font = new Font("Arial", Font.PLAIN, 16);
        labelTam.setFont(font);
        IntroduirNom.setFont(font);
        facil.setFont(font);
        medio.setFont(font);
        dificil.setFont(font);

        //Colors botons
        consultarRanquingButton.setBackground(Color.decode("#005A9E"));
        consultarRanquingButton.setForeground(Color.decode("#F5F5F5"));
        tamanyComboBox.setBackground(Color.decode("#005A9E"));
        tamanyComboBox.setForeground(Color.decode("#F5F5F5"));
        tornarEnrereButton.setBackground(Color.decode("#5BC0BE"));
        tornarEnrereButton.setForeground(Color.decode("#333333"));

        mainPanel.setBackground(Color.decode("#E9EDEF"));
    }

    /**
     * Inicialitza la taula de ranquing.
     */
    private void iniRanquingTable() {
        // Datos para la tabla
        String[] columnas = {"Posició", "Nom", "Temps"};
        Object[][] datos = {
                {"1", "-", "-"},
                {"2", "-", "-"},
                {"3", "-", "-"},
                {"4", "-", "-"},
                {"5", "-", "-"},
                {"6", "-", "-"},
                {"7", "-", "-"},
                {"8", "-", "-"},
                {"9", "-", "-"},
                {"10", "-", "-"}
        };

        // Crear el modelo de la tabla (no editable)
        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que todas las celdas sean no editables
            }
        };

        // Crear la tabla con el modelo
        ranquingTable = new JTable(model);

        // Configurar un renderizador de celdas personalizado para centrar el contenido y cambiar la fuente
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            private final Font cellFont = new Font("Arial", Font.PLAIN, 16); // Cambia el tamaño a 16

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                renderer.setFont(cellFont);
                ((JLabel) renderer).setHorizontalAlignment(SwingConstants.CENTER);
                return renderer;
            }
        };

        // Aplicar el renderizador personalizado a todas las celdas
        for (int i = 0; i < ranquingTable.getColumnCount(); i++) {
            ranquingTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Configurar la fuente de los encabezados de las columnas
        Font headerFont = new Font("Arial", Font.BOLD, 18); // Cambia el tamaño a 18
        ranquingTable.getTableHeader().setFont(headerFont);

        // Establecer el tamaño de las filas específicas
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                ranquingTable.setRowHeight(i, 80);
            } else if (i == 1) {
                ranquingTable.setRowHeight(i, 60);
            } else if (i == 2) {
                ranquingTable.setRowHeight(i, 45);
            } else {
                ranquingTable.setRowHeight(i, 30);
            }
        }
    }

    /**
    * Inicialitza els botons i el titol de la part superior de la vista opcions
    */
    private void botonsSuperiors() {
        //Panell pel botó "Tancar Sessió"
        JPanel enrerePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enrerePanel.add(tornarEnrereButton);
    
    
        //Panell superior pels dos botons
        JPanel buttonsPanel = new JPanel(new BorderLayout());
        buttonsPanel.add(enrerePanel, BorderLayout.WEST); // Botó "Tancar sessió" en la cantonada superior esquerra
    
        //Panell pel títol
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Label "Titol" centrat horizontalment
        Font font = new Font("Arial", Font.BOLD, 20);
        titolBenvinguda.setFont(font);
        titlePanel.add(titolBenvinguda);
    
        //Panell superior pel títol i els botons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonsPanel, BorderLayout.NORTH); // Panel de botons en la part superior
        topPanel.add(titlePanel, BorderLayout.CENTER); // Títol centrat horizontalmente sota dels botons

        enrerePanel.setBackground(Color.decode("#E9EDEF"));
        topPanel.setBackground(Color.decode("#E9EDEF"));
        titlePanel.setBackground(Color.decode("#E9EDEF"));
        buttonsPanel.setBackground(Color.decode("#E9EDEF"));


        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Assigna els listeners als components corresponents.
     */
    public void assignar_listenersComponents() {
        tornarEnrereButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonEnrere(e);
            }
        });
        consultarRanquingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonConsultar(e);
            }
        });
    }

    /**
     * Es canvia a la vista anterior: VistaOpcions.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEnrere(ActionEvent e) {
        ctrlPresentacio.vistaOpcions();
        setVisible(false);
    }

    /**
     * A la taula apareix el ranquing filtrat pels paràmetres indicats.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonConsultar(ActionEvent e) {
        String tamKenkenString = (String) tamanyComboBox.getSelectedItem();
        if (tamKenkenString == "-") {
            ctrlPresentacio.mostraAvis("No has seleccionat un tamany.");
            return;
        }
        int tamKenkenInt = Integer.parseInt(tamKenkenString);
        String dificultat;
        if (facil.isSelected()) {
            dificultat = "facil";
        } else if (medio.isSelected()) {
            dificultat = "mitja";
        } else if (dificil.isSelected()) {
            dificultat = "dificil";
        } else {
            ctrlPresentacio.mostraAvis("No has seleccionat cap dificultat.");
            return;
        }

        esborraTaula();

        String nom = buscadorNom.getText();
        if (!buscadorNom.getText().isEmpty()) {
            if (!ctrlPresentacio.existeixPerfil(nom)) {
                ctrlPresentacio.mostraAvis("No existeix un usuari amb aquest nom.");
                return;
            }
            int [] topTemps = ctrlPresentacio.filtarNom(tamKenkenInt, dificultat, nom);
            for (int i = 0; i < topTemps.length; ++i) {
                if (topTemps[i] == -1) ranquingTable.setValueAt("-", i, 2);
                else {
                    ranquingTable.setValueAt(transformarMinutsIHores(topTemps[i]), i, 2);
                    ranquingTable.setValueAt(nom, i, 1);
                }
            }
        }
        else {
            int[] topTemps = ctrlPresentacio.getTopTemps(tamKenkenInt, dificultat);
            String[] topUsuaris = ctrlPresentacio.getTopUsuaris(tamKenkenInt, dificultat);
            for (int i = 0; i < topTemps.length; ++i) {
                ranquingTable.setValueAt(topUsuaris[i], i, 1);
                if (topTemps[i] == -1) ranquingTable.setValueAt("-", i, 2);
                else ranquingTable.setValueAt(transformarMinutsIHores(topTemps[i]), i, 2);
            }
        }
    }

    /**
     * Mètode que transforma els segons a hores, minuts i segons
     */
    public String transformarMinutsIHores(int segons) {
        int hores = segons / 3600;
        int minuts = (segons % 3600) / 60;
        int segonsRestants = segons % 60;
        return String.format("%02d:%02d:%02d", hores, minuts, segonsRestants);
    }

    /**
     * Mètode que esborra les dades de la taula.
     */
    public void esborraTaula() {
        for (int i = 0; i < 10; ++i) {
            ranquingTable.setValueAt("-", i, 1);
            ranquingTable.setValueAt("-", i, 2);
        }
    }

}
