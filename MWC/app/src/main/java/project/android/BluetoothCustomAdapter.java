package project.android;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

class BluetoothCustomAdapter extends ArrayAdapter<BluetoothDevice> {

    private Activity mActivity;
    private BluetoothConnectionFragment mBluetoothConnFragment;
//    private TextView mTextView;

    BluetoothCustomAdapter(Activity activity, int textViewResourceId, ArrayList<BluetoothDevice> arrayList) {
        super(activity, textViewResourceId, arrayList);
        mActivity = activity;
        mBluetoothConnFragment = (BluetoothConnectionFragment) MainActivity.getBluetoothConnectionFragment();
    }

    @Override
    public int getCount() {
        super.getCount();
        return MainActivity.sState.noOfBondedDevices();
    }

    private class ViewHolder {
        private TextView mTextView;
        private ImageView mImageView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
            convertView = layoutInflater.inflate(R.layout.local_devices, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.code);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.code);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.connectIcon);
            convertView.setTag(viewHolder);
//            convertView.setTag(mTextView);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
//            mTextView = (TextView) convertView.getTag();
        }

        viewHolder.mTextView.setText(mBluetoothConnFragment.getTextFromList(position));
        viewHolder.mImageView.setImageResource(R.mipmap.connect_icon);
//            if(sConnectionAlive.get(serverInfo.getAddress())) {
//                viewHolder.mImageView.setImageResource(R.mipmap.connect_icon);
//            } else {
//                viewHolder.mImageView.setImageResource(R.mipmap.laptop_icon);
//            }

        return convertView;
    }
}
