package presentacio;

import excepcions.ExcepcioNoExisteixPerfil;
import excepcions.ExcepcioNoHiHaSessio;
import excepcions.ExcepcioNoTePartida;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe que representa una vista específica de la presentació per a mostrar el perfil de l'usuari actiu.
 * Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
 */
public class VistaEstadistiques extends JFrame {
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
     * Combo box per a seleccionar el tamany del Kenken
     */
    private JComboBox<String> tamanyComboBox = new JComboBox<>(new String[]{"-", "2", "3", "4", "5", "6", "7", "8", "9"});

    /**
     * Botó per buscar les estadistiques amb el tamany indicat.
     */
    private JButton buscarButton = new JButton("Busca estadistiques");

    /**
     * Etiqueta que indica el nom de l'usuari actiu.
     */
    private JLabel NomPerfil = new JLabel();

    /**
     * Etiqueta que indica els botons de tamany.
     */
    private JLabel labelTamany = new JLabel("Selecciona un tamany de Kenken:");

    /**
     * Etiqueta que indica el numero de Kenkens creats.
     */
    private JLabel labelCreats = new JLabel();

    /**
     * Etiqueta que indica el numero de Kenkens jugats.
     */
    private JLabel labelJugats = new JLabel();
    /**
     * Etiqueta que indica el titol de les estadistiques generals.
     */
    private JLabel titolEstadistiquesGenerals = new JLabel("Estadístiques generals");

    /**
     * Etiqueta que indica el titol de les estadistiques del record
     */
    private JLabel titolEstadistiquesRecords = new JLabel("Records personals");

    /**
     * Etiqueta que indica el record amb dificultat fàcil.
     */
    private JLabel labelFacil = new JLabel("Record en dificultat fàcil: -");

    /**
     * Etiqueta que indica el record amb dificultat mitjana.
     */
    private JLabel labelMitjana = new JLabel("Record en dificultat mitjana: -");

    /**
     * Etiqueta que indica el record amb dificultat difícil.
     */
    private JLabel labelDificil = new JLabel("Record en dificultat difícil: -");

    /**
     * Constructora de la vista.
     */
    public VistaEstadistiques(CtrlPresentacio ctrlPresentacio) {
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
        iniBotons();
        assignar_listenersComponents();
    }

