import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Class representing the main window that is displayed
 * first. It also contains the main method.
 */
class MainWindow extends JFrame implements ActionListener {

    private JFileChooser fc;
    private JButton selectButton, zoomButton, shrinkButton, filterButton, histogramButton;
    private JTextField factorFeildX, factorFeildY;
    private File[] files;
    private String factor;
    private Data data;

    /**
     * Constructor that creates the main window with
     * appropriate components.
     */
    public MainWindow() {
        super();

        JLabel label1 = new JLabel("Scaling factor : ");
        JLabel label2 = new JLabel("X");
        JLabel label3 = new JLabel("Y");

        setTitle("Image Processing Assignment");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(640,120));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "jpg"));
        selectButton = new JButton("Select Input File");
        selectButton.setActionCommand("b1");
        selectButton.addActionListener(this);
        zoomButton = new JButton("Zoom");
        zoomButton.setActionCommand("b2");
        zoomButton.addActionListener(this);
        shrinkButton = new JButton("Shrink");
        shrinkButton.setActionCommand("b3");
        shrinkButton.addActionListener(this);
        filterButton = new JButton("Noise Removal");
        filterButton.setActionCommand("b4");
        filterButton.addActionListener(this);
        histogramButton = new JButton("Hist. Eq.");
        histogramButton.setActionCommand("b5");
        histogramButton.addActionListener(this);
        factorFeildX = new JTextField("1", 3);
        factorFeildX.setHorizontalAlignment(JTextField.CENTER);
        factorFeildY = new JTextField("1", 3);
        factorFeildY.setHorizontalAlignment(JTextField.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(label1);
        buttonPanel.add(label2);
        buttonPanel.add(factorFeildX);
        buttonPanel.add(label3);
        buttonPanel.add(factorFeildY);
        buttonPanel.add(zoomButton);
        buttonPanel.add(shrinkButton);
        buttonPanel.add(filterButton);
        buttonPanel.add(histogramButton);
        add(selectButton, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Listener that is called when a button click occurs.
     * @param e the event generated.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("b1")) {
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                files = fc.getSelectedFiles();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(files.length > 1) {
                            selectButton.setText(files.length + " File(s) Selected");
                            data = new Data(files, "output.jpg");
                            System.out.println(files.length);
                        }
                        else {
                            selectButton.setText(files[0].getName());
                            data = new Data(files[0], "output.jpg");
                            data.printInputImgDetails();
                            displayImage(files[0].getName(), data.getInputImage());
                        }
                    }
                }).start();
            }
        } else if (e.getActionCommand().equals("b2")) {
            try {
                double sx = Double.parseDouble(factorFeildX.getText());
                double sy = Double.parseDouble(factorFeildY.getText());
                if (((sx>1 && sy>1) || (sx>1 && sy==1) || (sx==1 && sy>1)) && files[0] != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            displayImage("Output", ZoomAndShrink.zoom(data.getInputImage(), sx, sy));
                        }
                    }).start();
                }
            } catch (Exception ex) {
            }
        } else if (e.getActionCommand().equals("b3")) {
            try {
                double sx = Double.parseDouble(factorFeildX.getText());
                double sy = Double.parseDouble(factorFeildY.getText());
                if ((sx > 0) && (sy > 0 ))
                    if((sx<1 && sy<1) || (sx<1 && sy==1) || (sx==1 && sy<1) && files[0] != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            displayImage("Output", ZoomAndShrink.shrink(data.getInputImage(), sx, sy));
                        }
                    }).start();
                }
            } catch (Exception ex) {
            }
        } else if (e.getActionCommand().equals("b4")) {
            if (files[0] != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        displayImage("Output", NoiseRemoval.removeNoise(data.getInputImages()));
                    }
                }).start();
            }
        } else if (e.getActionCommand().equals("b5")) {
            if (files[0] != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        displayImage("Output", Histogram.equalize(data.getInputImage()));
                    }
                }).start();
            }
        }
    }

    /**
     * Displays an image in a window.
     * @param title the title of the window
     * @param image the image to be displayed
     */
    private static void displayImage(String title, BufferedImage image) {
        JFrame window = new JFrame(title);
        window.setMinimumSize(new Dimension(image.getWidth(), image.getHeight()+75));
        ImagePanel panel = new ImagePanel(image);
        window.add(panel);
        window.setResizable(false);
        window.setVisible(true);
    }

    /**
     * The main method.
     */
    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}