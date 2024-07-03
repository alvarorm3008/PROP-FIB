package presentacio;

import excepcions.ExcepcioNoHiHaSessio;
import excepcions.ExcepcioTamany;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * Classe que representa una vista específica de la presentació per a mostrar les opcions del joc del Kenken.
 * Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
 */
public class VistaOpcions extends JFrame{

    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;

    /**
     * Panell de continguts de la vista de opcions.
     */
    private JPanel mainPanel = new JPanel();
    /**
     * Botó per tancar sessió i tornar a la pàgina principal.
     */
    private JButton tancarSessioButton = new JButton("Tanca la sessió");

    /**
     * Botó per consultar les normes del joc.
     */
    private JButton normesButton = new JButton("Normes del joc" );

    /**
     * Botó per iniciar una partida.
     */
    private JButton jugarPartidaButton = new JButton("Jugar partida");

    /**
     * Botó per començar a crear un Kenken.
     */
    private JButton crearKenKenButton = new JButton("Crear Kenken");

    /**
     * Vista del directori home per importar un arxiu.
     */
    private JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

    /**
     * Filepath de l’arxiu importat.
     */
    private String filepath;

    /**
     * Botó per consultar el ranquing.
     */
    private JButton veureRanquingButton = new JButton("Consultar Ranquing");

    /**
     * Etiqueta que indica el nom de l'usuari actiu.
     */
    private JLabel NomPerfil;

    /**
     * Etiqueta del titol de la pantalla.
     */
    private JLabel titolBenvinguda = new JLabel("BENVINGUT/DA AL JOC DELS KENKENS");

    /**
     * Etiqueta per consultar el perfil.
     */
    private JLabel imagenPerfil = new JLabel();

    /**
     * Constructora de la vista.
     */
    public VistaOpcions (CtrlPresentacio ctrlPresentacio) {
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
        setTitle("Pantalla Opcions");
        // Color de fondo principal
        getContentPane().setBackground(Color.decode("#E9EDEF"));
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

        //Inicialitza els botons de la part superior de la pantalla
        botonsSuperiors();

        // BOTONS
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 20, 10, 20); //espai entre botons

        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(jugarPartidaButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        mainPanel.add(crearKenKenButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        mainPanel.add(veureRanquingButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        mainPanel.add(normesButton, constraints);

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        jugarPartidaButton.setToolTipText("Et dirigeix a la configuració d'una partida");
        crearKenKenButton.setToolTipText("Et dirigeix a la configuració d'un Kenken per crear-lo");
        veureRanquingButton.setToolTipText("Consultes els ranquings");
        tancarSessioButton.setToolTipText("Tanques la sessió i tornes a la pantalla principal");
        normesButton.setToolTipText("Consultes les normes del joc KenKen");
        imagenPerfil.setToolTipText("Consultes el teu perfil");
    }

    /**
     * Configura l'aparença dels components de la vista.
     */
    private void configuraAparencia() {
        //PERFIL
        int maxLabelWidth = 50;
        int maxLabelHeight = 45;

        ImageIcon iconoPerfil = new ImageIcon("FONTS/java/presentacio/Icons/Perfil.png");
        Image imagen = iconoPerfil.getImage();
        Image imagenEscalada = imagen.getScaledInstance(maxLabelWidth, maxLabelHeight, Image.SCALE_SMOOTH);
        ImageIcon iconoPerfilEscalado = new ImageIcon(imagenEscalada);

        // Crear el JLabel con el ImageIcon escalado
        imagenPerfil = new JLabel(iconoPerfilEscalado);

        // Agregar el MouseListener para detectar clics en la imagen
        imagenPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ctrlPresentacio.vistaPerfil();
                setVisible(false);
            }
        });

        //BOTONES
        Dimension buttonSize = new Dimension(200, 50);

