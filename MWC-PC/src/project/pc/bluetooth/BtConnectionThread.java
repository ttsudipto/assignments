package project.pc.bluetooth;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class BtConnectionThread implements Runnable {

    private boolean mStopFlag;
    private StreamConnection mConnection;
    private PrintWriter mWriter;
    private BufferedReader mReader;
    private BtNetworkState mState;
    private String mAddress;
    private String mName;

    public BtConnectionThread (StreamConnection conn, BtNetworkState state) {
        mState = state;
        mStopFlag = false;
        mConnection = conn;
        try {
            RemoteDevice dev = RemoteDevice.getRemoteDevice(mConnection);
            mAddress = dev.getBluetoothAddress();
            mName = dev.getFriendlyName(true);
            mWriter = new PrintWriter(mConnection.openOutputStream());
            mReader = new BufferedReader(new InputStreamReader(mConnection.openInputStream()));
        } catch (IOException e) {
            System.out.println("Error !!! Unable to open streams ...");
            e.printStackTrace();
        }

}

    public void setStopFlag() { mStopFlag = true; }

    private void send(char type) {
//        System.out.println("foo");

        mWriter.println(type);
        if(type == BtNetworkState.MESSAGE_DATA_TYPE) {
            mWriter.println("Who will win 2019 Cricket World Cup ?");
            mWriter.println("Pakistan");
            mWriter.println("Australia");
            mWriter.println("England");
            mWriter.println("India");
        } else if (type == BtNetworkState.RESULT_DATA_TYPE) {
            mWriter.println(BtServer.databaseManager.getCurrentVoteCount());
        }
        mWriter.flush();
        System.out.println("BtConnectionTread : Message sent !!!");
    }

    private void receive() {
        try {
//            Thread.sleep(BtNetworkState.READ_TIMEOUT);
            if(!mReader.ready())
                return;
            String type = mReader.readLine();
            if(type.charAt(0) == BtNetworkState.STOP_DATA_TYPE)
                mState.removeConnection(mAddress +"(" + mName + ")");
            else {
                String option = mReader.readLine();
                System.out.println("BtConnectionThread : Received option : " + option);
//                BtServer.databaseManager.addToDatabase(mAddress, option); // Database insertion
//                System.out.println("BtConnectionThread : Inserted to database");
                send(BtNetworkState.RESULT_DATA_TYPE);
            }
        } catch (IOException e) {e.printStackTrace();}
    }

    private void closeConnection() {
        try {
            send(BtNetworkState.STOP_DATA_TYPE); // send disconnection information
            mConnection.close();
            mWriter.close();
            mReader.close();
        } catch (IOException e) {
            System.out.println("Error !!! Unable to close connection ...");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        send(BtNetworkState.MESSAGE_DATA_TYPE); // send question and option
        while (!mStopFlag) {
            receive(); // receive either answer or disconnection information
        }
        closeConnection();
        System.out.println("BtConnectionThread : Thread stopped successfully");
    }
}
