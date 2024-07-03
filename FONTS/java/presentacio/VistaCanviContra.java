package presentacio;

import excepcions.ExcepcioMateixaContrasenya;
import excepcions.ExcepcioNoHiHaSessio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe que representa una vista específica de la presentació per a canviar la contrasenya d'un perfil.
 * Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
 */
public class VistaCanviContra extends JFrame {

    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;

    /**
     * Panell de continguts de la vista.
     */
    private JPanel mainPanel = new JPanel();

    /**
     * Botó per tornar a la pàgina anterior.
     */
    private JButton enrereButton = new JButton("Tornar enrere");

    /**
     * Text on s'escriu la nova contrasenya.
     */
    private JPasswordField textContra1 = new JPasswordField(20);

    /**
     * Text on s'es valida la nova contrasenya.
     */
    private JPasswordField textContra2 = new JPasswordField(20);

    /**
     * Botó per canviar la contrasenya del perfil.
     */
    private JButton canviaContrasenyaButton = new JButton("Canvia contrasenya");

    /**
     * Etiqueta amb el titol de la pantalla.
     */
    private JLabel titolContra = new JLabel("CANVIAR CONTRASENYA");

    /**
     * Etiqueta que indica que s'introdueixi la nova contrasenya.
     */
    private JLabel IntroduirContra1 = new JLabel("Introdueix una nova contrasenya: ");

    /**
     * Etiqueta que indica que s'introdueixi la validació de la contrasenya.
     */
    private JLabel IntroduirContra2 = new JLabel("Introdueix de nou la contrasenya: ");

    /**
     * Constructora de la vista.
     */
    public VistaCanviContra (CtrlPresentacio ctrlPresentacio) {
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
        setMinimumSize(new Dimension(900,550));
        setPreferredSize(getMinimumSize());
        setResizable(false);
        // Posicio
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Pantalla canvi contrasenya");
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

        // TITOL I BOTÓ ENRERE
        // Panel pel botó "Enrere"
        JPanel enrerePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enrerePanel.add(enrereButton);

        // Panel pel titol
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Font font = new Font("Arial", Font.BOLD, 20);
        titolContra.setFont(font);
        titlePanel.add(titolContra);

        // Panel superior pels dos
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(enrerePanel);
        topPanel.add(titlePanel);

        add(topPanel, BorderLayout.NORTH);

        // BOTONS
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 20, 10, 20); //espai entre botons

        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(IntroduirContra1, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        mainPanel.add(textContra1, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        mainPanel.add(IntroduirContra2, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        mainPanel.add(textContra2, constraints);

        constraints.gridx = 2;
        constraints.gridy = 4;
        mainPanel.add(canviaContrasenyaButton, constraints);

        enrerePanel.setBackground(Color.decode("#E9EDEF"));
        topPanel.setBackground(Color.decode("#E9EDEF"));
        titlePanel.setBackground(Color.decode("#E9EDEF"));

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        enrereButton.setToolTipText("Tornes a la pantalla anterior");
        textContra1.setToolTipText("Text on introduir la nova contrasenya");
        textContra2.setToolTipText("Text on introduir la validació de la contrasenya");
    }

    /**
     * Configura l'aparença dels components de la vista
     */
    public void configuraAparencia() {
        //BOTONES
        Dimension buttonSize = new Dimension(200, 50);
        canviaContrasenyaButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 40);
        enrereButton.setPreferredSize(buttonSize);

        // Fuente no negrita y tamaño 12 por defecto
        Font defaultFont = new Font("Arial", Font.PLAIN, 16);
        textContra2.setFont(defaultFont);
        textContra1.setFont(defaultFont);
        IntroduirContra1.setFont(defaultFont);
        IntroduirContra2.setFont(defaultFont);

        //Colors botons
        canviaContrasenyaButton.setBackground(Color.decode("#005A9E"));
        canviaContrasenyaButton.setForeground(Color.decode("#F5F5F5"));
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
        canviaContrasenyaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                try {
                    actionPerformed_buttonCanviaContrasenya(e);
                } catch (ExcepcioNoHiHaSessio ex) {
                    ex.printStackTrace();
                } catch (ExcepcioMateixaContrasenya ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Es canvia a la vista anterior: VistaPerfil.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEnrere(ActionEvent e) {
        ctrlPresentacio.vistaPerfil();
        setVisible(false);
    }

    /**
     * Es canvia la contrasenya i es canvia a la vista anterior: VistaPerfil.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonCanviaContrasenya(ActionEvent e) throws ExcepcioNoHiHaSessio, ExcepcioMateixaContrasenya {
        String contra1 = textContra1.getText();
        String contra2 = textContra2.getText();
        if (contra1.equals("")) {
            ctrlPresentacio.mostraAvis("No has introduit cap contrasenya");
            return;
        }
        if (!contra1.equals(contra2)) {
            ctrlPresentacio.mostraAvis("Les contrasenyes no coincideixen");
            return;
        }
        try {
            ctrlPresentacio.novaContrasenya(contra1);
            ctrlPresentacio.vistaPerfil();
            setVisible(false);
        } catch (ExcepcioMateixaContrasenya ex) {
            ctrlPresentacio.mostraAvis("La contrasenya nova és igual a l'anterior");
        }
    }
}
