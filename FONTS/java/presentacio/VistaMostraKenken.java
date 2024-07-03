package presentacio;

import excepcions.ExcepcioNoHiHaSessio;
import excepcions.ExcepcioTamany;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe que representa una vista específica de la presentació per a mostrar un Kenken.
 * Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
 */
public class VistaMostraKenken extends JFrame {
    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;

    /**
     * Matriu del kenken que es vol mostrar.
     */
    private int[][] mat;

    /**
     * Tamany del kenken.
     */
    private int tam;

    /**
     * Booleà que indica si s'ha guardat el kenken.
     */
    private boolean guardat;

    /**
     * Llista de les operacions de cada regió.
     */
    private String[] opsRegions;

    /**
     * Llista dels resultats de cada regió.
     */
    private Integer[] resRegions;

    /**
     * Vector amb el valor de cada casella.
     */
    private int[] valCaselles;

    /**
     * Panell de continguts de la vista de configuracio de la creació de Kenken.
     */
    private JPanel mainPanel = new JPanel();

    /**
     * Tauler del Kenken.
     */
    private JPanel board;

    /**
     * Botó per tornar a la pàgina anterior.
     */
    private JButton enrereButton = new JButton("Tornar enrere");

    /**
     * Botó per jugar una partida amb el kenken mostrat.
     */
    private JButton jugarButton = new JButton("Jugar partida");
    /**
     * Botó per guardar el Kenken creat.
     */
    private JButton guardarButton = new JButton("Guardar Kenken");

    /**
     * Botó per no guardar el Kenken creat.
     */
    private JButton noGuardarButton = new JButton("No guardar");

    /**
     * Etiqueta del titol de la pantalla.
     */
    private JLabel titolMostraKenken = new JLabel("VISTA DEL KENKEN");

    /**
     * Constructora de la vista.
     */
    public VistaMostraKenken (CtrlPresentacio ctrlPresentacio, int[][] mat, String[] ops, Integer[] res, int[] valCas) {
        ferVisible();
        this.ctrlPresentacio = ctrlPresentacio;
        this.mat = mat;
        tam = mat.length;
        opsRegions = ops;
        resRegions = res;
        valCaselles = valCas;
        guardat = false;
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
        iniMatriu();
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
        setTitle("Pantalla Mostra Kenken");
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
     * Inicialitza la representació del tauler del Kenken a la vista.
     */
    private void iniMatriu() {
        int tamCasella = 800 / tam; // Tamaño de cada celda del tablero (POR MIRAR)

        // Creamos una matriz de JLabels para almacenar las celdas del tablero
        JLabel[][] celdas = new JLabel[tam][tam];
        ArrayList<Boolean> regio = new ArrayList<>();
        for (int i=0; i < opsRegions.length; i++) regio.add(false);
        int z = 0;
        // Recorremos la matriz para inicializar cada celda del tablero
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                JLabel celda = new JLabel(); // Creamos una nueva celda (JLabel)

                // Configuramos el tamaño de la celda
                celda.setOpaque(true);
                celda.setBackground(Color.white);
                celda.setVisible(true);
                // Agregamos la celda al tablero y a la matriz de celdas
                board.add(celda);
                celdas[i][j] = celda;
                if (!regio.get(mat[i][j])) {
                    regio.set(mat[i][j], true);
                    String ops = opsRegions[mat[i][j]];
                    Integer res = resRegions[mat[i][j]];
                    JLabel lab = new JLabel();
                    Font font = lab.getFont();
                    int fontTam;
                    if (tam >= 7) fontTam = 10;
                    else if (tam < 5) fontTam = 20;
                    else fontTam = 15;
                    lab.setFont(new Font(font.getName(), font.getStyle(), fontTam));
                    lab.setText(res + " " + ops);
                    lab.setForeground(Color.decode("#005A9E"));

                    celda.add(lab);

                    int posx = 7; // Posición fija desde el borde izquierdo
                    int posy;
                    int tamLabel = tamCasella / 2; // Tamaño relativo a la celda

                    // Ajustamos la posición vertical en función del tamaño del tablero
                    if (tam > 7) {
                        posy = -12;
                    } else if (tam == 2) {
                        posy = -70;
                    } else if (tam < 5) {
                        posy = -35;
                    } else if (tam == 7) {
                        posy = -16;
                    } else {
                        posy = -22;
                    }

                    lab.setBounds(posx, posy, tamLabel, tamLabel);
                }
                int val = valCaselles[z];
                if (val == -1) celda.setText("");
                else {
                    Font font = celda.getFont();
                    int fontTam;
                    if (tam > 7) fontTam = 14;
                    else if (tam < 5) fontTam = 30;
                    else fontTam = 25;
                    celdas[i][j].setFont(new Font(font.getName(), font.getStyle(), fontTam));
                    celda.setText(Integer.toString(val));
                    celda.setHorizontalAlignment(JLabel.CENTER);
                    celda.setVerticalAlignment(JLabel.CENTER);
                }
                ++z;
            }
        }

