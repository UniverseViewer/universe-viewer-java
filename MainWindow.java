/*
Copyright © 2008 FONTAINE Julie
Copyright © 2008 ABATI Mathieu
 
This file is part of Universe Viewer.

Universe Viewer is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

Universe Viewer is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Universe Viewer; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/** 
 * @author FONTAINE Julie
 * @author ABATI Mathieu
 */

import java.awt.Cursor;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ToolTipManager;


/**
 * Main window class, used to show the main window, and its content
 */
public class MainWindow extends javax.swing.JFrame {
    private static final int SLIDES_PRECISION = 10;
    SelectionManagerWindow selectionManager;
    
    /** Creates new form MainWindow */
    public MainWindow(SelectionManagerWindow manager) {
        initComponents();
        
        selectionManager = manager;
        
        // general
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);  // ToolTipText on top
        ToolTipManager.sharedInstance().setInitialDelay(0);  // ToolTip immediately displayed
        SelectionToolsPanel.setVisible(false);
        Viewer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // viewer canvas
        ViewerCanvas v = Environment.getopenGLViewerCanvas();
        Viewer.add(v.getCanvas());
        
        // setting spinners models and default values
        // setEditor is used here to have more than 3 decimals in the spinners
        // defaults values are taken in the Environment, and step is set to 0.0001
        // cosmological constraints
        LambdaSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(Environment.getLambda()), null, null, Double.valueOf(0.0001d)));
        LambdaSpinner.setEditor(new JSpinner.NumberEditor(LambdaSpinner, "#.#####"));
        OmegaSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(Environment.getOmega()), null, null, Double.valueOf(0.0001d)));
        OmegaSpinner.setEditor(new JSpinner.NumberEditor(OmegaSpinner, "#.#####"));
        KappaSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(Environment.getKappa()), null, null, Double.valueOf(0.0001d)));
        KappaSpinner.setEditor(new JSpinner.NumberEditor(KappaSpinner, "#.#####"));
        AlphaSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(Environment.getAlpha()), null, null, Double.valueOf(0.0001d)));
        AlphaSpinner.setEditor(new JSpinner.NumberEditor(AlphaSpinner, "#.#####"));
        
        // setting sliders default values and bounds
        Ra1Slider.setValue((int)(SLIDES_PRECISION*Environment.getUserRa1Rad()));
        Ra1Slider.setMaximum(SLIDES_PRECISION*24);
        Ra1Slider.setMinimum(0);
        Ra1ValueLabel.setText(Double.toString(SLIDES_PRECISION*Environment.getUserRa1Rad()));
        Dec1Slider.setValue((int)(SLIDES_PRECISION*Environment.getUserDec1Deg()));
        Dec1Slider.setMaximum(SLIDES_PRECISION*90);
        Dec1Slider.setMinimum(-SLIDES_PRECISION*90);
        Dec1ValueLabel.setText(Double.toString(SLIDES_PRECISION*Environment.getUserDec1Deg()));
        BetaSlider.setValue((int)(SLIDES_PRECISION*Environment.getUserBetaHours()));
        BetaSlider.setMaximum(SLIDES_PRECISION*24);
        BetaSlider.setMinimum(0);
        BetaValueLabel.setText(Double.toString(SLIDES_PRECISION*Environment.getUserBetaHours()));
        
        Environment.setMainWindow(this);
    }
    
    
    // ---------
    // ACCESSORS
    // ---------
    
    public void updateSelection()
    {
        selectionManager.updateTable();
    }
    
    
    // ------------
    // MISC METHODS
    // ------------
    
    /**
     * Universe Viewer and its interface configuration are set as Universe Mode
     */
    private void setUniverseMode()
    {
        ViewerCanvas v = Environment.getopenGLViewerCanvas();            
        try{ v.setMode(v.UNIVERSE_MODE); }
        catch (Exception ex) {}

        // enable some widgets
        if(Environment.getKappa() != 0.0d)
        {
            View1ToggleButton.setEnabled(true);
            View2ToggleButton.setEnabled(true);
            View3ToggleButton.setEnabled(true);
        }
        View4ToggleButton.setEnabled(true);
        View5ToggleButton.setEnabled(true);
        View6ToggleButton.setEnabled(true);
        Ra1Slider.setEnabled(true);
        Dec1Slider.setEnabled(true);
        BetaSlider.setEnabled(true);
        if(LambdaRadio.isSelected() == false)
            LambdaSpinner.setEnabled(true);
        if(OmegaRadio.isSelected() == false)
            OmegaSpinner.setEnabled(true);
        if(KappaRadio.isSelected() == false)
            KappaSpinner.setEnabled(true);
        if(AlphaRadio.isSelected() == false)
            AlphaSpinner.setEnabled(true);
        LambdaRadio.setEnabled(true);
        OmegaRadio.setEnabled(true);
        KappaRadio.setEnabled(true);
        AlphaRadio.setEnabled(true);
    }
    
    /**
     * Universe Viewer and its interface configuration are set as Sky Mode
     */
    private void setSkyMode()
    {
        ViewerCanvas v = Environment.getopenGLViewerCanvas();   
        try{ v.setMode(v.SKY_MODE); }
        catch (Exception ex) {}

        // disable some widgets
        View1ToggleButton.setEnabled(false);
        View2ToggleButton.setEnabled(false);
        View3ToggleButton.setEnabled(false);
        View4ToggleButton.setEnabled(false);
        View5ToggleButton.setEnabled(false);
        View6ToggleButton.setEnabled(false);
        Ra1Slider.setEnabled(false);
        Dec1Slider.setEnabled(false);
        BetaSlider.setEnabled(false);
        LambdaSpinner.setEnabled(false);
        OmegaSpinner.setEnabled(false);
        KappaSpinner.setEnabled(false);
        AlphaSpinner.setEnabled(false);
        LambdaRadio.setEnabled(false);
        OmegaRadio.setEnabled(false);
        KappaRadio.setEnabled(false);
        AlphaRadio.setEnabled(false);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CosmoConstsGroup = new javax.swing.ButtonGroup();
        PleaseWaitFrame = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        ViewsButtonGroup = new javax.swing.ButtonGroup();
        ModeButtonGroup = new javax.swing.ButtonGroup();
        SelectionButtonGroup = new javax.swing.ButtonGroup();
        Viewer = new javax.swing.JPanel();
        CosmoConstsPanel = new javax.swing.JPanel();
        AlphaSpinner = new javax.swing.JSpinner();
        AlphaRadio = new javax.swing.JRadioButton();
        KappaSpinner = new javax.swing.JSpinner();
        KappaRadio = new javax.swing.JRadioButton();
        OmegaSpinner = new javax.swing.JSpinner();
        OmegaRadio = new javax.swing.JRadioButton();
        LambdaSpinner = new javax.swing.JSpinner();
        LambdaRadio = new javax.swing.JRadioButton();
        CurrentViewPanel = new javax.swing.JPanel();
        View1ToggleButton = new javax.swing.JToggleButton();
        View4ToggleButton = new javax.swing.JToggleButton();
        View2ToggleButton = new javax.swing.JToggleButton();
        View5ToggleButton = new javax.swing.JToggleButton();
        View3ToggleButton = new javax.swing.JToggleButton();
        View6ToggleButton = new javax.swing.JToggleButton();
        RaLabel = new javax.swing.JLabel();
        Dec1Label = new javax.swing.JLabel();
        BetaLabel = new javax.swing.JLabel();
        Ra1Slider = new javax.swing.JSlider();
        Dec1Slider = new javax.swing.JSlider();
        BetaSlider = new javax.swing.JSlider();
        Ra1ValueLabel = new javax.swing.JLabel();
        Dec1ValueLabel = new javax.swing.JLabel();
        BetaValueLabel = new javax.swing.JLabel();
        ComovingCheckBox = new javax.swing.JCheckBox();
        PrecisionCheckBox = new javax.swing.JCheckBox();
        SkyBtn = new javax.swing.JToggleButton();
        InfoLabel = new javax.swing.JLabel();
        SelectModeBtn = new javax.swing.JToggleButton();
        MoveModeBtn = new javax.swing.JToggleButton();
        Separator1 = new javax.swing.JSeparator();
        SelectionToolsPanel = new javax.swing.JPanel();
        ResetSelectBtn = new javax.swing.JButton();
        MultipleSelectBtn = new javax.swing.JToggleButton();
        ComboSelectBtn = new javax.swing.JToggleButton();
        Separator2 = new javax.swing.JSeparator();
        ShowReferencesMarksBtn = new javax.swing.JToggleButton();
        MenuBar = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        OpenMenu = new javax.swing.JMenuItem();
        ExitMenu = new javax.swing.JMenuItem();
        SettingsMenu = new javax.swing.JMenu();
        PrecisionMenu = new javax.swing.JCheckBoxMenuItem();
        ComovingSpaceMenu = new javax.swing.JCheckBoxMenuItem();
        SelectionMenu = new javax.swing.JMenu();
        ManageSelectionMenu = new javax.swing.JMenuItem();
        HelpMenu = new javax.swing.JMenu();
        AboutMenu = new javax.swing.JMenuItem();

        jLabel1.setText("Please wait...");

        javax.swing.GroupLayout PleaseWaitFrameLayout = new javax.swing.GroupLayout(PleaseWaitFrame.getContentPane());
        PleaseWaitFrame.getContentPane().setLayout(PleaseWaitFrameLayout);
        PleaseWaitFrameLayout.setHorizontalGroup(
            PleaseWaitFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PleaseWaitFrameLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jLabel1)
                .addContainerGap(89, Short.MAX_VALUE))
        );
        PleaseWaitFrameLayout.setVerticalGroup(
            PleaseWaitFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PleaseWaitFrameLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Universe Viewer");
        setBounds(new java.awt.Rectangle(0, 0, 800, 600));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Viewer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ViewerMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ViewerMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout ViewerLayout = new javax.swing.GroupLayout(Viewer);
        Viewer.setLayout(ViewerLayout);
        ViewerLayout.setHorizontalGroup(
            ViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        ViewerLayout.setVerticalGroup(
            ViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        getContentPane().add(Viewer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 500, 500));

        CosmoConstsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Consmological constants"));
        CosmoConstsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AlphaSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                AlphaSpinnerStateChanged(evt);
            }
        });
        CosmoConstsPanel.add(AlphaSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 150, -1));

        CosmoConstsGroup.add(AlphaRadio);
        AlphaRadio.setText("Alpha");
        AlphaRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AlphaRadioActionPerformed(evt);
            }
        });
        CosmoConstsPanel.add(AlphaRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));

        KappaSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KappaSpinnerStateChanged(evt);
            }
        });
        CosmoConstsPanel.add(KappaSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 150, -1));

        CosmoConstsGroup.add(KappaRadio);
        KappaRadio.setText("Kappa");
        KappaRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KappaRadioActionPerformed(evt);
            }
        });
        CosmoConstsPanel.add(KappaRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        OmegaSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                OmegaSpinnerStateChanged(evt);
            }
        });
        CosmoConstsPanel.add(OmegaSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 150, -1));

        CosmoConstsGroup.add(OmegaRadio);
        OmegaRadio.setText("Omega");
        OmegaRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OmegaRadioActionPerformed(evt);
            }
        });
        CosmoConstsPanel.add(OmegaRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        LambdaSpinner.setEnabled(false);
        LambdaSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                LambdaSpinnerStateChanged(evt);
            }
        });
        CosmoConstsPanel.add(LambdaSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 150, -1));

        CosmoConstsGroup.add(LambdaRadio);
        LambdaRadio.setSelected(true);
        LambdaRadio.setText("Lambda");
        LambdaRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LambdaRadioActionPerformed(evt);
            }
        });
        CosmoConstsPanel.add(LambdaRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        getContentPane().add(CosmoConstsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 310, 190));

        CurrentViewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Current view"));
        CurrentViewPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ViewsButtonGroup.add(View1ToggleButton);
        View1ToggleButton.setSelected(true);
        View1ToggleButton.setText("View Edge 1");
        View1ToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View1ToggleButtonActionPerformed(evt);
            }
        });
        CurrentViewPanel.add(View1ToggleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 120, -1));

        ViewsButtonGroup.add(View4ToggleButton);
        View4ToggleButton.setText("View Front 1");
        View4ToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View4ToggleButtonActionPerformed(evt);
            }
        });
        CurrentViewPanel.add(View4ToggleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 120, -1));

        ViewsButtonGroup.add(View2ToggleButton);
        View2ToggleButton.setText("View Edge 2");
        View2ToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View2ToggleButtonActionPerformed(evt);
            }
        });
        CurrentViewPanel.add(View2ToggleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 120, -1));

        ViewsButtonGroup.add(View5ToggleButton);
        View5ToggleButton.setText("View Front 2");
        View5ToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View5ToggleButtonActionPerformed(evt);
            }
        });
        CurrentViewPanel.add(View5ToggleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 120, -1));

        ViewsButtonGroup.add(View3ToggleButton);
        View3ToggleButton.setText("View Edge 3");
        View3ToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View3ToggleButtonActionPerformed(evt);
            }
        });
        CurrentViewPanel.add(View3ToggleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 120, -1));

        ViewsButtonGroup.add(View6ToggleButton);
        View6ToggleButton.setText("View Front 3");
        View6ToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View6ToggleButtonActionPerformed(evt);
            }
        });
        CurrentViewPanel.add(View6ToggleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 120, -1));

        RaLabel.setText("RA1:");
        CurrentViewPanel.add(RaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 50, -1));

        Dec1Label.setText("Dec1:");
        CurrentViewPanel.add(Dec1Label, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        BetaLabel.setText("Beta");
        CurrentViewPanel.add(BetaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        Ra1Slider.setMaximum(24);
        Ra1Slider.setValue(0);
        Ra1Slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                Ra1SliderStateChanged(evt);
            }
        });
        CurrentViewPanel.add(Ra1Slider, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, 200, -1));

        Dec1Slider.setMaximum(90);
        Dec1Slider.setMinimum(-90);
        Dec1Slider.setValue(0);
        Dec1Slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                Dec1SliderStateChanged(evt);
            }
        });
        CurrentViewPanel.add(Dec1Slider, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 200, -1));

        BetaSlider.setMaximum(24);
        BetaSlider.setPaintTicks(true);
        BetaSlider.setValue(0);
        BetaSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                BetaSliderStateChanged(evt);
            }
        });
        CurrentViewPanel.add(BetaSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 200, -1));

        Ra1ValueLabel.setText("0");
        CurrentViewPanel.add(Ra1ValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, -1, -1));

        Dec1ValueLabel.setText("0");
        CurrentViewPanel.add(Dec1ValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 190, -1, -1));

        BetaValueLabel.setText("0");
        CurrentViewPanel.add(BetaValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 230, -1, -1));

        getContentPane().add(CurrentViewPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 310, 280));

        ComovingCheckBox.setText("Comoving");
        ComovingCheckBox.setEnabled(false);
        getContentPane().add(ComovingCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 560, -1, -1));

        PrecisionCheckBox.setSelected(true);
        PrecisionCheckBox.setText("Precision");
        PrecisionCheckBox.setEnabled(false);
        PrecisionCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrecisionCheckBoxActionPerformed(evt);
            }
        });
        getContentPane().add(PrecisionCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 560, -1, -1));

        SkyBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/sky_mode.png"))); // NOI18N
        SkyBtn.setToolTipText("Sky mode");
        SkyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SkyBtnActionPerformed(evt);
            }
        });
        getContentPane().add(SkyBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 30, -1));

        InfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        InfoLabel.setText("Welcome!!!");
        InfoLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(InfoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 560, 530, -1));

        ModeButtonGroup.add(SelectModeBtn);
        SelectModeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/select.png"))); // NOI18N
        SelectModeBtn.setToolTipText("Selection mode");
        SelectModeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectModeBtnActionPerformed(evt);
            }
        });
        getContentPane().add(SelectModeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 30, -1));

        ModeButtonGroup.add(MoveModeBtn);
        MoveModeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/move.png"))); // NOI18N
        MoveModeBtn.setSelected(true);
        MoveModeBtn.setToolTipText("Move mode");
        MoveModeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoveModeBtnActionPerformed(evt);
            }
        });
        getContentPane().add(MoveModeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 30, -1));

        Separator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(Separator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 10, 35));

        ResetSelectBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/reset.png"))); // NOI18N
        ResetSelectBtn.setToolTipText("Reset selection");
        ResetSelectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetSelectBtnActionPerformed(evt);
            }
        });

        SelectionButtonGroup.add(MultipleSelectBtn);
        MultipleSelectBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/multiple_select.png"))); // NOI18N
        MultipleSelectBtn.setSelected(true);
        MultipleSelectBtn.setToolTipText("Enable multiple selection");
        MultipleSelectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MultipleSelectBtnActionPerformed(evt);
            }
        });

        SelectionButtonGroup.add(ComboSelectBtn);
        ComboSelectBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/combo_select.png"))); // NOI18N
        ComboSelectBtn.setToolTipText("Enable combo selection");
        ComboSelectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboSelectBtnActionPerformed(evt);
            }
        });

        Separator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout SelectionToolsPanelLayout = new javax.swing.GroupLayout(SelectionToolsPanel);
        SelectionToolsPanel.setLayout(SelectionToolsPanelLayout);
        SelectionToolsPanelLayout.setHorizontalGroup(
            SelectionToolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectionToolsPanelLayout.createSequentialGroup()
                .addComponent(Separator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(MultipleSelectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(ComboSelectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(ResetSelectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        SelectionToolsPanelLayout.setVerticalGroup(
            SelectionToolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectionToolsPanelLayout.createSequentialGroup()
                .addGroup(SelectionToolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Separator2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MultipleSelectBtn)
                    .addComponent(ComboSelectBtn)
                    .addComponent(ResetSelectBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(SelectionToolsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 130, 40));

        ShowReferencesMarksBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/grid.png"))); // NOI18N
        ShowReferencesMarksBtn.setSelected(true);
        ShowReferencesMarksBtn.setToolTipText("Show references marks");
        ShowReferencesMarksBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowReferencesMarksBtnActionPerformed(evt);
            }
        });
        getContentPane().add(ShowReferencesMarksBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 30, -1));

        FileMenu.setText("File");
        FileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileMenuActionPerformed(evt);
            }
        });

        OpenMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/open.png"))); // NOI18N
        OpenMenu.setText("Open...");
        OpenMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenMenuActionPerformed(evt);
            }
        });
        FileMenu.add(OpenMenu);

        ExitMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit.png"))); // NOI18N
        ExitMenu.setText("Exit");
        ExitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitMenuActionPerformed(evt);
            }
        });
        FileMenu.add(ExitMenu);

        MenuBar.add(FileMenu);

        SettingsMenu.setText("Settings");

        PrecisionMenu.setSelected(true);
        PrecisionMenu.setText("Enable more precision (takes more time)");
        PrecisionMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/precision.png"))); // NOI18N
        PrecisionMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrecisionMenuActionPerformed(evt);
            }
        });
        SettingsMenu.add(PrecisionMenu);

        ComovingSpaceMenu.setText("Comoving Space");
        ComovingSpaceMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/comoving.png"))); // NOI18N
        ComovingSpaceMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComovingSpaceMenuActionPerformed(evt);
            }
        });
        SettingsMenu.add(ComovingSpaceMenu);

        MenuBar.add(SettingsMenu);

        SelectionMenu.setText("Selection");

        ManageSelectionMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/select.png"))); // NOI18N
        ManageSelectionMenu.setText("Manage selection");
        ManageSelectionMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ManageSelectionMenuActionPerformed(evt);
            }
        });
        SelectionMenu.add(ManageSelectionMenu);

        MenuBar.add(SelectionMenu);

        HelpMenu.setText("Help");

        AboutMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/about.png"))); // NOI18N
        AboutMenu.setText("About...");
        AboutMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutMenuActionPerformed(evt);
            }
        });
        HelpMenu.add(AboutMenu);

        MenuBar.add(HelpMenu);

        setJMenuBar(MenuBar);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-847)/2, (screenSize.height-632)/2, 847, 632);
    }// </editor-fold>//GEN-END:initComponents

    private void OmegaRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OmegaRadioActionPerformed
        LambdaSpinner.setEnabled(true);
        OmegaSpinner.setEnabled(false);
        KappaSpinner.setEnabled(true);
        AlphaSpinner.setEnabled(true);
}//GEN-LAST:event_OmegaRadioActionPerformed

    private void LambdaRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LambdaRadioActionPerformed
        LambdaSpinner.setEnabled(false);
        OmegaSpinner.setEnabled(true);
        KappaSpinner.setEnabled(true);
        AlphaSpinner.setEnabled(true);
    }//GEN-LAST:event_LambdaRadioActionPerformed

    private void KappaRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KappaRadioActionPerformed
        LambdaSpinner.setEnabled(true);
        OmegaSpinner.setEnabled(true);
        KappaSpinner.setEnabled(false);
        AlphaSpinner.setEnabled(true);
}//GEN-LAST:event_KappaRadioActionPerformed

    private void AlphaRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AlphaRadioActionPerformed
        LambdaSpinner.setEnabled(true);
        OmegaSpinner.setEnabled(true);
        KappaSpinner.setEnabled(true);
        AlphaSpinner.setEnabled(false);
    }//GEN-LAST:event_AlphaRadioActionPerformed
    
    /**
     * Cosmological constants spinners values have changed.
     * This methode compute new values according to this equality:
     * lambda - kappa + omega + alpha = 1
     */
    private void CosmoConstsSpinnerStateChanged()
    {
        // getting current spinners values
        Double lambda = (Double) LambdaSpinner.getValue();
        Double omega = (Double) OmegaSpinner.getValue();
        Double kappa = (Double) KappaSpinner.getValue();
        Double alpha = (Double) AlphaSpinner.getValue();
        if(UniverseViewer.floor(lambda - kappa + omega + alpha, 5) == 1.0d)
            return;

        // constraint: lambda - kappa + omega + alpha = 1
        // computing new needed value
        if (LambdaRadio.isSelected())
            lambda = 1.0d + kappa - omega - alpha;
        else if (OmegaRadio.isSelected())
            omega = 1.0d - lambda + kappa - alpha;
        else if (KappaRadio.isSelected())
            kappa = lambda + alpha + omega - 1.0d;
        else if (AlphaRadio.isSelected())
            alpha = 1.0d - lambda + kappa - omega;

        // updating consological constraints
        try
        {
            Environment.setCosmoConsts(lambda, omega, kappa, alpha);
        }
        catch (Exception ex)    // fail, restoring old values
        {
            lambda = Environment.getLambda();
            omega = Environment.getOmega();
            kappa = Environment.getKappa();
            alpha = Environment.getAlpha();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cosmological constants", JOptionPane.WARNING_MESSAGE);
        }
        
        // updating spinners
        LambdaSpinner.setValue(lambda);
        OmegaSpinner.setValue(omega);
        KappaSpinner.setValue(kappa);
        AlphaSpinner.setValue(alpha);
        
        // if kappa = 0 (comoging space only), view 1, 2 and 3 (profile views) dont exist
        if(kappa == 0.0d)
        {
            // disabling profile views toggle buttons
            View1ToggleButton.setEnabled(false);
            View2ToggleButton.setEnabled(false);
            View3ToggleButton.setEnabled(false);
            // changing view if necessary
            if((View4ToggleButton.isSelected() == false) && (View5ToggleButton.isSelected() == false) && (View6ToggleButton.isSelected() == false))
            {
                View4ToggleButton.setSelected(true);
                try{ Environment.setView(4); }
                catch (Exception ex) {}
            }
        }
        else
        {
            View1ToggleButton.setEnabled(true);
            View2ToggleButton.setEnabled(true);
            View3ToggleButton.setEnabled(true);
        }
        
        try{ Environment.update(Environment.UPDATE_ALL); }
        catch (Exception ex) {}
    }
    private void OmegaSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_OmegaSpinnerStateChanged
        CosmoConstsSpinnerStateChanged();
    }//GEN-LAST:event_OmegaSpinnerStateChanged
    private void LambdaSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_LambdaSpinnerStateChanged
        CosmoConstsSpinnerStateChanged();
    }//GEN-LAST:event_LambdaSpinnerStateChanged
    private void KappaSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KappaSpinnerStateChanged
        CosmoConstsSpinnerStateChanged();
}//GEN-LAST:event_KappaSpinnerStateChanged
    private void AlphaSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_AlphaSpinnerStateChanged
        CosmoConstsSpinnerStateChanged();
    }//GEN-LAST:event_AlphaSpinnerStateChanged

    private void PrecisionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrecisionCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PrecisionCheckBoxActionPerformed
                              
    private void View1ToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_View1ToggleButtonActionPerformed
        try{ Environment.setView(1); }//GEN-LAST:event_View1ToggleButtonActionPerformed
        catch (Exception ex) {}
        try{ Environment.update(Environment.UPDATE_VIEW); }
        catch (Exception ex) {}
    }                                                 
    private void View2ToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_View2ToggleButtonActionPerformed
        try{ Environment.setView(2); }
        catch (Exception ex) {}
        try{ Environment.update(Environment.UPDATE_VIEW); }
        catch (Exception ex) {}
    }//GEN-LAST:event_View2ToggleButtonActionPerformed
    private void View3ToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_View3ToggleButtonActionPerformed
        try{ Environment.setView(3); }
        catch (Exception ex) {}
        try{ Environment.update(Environment.UPDATE_VIEW); }
        catch (Exception ex) {}
    }//GEN-LAST:event_View3ToggleButtonActionPerformed
    private void View4ToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_View4ToggleButtonActionPerformed
        try{ Environment.setView(4); }
        catch (Exception ex) {}
        try{ Environment.update(Environment.UPDATE_VIEW); }
        catch (Exception ex) {}
    }//GEN-LAST:event_View4ToggleButtonActionPerformed
    private void View5ToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_View5ToggleButtonActionPerformed
        try{ Environment.setView(5); }
        catch (Exception ex) {}
        try{ Environment.update(Environment.UPDATE_VIEW); }
        catch (Exception ex) {}
    }//GEN-LAST:event_View5ToggleButtonActionPerformed
    private void View6ToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_View6ToggleButtonActionPerformed
        try{ Environment.setView(6); }
        catch (Exception ex) {}
        try{ Environment.update(Environment.UPDATE_VIEW); }
        catch (Exception ex) {}
    }//GEN-LAST:event_View6ToggleButtonActionPerformed

    private void Ra1SliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_Ra1SliderStateChanged
        double val = (double)Ra1Slider.getValue()/SLIDES_PRECISION;
        Environment.setUserRa1(val);
        Ra1ValueLabel.setText(Double.toString(val));
        try{ Environment.update(Environment.UPDATE_VIEW); }
        catch (Exception ex) {}
}//GEN-LAST:event_Ra1SliderStateChanged

    private void Dec1SliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_Dec1SliderStateChanged
        double val = (double)Dec1Slider.getValue()/SLIDES_PRECISION;
        Environment.setUserDec1(val);
        Dec1ValueLabel.setText(Double.toString(val));
        try{ Environment.update(Environment.UPDATE_VIEW); }
        catch (Exception ex) {}
}//GEN-LAST:event_Dec1SliderStateChanged

    private void BetaSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_BetaSliderStateChanged
        double val = (double)BetaSlider.getValue()/SLIDES_PRECISION;
        Environment.setUserBeta(val);
        BetaValueLabel.setText(Double.toString(val));
        try{ Environment.update(Environment.UPDATE_VIEW); }
        catch (Exception ex) {}
}//GEN-LAST:event_BetaSliderStateChanged

    private void FileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FileMenuActionPerformed

    private void OpenMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenMenuActionPerformed
        JFileChooser choice = new JFileChooser();

        int retval = choice.showOpenDialog(this);
        if(retval == JFileChooser.APPROVE_OPTION)
        {
            String filename = choice.getSelectedFile().getAbsolutePath();
            ReadFile.read_txt_file(filename);
            
            setUniverseMode();
            
            try{ Environment.update(Environment.UPDATE_ALL); }
            catch (Exception ex) {}
            
            // updating infoLabel
            InfoLabel.setText(Environment.getQuasars().size() + " quasars loaded");
        }
    }//GEN-LAST:event_OpenMenuActionPerformed

    private void ExitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitMenuActionPerformed
         int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit", JOptionPane.OK_CANCEL_OPTION);
         
         if(choice == 0)
             System.exit(0);
    }//GEN-LAST:event_ExitMenuActionPerformed

    private void PrecisionMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrecisionMenuActionPerformed
        if(PrecisionMenu.isSelected())
        {
            Environment.enablePrecision(true);
            PrecisionCheckBox.setSelected(true);
        }
        else
        {
            Environment.enablePrecision(false);
            PrecisionCheckBox.setSelected(false);
        }
        
        // updating
        try{ Environment.update(Environment.UPDATE_ALL); }
        catch (Exception ex) {}
    }//GEN-LAST:event_PrecisionMenuActionPerformed

    private JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    private void AboutMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutMenuActionPerformed
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon1 = (ImageIcon) UniverseViewer.createImage("icons/authors.png");
        ImageIcon icon2 = (ImageIcon) UniverseViewer.createImage("icons/thanks_to.png");
        ImageIcon icon3 = (ImageIcon) UniverseViewer.createImage("icons/license.png");
        ImageIcon icon4 = (ImageIcon) UniverseViewer.createImage("icons/credits.png");

        JComponent panel1 = makeTextPanel("FONTAINE Julie - ABATI Mathieu");
        JComponent panel2 = makeTextPanel("TRIAY Roland");
        String ligne = "";
        String temp = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("COPYING")));
            while((ligne = br.readLine())!=null)
                temp = temp+'\n'+ligne;
        }
        catch(Exception e)
        {
            System.out.println("Cannot Open File : COPYING");
        }
        JTextArea text3 = new JTextArea(temp);
        temp = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("CREDITS")));
            while((ligne = br.readLine())!=null)
                temp = temp+'\n'+ligne;
        }
        catch(Exception e)
        {
            System.out.println("Cannot Open File : CREDITS");
        }
        JTextArea text4 = new JTextArea(temp);
        JScrollPane scroll = new JScrollPane(text3);
        JScrollPane scroll2 = new JScrollPane(text4);
        text3.setEditable(false);
        tabbedPane.addTab("Authors", icon1, panel1, "Authors");
        tabbedPane.addTab("Thanks to...", icon2, panel2, "Thanks to...");
        tabbedPane.addTab("License...", icon3, scroll, "License...");
        tabbedPane.addTab("Credits", icon4, scroll2, "Credits");
        
        JDialog DialogAbout = new JDialog(this, "About...", true);
        DialogAbout.setSize(550, 300);
        DialogAbout.setLocationRelativeTo(null);
        DialogAbout.add(tabbedPane);
        DialogAbout.setVisible(true);
        DialogAbout.setResizable(false);
    }//GEN-LAST:event_AboutMenuActionPerformed

    private void ComovingSpaceMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComovingSpaceMenuActionPerformed
        try
        {
            Environment.comovingSpace(ComovingSpaceMenu.isSelected());
            ComovingCheckBox.setSelected(ComovingSpaceMenu.isSelected());
        }
        catch(Exception e)
        {
            ComovingSpaceMenu.setSelected(true);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Cosmological constants", JOptionPane.WARNING_MESSAGE);
        }
        
        // updating
        try{ Environment.update(Environment.UPDATE_ALL); }
        catch (Exception ex) {}
}//GEN-LAST:event_ComovingSpaceMenuActionPerformed

    private void SkyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SkyBtnActionPerformed
        if(SkyBtn.isSelected())
        {
            setSkyMode();
        }
        else
        {
            setUniverseMode();
        }

        // updating
        try{ Environment.update(Environment.UPDATE_VIEWER); }
        catch (Exception ex) {}
    }//GEN-LAST:event_SkyBtnActionPerformed

    private void ResetSelectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetSelectBtnActionPerformed
        Vector<Quasar> q = Environment.getQuasars();
        if(q != null)
        {
            for(int i=0; i<q.size(); i++)
                q.get(i).setSelected(false);
            Quasar.setSelectedCount(0);
            Environment.getopenGLViewerCanvas().updateCanvas();
        }
    }//GEN-LAST:event_ResetSelectBtnActionPerformed

    private void SelectModeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectModeBtnActionPerformed
        SelectionToolsPanel.setVisible(true);
        Viewer.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        Environment.getopenGLViewerCanvas().enableSelectionMode(true);
    }//GEN-LAST:event_SelectModeBtnActionPerformed

    private void MoveModeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MoveModeBtnActionPerformed
        SelectionToolsPanel.setVisible(false);
        Viewer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Environment.getopenGLViewerCanvas().enableSelectionMode(false);
    }//GEN-LAST:event_MoveModeBtnActionPerformed

    private void MultipleSelectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MultipleSelectBtnActionPerformed
        Quasar.setMultipleSelection(true);
    }//GEN-LAST:event_MultipleSelectBtnActionPerformed

    private void ComboSelectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboSelectBtnActionPerformed
        Quasar.setMultipleSelection(false);
    }//GEN-LAST:event_ComboSelectBtnActionPerformed

    private void ShowReferencesMarksBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowReferencesMarksBtnActionPerformed
        Environment.getopenGLViewerCanvas().setShowReferencesMarks(ShowReferencesMarksBtn.isSelected());
        // updating
        try{ Environment.update(Environment.UPDATE_VIEWER); }
        catch (Exception ex) {}
    }//GEN-LAST:event_ShowReferencesMarksBtnActionPerformed

    private void ManageSelectionMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ManageSelectionMenuActionPerformed
        selectionManager.setVisible(true);
        selectionManager.updateTable();
    }//GEN-LAST:event_ManageSelectionMenuActionPerformed

    private void ViewerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewerMousePressed

    }//GEN-LAST:event_ViewerMousePressed

    private void ViewerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewerMouseReleased

    }//GEN-LAST:event_ViewerMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AboutMenu;
    private javax.swing.JRadioButton AlphaRadio;
    private javax.swing.JSpinner AlphaSpinner;
    private javax.swing.JLabel BetaLabel;
    private javax.swing.JSlider BetaSlider;
    private javax.swing.JLabel BetaValueLabel;
    private javax.swing.JToggleButton ComboSelectBtn;
    private javax.swing.JCheckBox ComovingCheckBox;
    private javax.swing.JCheckBoxMenuItem ComovingSpaceMenu;
    private javax.swing.ButtonGroup CosmoConstsGroup;
    private javax.swing.JPanel CosmoConstsPanel;
    private javax.swing.JPanel CurrentViewPanel;
    private javax.swing.JLabel Dec1Label;
    private javax.swing.JSlider Dec1Slider;
    private javax.swing.JLabel Dec1ValueLabel;
    private javax.swing.JMenuItem ExitMenu;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JMenu HelpMenu;
    private javax.swing.JLabel InfoLabel;
    private javax.swing.JRadioButton KappaRadio;
    private javax.swing.JSpinner KappaSpinner;
    private javax.swing.JRadioButton LambdaRadio;
    private javax.swing.JSpinner LambdaSpinner;
    private javax.swing.JMenuItem ManageSelectionMenu;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.ButtonGroup ModeButtonGroup;
    private javax.swing.JToggleButton MoveModeBtn;
    private javax.swing.JToggleButton MultipleSelectBtn;
    private javax.swing.JRadioButton OmegaRadio;
    private javax.swing.JSpinner OmegaSpinner;
    private javax.swing.JMenuItem OpenMenu;
    private javax.swing.JFrame PleaseWaitFrame;
    private javax.swing.JCheckBox PrecisionCheckBox;
    private javax.swing.JCheckBoxMenuItem PrecisionMenu;
    private javax.swing.JSlider Ra1Slider;
    private javax.swing.JLabel Ra1ValueLabel;
    private javax.swing.JLabel RaLabel;
    private javax.swing.JButton ResetSelectBtn;
    private javax.swing.JToggleButton SelectModeBtn;
    private javax.swing.ButtonGroup SelectionButtonGroup;
    private javax.swing.JMenu SelectionMenu;
    private javax.swing.JPanel SelectionToolsPanel;
    private javax.swing.JSeparator Separator1;
    private javax.swing.JSeparator Separator2;
    private javax.swing.JMenu SettingsMenu;
    private javax.swing.JToggleButton ShowReferencesMarksBtn;
    private javax.swing.JToggleButton SkyBtn;
    private javax.swing.JToggleButton View1ToggleButton;
    private javax.swing.JToggleButton View2ToggleButton;
    private javax.swing.JToggleButton View3ToggleButton;
    private javax.swing.JToggleButton View4ToggleButton;
    private javax.swing.JToggleButton View5ToggleButton;
    private javax.swing.JToggleButton View6ToggleButton;
    private javax.swing.JPanel Viewer;
    private javax.swing.ButtonGroup ViewsButtonGroup;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
    
}