        //Tamanys botons
        jugarPartidaButton.setPreferredSize(buttonSize);
        crearKenKenButton.setPreferredSize(buttonSize);
        veureRanquingButton.setPreferredSize(buttonSize);
        normesButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 40);

        tancarSessioButton.setPreferredSize(buttonSize);

        //Colors botons
        jugarPartidaButton.setBackground(Color.decode("#005A9E"));
        jugarPartidaButton.setForeground(Color.decode("#F5F5F5"));
        crearKenKenButton.setBackground(Color.decode("#5BC0BE"));
        crearKenKenButton.setForeground(Color.decode("#333333"));
        veureRanquingButton.setBackground(Color.decode("#5BC0BE"));
        veureRanquingButton.setForeground(Color.decode("#333333"));
        normesButton.setBackground(Color.decode("#5BC0BE"));
        normesButton.setForeground(Color.decode("#333333"));
        tancarSessioButton.setBackground(Color.decode("#E57373"));
        tancarSessioButton.setForeground(Color.decode("#333333"));

        mainPanel.setBackground(Color.decode("#E9EDEF"));
    }

    /**
     * Inicialitza els botons i el titol de la part superior de la vista.
     */
    private void botonsSuperiors() {
        //Panell pel botó "Tancar Sessió"
        JPanel tancarSessioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tancarSessioPanel.add(tancarSessioButton);

        //Panell pel botó "Perfil"
        JPanel perfilPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        NomPerfil = new JLabel(ctrlPresentacio.getNomUsuariActiu());
        NomPerfil.setBounds(5, 0, 200, 20);
        perfilPanel.add(NomPerfil);
        perfilPanel.add(imagenPerfil);

        //Panell superior pels dos botons
        JPanel buttonsPanel = new JPanel(new BorderLayout());
        buttonsPanel.add(tancarSessioPanel, BorderLayout.WEST); // Botó "Tancar sessió" en la cantonada superior esquerra
        buttonsPanel.add(perfilPanel, BorderLayout.EAST); // Botó "Perfil" en la cantonada superior dreta

        //Panell pel títol
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Label "Titol" centrat horizontalment
        Font font = new Font("Arial", Font.BOLD, 20);
        titolBenvinguda.setFont(font);
        titlePanel.add(titolBenvinguda);

        //Panell superior pel títol i els botons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonsPanel, BorderLayout.NORTH); // Panel de botons en la part superior
        topPanel.add(titlePanel, BorderLayout.CENTER); // Títol centrat horizontalmente sota dels botons

        tancarSessioPanel.setBackground(Color.decode("#E9EDEF"));
        titlePanel.setBackground(Color.decode("#E9EDEF"));
        buttonsPanel.setBackground(Color.decode("#E9EDEF"));
        perfilPanel.setBackground(Color.decode("#E9EDEF"));
        topPanel.setBackground(Color.decode("#E9EDEF"));

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Assigna els listeners als components corresponents.
     */
    public void assignar_listenersComponents() {
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
        jugarPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonJugarPartida(e);
            }
        });
        crearKenKenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonCrearKenken(e);
            }
        });
        veureRanquingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonVeureRanquing(e);
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
     * Es tanca la sessió i es canvia a la vista principal: VistaPantallaPrincipal.
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
     * Es canvia a la vista: VistaPerfil.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonPerfil(ActionEvent e) {
        ctrlPresentacio.vistaPerfil();
        setVisible(false);
    }

    /**
     * Es canvia a la vista: VistaConfiguracióPartida.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonJugarPartida(ActionEvent e) {
        ctrlPresentacio.vistaTipusPartida();
        setVisible(false);
    }

    /**
     * Es canvia a la vista: VistaCrearKenken.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonCrearKenken(ActionEvent e) {
        ctrlPresentacio.vistaConfigCreacio();
        setVisible(false);
    }

    /**
     * Es canvia a la vista: VistaVeureRanquing.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonVeureRanquing(ActionEvent e) {
        ctrlPresentacio.vistaRanquing();
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


