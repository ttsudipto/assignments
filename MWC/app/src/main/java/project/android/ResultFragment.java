package project.android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import project.android.bluetooth.CommunicationThread;
import project.android.bluetooth.Message;

//import android.app.Fragment;

public class ResultFragment extends Fragment {

    private View mView;
    private String mResultHeading = "Current Vote Counts";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment_result, container, false);
            Message receivedData = MainActivity.sState.getReceivedData();
            Message receivedResult = MainActivity.sState.getReceivedResult();

            TextView resultHeading = (TextView) mView.findViewById(R.id.result_heading);
            String rhText = mResultHeading + "\n\n" + receivedData.getQuestion();
            resultHeading.setText(rhText);

            String o1Text = receivedData.getOption1() + " - " + receivedResult.getOption1() + " vote(s)";
            String o2Text = receivedData.getOption2() + " - " + receivedResult.getOption2() + " vote(s)";
            String o3Text = receivedData.getOption3() + " - " + receivedResult.getOption3() + " vote(s)";
            String o4Text = receivedData.getOption4() + " - " + receivedResult.getOption4() + " vote(s)";

            ((TextView) mView.findViewById(R.id.result1)).setText(o1Text);
            ((TextView) mView.findViewById(R.id.result2)).setText(o2Text);
            ((TextView) mView.findViewById(R.id.result3)).setText(o3Text);
            ((TextView) mView.findViewById(R.id.result4)).setText(o4Text);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        Message receivedData = MainActivity.sState.getReceivedData();
//        Message receivedResult = MainActivity.sState.getReceivedResult();
//
//        TextView resultHeading = (TextView) mView.findViewById(R.id.result_heading);
//        String rhText = mResultHeading + "\n\n" + receivedData.getQuestion();
//        resultHeading.setText(rhText);
//
//        String o1Text = receivedData.getOption1() + " - " + receivedResult.getOption1() + " vote(s)";
//        String o2Text = receivedData.getOption2() + " - " + receivedResult.getOption2() + " vote(s)";
//        String o3Text = receivedData.getOption3() + " - " + receivedResult.getOption3() + " vote(s)";
//        String o4Text = receivedData.getOption4() + " - " + receivedResult.getOption4() + " vote(s)";
//
//        ((TextView) mView.findViewById(R.id.result1)).setText(o1Text);
//        ((TextView) mView.findViewById(R.id.result2)).setText(o2Text);
//        ((TextView) mView.findViewById(R.id.result3)).setText(o3Text);
//        ((TextView) mView.findViewById(R.id.result4)).setText(o4Text);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(mCheckedButtonId != -1)
//            mOptionGroup.check(mCheckedButtonId);
    }
}
