package presentacio;

import excepcions.ExcepcioJaExisteixPerfil;
import excepcions.ExcepcioNoHiHaSessio;
import excepcions.ExcepcioTamany;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe que representa una vista específica de la presentació per a configurar una partida.
 * Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
 */
public class VistaConfiguracioPartida extends JFrame {
    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;
    /**
     * Panell de continguts de la vista de registre.
     */
    private JPanel mainPanel = new JPanel();
    /**
     * Botó per tornar a la pàgina anterior.
     */
    private JButton enrereButton = new JButton("Tornar enrere");
    /**
     * Botó per consultar les normes del joc.
     */
    private JButton normesButton = new JButton("Normes del joc" );
    /**
     * Botó per iniciar una partida.
     */
    private JButton jugarPartidaButton = new JButton("Jugar");
    /**
     * Botó per començar a crear un Kenken.
     */
    private JButton importarKenkenButton = new JButton("Importar Kenken");
    /**
     * Vista del directori home per importar un arxiu.
     */
    private JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    /**
     * Filepath de l’arxiu importat.
     */
    private String filepath;
    /**
     * Etiqueta del titol de la pantalla.
     */
    private JLabel titolConfigPartida = new JLabel("CONFIGURACIÓ DE LA PARTIDA");
    /**
     * Etiqueta que indica els botons de tamany.
     */
    private JLabel labelTamany = new JLabel("Selecciona un tamany de Kenken:");
    /**
     * Etiqueta que indica els botons d'operació.
     */
    private JLabel labelOperacio = new JLabel("Selecciona la dificultat:");
    /**
     * Selecciona el tamany del Kenken.
     */
    Integer[] tams = {2, 3, 4, 5, 6, 7, 8, 9};
    private JComboBox seleccioTamany = new JComboBox<>(tams);
    /**
     * Radio buttons per a seleccionar la dificultat de la partida
     */
    private JRadioButton facil = new JRadioButton("Fàcil (+-)");
    private JRadioButton medio = new JRadioButton("Mitjana (+-*/)");
    private JRadioButton dificil = new JRadioButton("Difícil (+-*/%Max)");

    /**
     * Constructora de la vista
     */
    VistaConfiguracioPartida (CtrlPresentacio ctrlPresentacio) {
        ferVisible();
        this.ctrlPresentacio = ctrlPresentacio;
        iniComponents();
    }

    /**
     * Fa visible la vista configuració partida.
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
        setTitle("Pantalla Configuració Partida");
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
     * Inicialitza els botons de la vista configuració partida
     */
    private void iniBotons() {
        //Configura l'aparença dels components
        configuraAparencia();

        //Inicialitza els botons superiors de la pantalla
        botonsSuperiors();

        //Inicialitza els botons principals
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10); //espai entre botons

        constraints.gridy = 1;
        mainPanel.add(labelTamany, constraints);
        constraints.gridx = 1;
        mainPanel.add(seleccioTamany, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        mainPanel.add(labelOperacio, constraints);
        ButtonGroup operacionsGrup = new ButtonGroup();
        JRadioButton[] opButtons = {facil, medio, dificil};
        for (int i = 0; i < opButtons.length; i++) {
            constraints.gridx = i + 1;
            constraints.gridy = 3;
            mainPanel.add(opButtons[i], constraints);
            operacionsGrup.add(opButtons[i]);
        }

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1; // Ampliar para importar
        mainPanel.add(importarKenkenButton, constraints);

        constraints.gridx = GridBagConstraints.RELATIVE;
        constraints.gridwidth = GridBagConstraints.REMAINDER; // Alinear a la derecha
        mainPanel.add(jugarPartidaButton, constraints);

        add(mainPanel, BorderLayout.CENTER);

        // BOTÓ NORMES
        // Panel pel botó "Normes"
        JPanel normesPanel = new JPanel(new BorderLayout());
        normesButton.setBounds(5, 0, 200, 20);
        normesPanel.add(normesButton, BorderLayout.EAST); // Botó "Normes" en la cantonada superior dreta

        normesPanel.setBackground(Color.decode("#E9EDEF"));

        add(normesPanel, BorderLayout.SOUTH);

        // Tooltips

        seleccioTamany.setToolTipText("Selecciones el numero de files i columnes que vols que tingui el kenken");

        facil.setToolTipText("Dificultat fàcil amb operacions: Suma i Resta");
        medio.setToolTipText("Dificultat mitja amb operacions: Suma, Resta, Multiplicació i Divisió");
        dificil.setToolTipText("Dificultat difícil amb operacions: Suma, Resta, Multiplicació, Divisió, Mòdul i Màxim");

        importarKenkenButton.setToolTipText("Importes un fitxer .txt que inclogui les dades del Kenken");
        jugarPartidaButton.setToolTipText("Comences a jugar la partida amb les dades de kenken seleccionades");
        enrereButton.setToolTipText("Tornes a la pantalla anterior");
        normesButton.setToolTipText("Consultes les normes del joc KenKen");
    }

