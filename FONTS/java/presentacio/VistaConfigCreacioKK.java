package presentacio;

import excepcions.ExcepcioNoHiHaSessio;
import excepcions.ExcepcioTamany;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
* Classe que representa una vista específica de la presentació per a configurar un Kenken a crear.
* Proporciona mètodes per a interactuar amb la vista i mostrar dades a l'usuari.
*/
public class VistaConfigCreacioKK extends JFrame {
    /**
     * Ctrl presentacio.
     */
    private CtrlPresentacio ctrlPresentacio;
    /**
     * Panell de continguts de la vista de configuracio de la creació de kenken.
     */
    private JPanel mainPanel = new JPanel();

    /**
     * Botó per tornar a la pàgina anterior.
     */
    private JButton enrereButton = new JButton("Tornar enrere");

    /**
     * Botó per començar a crear un Kenken.
     */
    private JButton importarKenkenButton = new JButton("Importar Kenken");

    /**
     * Vista del directori home per importar un arxiu.
     */
    private JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

    /**
     * Filepath de l’arxiu importat.
     */
    private String filepath;

    /**
     * Etiqueta que indica la creació manual.
     */
    private JLabel labelTamany = new JLabel("Selecciona un tamany per crear un kenken:");

    /**
     * Selecciona el tamany del Kenken.
     */
    Integer[] tams = {2, 3, 4, 5, 6, 7, 8, 9};
    private JComboBox seleccioTamany = new JComboBox<>(tams);

    /**
     * Botó per crear el Kenken de manera manual.
     */
    private JButton crearManualButton = new JButton("Crea manualment");

    /**
     * Botó per a que el sistema crei un kenken aleatori.
     */
    private JButton crearAutoButton = new JButton("Crea automàtic");

    /**
     * Etiqueta del titol de la pantalla.
     */
    private JLabel titolConfigCrea = new JLabel("CREAR KENKEN");

    /**
     * Constructora de la vista.
     */
    public VistaConfigCreacioKK (CtrlPresentacio ctrlPresentacio) {
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
        setTitle("Pantalla Configuració Creació Kenken");
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

        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        //Inicialitza els botons superiors de la pantalla
        botonsSuperiors();

        //Inicialitza els botons principals
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 50, 10); //espai entre botons

        JPanel tamanyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tamanyPanel.add(labelTamany, constraints);
        tamanyPanel.add(Box.createRigidArea(new Dimension(20, 0))); //espai
        tamanyPanel.add(seleccioTamany, constraints);

