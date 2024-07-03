package presentacio;

import excepcions.ExcepcioNoHiHaSessio;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

/**
* Classe que representa una vista específica de la presentació per a crear automàticament un Kenken.
* Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
*/
public class VistaCreaAuto extends JFrame {
    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;
    /**
     * Tamany del tauler.
     */
    private int tam;

    /**
     * Panell de continguts de la vista de configuracio de la creació de kenken
     */
    private JPanel mainPanel = new JPanel();

    /**
     * Botó per tornar a la pàgina anterior.
     */
    private JButton enrereButton = new JButton("Tornar enrere");
    /**
     * Botó per seleccionar la resta.
     */
    private JButton RestaButton = new JButton("-");
    /**
     * Botó per seleccionar la multiplicació.
     */
    private JButton MultiButton = new JButton("x");
    /**
     * Botó per seleccionar la divisió.
     */
    private JButton DiviButton = new JButton("/");
    /**
     * Botó per seleccionar el mòdul.
     */
    private JButton ModulButton = new JButton("%");
    /**
     * Botó per seleccionar el màxim.
     */
    private JButton MaximButton = new JButton("Max");
    /**
     * Camp de text per indicar el numero de caselles resoltes.
     */
    private JTextField textCasResoltes = new JTextField(20);
    /**
     * Botó per crear el kenken.
     */
    private JButton creaKenkenAuto = new JButton("Crea el kenken");
    /**
     * Etiqueta del titol de la pantalla.
     */
    private JLabel titolCreaAuto = new JLabel("CREACIÓ KENKEN AUTOMÀTIC");
    /**
     * Etiqueta de l'explicació de la pantalla.
     */
    private JLabel explicacio = new JLabel("Pots escollir altres paràmetres per la creació però tots ells son opcionals.");
    /**
     * Etiqueta de l'explicació de la pantalla 2.
     */
    private JLabel explicacio2 = new JLabel("(L'operació suma sempre apareixerà als kenkens generats)");
    /**
     * Etiqueta que indica la selecció d'operacions.
     */
    private JLabel labelSelectOps = new JLabel("Selecciona les operacions:");
    /**
     * Etiqueta que indica la selecció de caselles resoltes.
     */
    private JLabel labelSelectCas = new JLabel();

