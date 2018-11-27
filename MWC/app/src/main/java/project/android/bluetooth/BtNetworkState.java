package project.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.Set;

public class BtNetworkState {

    private final BluetoothAdapter mAdapter;
    private ArrayList<BluetoothDevice> mAvailableDevices;
    private BluetoothDevice mConnectedDevice;
    private CommunicationThread mComThread;
    private boolean mAppEnabledToggle;
    private Message mReceivedData;
    private Message mReceivedResult;
    private String mStatusString;

    public static final String sUUID_String = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
    public static final int READ_TIMEOUT = 1000;
    public static final char MESSAGE_DATA_TYPE = 'M';
    public static final char STOP_DATA_TYPE = 'S';
    public static final char RESULT_DATA_TYPE = 'R';

    public BtNetworkState() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mAvailableDevices = new ArrayList<>();
        mConnectedDevice = null;
        mComThread = null;
        mAppEnabledToggle = false;
        mReceivedData = null;
        mReceivedResult = null;
        mStatusString = "Status : Disconnected";
    }

    public BluetoothAdapter getAdapter() { return mAdapter; }
    public BluetoothDevice getConnectedDevice() { return mConnectedDevice; }
    public CommunicationThread getComThread() { return mComThread; }
    public ArrayList<BluetoothDevice> getBondedDevices() { return mAvailableDevices; }
    public Message getReceivedData() { return (isConnected()) ? mReceivedData : null; }
    public Message getReceivedResult() { return (isConnected()) ? mReceivedResult : null; }
    public String getStatusString() { return mStatusString; }

    public boolean isAppEnabledToggle() { return mAppEnabledToggle; }

    public void setConnectedDevice(BluetoothDevice device) { mConnectedDevice = device; }
    public void setConnDeviceToNull() { mConnectedDevice = null; }
    public void setAppEnabledToggle(boolean isAppEnabled) { mAppEnabledToggle = isAppEnabled; }
    public void setReceivedData(Message data) { mReceivedData = data; }
    public void setReceivedResult(Message result) { mReceivedResult = result; }
    public void setStatusString(String status) { mStatusString = status; }

    public void setComThread(CommunicationThread thread) { mComThread = thread; }
    public void setComThreadToNull() { mComThread = null; }

    public boolean isConnected() { return mConnectedDevice != null; }
    public boolean isBluetoothEnabled() { return mAdapter.isEnabled(); }

    public void replaceConnDevices(Set newSet) {
        mAvailableDevices.clear();
        mAvailableDevices.addAll(newSet);
    }
    public void replaceServerList(ArrayList<BluetoothDevice> newList) {
        mAvailableDevices.clear();
        mAvailableDevices.addAll(newList);
    }
    public boolean connnectedDevEquals(BluetoothDevice dev) {
        if(isConnected())
            return mConnectedDevice.getAddress().equals(dev.getAddress());
        else
            return false;
    }

    public String getBondedDeviceAddr(int pos) { return mAvailableDevices.get(pos).getAddress(); }
    public String getBondedDeviceName(int pos) { return mAvailableDevices.get(pos).getName(); }
    public int noOfBondedDevices() {return mAvailableDevices.size(); }

    public void cancelDiscovery() { mAdapter.cancelDiscovery(); }
}
