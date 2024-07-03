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
 * Classe que representa una vista específica de la presentació per a crear les regions d'un Kenken manualment.
 * Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
 */
public class VistaCreaManualRegions extends JFrame {
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
     * Identificador de la regió.
     */
    private int idRegio;

    /**
     * Valor seleccionat.
     */
    private int valSelect;

    /**
     * Botó per tornar a la pàgina anterior.
     */
    private JButton enrereButton = new JButton("Tornar enrere");

    /**
     * ComboBox amb les operacions ops.
     */
    String[] ops = {"=", "+", "-", "x", "/", "%", "Max"};
    private JComboBox seleccioOps = new JComboBox<>(ops);
    /**
     * Camp de text per indicar el resultat de la regió.
     */
    private JTextField textResultat = new JTextField(15);

    /**
     * Botó per crear la regió.
     */
    private JButton creaRegioButton = new JButton("Crea la regió");

    /**
     * Botó per eliminar la regió.
     */
    private JButton eliminaRegioButton = new JButton("Elimina la regió");

    /**
     * Botó per afegir valors a les caselles.
     */
    private JButton afegirValorsButton = new JButton("Creació valors");

    /**
     * Botó per esborrar totes les dades del kenken.
     */
    private JButton esborraButton = new JButton("Esborra tot");

    /**
     * Botó per esborrar totes les dades del kenken.
     */
    private JButton acabarButton = new JButton("Acabar creació");

    /**
     * Botó per consultar les normes del joc.
     */
    private JButton normesButton = new JButton("Normes del joc" );

    /**
     * Etiqueta del titol de la pantalla.
     */
    private JLabel titolCreaManual = new JLabel("CREACIÓ REGIONS D'UN KENKEN");

    /**
     * Etiqueta que indica la selecció d'operacions.
     */
    private JLabel labelSelectOp = new JLabel("Selecciona l'operació:");

    /**
     * Etiqueta que indica la selecció de resultat.
     */
    private JLabel labelSelectRes = new JLabel("Introdueix el resultat:");