        JPanel tipusCreaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tipusCreaPanel.add(importarKenkenButton, constraints);
        tipusCreaPanel.add(Box.createRigidArea(new Dimension(10, 0))); //espai
        tipusCreaPanel.add(crearAutoButton, constraints);
        tipusCreaPanel.add(Box.createRigidArea(new Dimension(10, 0))); //espai
        tipusCreaPanel.add(crearManualButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(tamanyPanel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(tipusCreaPanel, constraints);

        tamanyPanel.setBackground(Color.decode("#E9EDEF"));
        tipusCreaPanel.setBackground(Color.decode("#E9EDEF"));

        add(mainPanel, BorderLayout.CENTER);

        // Tooltips
        seleccioTamany.setToolTipText("Selecciones el numero de files i columnes que vols que tingui el kenken");
        crearManualButton.setToolTipText("Crees un kenken a partir d'un tauler buit amb el tamany indicat");
        crearAutoButton.setToolTipText("El sistema crea un kenken del tamany indicat");
        importarKenkenButton.setToolTipText("Crees un kenken a partir del fitxer .txt que importis");
        enrereButton.setToolTipText("Tornes a la pantalla anterior");
    }

    /**
     * Inicialitza els botons de la part superior de la vista.
     */
    private void botonsSuperiors() {
        //Panell pel botó "Enrere"
        JPanel enrerePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enrerePanel.add(enrereButton);

        //Panell pel titol
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(titolConfigCrea);

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
     * Configura l'aparença dels components de la vista.
     */
    private void configuraAparencia() {
        //BOTONES
        Dimension buttonSize = new Dimension(200, 50);
        crearAutoButton.setPreferredSize(buttonSize);
        crearManualButton.setPreferredSize(buttonSize);
        importarKenkenButton.setPreferredSize(buttonSize);

        buttonSize = new Dimension(170, 40);
        enrereButton.setPreferredSize(buttonSize);

        //TEXTS
        Font font = new Font("Arial", Font.BOLD, 20);
        titolConfigCrea.setFont(font);

        font = new Font("Arial", Font.PLAIN, 16);
        seleccioTamany.setFont(font);
        labelTamany.setFont(font);

        //Colors botons
        crearManualButton.setBackground(Color.decode("#005A9E"));
        crearManualButton.setForeground(Color.decode("#F5F5F5"));
        crearAutoButton.setBackground(Color.decode("#005A9E"));
        crearAutoButton.setForeground(Color.decode("#F5F5F5"));
        importarKenkenButton.setBackground(Color.decode("#5BC0BE"));
        importarKenkenButton.setForeground(Color.decode("#333333"));
        enrereButton.setBackground(Color.decode("#5BC0BE"));
        enrereButton.setForeground(Color.decode("#333333"));
        seleccioTamany.setBackground(Color.decode("#005A9E"));
        seleccioTamany.setForeground(Color.decode("#F5F5F5"));

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
        crearManualButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonCrearManual(e);
            }
        });
        crearAutoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                actionPerformed_buttonCrearAuto(e);
            }
        });
        importarKenkenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = ((JButton) e.getSource()).getText();
                System.out.println("Has clickado el boton con texto: " + texto);
                try {
                    actionPerformed_buttonImportar(e);
                } catch (ExcepcioTamany ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
     * Es canvia a la vista: VistaCreaManualRegions.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonCrearManual(ActionEvent e) {
        Integer tamSeleccionat = (Integer) seleccioTamany.getSelectedItem();
        ctrlPresentacio.crearKK(tamSeleccionat);
        ctrlPresentacio.vistaCreacioRegions(tamSeleccionat); //supongo que tienes que pasarte el numero
        setVisible(false);

    }

    /**
     * Es crea un kenken amb el tamany indicat i es canvia a la vista: VistaKenken.
     * @param e L'esdeveniment que activa aquesta funció.
     */
    public void actionPerformed_buttonCrearAuto(ActionEvent e) {
        Integer tamSeleccionat = (Integer) seleccioTamany.getSelectedItem();
        ctrlPresentacio.vistaCreaAuto(tamSeleccionat);
        setVisible(false);
    }

    /**
     * S'obre l'escriptori de l'usuari per a que seleccioni un kenken, es crea el kenken i salta a la vista: VistaKenken.
     * @param e L'esdeveniment que activa aquesta funció
     */
    public void actionPerformed_buttonImportar(ActionEvent e) throws ExcepcioTamany, IOException {
        // Establecer el filtro para permitir solo archivos .txt
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Selecciona fitxer");

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Verificar que el archivo seleccionado tenga la extensión .txt
            if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                JOptionPane.showMessageDialog(null, "Només pots importar fitxers .txt.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            filepath = selectedFile.getAbsolutePath();
            try {
                ctrlPresentacio.creoKenkenPath(filepath);
            }
            catch (IOException | IllegalArgumentException ex){
                ctrlPresentacio.mostraAvis("Error a la importació del kenken.");
                return;
            }
            if (!ctrlPresentacio.validaGuions()) {
                ctrlPresentacio.mostraAvis("El format del kenken és invàlid");
                return;
            }

            int[][] mat = ctrlPresentacio.matTauler();
            String[] ops = ctrlPresentacio.vectorOps();
            Integer[] res = ctrlPresentacio.vectorRes();
            int[] vals = ctrlPresentacio.vectorVals();
            //Si los numeros no estan bien entrara aqui
            if(!ctrlPresentacio.validaSolucio()) {
                ctrlPresentacio.mostraAvis("El kenken creat no és vàlid");
                return;
            }
            //Vol dir que el kenken no té solució
            if(!ctrlPresentacio.resoldreKenken()) {
                ctrlPresentacio.mostraAvis("El kenken creat no és vàlid");
                return;
            }
            ctrlPresentacio.vistaMostraKenken(mat, ops, res, vals);
            setVisible(false);
        }
    }
}
