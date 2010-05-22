/*
 * AlphaRelease.java
 *
 * Created on 21 kwiecie� 2006, 16:21
 */
package edu.agh.eit.xorproblem.gui;

import edu.agh.eit.neural.NeuralLayer;
import edu.agh.eit.neural.NeuralNetwork;
import edu.agh.eit.neural.functions.IActivationFunction;
import edu.agh.eit.neural.functions.GaussianFunction;
import edu.agh.eit.neural.functions.LinearFunction;
import edu.agh.eit.neural.functions.SigmoidFunction;
import edu.agh.eit.neural.functions.TanhFunction;
import edu.agh.eit.xorproblem.gui.dialogs.AboutDialog;
import edu.agh.eit.xorproblem.gui.models.HiddenLayersTableModel;
import edu.agh.eit.xorproblem.gui.models.LearningPatternsDataModel;
import edu.agh.eit.xorproblem.gui.models.NetworkResponseTableModel;
import edu.agh.eit.xorproblem.gui.models.SpinnnerCelEditor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFormattedTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author Lukasz Bandzarewicz <lucassus@gmail.com>
 */
public class XorProblemPanel extends javax.swing.JPanel {

    private static int RESPONSE_PLANE_X = 100;
    private static int RESPONSE_PLANE_Y = 100;

    private enum ApplicationState {

        IDDLE,
        LEARNING_IN_PROGRESS
    }

    private class ResponsePanel extends JPanel {

        private int width;
        private int height;
        private BufferedImage image = null;

        public ResponsePanel() {
            this(RESPONSE_PLANE_X, RESPONSE_PLANE_Y);
        }

        public ResponsePanel(int width, int height) {
            super();
            this.width = width;
            this.height = height;

            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public void paint(Graphics g) {
            if (image != null) {
                g.drawImage(image, 0, 0, null);
            }
        }

        public void update() {
            Graphics2D g2d = image.createGraphics();

            int xScale = (int) (200 / RESPONSE_PLANE_X);
            int yScale = (int) (200 / RESPONSE_PLANE_Y);
            for (int i = 0; i < RESPONSE_PLANE_X; i++) {
                for (int j = 0; j < RESPONSE_PLANE_Y; j++) {

                    int red = (int) (networkResponse[i][j] * 255);
                    if (red > 255) {
                        red = 255;
                    } else if (red < 0) {
                        red = 0;
                    }

                    int green = Math.abs(255 - red);
                    if (green > 255) {
                        green = 255;
                    } else if (green < 0) {
                        green = 0;
                    }

                    g2d.setColor(new Color(red, green, 0));
                    g2d.fillRect(i * xScale, (RESPONSE_PLANE_Y - j - 1) * yScale, xScale, yScale);
                }
            }

            g2d.setColor(Color.BLACK);
            g2d.drawString("[A=0;B=0]", 0, 171);
            g2d.drawString("[A=0;B=1]", 0, 10);
            g2d.drawString("[A=1;B=0]", 140, 171);
            g2d.drawString("[A=1;B=1]", 140, 10);

            g2d.dispose();

            repaint();
        }
    }
    private ResponsePanel responsePanel = null;
    private double[][] networkInput;
    private double[][] networkDesiredOutput;
    private double[] networkOutput;
    private double[][] networkResponse = new double[RESPONSE_PLANE_X][RESPONSE_PLANE_Y];
    private ApplicationState state = ApplicationState.IDDLE;
    public static int MINIMUM_HIDDEN_LAYERS = 0;
    public static int MAXIMUM_HIDDEN_LAYERS = 20;
    private int hiddenLayersCount = 1;
    public static int MINIMUM_EPOCHS = 1;
    public static int MAXIMUM_EPOCHS = 10000000;
    private int learningPatternsCount = 4;
    public static int MINIMUM_DATA_PATTERNS = 1;
    public static int MAXIMUM_DATA_PATTERNS = 50;
    private HiddenLayersTableModel hiddenLayersTableModel = new HiddenLayersTableModel();
    private LearningPatternsDataModel learningPatternsDataModel = new LearningPatternsDataModel();
    private NetworkResponseTableModel networkResponseTableModel = new NetworkResponseTableModel();
    private JFormattedTextField tf;
    private NeuralNetwork neuralNetwork = null;
    private Object[][] xorData = {
        {0, 0, 0},
        {0, 1, 1},
        {1, 0, 1},
        {1, 1, 0}
    };
    private Object[][] andData = {
        {0, 0, 0},
        {0, 1, 0},
        {1, 0, 0},
        {1, 1, 1}
    };
    private Object[][] orData = {
        {0, 0, 0},
        {0, 1, 1},
        {1, 0, 1},
        {1, 1, 1}
    };
    private Object[][] nandData = {
        {0, 0, 1},
        {0, 1, 1},
        {1, 0, 1},
        {1, 1, 0}
    };
    private Object[][] norData = {
        {0, 0, 1},
        {0, 1, 0},
        {1, 0, 0},
        {1, 1, 0}
    };

