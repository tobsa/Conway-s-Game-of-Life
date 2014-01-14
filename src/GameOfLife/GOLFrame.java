package GameOfLife;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GOLFrame extends JFrame {
    private JButton simulationStartButton = new JButton("Start Simulation");
    private JButton simulationStopButton  = new JButton("Stop Simulation");
    private JButton simulationClearButton = new JButton("Clear Simulation");
    private JButton simulationNextButton  = new JButton("Next Simulation");
    private JSlider simulationSpeedSlider = new JSlider(0, 100, 100);
    private JSlider simulationStepSlider  = new JSlider(1, 10,  1);
    private JList<String> patternList     = new JList();
    private JScrollPane patternScrollPane = new JScrollPane(patternList);
    private Board simulationBoard = new Board(150, 120, 5);
    private Timer timer;
    private Map<String, List<Point>> patterns = new LinkedHashMap();
    private JTextField columnTextfield   = new JTextField(5);
    private JTextField rowTextfield      = new JTextField(5);
    private JTextField gridSizeTextfield = new JTextField(5);
    private JButton recreateButton       = new JButton("Recreate");
    
    public GOLFrame() {
        initComponents();
        initListeners();    
        initPanels();
   
        pack();
    }
    
    private void initComponents() {
        simulationStopButton.setEnabled(false);
        
        simulationSpeedSlider.setMajorTickSpacing(10);
        simulationSpeedSlider.setPaintTicks(true);
        simulationSpeedSlider.setPaintLabels(true);
        
        simulationStepSlider.setMajorTickSpacing(1);
        simulationStepSlider.setPaintTicks(true);
        simulationStepSlider.setPaintLabels(true);
        
        File folder = new File("patterns");
        File[] files = folder.listFiles();
        
        for(File file : files)
            patterns.put(file.getName(), loadPattern(file.toString()));        
        
        DefaultListModel model = new DefaultListModel();        
        for(String name : patterns.keySet())
            model.addElement(name);
        
        patternList.setModel(model);
        patternList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patternList.setSelectedIndex(0);
        patternScrollPane.setPreferredSize(new Dimension(200, 600));
    }
    
    private void initListeners() {
        timer = new Timer(simulationSpeedSlider.getMaximum() - simulationSpeedSlider.getValue(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerActionPerformed(e);
            }
        });
        
        simulationStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulationStartButtonActionPerformed(e);
            }
        });
        
        simulationStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulationStopButtonActionPerformed(e);
            }
        });
        
        simulationClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulationClearButtonActionPerformed(e);
            }
        });
        
        simulationNextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulationNextButtonActionPerformed(e);
            }
        });
        
        simulationSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                simulationSpeedSliderStateChanged(e);
            }
        });
                
        simulationBoard.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                simulationBoardMousePressed(e);
            }
        });
        
        recreateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recreateButtonActionPerformed(e);
            }
        });
    }
    
    private void initPanels() {        
        TitledBorder border1 = new TitledBorder("Start/Stop Simulation");
        TitledBorder border2 = new TitledBorder("Simulation Speed");
        TitledBorder border3 = new TitledBorder("Simulation Board");
        TitledBorder border4 = new TitledBorder("Simulation Controls");
        TitledBorder border5 = new TitledBorder("Patterns");
        TitledBorder border6 = new TitledBorder("Simulation Step Interval");
        TitledBorder border7 = new TitledBorder("Board Size");
        
        JPanel simulationStartStopPanel = new JPanel();
        simulationStartStopPanel.add(simulationStartButton);
        simulationStartStopPanel.add(simulationStopButton);
        
        JPanel simulationClearNextPanel = new JPanel();
        simulationClearNextPanel.add(simulationClearButton);
        simulationClearNextPanel.add(simulationNextButton);
        
        JPanel simulationPanel = new JPanel(new BorderLayout());
        simulationPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 0, 0, 0), border1));
        simulationPanel.add(simulationStartStopPanel, BorderLayout.NORTH);
        simulationPanel.add(simulationClearNextPanel, BorderLayout.CENTER);

        JPanel simulationSpeedPanel = new JPanel();
        simulationSpeedPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 0, 0, 0), border2));
        simulationSpeedPanel.add(simulationSpeedSlider);
        
        JPanel simulationStepPanel = new JPanel();
        simulationStepPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 0, 0, 0), border6));
        simulationStepPanel.add(simulationStepSlider);
        
        JPanel simulationBoardPanel = new JPanel();
        simulationBoardPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 10, 10, 5), border3));
        simulationBoardPanel.add(simulationBoard);
        
        JPanel boardPanel1 = new JPanel(new GridLayout(0, 2, 2, 2));
        boardPanel1.add(new JLabel("Columns"));
        columnTextfield.setText(String.valueOf(simulationBoard.getColumns()));
        boardPanel1.add(columnTextfield);
        boardPanel1.add(new JLabel("Rows"));
        rowTextfield.setText(String.valueOf(simulationBoard.getRows()));
        boardPanel1.add(rowTextfield);
        boardPanel1.add(new JLabel("Grid size"));
        gridSizeTextfield.setText(String.valueOf(simulationBoard.getGridSize()));
        boardPanel1.add(gridSizeTextfield);
        
        JPanel boardPanel2 = new JPanel();
        boardPanel2.add(recreateButton);
        
        JPanel boardPanel0 = new JPanel(new BorderLayout());
        boardPanel0.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 0, 0, 0), border7));
        boardPanel0.add(boardPanel1, BorderLayout.NORTH);
        boardPanel0.add(boardPanel2, BorderLayout.CENTER);
        
        JPanel simulationControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        simulationControlPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 5, 10), border4));
        simulationControlPanel.add(simulationPanel);
        simulationControlPanel.add(simulationSpeedPanel);
        simulationControlPanel.add(simulationStepPanel);
        simulationControlPanel.add(boardPanel0);
        
        JPanel patternPanel = new JPanel();
        patternPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 10, 10), border5));
        patternPanel.add(patternScrollPane);
        
        add(simulationControlPanel, BorderLayout.NORTH);
        add(simulationBoardPanel,   BorderLayout.CENTER);
        add(patternPanel,           BorderLayout.EAST);
    }
    
    private List<Point> loadPattern(String filename) {
        List<Point> points = new ArrayList();
        try {
            Scanner scanner = new Scanner(new FileReader(filename));
            scanner.nextLine(); // Skip first line
            
            while(scanner.hasNext()) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                points.add(new Point(x, y));
            }
            
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return points;
    }
    
    private void timerActionPerformed(ActionEvent e) {
        for(int i = 0; i < simulationStepSlider.getValue(); i++)
            simulationBoard.updateGrid();
    }
    
    private void simulationStartButtonActionPerformed(ActionEvent e) {
        timer.start();
        simulationStartButton.setEnabled(false);
        simulationStopButton.setEnabled(true);
        simulationNextButton.setEnabled(false);
    }
    
    private void simulationStopButtonActionPerformed(ActionEvent e) {
        timer.stop();
        simulationStartButton.setEnabled(true);
        simulationStopButton.setEnabled(false);
        simulationNextButton.setEnabled(true);
    }
    
    private void simulationClearButtonActionPerformed(ActionEvent e) {
        simulationBoard.clearGrid();
    }
    
    private void simulationNextButtonActionPerformed(ActionEvent e) {
        for(int i = 0; i < simulationStepSlider.getValue(); i++)
            simulationBoard.updateGrid();
    }
    
    private void simulationSpeedSliderStateChanged(ChangeEvent e) {
        timer.setDelay(simulationSpeedSlider.getMaximum() - simulationSpeedSlider.getValue());
    }
        
    private void simulationBoardMousePressed(MouseEvent e) {
        String pattern = patternList.getSelectedValue();
        if(pattern == null)
            return;
        
        int x = e.getX() / simulationBoard.getGridSize();
        int y = e.getY() / simulationBoard.getGridSize();
        
        simulationBoard.addPattern(x, y, patterns.get(pattern));
    }
    
    private void recreateButtonActionPerformed(ActionEvent e) {
        try {
            int columns  = Integer.parseInt(columnTextfield.getText());
            int rows     = Integer.parseInt(rowTextfield.getText());
            int gridSize = Integer.parseInt(gridSizeTextfield.getText());

            int screenWidth  = Toolkit.getDefaultToolkit().getScreenSize().width;
            int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
            
            boolean isWidthToBig  = columns * gridSize >=  screenWidth;
            boolean isHeightToBig = rows * gridSize >= screenHeight;
            
            String message = null;
            if(isWidthToBig)
                message = "Window width is to big (" + columns + " * " + gridSize + " = " + columns * gridSize + ")";
            else if(isHeightToBig)
                message = "Window height is to big (" + rows + " * " + gridSize + " = " + rows * gridSize + ")";
            
            if(isWidthToBig || isHeightToBig) {
                JOptionPane.showMessageDialog(null, message);
                return;
            }                
            
            getContentPane().removeAll();
            simulationBoard = new Board(columns, rows, gridSize);
            initPanels();
            simulationBoard.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    simulationBoardMousePressed(e);
                }
            });
            pack();
            setLocationRelativeTo(null);
        
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "You must enter a number: " + ex.getMessage());
        }
    }
}