    /**
     *  Inicialitza els botons de la part superior de la vista.
     */
    public void botonsSuperiors() {
        // Panell pel botó "Enrere"
        JPanel enrerePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enrerePanel.add(enrereButton);

        // Panell pel titol
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Font font = new Font("Arial", Font.BOLD, 20);
        titolConfigPartida.setFont(font);
        titlePanel.add(titolConfigPartida);

        // Panell superior pels dos
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(enrerePanel);
        topPanel.add(titlePanel);

        enrerePanel.setBackground(Color.decode("#E9EDEF"));
        topPanel.setBackground(Color.decode("#E9EDEF"));
        titlePanel.setBackground(Color.decode("#E9EDEF"));

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Configura l'aparença dels components de la vista
     */
    private void configuraAparencia() {

        //BOTONES
        Dimension buttonSize = new Dimension(200, 40);
        importarKenkenButton.setPreferredSize(buttonSize);
        jugarPartidaButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 30);
        enrereButton.setPreferredSize(buttonSize);
        normesButton.setPreferredSize(buttonSize);

        //TEXTS
        Font font = new Font("Arial", Font.BOLD, 20);
        titolConfigPartida.setFont(font);

        font = new Font("Arial", Font.PLAIN, 14);
        labelOperacio.setFont(font);
        labelTamany.setFont(font);
        seleccioTamany.setFont(font);
        facil.setFont(font);
        medio.setFont(font);
        dificil.setFont(font);

        //Colors botons
        importarKenkenButton.setBackground(Color.decode("#5BC0BE"));
        importarKenkenButton.setForeground(Color.decode("#333333"));
        jugarPartidaButton.setBackground(Color.decode("#005A9E"));
        jugarPartidaButton.setForeground(Color.decode("#F5F5F5"));
        seleccioTamany.setBackground(Color.decode("#005A9E"));
        seleccioTamany.setForeground(Color.decode("#F5F5F5"));
        enrereButton.setBackground(Color.decode("#5BC0BE"));
        enrereButton.setForeground(Color.decode("#333333"));
        normesButton.setBackground(Color.decode("#5BC0BE"));
        normesButton.setForeground(Color.decode("#333333"));

        mainPanel.setBackground(Color.decode("#E9EDEF"));
    }

    /**
     * Assigna els listeners als components corresponents.
     */
    public void assignar_listenersComponents() {

        enrereButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonEnrere(e);
            }

        });
        importarKenkenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                try {
                    actionPerformed_buttonImportar(e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        jugarPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                try {
                    actionPerformed_buttonJugar(e);
                } catch (ExcepcioTamany ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        normesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonNormes(e);
            }
        });
    }

    /**
     * Es canvia a la vista anterior: VistaPrincipal.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEnrere(ActionEvent e) {
        ctrlPresentacio.vistaTipusPartida();
        setVisible(false);
    }

    /**
     * S'obre l'escriptori de l'usuari per a que seleccioni un Kenken.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonImportar(ActionEvent e) throws ExcepcioTamany, IOException {
        // Establecer el filtro para permitir solo archivos .txt
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Selecciona fitxer");

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Verificar que el archivo seleccionado tenga la extensión .txt
            if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                JOptionPane.showMessageDialog(null, "Només pots importar fitxers .txt.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            filepath = selectedFile.getAbsolutePath();
            try {
                ctrlPresentacio.creoKenkenPath(filepath);
            }
            catch (IOException | IllegalArgumentException ex){
                ctrlPresentacio.mostraAvis("Error a la importació del kenken.");
                return;
            }
            if (!ctrlPresentacio.validaGuions()) {
                ctrlPresentacio.mostraAvis("El format del kenken és invàlid");
                return;
            }

            int[][] mat = ctrlPresentacio.matTauler();
            String[] ops = ctrlPresentacio.vectorOps();
            Integer[] res = ctrlPresentacio.vectorRes();
            int[] vals = ctrlPresentacio.vectorVals();

            if(!ctrlPresentacio.validaSolucio()) {
                ctrlPresentacio.mostraAvis("El kenken escollit no és vàlid per jugar.");
                return;
            }
            //Vol dir que el kenken no té solució
            if(!ctrlPresentacio.resoldreKenken()) {
                ctrlPresentacio.mostraAvis("El kenken escollit no és vàlid per jugar.");
                return;
            }

            //Vol dir que el kenken no té solució
            if(!ctrlPresentacio.kenkenValidBD()) {
                ctrlPresentacio.mostraAvis("El kenken escollit no és vàlid per jugar perque te masses caselles resoltes.");
                return;
            }

            ctrlPresentacio.crearPartida();

            ctrlPresentacio.vistaJugarPartida(mat, ops, res, vals, 0);
            setVisible(false);
        }
    }

    /**
     * Es selecciona un kenken amb les característiques o del .txt i es canvia a la vista: VistaJugarPartida.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonJugar(ActionEvent e) throws ExcepcioTamany, IOException {
        String dificultat;
        if (facil.isSelected()) {
            dificultat = "facil";
        }
        else if (medio.isSelected()) {
            dificultat = "mitja";
        }
        else if (dificil.isSelected()) {
            dificultat = "dificil";
        }
        else {
            ctrlPresentacio.mostraAvis("Ha d'haver una dificultat seleccionada");
            return;
        }
        int tamSeleccionat = (int) seleccioTamany.getSelectedItem();
        ctrlPresentacio.buscarKenkenRandom(tamSeleccionat, dificultat);

        int[][] mat = ctrlPresentacio.matTauler();
        String[] ops = ctrlPresentacio.vectorOps();
        Integer[] res = ctrlPresentacio.vectorRes();
        int[] vals = ctrlPresentacio.vectorVals();

        ctrlPresentacio.crearPartida();

        ctrlPresentacio.vistaJugarPartida(mat, ops, res, vals, 0);
        setVisible(false);
    }

    /**
     * Es canvia a la vista: VistaNormes.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonNormes(ActionEvent e) {
        ctrlPresentacio.vistaNormes();
    }
}

