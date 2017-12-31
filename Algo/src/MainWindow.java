import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

class MainWindow extends JFrame implements ActionListener, ItemListener {

//    private Data data;
    private EventDispatcher dispatcher;
    private JCheckBox arguement;
    private Data.GraphicsPanel graphicsPanel;

    public MainWindow(Data.GraphicsPanel gPanel, EventDispatcher e) {
        super();

//        data = d;
        graphicsPanel = gPanel;
        dispatcher = e;

        setTitle("Algo Assignments");
        setMinimumSize(new Dimension(1280, 720));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());


        add(gPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();            // Button Panel
        JButton resetButton = new JButton("Reset");   // Reset button
        resetButton.setActionCommand("b0");
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);
        JButton hullButton = new JButton("Hull");     // Hull button
        hullButton.setActionCommand("b1");
        hullButton.addActionListener(this);
        buttonPanel.add(hullButton);
        JButton checkHullButton = new JButton("Check Hull");    // Check Hull Button
        checkHullButton.setActionCommand("b2");
        checkHullButton.addActionListener(this);
        buttonPanel.add(checkHullButton);
        JButton arguementHull = new JButton("Arguement Hull");  //Arguement Hull
        arguementHull.setActionCommand("b3");
        arguementHull.addActionListener(this);
        buttonPanel.add(arguementHull);
        JButton closestPairButton = new JButton("Closest Pair");// Closest Pair button
        closestPairButton.setActionCommand("b4");
        closestPairButton.addActionListener(this);
        buttonPanel.add(closestPairButton);
        arguement = new JCheckBox("Arguement Point");
        arguement.addItemListener(this);
        buttonPanel.add(arguement);

        add(buttonPanel, BorderLayout.SOUTH);


        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("b0")) {
            if(arguement.isSelected())
                arguement.doClick();
            dispatcher.setOperationType(0);
            System.out.println("b0 clicked");
        }
        else if(e.getActionCommand().equals("b1")) {
            dispatcher.setOperationType(1);
            System.out.println("b1 clicked");
        }
        else if(e.getActionCommand().equals("b2")) {
            dispatcher.setOperationType(2);
            System.out.println("b2 clicked");
        }
        else if(e.getActionCommand().equals("b3")) {
            dispatcher.setOperationType(3);
            System.out.println("b3 clicked");
        }
        else if(e.getActionCommand().equals("b4")) {
            dispatcher.setOperationType(4);
            System.out.println("b4 clicked");
        }
        new Thread(dispatcher).start();
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        Object source = itemEvent.getItemSelectable();
        if(source == arguement) {
            if(itemEvent.getStateChange() == ItemEvent.SELECTED)
                graphicsPanel.setIsArguementPoint(true);
            else
                graphicsPanel.setIsArguementPoint(false);
        }
    }
}