    /** Creates new form AlphaRelease */
    public XorProblemPanel() {
        initComponents();

        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < info.length; i++) {
            // Get the name of the look and feel that is suitable for display to the user
            String humanReadableName = info[i].getName();
            final String className = info[i].getClassName();

            JMenuItem lookAndFeelMenuItem = new JMenuItem(humanReadableName);
            lookAndFeelMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    try {
                        UIManager.setLookAndFeel(className);
                        SwingUtilities.updateComponentTreeUI(XorProblemPanel.this);
                    } catch (InstantiationException e) {
                    } catch (ClassNotFoundException e) {
                    } catch (UnsupportedLookAndFeelException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
            });

            menuLookAndFeel.add(lookAndFeelMenuItem);
        }

        responsePanel = new ResponsePanel(200, 200);
        panelResponseTop.add(responsePanel, BorderLayout.CENTER);

        // help
        URL helpURL = XorProblem.class.getResource("/resources/help/help.html");
        if (helpURL != null) {
            try {
                editorPaneHelp.setPage(helpURL);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        // end help

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuItemExit = new javax.swing.JMenuItem();
        menuOptions = new javax.swing.JMenu();
        menuLookAndFeel = new javax.swing.JMenu();
        menuHelp = new javax.swing.JMenu();
        menuItemHelp = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        menuItemAbout = new javax.swing.JMenuItem();
        tabbedPane = new javax.swing.JTabbedPane();
        panelNetworkConfiguration = new javax.swing.JPanel();
        panelActivationFunction = new javax.swing.JPanel();
        comboBoxActivationFunction = new javax.swing.JComboBox();
        labelFunctionProperty = new javax.swing.JLabel();
        textFieldFunctionProperty = new javax.swing.JTextField();
        panelHiddenLayers = new javax.swing.JPanel();
        scrollPaneHiddenLayers = new javax.swing.JScrollPane();
        tableHiddenLayers = new javax.swing.JTable();
        spinnerHiddenLayersCount = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        panelLearning = new javax.swing.JPanel();
        scrollPaneLearningData = new javax.swing.JScrollPane();
        tableLearningData = new javax.swing.JTable();
        panelWeights = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        textFieldRandomizeWeightsMin = new javax.swing.JTextField();
        textFieldRandomizeWeightsMax = new javax.swing.JTextField();
        panelPatterns = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        comboBoxLearningPattern = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        spinnerDataPatterns = new javax.swing.JSpinner();
        panelEpochs = new javax.swing.JPanel();
        spinnerEpochs = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        textFieldLearningRate = new javax.swing.JTextField();
        panelNetworkResponse = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        scrollPaneNetworkResponse = new javax.swing.JScrollPane();
        tableNetworkResponse = new javax.swing.JTable();
        panelResponseTop = new javax.swing.JPanel();
        panelHelp = new javax.swing.JPanel();
        scrollPaneHelp = new javax.swing.JScrollPane();
        editorPaneHelp = new javax.swing.JEditorPane();
        panelAbout = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        progressBarLearning = new javax.swing.JProgressBar();
        labelApplicationStatus = new javax.swing.JLabel();
        buttonStartLearning = new javax.swing.JButton();

        menuFile.setText("File");

        menuItemExit.setText("Exit");
        menuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemExitActionPerformed(evt);
            }
        });
        menuFile.add(menuItemExit);

        menuBar.add(menuFile);

        menuOptions.setText("Options");

        menuLookAndFeel.setText("Look and Feel");
        menuOptions.add(menuLookAndFeel);

        menuBar.add(menuOptions);

        menuHelp.setText("Help");

        menuItemHelp.setText("Help");
        menuItemHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemHelpActionPerformed(evt);
            }
        });
        menuHelp.add(menuItemHelp);
        menuHelp.add(jSeparator1);

        menuItemAbout.setText("About");
        menuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAboutActionPerformed(evt);
            }
        });
        menuHelp.add(menuItemAbout);

        menuBar.add(menuHelp);

        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setToolTipText("Learning configuration");
        tableLearningData.setModel(learningPatternsDataModel);
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        panelNetworkConfiguration.setBorder(javax.swing.BorderFactory.createTitledBorder("Network configuration"));

        panelActivationFunction.setBorder(javax.swing.BorderFactory.createTitledBorder("Activation Function"));

        comboBoxActivationFunction.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Linear", "Sigmoid", "Hyperbolic tangent" }));
        comboBoxActivationFunction.setSelectedIndex(1);
        comboBoxActivationFunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxActivationFunctionActionPerformed(evt);
            }
        });

        labelFunctionProperty.setText("Sigmoid function BETA:");

        textFieldFunctionProperty.setFont(new java.awt.Font("Tahoma", 0, 12));
        textFieldFunctionProperty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textFieldFunctionProperty.setText("1.0");

        org.jdesktop.layout.GroupLayout panelActivationFunctionLayout = new org.jdesktop.layout.GroupLayout(panelActivationFunction);
        panelActivationFunction.setLayout(panelActivationFunctionLayout);
        panelActivationFunctionLayout.setHorizontalGroup(
            panelActivationFunctionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelActivationFunctionLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelActivationFunctionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, comboBoxActivationFunction, 0, 553, Short.MAX_VALUE)
                    .add(panelActivationFunctionLayout.createSequentialGroup()
                        .add(labelFunctionProperty)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(textFieldFunctionProperty, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelActivationFunctionLayout.setVerticalGroup(
            panelActivationFunctionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelActivationFunctionLayout.createSequentialGroup()
                .add(comboBoxActivationFunction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelActivationFunctionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(labelFunctionProperty)
                    .add(textFieldFunctionProperty, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelHiddenLayers.setBorder(javax.swing.BorderFactory.createTitledBorder("Hidden Layers"));

        tableHiddenLayers.setFont(new java.awt.Font("Tahoma", 0, 14));
        tableHiddenLayers.setModel(hiddenLayersTableModel);
        tableHiddenLayers.setRowSelectionAllowed(false);
        TableColumn col = tableHiddenLayers.getColumnModel().getColumn(1);
        col.setCellEditor(new SpinnnerCelEditor());
        col.setMinWidth(100);
        col.setMaxWidth(300);
        scrollPaneHiddenLayers.setViewportView(tableHiddenLayers);

        spinnerHiddenLayersCount.setFont(new java.awt.Font("Courier", 0, 12));
        spinnerHiddenLayersCount.setModel(new SpinnerNumberModel(1, MINIMUM_HIDDEN_LAYERS, MAXIMUM_HIDDEN_LAYERS, 1));
        // Disable keyboard edits in the spinner
        tf = ((JSpinner.DefaultEditor)spinnerHiddenLayersCount.getEditor()).getTextField();
        tf.setEditable(false);

        // The previous call sets the background to a disabled
        // color (usually gray). To change this disabled color,
        // reset the background color.
        tf.setBackground(Color.white);
        spinnerHiddenLayersCount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerHiddenLayersCountStateChanged(evt);
            }
        });

        jLabel1.setText("Number of hidden layers:");

        org.jdesktop.layout.GroupLayout panelHiddenLayersLayout = new org.jdesktop.layout.GroupLayout(panelHiddenLayers);
        panelHiddenLayers.setLayout(panelHiddenLayersLayout);
        panelHiddenLayersLayout.setHorizontalGroup(
            panelHiddenLayersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelHiddenLayersLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelHiddenLayersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, scrollPaneHiddenLayers, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                    .add(panelHiddenLayersLayout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(spinnerHiddenLayersCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelHiddenLayersLayout.setVerticalGroup(
            panelHiddenLayersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelHiddenLayersLayout.createSequentialGroup()
                .add(panelHiddenLayersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(spinnerHiddenLayersCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scrollPaneHiddenLayers, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout panelNetworkConfigurationLayout = new org.jdesktop.layout.GroupLayout(panelNetworkConfiguration);
        panelNetworkConfiguration.setLayout(panelNetworkConfigurationLayout);
        panelNetworkConfigurationLayout.setHorizontalGroup(
            panelNetworkConfigurationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelNetworkConfigurationLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelNetworkConfigurationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelHiddenLayers, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panelActivationFunction, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelNetworkConfigurationLayout.setVerticalGroup(
            panelNetworkConfigurationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelNetworkConfigurationLayout.createSequentialGroup()
                .add(panelActivationFunction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelHiddenLayers, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Network configuration", new javax.swing.ImageIcon(getClass().getResource("/resources/configure.gif")), panelNetworkConfiguration, "Neural Network configuration"); // NOI18N

        panelLearning.setBorder(javax.swing.BorderFactory.createTitledBorder("Learning configuration"));

        scrollPaneLearningData.setBorder(javax.swing.BorderFactory.createTitledBorder("Learning Patterns"));

        tableLearningData.setFont(new java.awt.Font("Tahoma", 0, 12));
        tableLearningData.setModel(learningPatternsDataModel);
        tableLearningData.setRowSelectionAllowed(false);
        tableLearningData.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                comboBoxLearningPattern.setSelectedItem(new String("User defined"));
            }
        });
        scrollPaneLearningData.setViewportView(tableLearningData);

        panelWeights.setBorder(javax.swing.BorderFactory.createTitledBorder("Randomize weights"));

        jLabel2.setText("min:");

        jLabel6.setText("max:");

        textFieldRandomizeWeightsMin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textFieldRandomizeWeightsMin.setText("-1.0");

        textFieldRandomizeWeightsMax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textFieldRandomizeWeightsMax.setText("1.0");

        org.jdesktop.layout.GroupLayout panelWeightsLayout = new org.jdesktop.layout.GroupLayout(panelWeights);
        panelWeights.setLayout(panelWeightsLayout);
        panelWeightsLayout.setHorizontalGroup(
            panelWeightsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelWeightsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelWeightsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel2)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelWeightsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(textFieldRandomizeWeightsMax, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .add(textFieldRandomizeWeightsMin, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelWeightsLayout.setVerticalGroup(
            panelWeightsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelWeightsLayout.createSequentialGroup()
                .add(panelWeightsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(textFieldRandomizeWeightsMin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelWeightsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(textFieldRandomizeWeightsMax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelPatterns.setBorder(javax.swing.BorderFactory.createTitledBorder("Pattern"));

        jLabel5.setText("Data patterns:");

        comboBoxLearningPattern.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "User defined", "XOR", "AND", "OR", "NAND", "NOR" }));
        comboBoxLearningPattern.setSelectedItem(new String("XOR"));
        comboBoxLearningPattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxLearningPatternActionPerformed(evt);
            }
        });

        jLabel7.setText("Pattern:");

        spinnerDataPatterns.setFont(new java.awt.Font("Courier", 0, 12));
        spinnerDataPatterns.setModel(new SpinnerNumberModel(4, MINIMUM_DATA_PATTERNS, MAXIMUM_DATA_PATTERNS, 1));
        // Disable keyboard edits in the spinner
        tf = ((JSpinner.DefaultEditor)spinnerDataPatterns.getEditor()).getTextField();
        tf.setEditable(false);

        // The previous call sets the background to a disabled
        // color (usually gray). To change this disabled color,
        // reset the background color.
        tf.setBackground(Color.white);
        spinnerDataPatterns.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerDataPatternsStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout panelPatternsLayout = new org.jdesktop.layout.GroupLayout(panelPatterns);
        panelPatterns.setLayout(panelPatternsLayout);
        panelPatternsLayout.setHorizontalGroup(
            panelPatternsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelPatternsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelPatternsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel7)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelPatternsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(spinnerDataPatterns, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .add(comboBoxLearningPattern, 0, 100, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelPatternsLayout.setVerticalGroup(
            panelPatternsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelPatternsLayout.createSequentialGroup()
                .add(panelPatternsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(comboBoxLearningPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelPatternsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(spinnerDataPatterns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelEpochs.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));

        spinnerEpochs.setFont(new java.awt.Font("Courier", 0, 12));
        spinnerEpochs.setModel(new SpinnerNumberModel(10000, MINIMUM_EPOCHS, MAXIMUM_EPOCHS, 1));

        jLabel3.setText("Epochs:");

        jLabel4.setText("Learning rate:");

        textFieldLearningRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textFieldLearningRate.setText("0.9");

        org.jdesktop.layout.GroupLayout panelEpochsLayout = new org.jdesktop.layout.GroupLayout(panelEpochs);
        panelEpochs.setLayout(panelEpochsLayout);
        panelEpochsLayout.setHorizontalGroup(
            panelEpochsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelEpochsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelEpochsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel4)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelEpochsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(spinnerEpochs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                    .add(textFieldLearningRate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelEpochsLayout.setVerticalGroup(
            panelEpochsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelEpochsLayout.createSequentialGroup()
                .add(panelEpochsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(spinnerEpochs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelEpochsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(textFieldLearningRate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout panelLearningLayout = new org.jdesktop.layout.GroupLayout(panelLearning);
        panelLearning.setLayout(panelLearningLayout);
        panelLearningLayout.setHorizontalGroup(
            panelLearningLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLearningLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelLearningLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, scrollPaneLearningData, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                    .add(panelLearningLayout.createSequentialGroup()
                        .add(panelPatterns, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelEpochs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelWeights, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelLearningLayout.setVerticalGroup(
            panelLearningLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLearningLayout.createSequentialGroup()
                .add(panelLearningLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(panelWeights, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panelPatterns, 0, 99, Short.MAX_VALUE)
                    .add(panelEpochs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scrollPaneLearningData, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Learning configuration", new javax.swing.ImageIcon(getClass().getResource("/resources/learn.gif")), panelLearning); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Network Response"));

        tableNetworkResponse.setModel(networkResponseTableModel);
        tableNetworkResponse.setRowSelectionAllowed(false);
        scrollPaneNetworkResponse.setViewportView(tableNetworkResponse);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(scrollPaneNetworkResponse, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(scrollPaneNetworkResponse, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelResponseTop.setBorder(javax.swing.BorderFactory.createTitledBorder("Response Plane"));
        panelResponseTop.setDoubleBuffered(false);
        panelResponseTop.setLayout(new java.awt.BorderLayout());

        org.jdesktop.layout.GroupLayout panelNetworkResponseLayout = new org.jdesktop.layout.GroupLayout(panelNetworkResponse);
        panelNetworkResponse.setLayout(panelNetworkResponseLayout);
        panelNetworkResponseLayout.setHorizontalGroup(
            panelNetworkResponseLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelNetworkResponseLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelResponseTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 204, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelNetworkResponseLayout.setVerticalGroup(
            panelNetworkResponseLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelNetworkResponseLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelNetworkResponseLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelResponseTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabbedPane.addTab("Network response", new javax.swing.ImageIcon(getClass().getResource("/resources/response.gif")), panelNetworkResponse); // NOI18N

        scrollPaneHelp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        editorPaneHelp.setEditable(false);
        scrollPaneHelp.setViewportView(editorPaneHelp);

        org.jdesktop.layout.GroupLayout panelHelpLayout = new org.jdesktop.layout.GroupLayout(panelHelp);
        panelHelp.setLayout(panelHelpLayout);
        panelHelpLayout.setHorizontalGroup(
            panelHelpLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelHelpLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollPaneHelp)
                .addContainerGap())
        );
        panelHelpLayout.setVerticalGroup(
            panelHelpLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelHelpLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollPaneHelp, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Help", new javax.swing.ImageIcon(getClass().getResource("/resources/help.gif")), panelHelp); // NOI18N

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/agh.gif"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel12.setText("XOR Problem");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Łukasz Bandzarewicz <lucassus@gmail.com>");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Maciej Kachel <kachelm@gmail.com>");

        jLabel9.setText("EiT AGH (C) 2006");

        jLabel13.setText("Supervisor: MSc. Adam Gołda");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel8)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel10)
                            .add(jLabel11)
                            .add(jLabel12)))
                    .add(jLabel9))
                .addContainerGap(64, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(276, Short.MAX_VALUE)
                .add(jLabel13)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel12)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 23, Short.MAX_VALUE)
                        .add(jLabel10)
                        .add(4, 4, 4)
                        .add(jLabel11)
                        .add(38, 38, 38)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel9)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel13)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout panelAboutLayout = new org.jdesktop.layout.GroupLayout(panelAbout);
        panelAbout.setLayout(panelAboutLayout);
        panelAboutLayout.setHorizontalGroup(
            panelAboutLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelAboutLayout.createSequentialGroup()
                .add(112, 112, 112)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        panelAboutLayout.setVerticalGroup(
            panelAboutLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelAboutLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(169, Short.MAX_VALUE))
        );

        tabbedPane.addTab("About", panelAbout);

        progressBarLearning.setStringPainted(true);

        labelApplicationStatus.setText("Iddle");

        buttonStartLearning.setText("Start learning");
        buttonStartLearning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startLearningEvent(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(buttonStartLearning, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
                    .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(labelApplicationStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 393, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(progressBarLearning, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonStartLearning)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(labelApplicationStatus)
                    .add(progressBarLearning, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboBoxActivationFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxActivationFunctionActionPerformed
        if (comboBoxActivationFunction.getSelectedIndex() == 1) {
            labelFunctionProperty.setEnabled(true);
            textFieldFunctionProperty.setEnabled(true);
        } else {
            labelFunctionProperty.setEnabled(false);
            textFieldFunctionProperty.setEnabled(false);
        }
    }//GEN-LAST:event_comboBoxActivationFunctionActionPerformed

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged
        if (tabbedPane.getSelectedIndex() == 2) {
            computeNetworkResponse();
            responsePanel.update();
        }
    }//GEN-LAST:event_tabbedPaneStateChanged

    private void comboBoxLearningPatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxLearningPatternActionPerformed
        String selected = (String) comboBoxLearningPattern.getSelectedItem();
        if (selected.equalsIgnoreCase("XOR")) {
            tableLearningData.setModel(new LearningPatternsDataModel(xorData));
        } else if (selected.equalsIgnoreCase("AND")) {
            tableLearningData.setModel(new LearningPatternsDataModel(andData));
        } else if (selected.equalsIgnoreCase("OR")) {
            tableLearningData.setModel(new LearningPatternsDataModel(orData));
        } else if (selected.equalsIgnoreCase("NAND")) {
            tableLearningData.setModel(new LearningPatternsDataModel(nandData));
        } else if (selected.equalsIgnoreCase("NOR")) {
            tableLearningData.setModel(new LearningPatternsDataModel(norData));
        }
    }//GEN-LAST:event_comboBoxLearningPatternActionPerformed

    private void spinnerDataPatternsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerDataPatternsStateChanged
        int newValue = Integer.parseInt(spinnerDataPatterns.getValue().toString());

        if (newValue > learningPatternsCount) {
            learningPatternsDataModel.addRow(new Object[]{0, 0, 0});
        } else if (newValue < learningPatternsCount) {
            learningPatternsDataModel.removeRow(learningPatternsCount - 1);
        }

        learningPatternsCount = newValue;
    }//GEN-LAST:event_spinnerDataPatternsStateChanged

    private void menuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemExitActionPerformed

        int answer = JOptionPane.showConfirmDialog(this, "Do you really want to exit?");

        if (answer == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

    }//GEN-LAST:event_menuItemExitActionPerformed

    private void menuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAboutActionPerformed
        AboutDialog about = new AboutDialog(new javax.swing.JFrame(), true);
        about.setLocationRelativeTo(this);
        about.setVisible(true);
    }//GEN-LAST:event_menuItemAboutActionPerformed

    private void menuItemHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemHelpActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_menuItemHelpActionPerformed

    private void spinnerHiddenLayersCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerHiddenLayersCountStateChanged
        int newValue = Integer.parseInt(spinnerHiddenLayersCount.getValue().toString());

        if (newValue > hiddenLayersCount) {
            hiddenLayersTableModel.addRow(new Object[2]);
        } else if (newValue < hiddenLayersCount) {
            hiddenLayersTableModel.removeRow(hiddenLayersCount - 1);
        }

        hiddenLayersCount = newValue;
    }//GEN-LAST:event_spinnerHiddenLayersCountStateChanged

    private void startLearningEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startLearningEvent

        if (state == ApplicationState.LEARNING_IN_PROGRESS) {

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    enableComponents(true);
                }
            });

            state = ApplicationState.IDDLE;

            return;
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                enableComponents(false);
            }
        });

        IActivationFunction activationFunction = null;

        int index = comboBoxActivationFunction.getSelectedIndex();
        switch (index) {
            case 0:
                activationFunction = new LinearFunction();
                break;
            case 1:
            default:
                double beta = 0.9;
                try {
                    beta = Double.parseDouble(textFieldFunctionProperty.getText());
                } catch (NumberFormatException e) {
                }
                activationFunction = new SigmoidFunction(beta);
                break;
            case 2:
                activationFunction = new TanhFunction();
                break;
            case 3:
                activationFunction = new GaussianFunction();
                break;
        }

        NeuralLayer[] layers = new NeuralLayer[hiddenLayersCount];
        for (int i = 0; i < hiddenLayersCount; i++) {
            int neuronsCount = Integer.parseInt(hiddenLayersTableModel.getValueAt(i, 1).toString());
            layers[i] = new NeuralLayer(neuronsCount, activationFunction);
        }

        neuralNetwork = new NeuralNetwork(2, 1, layers, activationFunction);

        // nerouns weights
        double lr = 0.9;
        double min = -1.0;
        double max = 1.0;
        try {
            lr = Double.parseDouble(textFieldLearningRate.getText());
            min = Double.parseDouble(textFieldRandomizeWeightsMin.getText());
            max = Double.parseDouble(textFieldRandomizeWeightsMax.getText());
        } catch (NumberFormatException e) {
        }
        neuralNetwork.randomizeWeight(min, max);
        // end neurons weights

        networkInput = new double[learningPatternsCount][2];
        networkDesiredOutput = new double[learningPatternsCount][];

        TableModel model = tableLearningData.getModel();
        for (int i = 0; i < learningPatternsCount; i++) {

            networkInput[i][0] = 0.0;
            networkInput[i][1] = 0.0;
            networkDesiredOutput[i] = new double[1];
            networkDesiredOutput[i][0] = 0.0;

            try {
                networkInput[i][0] = Double.parseDouble(model.getValueAt(i, 0).toString());
                networkInput[i][1] = Double.parseDouble(model.getValueAt(i, 1).toString());
                networkDesiredOutput[i][0] = Double.parseDouble(model.getValueAt(i, 2).toString());
            } catch (NumberFormatException e) {
            }

        }

        Object[][] data = new Object[networkInput.length][3];
        for (int i = 0; i < networkInput.length; i++) {
            data[i][0] = networkInput[i][0];
            data[i][1] = networkInput[i][1];
            data[i][2] = 0.0;
        }

        networkResponseTableModel = new NetworkResponseTableModel(data);
        tableNetworkResponse.setModel(networkResponseTableModel);

        buttonStartLearning.setText("Terminate learning");

        final int epochs = Integer.parseInt(spinnerEpochs.getValue().toString());
        progressBarLearning.setMaximum(epochs);
        final double learningRate = lr;

        Thread learningThread = new Thread(new Runnable() {

            public void run() {

                progressBarLearning.setValue(0);
                labelApplicationStatus.setText("Learning in progress");

                long startTime = System.currentTimeMillis();
                int i = 0;
                state = ApplicationState.LEARNING_IN_PROGRESS;
                while (state == ApplicationState.LEARNING_IN_PROGRESS && i < epochs) {
                    i++;

                    neuralNetwork.learn(networkInput, networkDesiredOutput, learningRate);

                    if (i % 10 == 0) {
                        progressBarLearning.setValue(i);
                        labelApplicationStatus.setText("Learning in progress, Trial #" + i + ", Error: " + neuralNetwork.getError());
                    }

                    if (tabbedPane.getSelectedIndex() == 2) {
                        // Response panel is visible
                        if (i % 5 == 0) {
                            computeNetworkResponse();
                            responsePanel.update();
                        }
                    }

                }

                long endTime = System.currentTimeMillis();
                double learningTime = ((double) (endTime - startTime)) / 1000;

                labelApplicationStatus.setText("Learnign finished after "
                        + learningTime
                        + " seconds,"
                        + " Error: " + neuralNetwork.getError());
                progressBarLearning.setValue(epochs);

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        enableComponents(true);
                        buttonStartLearning.setText("Start learning");
                    }
                });

                state = ApplicationState.IDDLE;
            }
        });

        learningThread.start();

    }//GEN-LAST:event_startLearningEvent

    private void computeNetworkResponse() {
        if (neuralNetwork == null) {
            return;
        }

        for (int i = 0; i < networkResponse.length; i++) {
            for (int j = 0; j < networkResponse[i].length; j++) {
                double[] var = {(double) i / networkResponse.length, (double) j / networkResponse[i].length};
                networkResponse[i][j] = neuralNetwork.compute(var)[0];
            }
        }

        networkOutput = new double[networkDesiredOutput.length];
        for (int i = 0; i < networkDesiredOutput.length; i++) {
            double[] var = {networkInput[i][0], networkInput[i][1]};
            networkOutput[i] = neuralNetwork.compute(var)[0];
        }

        Object[] data = new Object[networkDesiredOutput.length];
        for (int i = 0; i < networkDesiredOutput.length; i++) {
            data[i] = networkOutput[i];
            networkResponseTableModel.setValueAt(data[i], i, 2);
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonStartLearning;
    private javax.swing.JComboBox comboBoxActivationFunction;
    private javax.swing.JComboBox comboBoxLearningPattern;
    private javax.swing.JEditorPane editorPaneHelp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelApplicationStatus;
    private javax.swing.JLabel labelFunctionProperty;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuItemAbout;
    private javax.swing.JMenuItem menuItemExit;
    private javax.swing.JMenuItem menuItemHelp;
    private javax.swing.JMenu menuLookAndFeel;
    private javax.swing.JMenu menuOptions;
    private javax.swing.JPanel panelAbout;
    private javax.swing.JPanel panelActivationFunction;
    private javax.swing.JPanel panelEpochs;
    private javax.swing.JPanel panelHelp;
    private javax.swing.JPanel panelHiddenLayers;
    private javax.swing.JPanel panelLearning;
    private javax.swing.JPanel panelNetworkConfiguration;
    private javax.swing.JPanel panelNetworkResponse;
    private javax.swing.JPanel panelPatterns;
    private javax.swing.JPanel panelResponseTop;
    private javax.swing.JPanel panelWeights;
    private javax.swing.JProgressBar progressBarLearning;
    private javax.swing.JScrollPane scrollPaneHelp;
    private javax.swing.JScrollPane scrollPaneHiddenLayers;
    private javax.swing.JScrollPane scrollPaneLearningData;
    private javax.swing.JScrollPane scrollPaneNetworkResponse;
    private javax.swing.JSpinner spinnerDataPatterns;
    private javax.swing.JSpinner spinnerEpochs;
    private javax.swing.JSpinner spinnerHiddenLayersCount;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tableHiddenLayers;
    private javax.swing.JTable tableLearningData;
    private javax.swing.JTable tableNetworkResponse;
    private javax.swing.JTextField textFieldFunctionProperty;
    private javax.swing.JTextField textFieldLearningRate;
    private javax.swing.JTextField textFieldRandomizeWeightsMax;
    private javax.swing.JTextField textFieldRandomizeWeightsMin;
    // End of variables declaration//GEN-END:variables

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private void enableComponents(boolean enabled) {
        if (comboBoxActivationFunction.getSelectedIndex() == 1) {
            textFieldFunctionProperty.setEnabled(enabled);
        }

        spinnerHiddenLayersCount.setEnabled(enabled);
        tableHiddenLayers.setEnabled(enabled);

        spinnerEpochs.setEnabled(enabled);
        spinnerDataPatterns.setEnabled(enabled);
        textFieldLearningRate.setEnabled(enabled);
        comboBoxLearningPattern.setEnabled(enabled);
        tableLearningData.setEnabled(enabled);
    }
}
