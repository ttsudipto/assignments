package project.android.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

import project.android.BluetoothConnectionFragment;
import project.android.MainActivity;

public class DiscoveryThread implements Runnable {

    private Activity mActivity;
    private BroadcastReceiver mUuidReceiver;

    public DiscoveryThread(Activity activity) {
        mActivity = activity;
        mUuidReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_UUID.equals(action)){
                    BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Parcelable[] uuids = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                    Log.d("DiscoveryThread",
                            "Device name = "+dev.getName()+
                                    " Received no. of UUIDs = "+uuids.length);
                    for(int i=0;i<uuids.length;++i) {
                        ParcelUuid uuid = (ParcelUuid) uuids[i];
                        if (uuid.toString().equals(BtNetworkState.sUUID_String))
                            Log.d("DiscoveryThread", "UUIDs matched !!");
//                            MainActivity.sState.addServerToList(dev);
                    }
                }
            }
        };
    }

    @Override
    public void run() { ////TODO later add LE scan
        Log.d("DiscoveryThread" , "foo");

        Set<BluetoothDevice> devices = MainActivity.sState.getAdapter().getBondedDevices();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_UUID);
        mActivity.registerReceiver(mUuidReceiver, filter);
        for (BluetoothDevice dev : devices) {
            dev.fetchUuidsWithSdp();
        }
        MainActivity.sState.replaceConnDevices(devices);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BluetoothConnectionFragment fragment =
                        (BluetoothConnectionFragment) MainActivity.getBluetoothConnectionFragment();
                fragment.notifyDataSetChanged();
            }
        });
    }
}
