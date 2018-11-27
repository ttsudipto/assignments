package project.android.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import project.android.BluetoothConnectionFragment;
import project.android.MainActivity;

public class ServerFinder {

    private Activity mActivity;
    private BroadcastReceiver mUuidReceiver;
    private BroadcastReceiver mDiscoveryReceiver;
    private BroadcastReceiver mDiscoveryStateReceiver;
    private ArrayList<BluetoothDevice> mDevices;

    public ServerFinder(Activity activity) {
        mActivity = activity;
//        initUuidReceiver();
        initDiscoveryReceiver();
        initDiscoveryStateReceiver();
    }

    private void notifyUI() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BluetoothConnectionFragment fragment =
                        (BluetoothConnectionFragment) MainActivity.getBluetoothConnectionFragment();
                fragment.notifyDataSetChanged();
            }
        });
    }

    private void initDiscoveryReceiver() {
        mDiscoveryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                    final BluetoothDevice dev =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d("ServerFinder", "Found device : "+dev.getName());
                    mDevices.add(dev);
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mActivity.registerReceiver(mDiscoveryReceiver, filter);
    }

    private void initDiscoveryStateReceiver() {
        mDiscoveryStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String toast = "Discovering devices ...";
                            Toast.makeText(MainActivity.getBluetoothConnectionFragment().getContext(), toast, Toast.LENGTH_SHORT).show();
                            MainActivity.getBluetoothConnectionFragment().changeStatus("Status : " + toast);
                        }
                    });
                    Log.d("ServerFinder", "Discovering devices ...");
                    mDevices = new ArrayList<>();
                } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String toast = "Discovery finished !!!";
                            Toast.makeText(MainActivity.getBluetoothConnectionFragment().getContext(), toast, Toast.LENGTH_SHORT).show();
                            MainActivity.getBluetoothConnectionFragment().changeStatus("Status : " + toast);
                        }
                    });
                    Log.d("ServerFinder", "Discovery finished !!!");
                    MainActivity.sState.replaceServerList(mDevices);
                    notifyUI();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        mActivity.registerReceiver(mDiscoveryStateReceiver, filter);
    }

    public void discover() {
        requestPermissions();
        boolean started = MainActivity.sState.getAdapter().startDiscovery();
        if(!started) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String toast = "Cannot start device discovery ... ";
                    Toast.makeText(MainActivity.getBluetoothConnectionFragment().getContext(), toast, Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("ServerFinder", "Discovery start failed !!!");
        }
    }

    private void requestPermissions() {
        int MY_PERMISSIONS_REQUEST_BLUETOOTH = 1;
        int MY_PERMISSIONS_REQUEST_BLUETOOTH_ADMIN = 1;
        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.BLUETOOTH},
                MY_PERMISSIONS_REQUEST_BLUETOOTH);
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                MY_PERMISSIONS_REQUEST_BLUETOOTH_ADMIN);
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
    }

    public void unregisterReceivers() {
//        mActivity.unregisterReceiver(mUuidReceiver);
        mActivity.unregisterReceiver(mDiscoveryReceiver);
        mActivity.unregisterReceiver(mDiscoveryStateReceiver);
    }

    private void initUuidReceiver() {
        mUuidReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_UUID.equals(action)){
                    BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Parcelable[] uuids = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                    Log.d("ServerFinder",
                            dev.getName()+ " - Received no. of UUIDs = "+uuids.length);
                    for(int i=0;i<uuids.length;++i) {
                        ParcelUuid uuid = (ParcelUuid) uuids[i];
                        if (uuid.toString().equals(BtNetworkState.sUUID_String)) {
                            Log.d("ServerFinder", "UUID match for "+dev.getName()+" !!");
//                            MainActivity.sState.addServerToList(dev);
//                            notifyUI();
                        }
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_UUID);
        mActivity.registerReceiver(mDiscoveryReceiver, filter);
    }

    private void startUuidSearch(BluetoothDevice device) {
        Log.d("ServerFinder", "Starting UUID search on "+ device.getName()+" ...");
        device.fetchUuidsWithSdp();
    }
}
