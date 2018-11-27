package project.pc;

import project.pc.bluetooth.BtServer;
import project.pc.bluetooth.BtServerThread;
import project.pc.db.DatabaseManager;
import project.pc.gui.MainWindow;
import project.pc.net.NetworkManager;
import project.pc.net.ServerInfo;
import project.pc.security.EKEProvider;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.awt.AWTException;
import java.io.*;
import java.net.InetAddress;

/**
 * Class containing the <code>main()</code> method.
 *
 * <p>
 *     The {@link #main(String[])} method :
 *     <ul>
 *         <li>Generates the public key of the server.</li>
 *         <li>
 *             Instantiates the {@link project.pc.net.NetworkManager}
 *             for handling the network operations.
 *         </li>
 *         <li>
 *             Displays the main window of the GUI.
 *         </li>
 *     </ul>
 * </p>
 *
 * @see #main(String[])
 * @see project.pc.net.NetworkManager
 * @see project.pc.net.ServerInfo
 */

class Main{

    private static void startServer() throws IOException{

        //Create a UUID for SPP
        UUID uuid = new UUID("0f14d0ab96054a62a9e45ed26688389b", false);
        //Create the servicve url
        String connectionString = "btspp://localhost:" + uuid.toString() +";name=Sample SPP Server";

        //open server url
        StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open( connectionString );

        //Wait for client connection
        System.out.println("\nServer Started. Waiting for clients to connect...");
        StreamConnection connection=streamConnNotifier.acceptAndOpen();

        RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
        System.out.println("Remote device address: "+dev.getBluetoothAddress());
        System.out.println("Remote device name: "+dev.getFriendlyName(true));

        //read string from spp client
        InputStream inStream=connection.openInputStream();
        BufferedReader bReader=new BufferedReader(new InputStreamReader(inStream));
        String lineRead=bReader.readLine();
        System.out.println(lineRead);

        //send response to spp client
        OutputStream outStream=connection.openOutputStream();
        BufferedWriter bWriter=new BufferedWriter(new OutputStreamWriter(outStream));
        bWriter.write("Response String from SPP Server\r\n");

        /*PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
        pWriter.write("Response String from SPP Server\r\n");
        pWriter.flush();
        pWriter.close();*/

        streamConnNotifier.close();

    }

    /**
     * The <code>main</code> method.
     *
     * @param args the command-line arguments.
     * @throws IOException
     * @throws AWTException
     * @throws InterruptedException
     */
    public static void main(String args[]) throws IOException,AWTException,InterruptedException {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: "+localDevice.getBluetoothAddress());
        System.out.println("Name: "+localDevice.getFriendlyName());

//        startServer();

        BtServer.databaseManager = new DatabaseManager();
        new MainWindow();
    }
}