    /**
     * Inicialitza el marc de la vista.
     */
    private void iniFrameVista() {
        // Tamany
        setMinimumSize(new Dimension(900, 550));
        setPreferredSize(getMinimumSize());
        setResizable(false);
        // Posicio
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Pantalla Estadistiques");
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
        //Configura l'aparença dels components
        configuraAparencia();

        // Inicialitza els botons superiors de la pantalla
        botonsSuperiors();

        // Primer panel: Kenkens creats i jugats
        JPanel panelEstadistiques = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;

        labelCreats.setText("Kenkens creats: " + ctrlPresentacio.getKenkenscreats());
        labelJugats.setText("Kenkens jugats: " + ctrlPresentacio.getKenkensjugats());

        panelEstadistiques.add(titolEstadistiquesGenerals, gbc);

        gbc.gridy++;
        panelEstadistiques.add(labelCreats, gbc);

        gbc.insets = new Insets(10, 10, 40, 10);

        gbc.gridy++;
        panelEstadistiques.add(labelJugats, gbc);

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy++;
        panelEstadistiques.add(titolEstadistiquesRecords, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1; // Reset to default
        panelEstadistiques.add(labelTamany, gbc);

        gbc.gridx = 1;
        panelEstadistiques.add(tamanyComboBox, gbc);

        gbc.gridx = 2;
        panelEstadistiques.add(buscarButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3; // Back to spanning three columns
        panelEstadistiques.add(labelFacil, gbc);

        gbc.gridy++;
        panelEstadistiques.add(labelMitjana, gbc);

        gbc.gridy++;
        panelEstadistiques.add(labelDificil, gbc);

        // Panel principal
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        mainPanel.add(panelEstadistiques, constraints);

        panelEstadistiques.setBackground(Color.decode("#E9EDEF"));

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        tamanyComboBox.setToolTipText("Selecciona un tamany de Kenken");
    }

    /**
     * Inicialitza els botons i el titol de la part superior de la vista.
     */
    public void botonsSuperiors() {
        // Panel pel botó "Enrere"
        JPanel EnrerePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        EnrerePanel.add(enrereButton);

        // Panel pel títol
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Font font = new Font("Arial", Font.BOLD, 20);
        NomPerfil = new JLabel("ESTADISTIQUES");
        NomPerfil.setFont(font);
        titlePanel.add(NomPerfil);

        // Panel superior pel títol i el botó
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(EnrerePanel, BorderLayout.NORTH); // Botó "Enrere" en la part superior
        topPanel.add(titlePanel, BorderLayout.CENTER); // Títol centrat

        EnrerePanel.setBackground(Color.decode("#E9EDEF"));
        topPanel.setBackground(Color.decode("#E9EDEF"));
        titlePanel.setBackground(Color.decode("#E9EDEF"));

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Configura l'aparença dels components de la vista
     */
    public void configuraAparencia() {
        //BOTONES
        Dimension buttonSize = new Dimension(180, 40);
        buscarButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 40);
        enrereButton.setPreferredSize(buttonSize);

        // Fuente no negrita y tamaño 12 por defecto
        Font defaultFont = new Font("Arial", Font.PLAIN, 16);
        labelCreats.setFont(defaultFont);
        labelJugats.setFont(defaultFont);
        labelFacil.setFont(defaultFont);
        labelMitjana.setFont(defaultFont);
        labelDificil.setFont(defaultFont);
        labelTamany.setFont(defaultFont);

        // Fuente negrita y tamaño 15 para los títulos
        Font boldFont = new Font("Arial", Font.BOLD, 18);
        titolEstadistiquesGenerals.setForeground(Color.decode("#005A9E"));
        titolEstadistiquesRecords.setForeground(Color.decode("#005A9E"));
        titolEstadistiquesGenerals.setFont(boldFont);
        titolEstadistiquesRecords.setFont(boldFont);

        //Colors botons
        buscarButton.setBackground(Color.decode("#005A9E"));
        buscarButton.setForeground(Color.decode("#F5F5F5"));
        tamanyComboBox.setBackground(Color.decode("#005A9E"));
        tamanyComboBox.setForeground(Color.decode("#F5F5F5"));
        enrereButton.setBackground(Color.decode("#5BC0BE"));
        enrereButton.setForeground(Color.decode("#333333"));

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
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonBuscar(e);
            }
        });
    }

    /**
     * Es canvia a la vista anterior: VistaOpcions.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEnrere(ActionEvent e) {
        ctrlPresentacio.vistaPerfil();
        setVisible(false);
    }

    /**
     * Busca les estadistiques corresponents segons el tamany.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonBuscar(ActionEvent e) {
        String tamKenkenString = (String) tamanyComboBox.getSelectedItem();
        if (tamKenkenString == "-") {
            ctrlPresentacio.mostraAvis("No has seleccionat un tamany.");
            return;
        }
        int tamKenkenInt = Integer.parseInt(tamKenkenString);
        int facil = ctrlPresentacio.recordFacil(tamKenkenInt);
        int mitjana = ctrlPresentacio.recordMitja(tamKenkenInt);
        int dificil = ctrlPresentacio.recordDificil(tamKenkenInt);
        labelFacil.setText("Record en dificultat fàcil: -");
        labelMitjana.setText("Record en dificultat mitjana: -");
        labelDificil.setText("Record en dificultat difícil: -");
        if (facil != -1) labelFacil.setText("Record en dificultat fàcil: " + transformarMinutsIHores(facil));
        if (mitjana != -1) labelMitjana.setText("Record en dificultat mitjana: " + transformarMinutsIHores(mitjana));
        if (dificil != -1) labelDificil.setText("Record en dificultat difícil: " + transformarMinutsIHores(dificil));

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
}
