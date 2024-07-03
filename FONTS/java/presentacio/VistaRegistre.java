package presentacio;

import excepcions.ExcepcioJaExisteixPerfil;
import excepcions.ExcepcioNoHiHaSessio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Classe que representa una vista específica de la presentació per a que un jugador es pugui registrar al joc.
 * Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
 */
public class VistaRegistre extends JFrame {
    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;

    /**
     * Panell de continguts de la vista.
     */
    private JPanel mainPanel = new JPanel();

    /**
     * Text on s'introdueix el nom d'usuari.
     */
    private JTextField textNomUsuari = new JTextField( 20);

    /**
     * Text on s'introdueix la contrasenya.
     */
    private JPasswordField textContra1 = new JPasswordField( 20);

    /**
     * Text on s'introdueix la validació de la contrasenya.
     */
    private JPasswordField textContra2 = new JPasswordField( 20);

    /**
     * Botó per crear el perfil.
     */
    private JButton creaPerfilButton = new JButton("Crear perfil");

    /**
     * Botó per tornar a la pàgina anterior.
     */
    private JButton enrereButton = new JButton("Tornar enrere");

    /**
     * Etiqueta que indica que s'ha d’introduir el nom d’usuari a la capsa de text corresponent.
     */
    private JLabel IntroduirNom = new JLabel("Introdueix un nom d'usuari: ");

    /**
     * Etiqueta que indica que s'ha d’introduir la contrasenya a la capsa de text corresponent.
     */
    private JLabel IntroduirContra = new JLabel("Introdueix una contrasenya: ");

    /**
     * Etiqueta que indica que s'ha d’introduir de nou la contrasenya a la capsa de text corresponent.
     */
    private JLabel IntroduirContra2 = new JLabel("Introdueix de nou la contrasenya: ");

    /**
     * Etiqueta amb el títol de la pantalla.
     */
    private JLabel titolRegistre = new JLabel("REGISTRE");

    /**
     * Constructora de la vista.
     */
    public VistaRegistre (CtrlPresentacio ctrlPresentacio) {
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
        setTitle("Pantalla Registre");
    }

    /**
     * Inicialitza els botons de la vista.
     */
    private void iniBotons() {
        //Configura l'aparença dels components
        configuraAparencia();

        // Panel pel botó "Enrere"
        JPanel enrerePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enrerePanel.add(enrereButton);

        // Panel pel titol
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(titolRegistre);

        // Panel superior pels dos
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(enrerePanel);
        topPanel.add(titlePanel);

        enrerePanel.setBackground(Color.decode("#E9EDEF"));
        topPanel.setBackground(Color.decode("#E9EDEF"));
        titlePanel.setBackground(Color.decode("#E9EDEF"));

        add(topPanel, BorderLayout.NORTH);

        // BOTONS
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 20, 10, 20); //espai entre botons

        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(IntroduirNom, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        mainPanel.add(textNomUsuari, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        mainPanel.add(IntroduirContra, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        mainPanel.add(textContra1, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        mainPanel.add(IntroduirContra2, constraints);
        constraints.gridx = 1;
        constraints.gridy = 3;
        mainPanel.add(textContra2, constraints);

        constraints.gridx = 2;
        constraints.gridy = 4;
        mainPanel.add(creaPerfilButton, constraints);

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        textNomUsuari.setToolTipText("Text on introdueixes el nom d'usuari");
        textContra1.setToolTipText("Text on introdueixes la contrasenya");
        textContra2.setToolTipText("Text on introdueixes la validació de la contrasenya");
        creaPerfilButton.setToolTipText("Es crea el perfil amb les dades indicades");
        enrereButton.setToolTipText("Tornes a la pantalla anterior");
    }

    /**
     * Configura l'aparença dels components de la vista
     */
    private void configuraAparencia() {

        //BOTONES
        Dimension buttonSize = new Dimension(200, 50);
        creaPerfilButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 20);
        textNomUsuari.setPreferredSize(buttonSize);
        textContra1.setPreferredSize(buttonSize);
        textContra2.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 40);
        enrereButton.setPreferredSize(buttonSize);

        //TEXTS
        Font font = new Font("Arial", Font.BOLD, 20);
        titolRegistre.setFont(font);

        font = new Font("Arial", Font.PLAIN, 16);
        IntroduirContra.setFont(font);
        IntroduirContra2.setFont(font);
        IntroduirNom.setFont(font);
        textNomUsuari.setFont(font);
        textContra1.setFont(font);
        textContra2.setFont(font);

        //Colors botons
        creaPerfilButton.setBackground(Color.decode("#005A9E"));
        creaPerfilButton.setForeground(Color.decode("#F5F5F5"));
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
        creaPerfilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                try {
                    actionPerformed_buttonCreaPerfil(e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
    }

    /**
     * Es canvia a la vista anterior: VistaPrincipal.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEnrere(ActionEvent e) {
        ctrlPresentacio.vistaPantallaPrincipal();
        setVisible(false);
    }

    /**
     * Es crea el perfil, s'inicia sessió i es canvia a la vista VistaOpcions.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonCreaPerfil(ActionEvent e) throws ExcepcioJaExisteixPerfil {
        String nomUs = textNomUsuari.getText();
        String contra1 = textContra1.getText();
        String contra2 = textContra2.getText();
        if (nomUs.equals("")) {
            ctrlPresentacio.mostraAvis("No has introduit cap nom de perfil");
            return;
        }
        if (nomUs.contains(" ")) {
            ctrlPresentacio.mostraAvis("El nom de perfil no pot contenir espais");
            return;
        }
        if (contra1.equals("")) {
            ctrlPresentacio.mostraAvis("No has introduit cap contrasenya");
            return;
        }
        if (contra1.contains(" ")) {
            ctrlPresentacio.mostraAvis("La contrasenya no pot contenir espais");
            return;
        }
        if (!contra1.equals(contra2)) {
            ctrlPresentacio.mostraAvis("Les contrasenyes no coincideixen");
            return;
        }
        try {
            ctrlPresentacio.creaPerfil(nomUs, contra1);
            ctrlPresentacio.vistaOpcions();
            setVisible(false);
        } catch (ExcepcioJaExisteixPerfil | IOException ex) {
            ctrlPresentacio.mostraAvis("Ja existeix un perfil amb el nom indicat");
        }
    }
}