        // Configuramos el layout del tablero (GridLayout)
        board.setLayout(new GridLayout(tam, tam));

        mat = transposaMat(mat);
        // Llamamos al método para configurar los bordes de las regiones
        frontissesRegions(celdas);
        mat = transposaMat(mat);
    }

    /**
     * Mètode que transposa la matriu passada per paràmetre.
     * @param mat Matriu a transposar.
     */
    private int[][] transposaMat (int[][] mat) {
        int[][] mat2 = new int[tam][tam];
        for (int i=0; i<tam; i++) {
            for (int j=0; j<tam; j++) {
                mat2[j][i] = mat[i][j];
            }
        }
        return mat2;
    }

    /**
     * Mètode que frontissa les regions del kenken.
     * @param celdas Matriu de JLabels que representa les caselles del kenken.
     */
    private void frontissesRegions(JLabel[][] celdas) {
        int n = tam - 1;
        for (int i = 0; i < tam; ++i) {
            for (int j = 0; j < tam; ++j) {
                int Me = mat[i][j];
                int u = 1, d = 1, l = 1, r = 1;

                // Lógica para determinar los bordes de cada celda
                if (i == 0) {
                    u+=2;
                    if (Me != mat[i+1][j]) d+=1;
                    if (j>0 && j<n) {
                        if (Me != mat[i][j+1]) {
                            r+=1;
                        }
                        if (Me != mat[i][j-1]) l+=1;
                    }
                }
                else if (i == n) {
                    d+=2;
                    if (Me != mat[i-1][j]) u+=1;
                    if (j>0 && j<n) {
                        if (Me != mat[i][j+1]) r+=1;
                        if (Me != mat[i][j-1]) l+=1;
                    }
                }
                if (j == 0) {
                    l+=2;
                    if (Me != mat[i][j+1]) r+=1;
                    if (i>0 && i<n) {
                        if (Me != mat[i+1][j]) d+=1;
                        if (Me != mat[i-1][j]) u+=1;
                    }
                }
                if (j == n) {
                    r+=2;
                    if (Me != mat[i][j-1]) l+=1;
                    if (i>0 && i<n) {
                        if (Me != mat[i+1][j]) d+=1;
                        if (Me != mat[i-1][j]) u+=1;
                    }
                }
                if (i!=0 && i!=n && j!=0 && j!=n) {
                    if (Me != mat[i+1][j]) d+=1;
                    if (Me != mat[i-1][j]) u+=1;
                    if (Me != mat[i][j+1]) r+=1;
                    if (Me != mat[i][j-1]) l+=1;
                }

                // Configuramos los bordes de la celda correspondiente
                JLabel celda = celdas[j][i];
                celda.setBorder(BorderFactory.createMatteBorder(l, u, r, d, Color.black));
            }
        }
    }

    /**
     * Inicialitza els botons de la vista.
     */
    private void iniBotons() {
        //Configura l'aparença dels components
        configuraAparencia();
        GridBagConstraints constraints = new GridBagConstraints();

        //Inicialitza els botons de la part superior de la pantalla
        botonsSuperiors();

        //Inicialitza el tauler
        board = new JPanel();

        //Inicialitza els botons per guardar o no guardar.
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 40, 20, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonsPanel.add(jugarButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonsPanel.add(guardarButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        buttonsPanel.add(noGuardarButton, constraints);

        //Panell principal
        mainPanel.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(board, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        mainPanel.add(buttonsPanel, constraints);

        buttonsPanel.setBackground(Color.decode("#E9EDEF"));

        add(mainPanel, BorderLayout.CENTER);

        board.setPreferredSize(new Dimension(400, 400));

        // Tooltips
        jugarButton.setToolTipText("");
        guardarButton.setToolTipText("Es guarda el kenken mostrat a la base de dades");
        noGuardarButton.setToolTipText("No es guarda el kenken mostrat i tornes a la pagina anterior");
        enrereButton.setToolTipText("Tornes a la pantalla configuració de creació de kenkens");
    }

    /**
     * Configura l'aparença dels components de la vista.
     */
    public void configuraAparencia() {
        //BOTONES
        Dimension buttonSize = new Dimension(200, 50);
        guardarButton.setPreferredSize(buttonSize);
        noGuardarButton.setPreferredSize(buttonSize);
        jugarButton.setPreferredSize(buttonSize);


        buttonSize = new Dimension(170, 30);
        enrereButton.setPreferredSize(buttonSize);

        //Colors botons
        jugarButton.setBackground(Color.decode("#005A9E"));
        jugarButton.setForeground(Color.decode("#F5F5F5"));
        guardarButton.setBackground(Color.decode("#5BC0BE"));
        guardarButton.setForeground(Color.decode("#333333"));
        noGuardarButton.setBackground(Color.decode("#E57373"));
        noGuardarButton.setForeground(Color.decode("#333333"));
        enrereButton.setBackground(Color.decode("#5BC0BE"));
        enrereButton.setForeground(Color.decode("#333333"));

        mainPanel.setBackground(Color.decode("#E9EDEF"));
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
        titolMostraKenken.setFont(font);
        titlePanel.add(titolMostraKenken);

        //Panell superior pels dos
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(enrerePanel);
        topPanel.add(titlePanel);

        enrerePanel.setBackground(Color.decode("#E9EDEF"));
        topPanel.setBackground(Color.decode("#E9EDEF"));
        titlePanel.setBackground(Color.decode("#E9EDEF"));

        add(topPanel, BorderLayout.NORTH);
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
                try {
                    actionPerformed_buttonEnrere(e);
                } catch (ExcepcioTamany ex) {
                    ex.printStackTrace();
                }
            }
        });
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                try {
                    actionPerformed_buttonGuardar(e);
                } catch (ExcepcioTamany ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        noGuardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonNoGuardar(e);
            }
        });
        jugarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonJugar(e);
            }
        });
    }

    /**
     * Es canvia a la vista anterior: VistaConfigCreacioKK.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEnrere(ActionEvent e) throws ExcepcioTamany {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Segur que vols tornar enrere? Si no has guardat el kenken, es perdran les dades.", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            ctrlPresentacio.vistaConfigCreacio();
            setVisible(false);
        }
    }
    /**
     * Es guarda el kenken mostrat a la vista a la base de dades del sistema.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonGuardar(ActionEvent e) throws ExcepcioTamany, IOException {
        if (guardat) {
            ctrlPresentacio.mostraAvis("Ja has guardat aquest Kenken.");
            return;
        }
        //comprovem si el kenken no té més de la meitat caselles col·locades i que
        //el numero de regions sigui major o igual a meitat de caselles del kenken i que
        //el numero de regions sigui menor al numero total de les caselles.
        if (!ctrlPresentacio.kenkenValidBD()) {
            ctrlPresentacio.mostraAvis("El kenken creat no és vàlid per guardar perquè té masses caselles resoltes");
            return;
        }
        ctrlPresentacio.guardarKenken();
        guardat = true;
        ctrlPresentacio.mostraAvis("S'ha guardat el kenken");
    }
    /**
     * No es guarda el kenken mostrat a la vista i torna a la vista anterior: VistaConfigCreacioKK.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonNoGuardar(ActionEvent e) {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Segur que no vols guardar?", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            ctrlPresentacio.vistaOpcions();
            setVisible(false);
        }
    }

    /**
     * No es guarda el kenken mostrat a la vista i torna a la vista anterior: VistaConfigCreacioKK.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonJugar(ActionEvent e) {
        if (!guardat) {
            int confirmed = JOptionPane.showConfirmDialog(null,
                    "Segur que inciar la partida sense guardar el kenken?", "Avís",
                    JOptionPane.YES_NO_OPTION);

            if (confirmed == JOptionPane.YES_OPTION) {
                //comprovem si el kenken no té més de la meitat caselles col·locades i que
                //el numero de regions sigui major o igual a meitat de caselles del kenken i que
                //el numero de regions sigui menor al numero total de les caselles.
                if (!ctrlPresentacio.kenkenValidBD()) {
                    ctrlPresentacio.mostraAvis("El kenken no és vàlid per ser jugat.");
                    return;
                }

                ctrlPresentacio.crearPartida();
                ctrlPresentacio.vistaJugarPartida(mat, opsRegions, resRegions, valCaselles, 0);
                setVisible(false);
            }
        }
        else {
            //comprovem si el kenken no té més de la meitat caselles col·locades i que
            //el numero de regions sigui major o igual a meitat de caselles del kenken i que
            //el numero de regions sigui menor al numero total de les caselles.
            if (!ctrlPresentacio.kenkenValidBD()) {
                ctrlPresentacio.mostraAvis("El kenken creat no és vàlid per jugar perquè té masses caselles resoltes");
                return;
            }
            ctrlPresentacio.crearPartida();
            ctrlPresentacio.vistaJugarPartida(mat, opsRegions, resRegions, valCaselles, 0);
            setVisible(false);
        }
    }
}

