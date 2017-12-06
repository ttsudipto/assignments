import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MainWindow extends JFrame implements ActionListener {

//    private Data data;
    private EventDispatcher dispatcher;

    public MainWindow(Data.GraphicsPanel gPanel, EventDispatcher e) {
        super();

//        data = d;
        dispatcher = e;

        setTitle("Algo Assignments");
        setMinimumSize(new Dimension(640, 480));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());


        add(gPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton hullButton = new JButton("Hull");
        hullButton.setActionCommand("b1");
        hullButton.addActionListener(this);
        buttonPanel.add(hullButton);
        add(buttonPanel, BorderLayout.SOUTH);


        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("b1")) {
            dispatcher.setOperationType(1);
            new Thread(dispatcher).start();
            System.out.println("b1 clicked");
        }
    }
}