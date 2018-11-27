package project.android.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import project.android.MainActivity;
import project.android.R;
import project.android.ResultFragment;

public class CommunicationThread implements Runnable {

    private BluetoothSocket mSocket;
    private boolean mStopFlag;
    private PrintWriter mWriter;
    private BufferedReader mReader;

    public CommunicationThread(BluetoothSocket s) {
        mSocket = s;
        mStopFlag = false;
        try {
            mWriter = new PrintWriter(mSocket.getOutputStream());
            mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        } catch(IOException e) {
            Log.e("CommunicationThread", "Error creating communication streams ...");
            e.printStackTrace();
        }
    }

    public void setStopFlag() { mStopFlag = true; }

    public void send(String msg) {
        mWriter.println(BtNetworkState.MESSAGE_DATA_TYPE);
        mWriter.println(msg);
        mWriter.flush();
        Log.d("CommunicationThread", "Message sent !!!");
    }

    private void sendStop() { mWriter.println(BtNetworkState.STOP_DATA_TYPE); }

    private void receive() {
        try {
            if (!mReader.ready()) return;
            Log.d("CommunicationThread", "foo");
            String type = mReader.readLine();
            if(type.length() == 0) return;
            if(type.charAt(0) == BtNetworkState.STOP_DATA_TYPE) {
                setStopFlag();
            } else if (type.charAt(0) == BtNetworkState.RESULT_DATA_TYPE) {
                String option1 = mReader.readLine();
                String option2 = mReader.readLine();
                String option3 = mReader.readLine();
                String option4 = mReader.readLine();

                Log.d("CommunicationThread", "Received Result1 - " + option1);
                Log.d("CommunicationThread", "Received Result2 - " + option2);
                Log.d("CommunicationThread", "Received Result3 - " + option3);
                Log.d("CommunicationThread", "Received Result4 - " + option4);

                MainActivity.sState.setReceivedResult
                        (new Message("", option1, option2, option3, option4));
                showResultFragment();
            } else {
                String question = mReader.readLine();
                String option1 = mReader.readLine();
                String option2 = mReader.readLine();
                String option3 = mReader.readLine();
                String option4 = mReader.readLine();
                Log.d("CommunicationThread", "Received Question - " + question);
                Log.d("CommunicationThread", "Received Option1 - " + option1);
                Log.d("CommunicationThread", "Received Option2 - " + option2);
                Log.d("CommunicationThread", "Received Option3 - " + option3);
                Log.d("CommunicationThread", "Received Option4 - " + option4);

                MainActivity.sState.setReceivedData
                        (new Message(question, option1, option2, option3, option4));
            }
        } catch (IOException e) {
            Log.e("CommunicationThread", "Error receiving ...");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!mStopFlag) {
            receive(); // Receive question and options
        }
        while(!mStopFlag) {
            receive(); // Receive stop or result
        }
        closeConnection();
    }

    private void closeConnection() {
        sendStop();
        MainActivity.sState.setConnDeviceToNull();
        MainActivity.sState.setComThreadToNull();
        try {
            mWriter.close();
            mReader.close();
            mSocket.close();
            MainActivity.getBluetoothConnectionFragment().getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "Closing connection with " + mSocket.getRemoteDevice().getName() + " ...";
                    Toast.makeText(MainActivity.getBluetoothConnectionFragment().getContext(), msg, Toast.LENGTH_SHORT).show();
                    MainActivity.getBluetoothConnectionFragment().changeStatus("Status : Disconnected");
                }
            });
        } catch (IOException e) {
            Log.e("CommunicationThread", "Error closing Server ...");
            e.printStackTrace();
        }
    }

    private void showResultFragment() {
        MainActivity.getVoteFragment().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ResultFragment resultFragment = new ResultFragment();
                MainActivity.getVoteFragment().getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.content_frame, resultFragment).commit();
//                if(MainActivity.getVoteFragment().getActivity().getActionBar() != null)
//                    MainActivity.getVoteFragment().getActivity().getActionBar().setTitle("Current Vote Count");
            }
        });
    }
}
