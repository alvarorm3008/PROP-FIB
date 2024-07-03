package presentacio;

import excepcions.ExcepcioContrasenyaIncorrecta;
import excepcions.ExcepcioNoExisteixPerfil;
import excepcions.ExcepcioNoHiHaSessio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Classe que representa una vista específica de la presentació per a inciar sessió.
 * Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
 */
public class VistaIniciSessio extends JFrame {

    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;

    /**
     * Panell de continguts de la vista de iniciar sessió.
     */
    private JPanel mainPanel = new JPanel();

    /**
     * Text on s'introdueix el nom d'usuari.
     */
    private JTextField textNomUsuari = new JTextField( 20);

    /**
     * Text on s'introdueix la contrasenya.
     */
    private JPasswordField textContra = new JPasswordField( 20);

    /**
     * Botó per iniciar sessió.
     */
    private JButton iniciSessioButton = new JButton("Inicia sessió");

    /**
     * Botó per tornar a la pàgina anterior.
     */
    private JButton enrereButton = new JButton("Tornar enrere");

    /**
     * Etiqueta que indica s'ha d’introduir el nom d’usuari a la capsa de text corresponent.
     */
    private JLabel IntroduirNom = new JLabel("Introdueix un nom d'usuari: ");

    /**
     * Etiqueta que indica s'ha d’introduir la contrasenya a la capsa de text corresponent.
     */
    private JLabel IntroduirContra = new JLabel("Introdueix una contrasenya: ");

    /**
     * Etiqueta amb el títol de la pantalla.
     */
    private JLabel titolIniciSessio = new JLabel("INICI DE SESSIÓ");

    /**
     * Constructora de la vista.
     */
    public VistaIniciSessio (CtrlPresentacio ctrlPresentacio) {
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
        setTitle("Pantalla Inici Sessió");
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
        titlePanel.add(titolIniciSessio);

        // Panel superior pels dos
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(enrerePanel);
        topPanel.add(titlePanel);

        add(topPanel, BorderLayout.NORTH);

        enrerePanel.setBackground(Color.decode("#E9EDEF"));
        topPanel.setBackground(Color.decode("#E9EDEF"));
        titlePanel.setBackground(Color.decode("#E9EDEF"));

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
        mainPanel.add(textContra, constraints);

        constraints.gridx = 2;
        constraints.gridy = 4;
        mainPanel.add(iniciSessioButton, constraints);

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        textNomUsuari.setToolTipText("Text on introdueixes el nom d'usuari");
        textContra.setToolTipText("Text on introdueixes la contrasenya");
        iniciSessioButton.setToolTipText("Inicies sessió al perfil indicat");
        enrereButton.setToolTipText("Tornes a la pantalla anterior");
    }

    /**
     * Configura l'aparença dels components de la vista
     */
    private void configuraAparencia() {

        //BOTONES
        Dimension buttonSize = new Dimension(200, 50);
        iniciSessioButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 20);
        textNomUsuari.setPreferredSize(buttonSize);
        textContra.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 40);
        enrereButton.setPreferredSize(buttonSize);

        //TEXTS
        Font font = new Font("Arial", Font.BOLD, 20);
        titolIniciSessio.setFont(font);

        font = new Font("Arial", Font.PLAIN, 16);
        IntroduirContra.setFont(font);
        IntroduirNom.setFont(font);
        textNomUsuari.setFont(font);
        textContra.setFont(font);

        //Colors botons
        iniciSessioButton.setBackground(Color.decode("#005A9E"));
        iniciSessioButton.setForeground(Color.decode("#F5F5F5"));
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
        iniciSessioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                try {
                    actionPerformed_buttonIniciSessio(e);
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
     * S'inicia la sessió del perfil indicat i es canvia a la vista: VistaOpcions.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonIniciSessio(ActionEvent e) throws ExcepcioContrasenyaIncorrecta, ExcepcioNoExisteixPerfil {
        String nomUs = textNomUsuari.getText();
        String contra = textContra.getText();
        if (nomUs.equals("")) {
            ctrlPresentacio.mostraAvis("No has introduit cap nom de perfil");
            return;
        }
        if (contra.equals("")) {
            ctrlPresentacio.mostraAvis("No has introduit cap contrasenya");
            return;
        }
        try {
            ctrlPresentacio.iniciaSessio(nomUs, contra); //es col·loca com a usuari actiu el que és.
            ctrlPresentacio.vistaOpcions();
            setVisible(false);
        } catch (ExcepcioNoExisteixPerfil ex) {
            ctrlPresentacio.mostraAvis("No existeix cap perfil amb el nom d'usuari indicat");
        } catch (ExcepcioContrasenyaIncorrecta ex) {
            ctrlPresentacio.mostraAvis("La contrasenya és incorrecta");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}


