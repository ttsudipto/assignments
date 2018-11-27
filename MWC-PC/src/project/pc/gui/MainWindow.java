package project.pc.gui;

import project.pc.bluetooth.BtNetworkState;
import project.pc.bluetooth.BtServerThread;
import project.pc.net.NetworkManager;
import project.pc.net.NetworkState;
import project.pc.net.NetworkThread;
import project.pc.net.ServerThread;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class MainWindow extends JFrame implements ActionListener, WindowListener {

    private JList mList;
    private boolean mLastSelectedOption;
    private static DefaultListModel<String> mListModel;

    private BtNetworkState state;

    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 480;
    private static final long DIALOG_TIMEOUT = 2000;

    public MainWindow() {
        mListModel =new DefaultListModel<>();

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception e) {
            System.out.println("Error in loading look-and-feel ...");
            e.printStackTrace();
        }

        this.setTitle("SmartIO");
        this.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(this);

        this.setLayout(new BorderLayout());

        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.setMargin(new Insets(5,5,5,5));
        disconnectButton.setActionCommand("disconnect_clicked");
        disconnectButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(new GridLayout(1,0));
        buttonPanel.add(disconnectButton);

        mList = new JList<>(mListModel);
        mList.setLayoutOrientation(JList.VERTICAL);
        mList.setFixedCellHeight(50);

        this.add(buttonPanel,BorderLayout.SOUTH);
        this.add(mList, BorderLayout.CENTER);

        BtServerThread serverThread = new BtServerThread();
        state = serverThread.getServer().getState();
        new Thread(serverThread).start();

        this.setVisible(true);
    }

//    public static void showConnectionConfirmationDialog(String title, ServerThread st) {
//        String message = "Connected !!";
//        JOptionPane option = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
//        JDialog dialog = st.getDialog();
//        try {
//            dialog.dispose();
//        } catch(NullPointerException ignored) {}
//        dialog = option.createDialog(title);
//        dialog.setModal(false);
//        st.setDialog(dialog);
//        dialog.setVisible(true);
//        try {
//            Thread.sleep(DIALOG_TIMEOUT);
//            dialog.dispose();
//        } catch (InterruptedException | NullPointerException ignored) {}
//    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getActionCommand().equals("disconnect_clicked")) {
            List<String> selectedValues = mList.getSelectedValuesList();
            try {
                for (String value: selectedValues) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            state.removeConnection(value);
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("MainWindow : Disconnect clicked !!!");
        }
    }

    public static void addToList(String e) { if(!mListModel.contains(e)) mListModel.addElement(e); }
    public static void removeFromList(String e) { if(mListModel.contains(e)) mListModel.removeElement(e); }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
//        try {
//            mManager.stopServer();
//            mThread = null;
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {}

    @Override
    public void windowClosed(WindowEvent windowEvent) {}

    @Override
    public void windowIconified(WindowEvent windowEvent) {}

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {}

    @Override
    public void windowActivated(WindowEvent windowEvent) {}

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {}
}