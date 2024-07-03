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
public class VistaPerfil extends JFrame {
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
     * Botó per veure les estadístiques del perfil.
     */
    private JButton estadistiquesButton = new JButton("Consultar estadístiques");

    /**
     * Botó per canviar la contrasenya del perfil.
     */
    private JButton canviarContrasenyaButton = new JButton("Canviar contrasenya");

    /**
     * Botó per tancar sessió i tornar a la pàgina principal.
     */
    private JButton tancarSessioButton = new JButton("Tanca la sessió");

    /**
     * Botó per eliminar el perfil actiu i tornar a la pàgina principal.
     */
    private JButton eliminarPerfilButton = new JButton("Elimina el perfil");

    /**
     * Etiqueta que indica el nom de l'usuari actiu.
     */
    private JLabel NomPerfil = new JLabel();

    /**
     * Constructora de la vista.
     */
    public VistaPerfil(CtrlPresentacio ctrlPresentacio) {
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
        setTitle("Pantalla Perfil");
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
        //Configura l'aparença dels components
        configuraAparencia();

        // Inicialitza els botons superiors de la pantalla
        botonsSuperiors();

        //Panel: Botones de configuración
        JPanel panelConfig = new JPanel(new GridBagLayout());
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panelConfig.add(estadistiquesButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panelConfig.add(canviarContrasenyaButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panelConfig.add(Box.createRigidArea(new Dimension(20, 0)), constraints); // Separador
        constraints.gridx = 0;
        constraints.gridy = 3;
        panelConfig.add(tancarSessioButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        panelConfig.add(eliminarPerfilButton, constraints);


        // Panel principal
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(panelConfig, constraints);

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        canviarContrasenyaButton.setToolTipText("Et dirigeix a la configuració de la contrasenya");
        tancarSessioButton.setToolTipText("Tanques la sessió i tornes a la pantalla principal");
        eliminarPerfilButton.setToolTipText("Elimines el perfil i tornes a la pantalla principal");
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
        NomPerfil.setText("PERFIL " + ctrlPresentacio.getNomUsuariActiu());
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
    private void configuraAparencia() {
        //BOTONES
        Dimension buttonSize = new Dimension(200, 50);
        estadistiquesButton.setPreferredSize(buttonSize);
        canviarContrasenyaButton.setPreferredSize(buttonSize);
        tancarSessioButton.setPreferredSize(buttonSize);
        eliminarPerfilButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 40);
        enrereButton.setPreferredSize(buttonSize);

        //TEXTS
        Font font = new Font("Arial", Font.BOLD, 20);
        NomPerfil.setFont(font);

        //Colors botons
        estadistiquesButton.setBackground(Color.decode("#005A9E"));
        estadistiquesButton.setForeground(Color.decode("#F5F5F5"));
        canviarContrasenyaButton.setBackground(Color.decode("#005A9E"));
        canviarContrasenyaButton.setForeground(Color.decode("#F5F5F5"));
        tancarSessioButton.setBackground(Color.decode("#E57373"));
        tancarSessioButton.setForeground(Color.decode("#333333"));
        eliminarPerfilButton.setBackground(Color.decode("#E57373"));
        eliminarPerfilButton.setForeground(Color.decode("#333333"));
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
        canviarContrasenyaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonCanviarContrasenya(e);
            }
        });
        tancarSessioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                try {
                    actionPerformed_buttonTancarSessio(e);
                } catch (ExcepcioNoHiHaSessio ex) {
                    ex.printStackTrace();
                }
            }
        });
        eliminarPerfilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                try {
                    actionPerformed_buttonEliminarPerfil(e);
                } catch (ExcepcioNoExisteixPerfil ex) {
                    ex.printStackTrace();
                } catch (ExcepcioNoHiHaSessio ex) {
                    ex.printStackTrace();
                }
            }
        });
        estadistiquesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonEstadistiques(e);
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
     * Es canvia a la vista a la VistaCanviContrasenya.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEstadistiques(ActionEvent e) {
        ctrlPresentacio.vistaEstadistiques();
        setVisible(false);
    }

    /**
     * Es canvia a la vista a la VistaCanviContrasenya.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonCanviarContrasenya(ActionEvent e) {
        ctrlPresentacio.vistaCanviContrasenya();
        setVisible(false);
    }

    /**
     * Es tanca la sessió i es canvia a la vista VistaPrincipal.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonTancarSessio(ActionEvent e) throws ExcepcioNoHiHaSessio {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Segur que vols tancar sessió?", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            ctrlPresentacio.tancaSessio();
            ctrlPresentacio.vistaPantallaPrincipal();
            setVisible(false);
        }
    }

    /**
     * Es tanca la sessió i es canvia a la vista VistaPrincipal.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEliminarPerfil(ActionEvent e) throws ExcepcioNoExisteixPerfil, ExcepcioNoHiHaSessio {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Segur que vols eliminar el perfil?", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            try {
                ctrlPresentacio.eliminaPerfil();
                ctrlPresentacio.vistaPantallaPrincipal();
                setVisible(false);
            } catch (ExcepcioNoExisteixPerfil ex) {
                ctrlPresentacio.mostraAvis("No existeix el perfil que vols eliminar");
            } catch (ExcepcioNoHiHaSessio ex) {
                ctrlPresentacio.mostraAvis("No has iniciat sessió");
            } catch (ExcepcioNoTePartida ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}