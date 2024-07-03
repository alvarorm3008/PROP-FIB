package presentacio;

import excepcions.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

/**
 * Classe que representa una vista específica de la presentació per a jugar una partida al Kenken.
 * Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
 */
public class VistaJugarPartida extends JFrame {
    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;

    /**
     * Tamany del tauler
     */
    private int tam;

    /**
     * Matriu del Kenken que s'està creant.
     */
    private int[][] mat;

    /**
     * Llista de les operacions de cada regió.
     */
    private String[] opsRegions;

    /**
     * Llista dels resultats de cada regió.
     */
    private Integer[] resRegions;

    /**
     * Vector dels valors de les caselles.
     */
    private int[] valCaselles;

    /**
     * Panell de continguts de la vista de configuracio de la creació de Kenken.
     */
    private JPanel mainPanel = new JPanel();

    /**
     * Tauler del kenken que s'està creant.
     */
    private JPanel board;

    /**
     * Matriu d'etiquetes pel tauler
     */
    private JLabel[][] celdas;

    /**
     * Posició del tauler seleccionada.
     */
    private Pair valSelect;

    /**
     * Numero de segons transcorreguts a la partida.
     */
    private int segons;
    /**
     * Boooleà que indica l'estat de la partida (pausada o no).
     */
    private boolean pausada;
    /**
     * Timer que simularà el contador de temps de la partida.
     */
    private Timer timer;
    /**
     * Botó per tornar a la pàgina anterior.
     */
    private JButton enrereButton = new JButton("Tornar enrere");

    /**
     * Combo box per a seleccionar el valor de la casella.
     */
    private JComboBox seleccioValor = new JComboBox<>();

    /**
     * Botó per a que el sistema et retorna el valor correcte de la casella seleccionada.
     */
    private JButton pistaButton = new JButton("Revela");

    /**
     * Botó per comprovar si els valors col·locats actualment son correctes per resoldre el kenken.
     */
    private JButton comprovaButton = new JButton("Check");

    /**
     * Botó per mostrar la solucio del Kenken.
     */
    private JButton solucioButton = new JButton("Solució");

    /**
     * Botó per pausar la partida.
     */
    private JLabel imagenPausa = new JLabel();

    /**
     * Botó per guardar la partida.
     */
    private JButton guardarButton = new JButton("Guardar");

    /**
     * Botó per afegir el valor a la casella.
     */
    private JButton afegirValButton = new JButton("Set valor");

    /**
     * Botó per eliminar el valor de la casella seleccionada.
     */
    private JButton eliminaValButton = new JButton("Borra el valor");

    /**
     * Botó per eliminar tots els valors de les caselles.
     */
    private JButton eliminaTotButton = new JButton("Esborra tot");

    /**
     * Botó per finalitzar partida.
     */
    private JButton finalitzaButton = new JButton("Finalitza");

    /**
     * Botó per consultar les normes del joc.
     */
    private JButton normesButton = new JButton("Normes del joc");

    /**
     * Etiqueta del titol de la pantalla.
     */
    private JLabel labelPistes = new JLabel();

    /**
     * Etiqueta que indica el temps transcorregut a la partida.
     */
    private JLabel labelTemps = new JLabel();

    /**
     * Etiqueta que indica la selecció de valors.
     */
    private JLabel labelSelectVal = new JLabel("Selecciona el valor:");

