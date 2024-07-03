package presentacio;

import excepcions.ExcepcioNoHiHaPartidaGuardada;
import excepcions.ExcepcioNoHiHaSessio;
import excepcions.ExcepcioTamany;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
* Classe que representa una vista específica de la presentació per a mostrar les opcions que té l'usuari per jugar una partida.
* Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
*/
public class VistaTipusPartida extends JFrame {
    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;

    /**
     * Panell de continguts de la vista de nova o carrega partida.
     */
    private JPanel mainPanel = new JPanel();

    /**
     * Botó per tornar a la pàgina anterior.
     */
    private JButton enrereButton = new JButton("Tornar enrere");

    /**
     * Botó per iniciar una nova partida.
     */
    private JButton novaPartidaButton = new JButton("Nova partida");

    /**
     * Botó per reaunadar una partida guardada.
     */
    private JButton carregarPartidaButton = new JButton("Carrega partida");

    /**
     * Etiqueta del titol de la pantalla.
     */
    private JLabel titolNovaCarga = new JLabel("JUGAR PARTIDA");

    /**
     * Constructora de la vista.
     */
    public VistaTipusPartida (CtrlPresentacio ctrlPresentacio) {
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
        setTitle("Pantalla Tipus Partida");
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
        titlePanel.add(titolNovaCarga);

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
        mainPanel.add(novaPartidaButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        mainPanel.add(carregarPartidaButton, constraints);

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        novaPartidaButton.setToolTipText("Et dirigeix a la configuració d'una nova partida");
        carregarPartidaButton.setToolTipText("Et dirigeix a la cerca de partides guardades");
    }

    /**
     * Configura l'aparença dels components de la vista
     */
    private void configuraAparencia() {
        //BOTONES
        Dimension buttonSize = new Dimension(200, 50);
        novaPartidaButton.setPreferredSize(buttonSize);
        carregarPartidaButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 40);
        enrereButton.setPreferredSize(buttonSize);

        //TEXTS
        Font font = new Font("Arial", Font.BOLD, 20);
        titolNovaCarga.setFont(font);

        //Colors botons
        novaPartidaButton.setBackground(Color.decode("#005A9E"));
        novaPartidaButton.setForeground(Color.decode("#F5F5F5"));
        carregarPartidaButton.setBackground(Color.decode("#5BC0BE"));
        carregarPartidaButton.setForeground(Color.decode("#333333"));
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
        novaPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonNovaPartida(e);
            }
        });
        carregarPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonCarregarPartida(e);
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
     * Es canvia a la vista: VistaConfiguracióPartida.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonNovaPartida(ActionEvent e) {
        ctrlPresentacio.vistaConfiguracioPartida();
        setVisible(false);
    }

    /**
     * Es canvia a la vista: VistaJugarPartida.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonCarregarPartida(ActionEvent e) {
        try {
            ctrlPresentacio.carregarPartida();
            int[][] mat = ctrlPresentacio.matTauler();
            String[] ops = ctrlPresentacio.vectorOps();
            Integer[] res = ctrlPresentacio.vectorRes();
            int[] vals = ctrlPresentacio.vectorVals();
            int segons = ctrlPresentacio.getSegons();

            ctrlPresentacio.vistaJugarPartida(mat, ops, res, vals, segons);
            setVisible(false);
        } catch (ExcepcioNoHiHaPartidaGuardada ex) {
            ctrlPresentacio.mostraAvis("No tens cap partida guardada.");
        } catch (ExcepcioTamany ex) {
            ex.printStackTrace();
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
