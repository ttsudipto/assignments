package project.pc.bluetooth;

import project.pc.db.DatabaseManager;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;

public class BtServer {

    private final UUID mUuid;
    private String mConnectionString;
    private StreamConnectionNotifier mStreamConnNotifier;
    private BtNetworkState mState;
    public static DatabaseManager databaseManager;

    public BtServer() {
        mState = new BtNetworkState();
        mUuid = BtNetworkState.getUUID();
        mConnectionString = "btspp://localhost:" + mUuid.toString() +";name=Sample SPP Server";
    }

    public UUID getUUID() { return mUuid; }
    public BtNetworkState getState() { return mState; }

    public void start() {
        try {
            mStreamConnNotifier = (StreamConnectionNotifier) Connector.open(mConnectionString);

            System.out.println("BtServer : Server started !!!");
        } catch (IOException e) {
            System.out.println("Error !!! Cannot start server ...");
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            StreamConnection connection = mStreamConnNotifier.acceptAndOpen();

            RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
            System.out.println("BtServer : Connected to "+ dev.getBluetoothAddress() + "-" +dev.getFriendlyName(true));

            BtConnectionThread connThread = new BtConnectionThread(connection, mState);
            mState.addConnection(connection, connThread);
            new Thread(connThread).start();
        } catch (IOException e) {
            System.out.println("Error !!! Unable to accept client connection ...");
            e.printStackTrace();
        }
    }
}