    /**
     * Constructora de la vista.
     */
    public VistaJugarPartida(CtrlPresentacio ctrlPresentacio, int[][] mat, String[] ops, Integer[] res, int[] valCas, int segons) {
        ferVisible();
        this.ctrlPresentacio = ctrlPresentacio;
        this.mat = mat;
        tam = mat.length;
        opsRegions = ops;
        resRegions = res;
        valCaselles = valCas;
        this.segons = segons;
        this.timer = null;
        this.pausada = true;
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
        tempsCorrents();
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
        setTitle("Pantalla Jugar Partida");
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
        celdas = new JLabel[tam][tam];
        ArrayList<Boolean> regio = new ArrayList<>();
        for (int i=0; i < opsRegions.length; i++) regio.add(false);
        int z = 0;
        // Recorremos la matriz para inicializar cada celda del tablero
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                JLabel celda = new JLabel(); // Creamos una nueva celda (JLabel)

                // Configuramos el tamaño de la celda
                celda.setPreferredSize(new Dimension(tamCasella, tamCasella));
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
                    if (ctrlPresentacio.casellaBloquejada(i+1, j+1)) {
                        celdas[i][j].setForeground(Color.decode("#80E084"));
                    }
                }
                ++z;
                celda.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int x = -1;
                        int y = -1;
                        // Buscar la celda en la matriz de celdas para obtener sus coordenadas en la matriz mat
                        for (int i = 0; i < tam; i++) {
                            for (int j = 0; j < tam; j++) {
                                if (celdas[i][j] == celda) {
                                    x = i;
                                    y = j;
                                }
                            }
                            if (x != -1 && y != -1) {
                                break;
                            }
                        }

