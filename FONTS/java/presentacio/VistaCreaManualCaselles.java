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
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

/**
 * Classe que representa una vista específica de la presentació per a configurar les caselles del Kenken que vols crear.
 * Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
 */
public class VistaCreaManualCaselles extends JFrame {
    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;
    /**
     * Tamany del tauler.
     */
    private int tam;
    /**
     * Matriu del kenken que s'està creant.
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
     * Panell de continguts de la vista de configuracio de la creació de Kenken.
     */
    private JPanel mainPanel = new JPanel();
    /**
     * Tauler del kenken que s'està creant.
     */
    private JPanel board;
    /**
     * Matriu d'etiquetes pel tauler.
     */
    private JLabel[][] celdas;
    /**
     * Posició del tauler seleccionada.
     */
    private Pair valSelect;
    /**
     * Numero de regions.
     */
    private int numRegions;
    /**
     * Botó per tornar a la pàgina anterior.
     */
    private JButton enrereButton = new JButton("Tornar enrere");
    /**
     * Selecciona el valor de la casella.
     */
    private JComboBox seleccioValor = new JComboBox<>();
    /**
     * Botó per afegir el valor a la casella.
     */
    private JButton afegirValButton = new JButton("Col·loca el valor");
    /**
     * Botó per eliminar el valor de la casella.
     */
    private JButton eliminaValButton = new JButton("Borra el valor");
    /**
     * Botó per acabar la creació del kenken.
     */
    private JButton acabarButton = new JButton("Acabar creació");
    /**
     * Botó per esborrar totes les dades del kenken.
     */
    private JButton esborraButton = new JButton("Esborra tot");
    /**
     * Botó per consultar les normes del joc.
     */
    private JButton normesButton = new JButton("Normes del joc" );
    /**
     * Etiqueta del titol de la pantalla.
     */
    private JLabel titolCreaManual = new JLabel("CREACIÓ VALORS CASELLES D'UN KENKEN");
    /**
     * Etiqueta que indica la selecció de valors.
     */
    private JLabel labelSelectVal = new JLabel("Selecciona el valor:");
    /**
     * Constructora de la vista.
     */
    public VistaCreaManualCaselles(CtrlPresentacio ctrlPresentacio, int[][] mat, String[] ops, Integer[] res, int numRegions) {
        ferVisible();
        this.ctrlPresentacio = ctrlPresentacio;
        this.mat = mat;
        tam = mat.length;
        opsRegions = ops;
        resRegions = res;
        this.numRegions = numRegions;
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
     * Inicialitza els components de la vista.
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
        setTitle("Pantalla Creació Manual de Valors de Caselles d'un Kenken");
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
        valSelect = null;
        // Creamos una matriz de JLabels para almacenar las celdas del tablero
        celdas = new JLabel[tam][tam];
        ArrayList<Boolean> regio = new ArrayList<>();
        for (int i=0; i < opsRegions.length; i++) regio.add(false);

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

        //Inicialitza els botons de la part superior de la pantalla
        botonsSuperiors();

        //Inicialitza el tauler
        board = new JPanel();

        //Inicialitza els botons de casella
        JPanel buttonsCasella = new JPanel(new GridBagLayout());
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        Integer[] vals = IntStream.rangeClosed(1, tam).boxed().toArray(Integer[]::new);
        for (Integer val : vals) {
            seleccioValor.addItem(val);
        }

        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonsCasella.add(labelSelectVal,constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        buttonsCasella.add(seleccioValor,constraints);

        //Inicialitza els botons per guardar o esborrar el kenken creat.
        JPanel buttonsFinals = new JPanel(new GridBagLayout());
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonsFinals.add(afegirValButton,constraints);
        constraints.insets = new Insets(10, 10, 40, 10);
        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonsFinals.add(eliminaValButton,constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        buttonsFinals.add(esborraButton, constraints);
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 3;
        buttonsFinals.add(acabarButton, constraints);

        //Panell amb tots els botons de la dreta.
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 80, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonsPanel.add(buttonsCasella, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonsPanel.add(buttonsFinals, constraints);

        //Panell principal
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(board, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(buttonsPanel, gbc);

        board.setPreferredSize(new Dimension(400, 400));

        buttonsFinals.setBackground(Color.decode("#E9EDEF"));
        buttonsCasella.setBackground(Color.decode("#E9EDEF"));
        buttonsPanel.setBackground(Color.decode("#E9EDEF"));

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        seleccioValor.setToolTipText("Selecciones un valor per la casella marcada");
        afegirValButton.setToolTipText("La casella marcada tindrà el valor indicat");
        eliminaValButton.setToolTipText("S'elimina el valor de la casella indicada");
        acabarButton.setToolTipText("");
        esborraButton.setToolTipText("Es reseteja el tauler.");
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

        //Panell pel títol
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Label "Titol" centrat horizontalment
        Font font = new Font("Arial", Font.BOLD, 20);
        titolCreaManual.setFont(font);
        titlePanel.add(titolCreaManual);

        //Panell superior pel títol i els botons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonsPanel, BorderLayout.NORTH); // Panel de botons en la part superior
        topPanel.add(titlePanel, BorderLayout.CENTER); // Títol centrat horizontalment sota dels botons

        enrerePanel.setBackground(Color.decode("#E9EDEF"));
        topPanel.setBackground(Color.decode("#E9EDEF"));
        titlePanel.setBackground(Color.decode("#E9EDEF"));
        buttonsPanel.setBackground(Color.decode("#E9EDEF"));

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Método auxiliar para alternar el color de un botón entre dos colores
     */
    private void configuraAparencia() {

        //BOTONES
        Dimension buttonSize = new Dimension(200, 40);
        afegirValButton.setPreferredSize(buttonSize);
        eliminaValButton.setPreferredSize(buttonSize);
        esborraButton.setPreferredSize(buttonSize);
        acabarButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 30);
        enrereButton.setPreferredSize(buttonSize);
        normesButton.setPreferredSize(buttonSize);

        //TEXTS
        Font font = new Font("Arial", Font.BOLD, 20);
        titolCreaManual.setFont(font);

        font = new Font("Arial", Font.PLAIN, 14);
        labelSelectVal.setFont(font);
        seleccioValor.setFont(font);

        //Colors botons
        afegirValButton.setBackground(Color.decode("#005A9E"));
        afegirValButton.setForeground(Color.decode("#F5F5F5"));
        seleccioValor.setBackground(Color.decode("#005A9E"));
        seleccioValor.setForeground(Color.decode("#F5F5F5"));
        eliminaValButton.setBackground(Color.decode("#E57373"));
        eliminaValButton.setForeground(Color.decode("#333333"));
        esborraButton.setBackground(Color.decode("#E57373"));
        esborraButton.setForeground(Color.decode("#333333"));
        acabarButton.setBackground(Color.decode("#5BC0BE"));
        acabarButton.setForeground(Color.decode("#333333"));
        enrereButton.setBackground(Color.decode("#5BC0BE"));
        enrereButton.setForeground(Color.decode("#333333"));
        normesButton.setBackground(Color.decode("#5BC0BE"));
        normesButton.setForeground(Color.decode("#333333"));

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
        acabarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                try {
                    actionPerformed_buttonAcabar(e);
                } catch (ExcepcioTamany ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        esborraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonEsborra(e);
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
                "Segur que vols tornar enrere? Es perdran tots els canvis realitzats.", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            ctrlPresentacio.vistaConfigCreacio();
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
        celdas[x][y].setText("");
        Integer intVal = (Integer) seleccioValor.getSelectedItem();
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
        celdas[x][y].setText("");
    }

    /**
     * Es guarda el kenken que s'està creant si és vàlid.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonAcabar(ActionEvent e) throws ExcepcioTamany, IOException {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Segur que vols acabar la creació? No podràs tornar a editar el kenken.", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            ctrlPresentacio.crearKK(tam);
            Integer[] resReg = new Integer[numRegions];
            Integer[] opsReg = new Integer[numRegions];
            String[] opsRegString = new String[numRegions];
            int[] valsCas = new int[tam*tam];

            for (int i = 0; i < tam; i++) {
                for (int j = 0; j < tam; j++) {
                    String info = encontrarLabel(i, j);
                    if (info != null) {
                        int res = obtenerResultado(info);
                        String op = obtenerOperador(info);
                        int opInt = convertirSimboloAInt(op);
                        resReg[mat[i][j]] = res;
                        opsReg[mat[i][j]] = opInt;
                        opsRegString[mat[i][j]] = op;
                    }
                    int valor;
                    String texto = celdas[i][j].getText();
                    if (texto.equals("")) {
                        valor = -1;
                    }
                    else {
                        valor = Integer.parseInt(celdas[i][j].getText());
                    }
                    valsCas[i*tam + j] = valor;
                }
            }

            ctrlPresentacio.actualitzaRegions(mat, opsReg, resReg, numRegions + 1);
            ctrlPresentacio.actualitzaValorsBloquejats(valsCas);

            if (!ctrlPresentacio.validaSolucio()) {
                ctrlPresentacio.mostraAvis("El kenken creat no és vàlid");
                return;
            }
            if (!ctrlPresentacio.resoldreKenken()) {
                ctrlPresentacio.mostraAvis("El kenken creat no és vàlid");
                return;
            }
            if (!ctrlPresentacio.kenkenValidBD()) {
                ctrlPresentacio.mostraAvis("El kenken creat no és vàlid perquè té masses caselles resoltes");
                return;
            }

            ctrlPresentacio.vistaMostraKenken(mat, opsRegString, resReg, valsCas);
            setVisible(false);
        }
    }

    /**
     * Mètode que retorna el text d'un JLabel d'una casella, a partir dels paràmetres.
     * @param x Fila de la casella.
     * @param y Columna de la casella.
     * @return Text del JLabel de la casella.
     */
    public String encontrarLabel(int x, int y) {
        // Obtiene todos los componentes dentro de la celda
        Component[] componentes = celdas[x][y].getComponents();
        // Recorre los componentes para buscar el JLabel
        for (Component componente : componentes) {
            if (componente instanceof JLabel) {
                // Si se encuentra un JLabel, devuelve su texto
                return ((JLabel) componente).getText();
            }
        }
        // Si no se encuentra ningún JLabel, devuelve null o un valor por defecto según tu necesidad
        return null;
    }

    /**
     * Mètode que retorna el resultat d'una operació, a partir del paràmetre.
     * @param entrada String que conté el resultat i l'operador.
     */
    public static int obtenerResultado(String entrada) {
        // Divide la entrada en partes usando el espacio como separador
        String[] partes = entrada.split(" ");
        // Convierte la primera parte (el resultado) a un entero
        return Integer.parseInt(partes[0]);
    }

    /**
     * Mètode que retorna l'operador de la regió, a partir del paràmetre.
     * @param entrada String que conté el resultat i l'operador.
     */
    public static String obtenerOperador(String entrada) {
        // Divide la entrada en partes usando el espacio como separador
        String[] partes = entrada.split(" ");
        // Devuelve el segundo elemento (el operador)
        return partes[1];
    }

    /**
     * Mètode que converteix un símbol a un enter, a partir del paràmetre.
     * @param simbolo Símbol a convertir.
     */
    public static int convertirSimboloAInt(String simbolo) {
        switch (simbolo) {
            case "=":
                return 0;
            case "+":
                return 1;
            case "-":
                return 2;
            case "x":
                return 3;
            case "/":
                return 4;
            case "%":
                return 5;
            case "Max":
                return 6;
            default:
                // Si el símbolo no está en la lista, puedes devolver un valor por defecto o lanzar una excepción según tu necesidad
                return -1; // Por ejemplo, devolvemos -1 para indicar que el símbolo no es válido
        }
    }

    /**
     * Es descarta el kenken.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEsborra(ActionEvent e) {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Segur que vols esborrar? Es perdran tots els canvis realitzats.", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            esborraCaselles();
        }
    }

    /**
     * Mètode que buida totes les caselles del kenken
     */
    public void esborraCaselles() {
        for (int i = 0; i < tam; ++i) {
            for (int j = 0; j < tam; j++) {
                celdas[i][j].setText("");
            }
        }
    }

    /**
     * Es canvia a la vista: VistaNormes.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonNormes(ActionEvent e) {
        ctrlPresentacio.vistaNormes();
    }
}

