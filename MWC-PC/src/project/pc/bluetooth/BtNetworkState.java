package project.pc.bluetooth;

import project.pc.gui.MainWindow;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.util.HashMap;

public class BtNetworkState {

    private static final UUID sUUID = new UUID("0f14d0ab96054a62a9e45ed26688389b", false);
    private HashMap<String, BtConnectionThread> mConnnectionMap;

    public static final int READ_TIMEOUT = 1000;
    public static final char MESSAGE_DATA_TYPE = 'M';
    public static final char STOP_DATA_TYPE = 'S';
    public static final char RESULT_DATA_TYPE = 'R';

    public BtNetworkState() {
        mConnnectionMap = new HashMap<>();
    }

    public static UUID getUUID() { return sUUID; }

    public void addConnection(StreamConnection connection, BtConnectionThread thread) {
        try {
            RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
            String name = dev.getFriendlyName(true);
            String address = dev.getBluetoothAddress();
//            System.out.println(address);
            mConnnectionMap.put(address, thread);
            MainWindow.addToList(address + "(" + name + ")");
        } catch (IOException e) {
            System.out.println("BtNetworkState : Cannot get remote device ...");
            e.printStackTrace();
        }
    }

    public void removeConnection(String info) {
//        System.out.println(address);
        try {
            String address = info.substring(0, info.indexOf("("));
            BtConnectionThread thread = mConnnectionMap.get(address);
            thread.setStopFlag();
//            Thread.sleep(READ_TIMEOUT);
            MainWindow.removeFromList(info);
            mConnnectionMap.remove(address);
        } catch (Exception e) {
            System.out.println("Error !!! Interrupted ...");
            e.printStackTrace();
        }
    }
}