    /**
     * Constructora de la vista.
     */
    public VistaCreaManualRegions(CtrlPresentacio ctrlPresentacio, int N) {
        ferVisible();
        this.ctrlPresentacio = ctrlPresentacio;
        tam = N;
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
        setTitle("Pantalla Creació Manual de Regions d'un Kenken");
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
     * Inicialitza la matriu que correspon al tauler.
     */
    private void iniMatriu() {
        mat = new int[tam][tam];
        idRegio = 0;
        valSelect = -1;
        for (int i=0; i<tam; i++) {
            for (int j=0; j<tam; ++j) {
                mat[i][j] = -1;
            }
        }
        int tamCasella = 800/tam;
        // Creamos una matriz de JLabels para almacenar las celdas del tablero
        celdas = new JLabel[tam][tam];

        // Recorremos la matriz para inicializar cada celda del tablero
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                JLabel celda = new JLabel(); // Creamos una nueva celda (JLabel)

                // Configuramos el tamaño de la celda
                celda.setPreferredSize(new Dimension(tamCasella, tamCasella));
                celda.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
                celda.setOpaque(true);
                celda.setBackground(Color.white);
                celda.setVisible(true);
                // Agregamos la celda al tablero y a la matriz de celdas
                board.add(celda);
                celdas[i][j] = celda;
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
                                    break;
                                }
                            }
                            if (x != -1 && y != -1) {
                                break;
                            }
                        }

                        if (x != -1 && y != -1) {
                            valSelect = mat[x][y];
                            if (valSelect == -1) { //Selecciono casella
                                mat[x][y] = idRegio;
                                celda.setBackground(Color.LIGHT_GRAY);
                                valSelect = mat[x][y];
                                seleccioRegio(false);
                            }
                            else if (valSelect == idRegio) { //Deselecciono casella
                                mat[x][y] = -1;
                                celda.setBackground(Color.WHITE);
                            }
                            else { //Selecciono o deselecciono regió
                                boolean deseleccio = false;
                                if (celdas[x][y].getBackground() == Color.LIGHT_GRAY) deseleccio = true;
                                seleccioRegio(deseleccio);
                            }
                        }
                    }
                });
            }
        }
        mat = transposaMat(mat);
        frontissesRegions(celdas);
        mat = transposaMat(mat);

        board.setLayout(new GridLayout(tam, tam));
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

        //Inicialitza els botons de la regió
        JPanel buttonsRegio = new JPanel(new GridBagLayout());
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 20, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonsRegio.add(labelSelectOp,constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        buttonsRegio.add(seleccioOps,constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonsRegio.add(labelSelectRes,constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        buttonsRegio.add(textResultat,constraints);

        //Inicialitza els botons de la regió
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 2;
        buttonsPanel.add(creaRegioButton,constraints);
        constraints.insets = new Insets(10, 10, 35, 10);
        constraints.gridx = 0;
        constraints.gridy = 3;
        buttonsPanel.add(eliminaRegioButton,constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        buttonsPanel.add(esborraButton, constraints);
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 5;
        buttonsPanel.add(afegirValorsButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 6;
        buttonsPanel.add(acabarButton, constraints);

        //Panell amb tots els botons de la dreta.
        JPanel buttonsFinal = new JPanel(new GridBagLayout());
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonsFinal.add(buttonsRegio, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonsFinal.add(buttonsPanel, constraints);

        //Panell principal
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(board, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(buttonsFinal, gbc);

        board.setPreferredSize(new Dimension(400, 400));

        buttonsFinal.setBackground(Color.decode("#E9EDEF"));
        buttonsRegio.setBackground(Color.decode("#E9EDEF"));
        buttonsPanel.setBackground(Color.decode("#E9EDEF"));

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        seleccioOps.setToolTipText("Selecciones una operació per la regió marcada");
        textResultat.setToolTipText("Text on introdueixes un enter que correspon al resultat de la regió");
        creaRegioButton.setToolTipText("Crees la regió marcada al tauler amb l'operació i resultat indicats");
        eliminaRegioButton.setToolTipText("Elimines la regió seleccionada");
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
     *  Configura l'aparença dels components de la vista
     */
    private void configuraAparencia() {

        //BOTONES
        Dimension buttonSize = new Dimension(200, 30);
        creaRegioButton.setPreferredSize(buttonSize);
        eliminaRegioButton.setPreferredSize(buttonSize);
        afegirValorsButton.setPreferredSize(buttonSize);
        esborraButton.setPreferredSize(buttonSize);
        acabarButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 30);
        enrereButton.setPreferredSize(buttonSize);
        normesButton.setPreferredSize(buttonSize);

        //TEXTS
        Font font = new Font("Arial", Font.BOLD, 20);
        titolCreaManual.setFont(font);

        font = new Font("Arial", Font.PLAIN, 14);
        labelSelectOp.setFont(font);
        labelSelectRes.setFont(font);
        textResultat.setFont(font);
        seleccioOps.setFont(font);

        //Colors botons
        creaRegioButton.setBackground(Color.decode("#005A9E"));
        creaRegioButton.setForeground(Color.decode("#F5F5F5"));
        creaRegioButton.setBackground(Color.decode("#005A9E"));
        creaRegioButton.setForeground(Color.decode("#F5F5F5"));
        eliminaRegioButton.setBackground(Color.decode("#E57373"));
        eliminaRegioButton.setForeground(Color.decode("#333333"));
        esborraButton.setBackground(Color.decode("#E57373"));
        esborraButton.setForeground(Color.decode("#333333"));
        afegirValorsButton.setBackground(Color.decode("#5BC0BE"));
        afegirValorsButton.setForeground(Color.decode("#333333"));
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
        creaRegioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonCreaRegio(e);
            }
        });
        eliminaRegioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonEliminaRegio(e);
            }
        });
        afegirValorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonAfegirValors(e);
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
     * Crea la regió amb les caselles marcades i li assigna l'operació i resultat indicats.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonCreaRegio(ActionEvent e) {
        //Recorro les caselles del tauler
        int cont = 0;
        Pair pos = new Pair(-1,-1);
        boolean primer = true;
        for(int i = 0; i < tam; ++i) {
            for(int j = 0; j < tam; ++j) {
                if (mat[i][j] == idRegio) { //Si està seleccionada, conto
                    if(primer) { //hem guardo la primera posició seleccionada
                        pos = new Pair(i,j);
                        primer = false;
                    }
                    ++cont;
                }
            }
        }
        //Miro que s'hagi seleccionat alguna casella
        if (pos.getFirst() == -1) {
            ctrlPresentacio.mostraAvis("No has seleccionat cap casella disponible");
            return;
        }
        //Miro que les caselles siguin contigues
        if (!bfs(mat, pos)) {
            ctrlPresentacio.mostraAvis("Les caselles no son contigües");
            return;
        }
        //Miro que el resultat indicat sigui vàlid
        try {
            int numero = Integer.parseInt(textResultat.getText());
            if (numero < 0) {
                ctrlPresentacio.mostraAvis("El resultat introduit no és vàlid");
                return;
            }

        } catch (NumberFormatException ex) {
            ctrlPresentacio.mostraAvis("El resultat introduit no és vàlid");
            return;
        }

        //Miro que l'operació seleccionada coincideixi amb el numero de caselles seleccionades
        if (cont == 1 && seleccioOps.getSelectedItem() != "=") {
            ctrlPresentacio.mostraAvis("L'operació indicada no es pot col·locar a una regió amb una casella");
            return;
        }
        if (cont > 1 && seleccioOps.getSelectedItem() == "=") {
            ctrlPresentacio.mostraAvis("L'operació indicada només es pot col·locar a regions amb una casella");
            return;
        }
        if (cont > 2 && seleccioOps.getSelectedItem() != "+" && seleccioOps.getSelectedItem() != "x") {
            ctrlPresentacio.mostraAvis("L'operació indicada només es pot col·locar a regions amb dues caselles");
            return;
        }

        //Creo region
        mat = transposaMat(mat);
        //Marco la regio en el tauler
        frontissesRegions(celdas);
        mat = transposaMat(mat);
        //Marco el resultat i l'operació a la regió
        int x = pos.getFirst();
        int y = pos.getSecond();
        String op = (String) seleccioOps.getSelectedItem();
        String res = textResultat.getText();
        JLabel lab = new JLabel();
        celdas[x][y].add(lab);
        Font font = lab.getFont();
        int fontTam;
        if (tam >= 7) fontTam = 10;
        else if (tam < 5) fontTam = 20;
        else fontTam = 15;
        lab.setFont(new Font(font.getName(), font.getStyle(), fontTam));
        lab.setText(res + " " + op);
        lab.setForeground(Color.decode("#005A9E"));

        int posx = 7; // Posición fija desde el borde izquierdo
        int posy;
        int tamLabel = 800/tam/ 2; // Tamaño relativo a la celda

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
        //Inicialitzo el seleccioOps i el textResultat.
        seleccioOps.setSelectedIndex(0);
        textResultat.setText("");
        //incremento idRegio
        ++idRegio;
        valSelect = -1;
    }

    /**
     * Retorna true si les caselles seleccionades son contigues.
     */
    public boolean bfs(int[][] mat, Pair pos) {
        boolean[][] visited = new boolean[tam][tam];
        Queue<Pair> queue = new LinkedList<>();
        queue.add(pos);
        visited[pos.getFirst()][pos.getSecond()] = true;

        while (!queue.isEmpty()) {
            Pair current = queue.poll();
            int x = current.getFirst();
            int y = current.getSecond();

            //Posicions contigues a una casella: adalt, abaix, esquerra, dreta
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (newX >= 0 && newX < tam && newY >= 0 && newY < tam && mat[newX][newY] == idRegio && !visited[newX][newY]) {
                    queue.add(new Pair(newX, newY));
                    visited[newX][newY] = true;
                }
            }
        }
        // Verifica si todas las casillas marcadas como seleccionadas han sido visitadas
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (mat[i][j] == idRegio && !visited[i][j]) {
                    return false; // No son contiguas
                }
            }
        }
        return true;
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
                if (mat[i][j] == idRegio) celdas[j][i].setBackground(Color.white);

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
     * Selecciona la regió marcada.
     * @param deselecciono Booleà que indica si es deselecciona la regió.
     */
    public void seleccioRegio(boolean deselecciono) {
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (mat[i][j] == valSelect) { //si la casella pertany a la regió
                    if (deselecciono) {
                        celdas[i][j].setBackground(Color.WHITE);
                    }
                    else {    //selecciono les caselles de la regió
                        celdas[i][j].setBackground(Color.LIGHT_GRAY);
                    }
                }
                else if (!deselecciono) {
                    celdas[i][j].setBackground(Color.WHITE); //deselecciono les caselles que no pertanyen a la regio
                    if (mat[i][j] == idRegio) mat[i][j] = -1; //deixo buides les caselles que no pertanyen a una regió
                }
            }
        }
    }

    /**
     * Elimina la regió seleccionada.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEliminaRegio(ActionEvent e) {
        if (valSelect == idRegio || valSelect == -1) {
            ctrlPresentacio.mostraAvis("No estas seleccionant una regió");
            return;
        }
        esborraRegio();
    }

    /**
     * Esborra la regió seleccionada.
     */
    public void esborraRegio() {
        int x = -1;
        int y = -1;
        --idRegio;
        boolean primer = true;
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (mat[i][j] > valSelect) mat[i][j]--;
                else if (mat[i][j] == valSelect) { //si la casella pertany a la regió
                    mat[i][j] = -1;
                    celdas[i][j].setBackground(Color.WHITE);
                    if(primer) { //hem guardo la primera posició seleccionada
                        x = i;
                        y = j;
                        primer = false;
                    }
                }
            }
        }
        celdas[x][y].removeAll();
        revalidate();
        repaint();

        mat = transposaMat(mat);
        frontissesRegions(celdas);
        mat = transposaMat(mat);
    }

    /**
     * Es canvia a la vista: vistaCreaManualCaselles.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonAfegirValors(ActionEvent e) {
        if (!regionsCompletes()) {
            ctrlPresentacio.mostraAvis("Queden regions sense crear");
            return;
        }
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Segur que vols continuar? No podras tornar per modificar les regions.", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {

            Integer[] resReg = new Integer[idRegio];
            Integer[] opsReg = new Integer[idRegio];
            String[] opsRegString = new String[idRegio];
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
                }
            }

            ctrlPresentacio.actualitzaRegions(mat, opsReg, resReg, idRegio + 1);

            if (!ctrlPresentacio.resoldreKenken()) {
                ctrlPresentacio.crearKK(tam);
                ctrlPresentacio.mostraAvis("El kenken creat no és vàlid");
                return;
            }

            ctrlPresentacio.vistaCreacioCaselles(mat, opsRegString, resReg, idRegio);
            setVisible(false);
        }
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
            if (!regionsCompletes()) {
                ctrlPresentacio.mostraAvis("Queden regions sense crear");
                return;
            }

            ctrlPresentacio.crearKK(tam);

            Integer[] resReg = new Integer[idRegio];
            Integer[] opsReg = new Integer[idRegio];
            String[] opsRegString = new String[idRegio];
            int[] valCas = new int[tam*tam];

            for (int i = 0; i < tam; i++) {
                for (int j = 0; j < tam; j++) {
                    valCas[i*tam + j] = -1;
                    String info = encontrarLabel(i, j);
                    if (info != null) {
                        int res = obtenerResultado(info);
                        String op = obtenerOperador(info);
                        int opInt = convertirSimboloAInt(op);
                        resReg[mat[i][j]] = res;
                        opsReg[mat[i][j]] = opInt;
                        opsRegString[mat[i][j]] = op;
                    }
                }
            }

            ctrlPresentacio.actualitzaRegions(mat, opsReg, resReg, idRegio + 1);

            if (!ctrlPresentacio.resoldreKenken()) {
                ctrlPresentacio.mostraAvis("El kenken creat no és vàlid");
                return;
            }

            ctrlPresentacio.vistaMostraKenken(mat, opsRegString, resReg, valCas);
            setVisible(false);
        }
    }

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

    // Método para obtener el resultado (int) de la entrada
    public static int obtenerResultado(String entrada) {
        // Divide la entrada en partes usando el espacio como separador
        String[] partes = entrada.split(" ");
        // Convierte la primera parte (el resultado) a un entero
        return Integer.parseInt(partes[0]);
    }

    // Método para obtener el operador (String) de la entrada
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
     * Retorna true si totes les regions estan completes.
     */
    public boolean regionsCompletes() {
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (mat[i][j] == -1 || mat[i][j] == idRegio) return false;
            }
        }
        return true;
    }

    /**
     * Es descarta el Kenken.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonEsborra(ActionEvent e) {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Segur que vols esborrar? Es perdran tots els canvis realitzats.", "Avís",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            resetMat();
        }
    }

    /**
     * Reseteja la matriu del kenken.
     */
    public void resetMat() {
        idRegio = 0;
        valSelect = -1;
        for (int i=0; i<tam; i++) {
            for (int j=0; j<tam; ++j) {
                mat[i][j] = -1;
                celdas[i][j].setBackground(Color.WHITE);
                celdas[i][j].removeAll();
            }
        }
        frontissesRegions(celdas);
    }

    /**
     * Es canvia a la vista: VistaNormes.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonNormes(ActionEvent e) {
        ctrlPresentacio.vistaNormes();
    }
}