    /**
     * Constructora de la vista.
     */
    public VistaCreaAuto (CtrlPresentacio ctrlPresentacio, int N) {
        ferVisible();
        this.ctrlPresentacio = ctrlPresentacio;
        tam = N;
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
        setMinimumSize(new Dimension(900,550));
        setPreferredSize(getMinimumSize());
        setResizable(false);
        // Posicio
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Pantalla Creació Automàtica de Kenken");
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
        GridBagConstraints constraints = new GridBagConstraints();

        // Configura l'aparença dels components
        configuraAparencia();

        // Inicializa los botones de la parte superior de la pantalla
        botonsSuperiors();

        // Panell etiquetes
        JPanel explicacioPanel = new JPanel(new GridBagLayout());
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        explicacioPanel.add(explicacio, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        explicacioPanel.add(explicacio2, constraints);

        JPanel operationsPanel = new JPanel(new GridBagLayout());
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
        operationsPanel.add(labelSelectOps, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        operationsPanel.add(RestaButton, constraints);
        constraints.gridx = 2;
        constraints.gridy = 0;
        operationsPanel.add(MultiButton, constraints);
        constraints.gridx = 3;
        constraints.gridy = 0;
        operationsPanel.add(DiviButton, constraints);
        constraints.gridx = 4;
        constraints.gridy = 0;
        operationsPanel.add(ModulButton, constraints);
        constraints.gridx = 5;
        constraints.gridy = 0;
        operationsPanel.add(MaximButton, constraints);

        labelSelectCas.setText("Selecciona el numero de caselles resoltes (ha de ser entre 0 i " + (tam*tam)/2 + "):");

        JPanel casellesPanel = new JPanel(new GridBagLayout());
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        casellesPanel.add(labelSelectCas, constraints);
        constraints.gridx = 1;
        casellesPanel.add(Box.createRigidArea(new Dimension(20, 0))); // espai
        constraints.gridx = 2;
        casellesPanel.add(textCasResoltes, constraints);

        JPanel creaPanel = new JPanel(new GridBagLayout());
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        creaPanel.add(creaKenkenAuto, constraints);

        mainPanel.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(explicacioPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(operationsPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        mainPanel.add(casellesPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        mainPanel.add(creaPanel, constraints);

        explicacioPanel.setBackground(Color.decode("#E9EDEF"));
        casellesPanel.setBackground(Color.decode("#E9EDEF"));
        creaPanel.setBackground(Color.decode("#E9EDEF"));
        operationsPanel.setBackground(Color.decode("#E9EDEF"));

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        RestaButton.setToolTipText("Selecciones l'operació resta");
        MultiButton.setToolTipText("Selecciones l'operació multiplicació");
        DiviButton.setToolTipText("Selecciones l'operació divisió");
        ModulButton.setToolTipText("Selecciones l'operació mòdul");
        MaximButton.setToolTipText("Selecciones l'operació màxim");
        textCasResoltes.setToolTipText("Text on introdueixes un enter que correspon al numero de caselles resoltes");
        creaKenkenAuto.setToolTipText("Es crea el kenken amb els paràmetres indicats");
        enrereButton.setToolTipText("Tornes a la pantalla anterior");
    }

    /**
     * Inicialitza els botons i el titol de la part superior de la vista.
     */
    private void botonsSuperiors() {
        //Panell pel botó "Enrere"
        JPanel enrerePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enrerePanel.add(enrereButton);

        //Panell pel titol
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Font font = new Font("Arial", Font.BOLD, 20);
        titolCreaAuto.setFont(font);
        titlePanel.add(titolCreaAuto);

        //Panell superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(enrerePanel, BorderLayout.NORTH); // Panel de botons en la part superior
        topPanel.add(titlePanel, BorderLayout.CENTER); // Títol centrat horizontalment sota dels botons

        enrerePanel.setBackground(Color.decode("#E9EDEF"));
        titlePanel.setBackground(Color.decode("#E9EDEF"));
        topPanel.setBackground(Color.decode("#E9EDEF"));

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Configura l'aparença dels components de la vista.
     */
    private void configuraAparencia() {
        //TEXTS
        Font font = new Font("Arial", Font.BOLD, 20);
        titolCreaAuto.setFont(font);

        font = new Font("Arial", Font.BOLD, 14);
        explicacio.setFont(font);
        explicacio2.setFont(font);

        font = new Font("Arial", Font.PLAIN, 14);
        labelSelectOps.setFont(font);
        labelSelectCas.setFont(font);

        //BOTONES
        Dimension buttonSize = new Dimension(200, 50);
        creaKenkenAuto.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 40);
        enrereButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(60, 50);
        RestaButton.setPreferredSize(buttonSize);
        MultiButton.setPreferredSize(buttonSize);
        DiviButton.setPreferredSize(buttonSize);
        ModulButton.setPreferredSize(buttonSize);
        MaximButton.setPreferredSize(buttonSize);

        //Colors botons
        creaKenkenAuto.setBackground(Color.decode("#005A9E"));
        creaKenkenAuto.setForeground(Color.decode("#F5F5F5"));
        enrereButton.setBackground(Color.decode("#5BC0BE"));
        enrereButton.setForeground(Color.decode("#333333"));
        RestaButton.setBackground(Color.decode("#5BC0BE"));
        RestaButton.setForeground(Color.decode("#333333"));
        MultiButton.setBackground(Color.decode("#5BC0BE"));
        MultiButton.setForeground(Color.decode("#333333"));
        DiviButton.setBackground(Color.decode("#5BC0BE"));
        DiviButton.setForeground(Color.decode("#333333"));
        ModulButton.setBackground(Color.decode("#5BC0BE"));
        ModulButton.setForeground(Color.decode("#333333"));
        MaximButton.setBackground(Color.decode("#5BC0BE"));
        MaximButton.setForeground(Color.decode("#333333"));

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
        RestaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonResta(e);
            }
        });
        MultiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonMulti(e);
            }
        });
        DiviButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonDivi(e);
            }
        });
        ModulButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonModul(e);
            }
        });
        MaximButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonMaxim(e);
            }
        });
        creaKenkenAuto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonCreaKenkenAuto(e);
            }
        });
    }

    /**
     * Es canvia a la vista anterior: VistaConfigCreacioKK.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEnrere(ActionEvent e) {
        ctrlPresentacio.vistaConfigCreacio();
        setVisible(false);
    }

    private void actionPerformed_buttonResta(ActionEvent e) {
        toggleButtonColor(RestaButton);
    }

    private void actionPerformed_buttonMulti(ActionEvent e) {
        toggleButtonColor(MultiButton);
    }

    private void actionPerformed_buttonDivi(ActionEvent e) {
        toggleButtonColor(DiviButton);
    }

    private void actionPerformed_buttonModul(ActionEvent e) {
        toggleButtonColor(ModulButton);
    }

    private void actionPerformed_buttonMaxim(ActionEvent e) {
        toggleButtonColor(MaximButton);
    }

    /**
     * Método auxiliar para alternar el color de un botón entre dos colores
     */
    private void toggleButtonColor(JButton button) {
        Color defaultColor = Color.decode("#5BC0BE");
        Color selectedColor = Color.gray;

        if (button.getBackground().equals(defaultColor)) {
            button.setBackground(selectedColor);
        } else {
            button.setBackground(defaultColor);
        }
        button.repaint(); // Fuerza la actualización del botón
    }

    /**
     * Crea el kenken a partir dels paràmetres indicats.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonCreaKenkenAuto(ActionEvent e) {
        ArrayList<Integer> operacions = new ArrayList<>();
        operacions.add(1);
        if (RestaButton.getBackground().equals(Color.gray)) operacions.add(2);
        if (MultiButton.getBackground().equals(Color.gray)) operacions.add(3);
        if (DiviButton.getBackground().equals(Color.gray)) operacions.add(4);
        if (ModulButton.getBackground().equals(Color.gray)) operacions.add(5);
        if (MaximButton.getBackground().equals(Color.gray)) operacions.add(6);

        int numCasellesResoltes = -1;
        //Miro que el numero de caselles resoltes indicat sigui vàlid
        if (!textCasResoltes.getText().isEmpty()) {
            try {
                numCasellesResoltes = Integer.parseInt(textCasResoltes.getText());
                if (numCasellesResoltes < 0 || numCasellesResoltes > (tam*tam)/2 ) {
                    ctrlPresentacio.mostraAvis("El número de caselles resoltes ha de ser entre 0 i " + (tam*tam)/2);
                    return;
                }
            } catch (NumberFormatException ex) {
                ctrlPresentacio.mostraAvis("El número de caselles resoltes introduit no és vàlid");
                return;
            }
        }

        ctrlPresentacio.generaKenkenParams(tam, numCasellesResoltes, operacions);

        int[][] mat = ctrlPresentacio.matTauler();
        String[] ops = ctrlPresentacio.vectorOps();
        Integer[] res = ctrlPresentacio.vectorRes();
        int[] valCas = ctrlPresentacio.vectorVals();

        ctrlPresentacio.vistaMostraKenken(mat, ops, res, valCas);
        setVisible(false);
    }
}
