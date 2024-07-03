package presentacio;
import excepcions.ExcepcioNoHiHaSessio;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

/**
 * Classe que representa una vista específica de la presentació per a donar la benvinguda al joc del Kenken.
 * Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
 */
public class VistaPrincipal extends JFrame {

    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;

    /**
     * Panell de continguts de la vista principal.
     */
    private JPanel mainPanel = new JPanel();

    /**
     * Botó per iniciar sessió a la aplicació.
     */
    private JButton iniciaSessioButton = new JButton("Inicia Sessió");

    /**
     * Botó per registrarse a l'aplicació.
     */
    private JButton registraTButton = new JButton("Registra't");

    /**
     * Etiqueta amb el titol de la pantalla.
     */
    private JLabel titol = new JLabel("JOC DE KENKENS");

    /**
     * Etiqueta que mostra el logo del joc.
     */
    private JLabel imagenLogo = new JLabel();

    /**
     * Constructora de la vista.
     */
    public VistaPrincipal (CtrlPresentacio ctrlPresentacio) {
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
        setTitle("Pantalla Principal");
    }

    /**
     * Inicialitza els botons de la vista.
     */
    private void iniBotons() {
        GridBagConstraints constraints = new GridBagConstraints();

        //Configura l'aparença dels components
        configuraAparencia();

        JPanel panelLogo = new JPanel();
        panelLogo.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        panelLogo.add(imagenLogo, constraints);

        //BOTONS
        JPanel panelButtons = new JPanel(new GridBagLayout());
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;

        // Configuración de los botones con separación
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        panelButtons.add(iniciaSessioButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        panelButtons.add(Box.createRigidArea(new Dimension(20, 0)), constraints); // Separador

        constraints.gridx = 2;
        constraints.gridy = 0;
        panelButtons.add(registraTButton, constraints);

        //Panell principal
        mainPanel.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(panelLogo, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(panelButtons, constraints);

        panelLogo.setBackground(Color.decode("#E9EDEF"));
        panelButtons.setBackground(Color.decode("#E9EDEF"));

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        iniciaSessioButton.setToolTipText("Obre la pantalla d'iniciar sessió");
        registraTButton.setToolTipText("Obre la pantalla de registre de perfil");
    }

    /**
     * Configura l'aparença dels components de la vista
     */
    private void configuraAparencia() {
        //PERFIL
        int maxLabelWidth = 350;
        int maxLabelHeight = 350;

        ImageIcon iconoLogo = new ImageIcon("FONTS/java/presentacio/Icons/Logo.png");
        Image imagen = iconoLogo.getImage();
        Image imagenEscalada = imagen.getScaledInstance(maxLabelWidth, maxLabelHeight, Image.SCALE_SMOOTH);
        ImageIcon iconoLogoEscalado = new ImageIcon(imagenEscalada);

        // Crear el JLabel con el ImageIcon escalado
        imagenLogo = new JLabel(iconoLogoEscalado);

        //BOTONES
        Dimension buttonSize = new Dimension(200, 50);

        //Tamanys botons
        iniciaSessioButton.setPreferredSize(buttonSize);
        registraTButton.setPreferredSize(buttonSize);

        //Colors botons
        iniciaSessioButton.setBackground(Color.decode("#005A9E"));
        iniciaSessioButton.setForeground(Color.decode("#F5F5F5"));
        registraTButton.setBackground(Color.decode("#5BC0BE"));
        registraTButton.setForeground(Color.decode("#333333"));

        mainPanel.setBackground(Color.decode("#E9EDEF"));
    }

    /**
     * Assigna els listeners als components corresponents.
     */
    public void assignar_listenersComponents() {
        iniciaSessioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonIniciaSessio(e);
            }

        });
        registraTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonRegistraT(e);
            }

        });
    }

    /**
     * Es canvia a la vista: VistaIniciSessio.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonIniciaSessio(ActionEvent e) {
        ctrlPresentacio.vistaIniciaSessio();
        setVisible(false);
    }

    /**
     * Es canvia a la vista: VistaRegistre.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonRegistraT (ActionEvent e) {
        ctrlPresentacio.vistaRegistrarse();
        setVisible(false);
    }
}