                        if (x != -1 && y != -1) {
                            valSelect = new Pair(x, y);
                            if (celdas[x][y].getBackground() == Color.LIGHT_GRAY) { //Deselecciono casella
                                celda.setBackground(Color.white);
                                valSelect = null;
                            }
                            else {
                                // Deselecciono totes les caselles
                                for (int i = 0; i < tam; i++) {
                                    for (int j = 0; j < tam; j++) {
                                        celdas[i][j].setBackground(Color.white);
                                    }
                                }
                                // Selecciono la casella actual
                                celda.setBackground(Color.LIGHT_GRAY);
                                valSelect = new Pair(x, y);
                            }
                        }
                    }
                });
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
     * @return Matriu transposada.
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
        GridBagConstraints constraints = new GridBagConstraints();

        //Configura l'aparença dels components
        configuraAparencia();

        // Inicialitza els botons de la part superior de la pantalla
        botonsSuperiors();

        // Inicialitza el tauler
        board = new JPanel();
        Dimension boardSize = new Dimension(400, 400);
        board.setPreferredSize(boardSize); // Tamaño preferido del tablero
        board.setMinimumSize(boardSize);   // Tamaño mínimo del tablero
        board.setMaximumSize(boardSize);   // Tamaño máximo del tablero

        int pistes = 3 - ctrlPresentacio.getPistes();
        labelPistes.setText("Pistes: " + pistes + "/3");

        labelPistes.setFont(labelTemps.getFont().deriveFont(20.0f));

        JPanel buttonsEsquerra = new JPanel(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(10, 10, 110, 10);

        buttonsEsquerra.add(labelPistes, constraints);
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridy = 1;
        buttonsEsquerra.add(pistaButton, constraints);
        constraints.gridy = 2;
        buttonsEsquerra.add(comprovaButton, constraints);
        constraints.gridy = 3;
        buttonsEsquerra.add(solucioButton, constraints);

        //Inicialitza el tauler
        board = new JPanel();

        Integer[] vals = IntStream.rangeClosed(1, tam).boxed().toArray(Integer[]::new);
        for (Integer val : vals) {
            seleccioValor.addItem(val);
        }
        labelTemps.setFont(labelTemps.getFont().deriveFont(24.0f));

        JPanel buttonsDreta = new JPanel(new GridBagLayout());
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(10, 10, 40, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonsDreta.add(labelTemps, constraints);
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 1;
        constraints.gridy = 0;
        buttonsDreta.add(imagenPausa, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonsDreta.add(labelSelectVal, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        buttonsDreta.add(seleccioValor, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        buttonsDreta.add(afegirValButton, constraints);
        constraints.insets = new Insets(10, 10, 20, 10);

        constraints.gridx = 0;
        constraints.gridy = 3;
        buttonsDreta.add(eliminaValButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        buttonsDreta.add(eliminaTotButton, constraints);

        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 5;
        buttonsDreta.add(guardarButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 6;
        buttonsDreta.add(finalitzaButton, constraints);

        //Panell principal
        mainPanel.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(buttonsEsquerra, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        mainPanel.add(board, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        mainPanel.add(buttonsDreta, constraints);

        buttonsDreta.setBackground(Color.decode("#E9EDEF"));
        buttonsEsquerra.setBackground(Color.decode("#E9EDEF"));

        add(mainPanel, BorderLayout.CENTER);

        board.setPreferredSize(new Dimension(400, 400));

        // Tooltips
        seleccioValor.setToolTipText("Selecciones un valor per la casella marcada");
        afegirValButton.setToolTipText("La casella marcada tindrà el valor indicat");
        eliminaValButton.setToolTipText("S'elimina el valor de la casella indicada");
        imagenPausa.setToolTipText("Es pausa la partida");
        pistaButton.setToolTipText("Es coloca automaticament el valor d'una casella");
        comprovaButton.setToolTipText("Es comprova si el valor introduit a la casella seleccionada es correcte");
        solucioButton.setToolTipText("Es mostra la solució del kenken");
        enrereButton.setToolTipText("Tornes a la pantalla anterior");
    }

    /**
     * Inicialitza els botons i el titol de la part superior de la vista.
     */
    private void botonsSuperiors() {
        //Panell pel botó "Enrere"
        JPanel enrerePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enrerePanel.add(enrereButton);

        //Panell pel botó "Normes"
        JPanel normesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        normesPanel.add(normesButton);

        //Panell superior pels dos botons
        JPanel buttonsPanel = new JPanel(new BorderLayout());
        buttonsPanel.add(enrerePanel, BorderLayout.WEST); // Botó "Enrere" en la cantonada superior esquerra
        buttonsPanel.add(normesPanel, BorderLayout.EAST); // Botó "Normes" en la cantonada superior dreta

        //Panell superior pel títol i els botons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonsPanel, BorderLayout.NORTH); // Panel de botons en la part superior

        enrerePanel.setBackground(Color.decode("#E9EDEF"));
        topPanel.setBackground(Color.decode("#E9EDEF"));
        buttonsPanel.setBackground(Color.decode("#E9EDEF"));

        add(topPanel, BorderLayout.NORTH);
    }

    private void configuraAparencia() {
        int maxLabelWidth = 40;
        int maxLabelHeight = 35;

        // Inicializar iconos de pausa y reanudar
        ImageIcon iconoPausa = new ImageIcon("FONTS/java/presentacio/Icons/Pausa.png");
        Image imagenP = iconoPausa.getImage().getScaledInstance(maxLabelWidth, maxLabelHeight, Image.SCALE_SMOOTH);
        ImageIcon iconoPausaEscalado = new ImageIcon(imagenP);

        ImageIcon iconoReanuda = new ImageIcon("FONTS/java/presentacio/Icons/Reanudar.png");
        Image imagenR = iconoReanuda.getImage().getScaledInstance(maxLabelWidth, maxLabelHeight, Image.SCALE_SMOOTH);
        ImageIcon iconoReanudaEscalado = new ImageIcon(imagenR);

        // Crear el JLabel con el ImageIcon escalado de pausa inicialmente
        imagenPausa = new JLabel(iconoPausaEscalado);

        // Agregar el MouseListener para detectar clics en la imagen
        imagenPausa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (imagenPausa.isEnabled()) {
                    pausaTemps();
                    if (pausada) {
                        imagenPausa.setIcon(iconoReanudaEscalado);
                        habilitarBotons(false); // Deshabilitar botones
                        imagenPausa.setEnabled(true); // Asegurar que el botón de "Pausa" esté habilitado
                        enrereButton.setEnabled(true); // Asegurar que el botón de "Tornar enrere" esté habilitado
                    } else {
                        imagenPausa.setIcon(iconoPausaEscalado);
                        habilitarBotons(true); // Habilitar todos los botones
                    }
                }
            }
        });

        // Aquí debes añadir imagenPausaLabel al panel adecuado de tu interfaz
        // mainPanel.add(imagenPausaLabel); // Ejemplo

        // BOTONES
        Dimension buttonSize = new Dimension(120, 40);
        afegirValButton.setPreferredSize(buttonSize);
        eliminaValButton.setPreferredSize(buttonSize);
        guardarButton.setPreferredSize(buttonSize);
        finalitzaButton.setPreferredSize(buttonSize);
        pistaButton.setPreferredSize(buttonSize);
        comprovaButton.setPreferredSize(buttonSize);
        solucioButton.setPreferredSize(buttonSize);
        eliminaTotButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 30);
        enrereButton.setPreferredSize(buttonSize);
        normesButton.setPreferredSize(buttonSize);

        // TEXTOS
        Font font = new Font("Arial", Font.BOLD, 20);
        labelPistes.setFont(font);
        labelTemps.setFont(font);

        font = new Font("Arial", Font.PLAIN, 14);
        labelSelectVal.setFont(font);
        seleccioValor.setFont(font);

        // Colores botones
        seleccioValor.setBackground(Color.decode("#005A9E"));
        seleccioValor.setForeground(Color.decode("#F5F5F5"));
        afegirValButton.setBackground(Color.decode("#005A9E"));
        afegirValButton.setForeground(Color.decode("#F5F5F5"));
        eliminaValButton.setBackground(Color.decode("#E57373"));
        eliminaValButton.setForeground(Color.decode("#333333"));
        eliminaTotButton.setBackground(Color.decode("#E57373"));
        eliminaTotButton.setForeground(Color.decode("#333333"));
        guardarButton.setBackground(Color.decode("#5BC0BE"));
        guardarButton.setForeground(Color.decode("#333333"));
        finalitzaButton.setBackground(Color.decode("#5BC0BE"));
        finalitzaButton.setForeground(Color.decode("#333333"));
        pistaButton.setBackground(Color.decode("#5BC0BE"));
        pistaButton.setForeground(Color.decode("#333333"));
        comprovaButton.setBackground(Color.decode("#5BC0BE"));
        comprovaButton.setForeground(Color.decode("#333333"));
        solucioButton.setBackground(Color.decode("#5BC0BE"));
        solucioButton.setForeground(Color.decode("#333333"));
        enrereButton.setBackground(Color.decode("#5BC0BE"));
        enrereButton.setForeground(Color.decode("#333333"));
        normesButton.setBackground(Color.decode("#5BC0BE"));
        normesButton.setForeground(Color.decode("#333333"));

        mainPanel.setBackground(Color.decode("#E9EDEF"));
    }

    /**
     * Mètode que inicia un timer que incrementa en 1 la variable segons cada cop que pasa un segon.
     * @throws IllegalStateException si el timer ja estava actiu.
     */
    public void tempsCorrents () throws IllegalStateException {
        if (pausada) {
            pausada = false;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    segons++;
                    transformarMinutsIHores();
                }
            }, 0000, 1000); //comença despres d'1 sec i repeteix cada 1 sec.
        }
        else {
            throw new IllegalStateException("El temps ja està corrents.");
        }
    }

    /**
     * Mètode que transforma els segons a hores, minuts i segons
     */
    public void transformarMinutsIHores() {
        int hores = segons / 3600;
        int minuts = (segons % 3600) / 60;
        int segonsRestants = segons % 60;
        labelTemps.setText(String.format("%02d:%02d:%02d", hores, minuts, segonsRestants));
    }

    /**
     * Mètode que s'encarrega de pausar el timer i de deixar d'incrementar els segons.
     * @throws IllegalStateException si la partida ja estava pausada
     */
    public void pausaTemps () {
        if (timer != null && !pausada) {
            pausada = true;
            timer.cancel();
            timer = null;
            transformarMinutsIHores();
        }
        else {
            tempsCorrents();
        }
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
        afegirValButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonAfegirValor(e);
            }
        });
        eliminaValButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonEliminarValor(e);
            }
        });
        eliminaTotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonEliminarTot(e);
            }
        });
        pistaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonPista(e);
            }
        });
        comprovaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonComprova(e);
            }
        });
        solucioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonSolucio(e);
            }
        });
        finalitzaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                try {
                    actionPerformed_buttonFinalitza(e);
                } catch (ExcepcioNoTePartida ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
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
                } catch (ExcepcioNoTePartida ex) {
                    ex.printStackTrace();
                }
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
     * Es canvia a la vista anterior: VistaConfigCreacioKK.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEnrere(ActionEvent e) {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Segur que vols tornar enrere? Si no has guardat es perdran tots els canvis realitzats.", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            ctrlPresentacio.sortirPartida();
            ctrlPresentacio.vistaOpcions();
            setVisible(false);
        }
    }

    /**
     * Afegeix el valor indicat a la casella seleccionada.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonAfegirValor(ActionEvent e) {
        if (valSelect == null) {
            ctrlPresentacio.mostraAvis("No estas seleccionant cap casella");
            return;
        }

        int x = valSelect.getFirst();
        int y = valSelect.getSecond();

        if (celdas[x][y].getForeground().equals(Color.decode("#80E084"))) {
            ctrlPresentacio.mostraAvis("No pots col·locar valors a una casella ja fixada");
            return;
        }

        celdas[x][y].setText("");

        Integer intVal = (Integer) seleccioValor.getSelectedItem();
        valCaselles[x*tam + y] = intVal;

        String val = intVal.toString(); // Convertir el valor a String
        celdas[x][y].setText(val);

        Font font = celdas[x][y].getFont();
        int fontTam;
        if (tam > 7) fontTam = 14;
        else if (tam < 5) fontTam = 30;
        else fontTam = 25;

        celdas[x][y].setFont(new Font(font.getName(), font.getStyle(), fontTam));
        celdas[x][y].setHorizontalAlignment(JLabel.CENTER);
        celdas[x][y].setVerticalAlignment(JLabel.CENTER);
    }

    /**
     * Elimina el valor de la casella seleccionada.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEliminarValor(ActionEvent e) {
        if (valSelect == null) {
            ctrlPresentacio.mostraAvis("No estas seleccionant cap casella");
            return;
        }
        int x = valSelect.getFirst();
        int y = valSelect.getSecond();
        if (celdas[x][y].getForeground().equals(Color.decode("#80E084"))) {
            ctrlPresentacio.mostraAvis("No pots esborrar un valor ja fixat");
            return;
        }
        valCaselles[x*tam + y] = -1;
        celdas[x][y].setText("");
    }

    /**
     * Elimina tots els valors col·locats a les caselles.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEliminarTot(ActionEvent e) {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Segur que vols esborrar tots els valors?", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            for (int i = 0; i < tam; ++i) {
                for (int j = 0; j < tam; ++j) {
                    if (!celdas[i][j].getForeground().equals(Color.decode("#80E084"))) {
                        celdas[i][j].setText("");
                        valCaselles[i*tam + j] = -1;
                    }
                }
            }
        }
    }

    /**
     * Es mostra el valor d'una casella random.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonPista(ActionEvent e) {
        if(ctrlPresentacio.getPistes() == 0) {
            int confirmed = JOptionPane.showConfirmDialog(null,
                    "Segur que vols utilitzar la pista? La partida quedarà fora del rànquing.", "Avís",
                    JOptionPane.YES_NO_OPTION);

            if (confirmed == JOptionPane.YES_OPTION) {
                if (valSelect == null) {
                    ctrlPresentacio.mostraAvis("No estas seleccionant cap casella");
                    return;
                }
                int x = valSelect.getFirst();
                int y = valSelect.getSecond();
                String textValorCasella = celdas[x][y].getText();

                if (textValorCasella != "") {
                    ctrlPresentacio.mostraAvis("Aquesta casella ja té un valor");
                    return;
                }

                int valResolt = 0;
                try {
                    valResolt = ctrlPresentacio.resolCasella(valCaselles, valorsBloquejats(), x+1, y+1);
                } catch (ExcepcioTamany ex) {
                    ex.printStackTrace();
                } catch (ExcepcioPistesMax ex) {
                    ctrlPresentacio.mostraAvis("Ja has utilitzat totes les pistes possibles.");
                    return;
                } catch (ExcepcioNoTeSolucio ex) {
                }
                if (valResolt == -1) {
                    ctrlPresentacio.mostraAvis("No s'ha pogut resoldre la casella. Revisa els valors col·locats");
                    return;
                }
                int pistes = 3 - ctrlPresentacio.getPistes();
                labelPistes.setText("Pistes: " + pistes + "/3");
                valCaselles[x*tam + y] = valResolt;
                celdas[x][y].setText(Integer.toString(valResolt));
                Font font = celdas[x][y].getFont();
                int fontTam;
                if (tam > 7) fontTam = 14;
                else if (tam < 5) fontTam = 30;
                else fontTam = 25;
                celdas[x][y].setFont(new Font(font.getName(), font.getStyle(), fontTam));
                celdas[x][y].setHorizontalAlignment(JLabel.CENTER);
                celdas[x][y].setVerticalAlignment(JLabel.CENTER);
                celdas[x][y].setForeground(Color.decode("#80E084"));
            }
        }
        else {
            if (valSelect == null) {
                ctrlPresentacio.mostraAvis("No estas seleccionant cap casella");
                return;
            }
            int x = valSelect.getFirst();
            int y = valSelect.getSecond();
            String textValorCasella = celdas[x][y].getText();

            if (textValorCasella != "") {
                ctrlPresentacio.mostraAvis("Aquesta casella ja té un valor");
                return;
            }

            int valResolt = 0;
            try {
                valResolt = ctrlPresentacio.resolCasella(valCaselles, valorsBloquejats(), x+1, y+1);
            } catch (ExcepcioTamany ex) {
                ex.printStackTrace();
            } catch (ExcepcioPistesMax ex) {
                ctrlPresentacio.mostraAvis("Ja has utilitzat totes les pistes possibles.");
                return;
            } catch (ExcepcioNoTeSolucio ex) {}
            if (valResolt == -1) {
                ctrlPresentacio.mostraAvis("No s'ha pogut resoldre la casella. Revisa els valors col·locats");
                return;
            }
            int pistes = 3 - ctrlPresentacio.getPistes();
            labelPistes.setText("Pistes: " + pistes + "/3");
            valCaselles[x*tam + y] = valResolt;
            celdas[x][y].setText(Integer.toString(valResolt));
            Font font = celdas[x][y].getFont();
            int fontTam;
            if (tam > 7) fontTam = 14;
            else if (tam < 5) fontTam = 30;
            else fontTam = 25;
            celdas[x][y].setFont(new Font(font.getName(), font.getStyle(), fontTam));
            celdas[x][y].setHorizontalAlignment(JLabel.CENTER);
            celdas[x][y].setVerticalAlignment(JLabel.CENTER);
            celdas[x][y].setForeground(Color.decode("#80E084"));
        }
    }

    /**
     * Marca si el valor seleccionat es correcte.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonComprova(ActionEvent e) {
        if(ctrlPresentacio.getPistes() == 0) {
            int confirmed = JOptionPane.showConfirmDialog(null,
                    "Segur que vols utilitzar la pista? La partida quedarà fora del rànquing.", "Avís",
                    JOptionPane.YES_NO_OPTION);

            if (confirmed == JOptionPane.YES_OPTION) {
                if (!hihaValorsIntroduits()) {
                    ctrlPresentacio.mostraAvis("No hi ha cap valor introduit al Kenken.");
                    return;
                }
                try {
                    ctrlPresentacio.demanaPista();
                } catch (ExcepcioPistesMax ex) {
                    ctrlPresentacio.mostraAvis("Ja has utilitzat totes les pistes possibles.");
                    return;
                }

                int pistes = 3 - ctrlPresentacio.getPistes();
                labelPistes.setText("Pistes: " + pistes + "/3");
                ctrlPresentacio.actualitzaValorsBloquejats(valCaselles);
                if (ctrlPresentacio.validaSolucio()) {
                    if (ctrlPresentacio.resoldreKenken()) {
                        ctrlPresentacio.mostraAvis("Els valors col·locats son correctes!");
                    }
                    else ctrlPresentacio.mostraAvis("Els valors col·locats no son correctes. Revisa'ls!");
                } else {
                    ctrlPresentacio.mostraAvis("Els valors col·locats no son correctes. Revisa'ls!");
                }
                ctrlPresentacio.eliminaValorsCreacio(valCaselles);
            }
        }
        else {
            if (!hihaValorsIntroduits()) {
                ctrlPresentacio.mostraAvis("No hi ha cap valor introduit al Kenken.");
                return;
            }
            try {
                ctrlPresentacio.demanaPista();
            } catch (ExcepcioPistesMax ex) {
                ctrlPresentacio.mostraAvis("Ja has utilitzat totes les pistes possibles.");
                return;
            }

            int pistes = 3 - ctrlPresentacio.getPistes();
            labelPistes.setText("Pistes: " + pistes + "/3");
            ctrlPresentacio.actualitzaValorsBloquejats(valCaselles);
            if (ctrlPresentacio.validaSolucio()) {
                if (ctrlPresentacio.resoldreKenken()) {
                    ctrlPresentacio.mostraAvis("Els valors col·locats son correctes!");
                }
                else ctrlPresentacio.mostraAvis("Els valors col·locats no son correctes. Revisa'ls!");
            } else {
                ctrlPresentacio.mostraAvis("Els valors col·locats no son correctes. Revisa'ls!");
            }
            ctrlPresentacio.eliminaValorsCreacio(valCaselles);
        }
    }

    /**
     * Es mostra la solució del Kenken.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonSolucio(ActionEvent e) {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Segur que vols veure la solució? La partida finalitzarà.", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            pausaTemps();
            habilitarBotons(false); // Deshabilitar botones
            enrereButton.setEnabled(true); // Asegurar que el botón de "Tornar enrere" esté habilitado

            if (!ctrlPresentacio.resoldreKenken()) {
                ctrlPresentacio.mostraAvis("No s'ha pogut resoldre el kenken.");
                return;
            }

            int[] solucioValors = ctrlPresentacio.mostrarSolucio();

            for (int i = 0; i < solucioValors.length; ++i) {
                valCaselles[i] = solucioValors[i];

            }
            for (int i = 0; i < tam; ++i) {
                for (int j = 0; j < tam; ++j) {
                    String val = String.valueOf(valCaselles[i*tam + j]); // Convertir el valor a String
                    celdas[i][j].setText(val);
                    Font font = celdas[i][j].getFont();
                    int fontTam;
                    if (tam > 7) fontTam = 14;
                    else if (tam < 5) fontTam = 30;
                    else fontTam = 25;
                    celdas[i][j].setFont(new Font(font.getName(), font.getStyle(), fontTam));
                    celdas[i][j].setHorizontalAlignment(JLabel.CENTER);
                    celdas[i][j].setVerticalAlignment(JLabel.CENTER);
                }
            }
        }
    }

    /**
     * Acaba la partida si el kenken és correcte.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonFinalitza(ActionEvent e) throws ExcepcioNoTePartida, IOException {
        pausaTemps();
        ctrlPresentacio.actualitzaValorsFixats(valCaselles);
        if (!ctrlPresentacio.finalitzarPartida(segons)) {
            ctrlPresentacio.mostraAvis("El Kenken no és correcte!");
            ctrlPresentacio.eliminaValorsPartida(valCaselles);
            tempsCorrents();
            return;
        }
        ctrlPresentacio.mostraAvis("Felicitats! Has resolt el Kenken");
        ctrlPresentacio.vistaOpcions();
        setVisible(false);
    }

    /**
     * Guarda la partida si aquesta está pausada.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonGuardar(ActionEvent e) throws ExcepcioNoTePartida {
        pausaTemps();
        if (!ctrlPresentacio.partidaGuardada()) { //no hay nada guardado
            int confirmed = JOptionPane.showConfirmDialog(null,
                    "Segur que vols guardar? Sortiràs de la partida.", "Avís",
                    JOptionPane.YES_NO_OPTION);

            if (confirmed == JOptionPane.YES_OPTION) {
                ctrlPresentacio.actualitzaValorsFixats(valCaselles);
                ctrlPresentacio.actualitzaValorsBloquejats(valorsBloquejats());
                ctrlPresentacio.actualitzaTemps(segons);
                ctrlPresentacio.guardarPartida();
                ctrlPresentacio.sortirPartida();
                ctrlPresentacio.vistaOpcions();
                setVisible(false);
            } else {
                tempsCorrents();
            }
        }
        else {
            int confirmed = JOptionPane.showConfirmDialog(null,
                    "Segur que vols guardar? Sobreescriuras la partida que tens guardada.", "Avís",
                    JOptionPane.YES_NO_OPTION);
            if (confirmed == JOptionPane.YES_OPTION) {
                ctrlPresentacio.actualitzaValorsFixats(valCaselles);
                ctrlPresentacio.actualitzaValorsBloquejats(valorsBloquejats());
                ctrlPresentacio.actualitzaTemps(segons);
                ctrlPresentacio.guardarPartida();
                ctrlPresentacio.sortirPartida();
                ctrlPresentacio.vistaOpcions();
                setVisible(false);
            } else {
                tempsCorrents();
            }
        }
    }

    /**
     * Retorna els valors de les caselles bloquejades.
     * @return Un array d'enters amb els valors de les caselles bloquejades.
     */
    public int[] valorsBloquejats() {
        int[] valsBloquejats = new int[tam*tam];
        for (int i = 0; i < tam*tam; ++i) {
            valsBloquejats[i] = -1;
        }
        for (int i = 0; i < tam; ++i) {
            for (int j = 0; j < tam; ++j) {
                if (celdas[i][j].getForeground().equals(Color.decode("#80E084"))) {
                    valsBloquejats[i*tam +j] = valCaselles[i*tam +j];
                }
            }
        }
        return valsBloquejats;
    }

    /**
     * Comprova si hi ha valors introduits al Kenken.
     * @return True si hi ha valors introduits, false altrament.
     */
    public boolean hihaValorsIntroduits() {
        for (int i = 0; i < tam; ++i) {
            for (int j = 0; j < tam; ++j) {
                if (!celdas[i][j].getText().isEmpty() && !celdas[i][j].getForeground().equals(Color.decode("#80E084"))) { 
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Es canvia a la vista: VistaNormes.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonNormes(ActionEvent e) {
        ctrlPresentacio.vistaNormes();
    }

    /**
     * Habilita o deshabilita els botons de la vista.
     * @param habilitar True si es volen habilitar, false altrament.
     */
    private void habilitarBotons(boolean habilitar) {
        imagenPausa.setEnabled(habilitar);
        pistaButton.setEnabled(habilitar);
        comprovaButton.setEnabled(habilitar);
        solucioButton.setEnabled(habilitar);
        guardarButton.setEnabled(habilitar);
        afegirValButton.setEnabled(habilitar);
        eliminaValButton.setEnabled(habilitar);
        finalitzaButton.setEnabled(habilitar);
        seleccioValor.setEnabled(habilitar);
        eliminaTotButton.setEnabled(habilitar);
    }
}


