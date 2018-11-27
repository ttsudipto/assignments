package project.android.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import project.android.MainActivity;

public class ConnectThread implements Runnable {

    private BluetoothDevice mDevice;
    private BluetoothSocket mSocket;

    public ConnectThread(BluetoothDevice device) {
        mDevice = device;
        BluetoothSocket tmp = null;

        try {
            tmp = mDevice.createRfcommSocketToServiceRecord(UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b"));
        } catch (IOException e) {
            Log.e("ConnectThread", "Error creating socket ...");
            e.printStackTrace();
        }

        mSocket = tmp;
    }

    public boolean isSocketCreated() { return mSocket != null; }

    @Override
    public void run() {
        MainActivity.sState.cancelDiscovery();

        try {
            mSocket.connect();
            MainActivity.sState.setConnectedDevice(mDevice);

            Log.d("ConnectThread", "Socket connected" + mSocket.getRemoteDevice().getName());
            MainActivity.getBluetoothConnectionFragment().getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "Connected to " + mSocket.getRemoteDevice().getName() + ".";
                    Toast.makeText(MainActivity.getBluetoothConnectionFragment().getContext(), msg, Toast.LENGTH_SHORT).show();
                    MainActivity.getBluetoothConnectionFragment().changeStatus("Status : " + msg);
                }
            });

            CommunicationThread ioThread = new CommunicationThread(mSocket);
            MainActivity.sState.setComThread(ioThread);
            new Thread(ioThread).start();
        } catch (IOException e) {
            Log.e("ConnectThread", "Error connecting ...");
            e.printStackTrace();
        }
    }

    public void closeConnection() { ////TODO May be changed
        try {
            mSocket.close();
        } catch (IOException e1) {
            Log.e("ConnectThread", "Error closing socket ...");
            e1.printStackTrace();
        }

        MainActivity.sState.setConnDeviceToNull();
    }
}
