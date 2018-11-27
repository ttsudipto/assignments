package project.android;


import android.app.Activity;
//import android.app.Fragment;
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

public class VoteFragment extends Fragment {

    private View mView;
    private String mQuestionHeading = "Question 1";
//    private String mQuestionDesc = "Who will win World Cup ?";
//    private String option1 = "India";
//    private String option2 = "Pakistan";
//    private String option3 = "Australia";
//    private String option4 = "England";
    RadioGroup mOptionGroup;
    int mCheckedButtonId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment_vote, container, false);
            mCheckedButtonId = -1;           // no radio buttons checked
            mOptionGroup = (RadioGroup) mView.findViewById(R.id.option_group);
            mOptionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int id) {
                    if(group == mOptionGroup)
                        mCheckedButtonId = id;
                }
            });

            Button submitButton = (Button) mView.findViewById(R.id.submit);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String msg;
                    int option = mOptionGroup.getCheckedRadioButtonId();
                    if(option == -1) {
                        ////TODO Toast to select a option
                        String toast = "Please select an option ...";
                        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(option == R.id.option1) msg = "1";
                    else if(option == R.id.option2) msg = "2";
                    else if(option == R.id.option3) msg = "3";
                    else if(option == R.id.option4) msg = "4";
                    else msg = "";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(MainActivity.sState.isConnected()) {
                                CommunicationThread ct = MainActivity.sState.getComThread();
                                ct.send(msg);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String toast = "Option submitted !";
                                        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT);
                                    }
                                });
                            }
                        }
                    }).start();
                }
            });
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView question = (TextView) mView.findViewById(R.id.question);
        String qText = mQuestionHeading + "\n\n" + MainActivity.sState.getReceivedData().getQuestion();
        question.setText(qText);
        ((RadioButton) mView.findViewById(R.id.option1)).setText(MainActivity.sState.getReceivedData().getOption1());
        ((RadioButton) mView.findViewById(R.id.option2)).setText(MainActivity.sState.getReceivedData().getOption2());
        ((RadioButton) mView.findViewById(R.id.option3)).setText(MainActivity.sState.getReceivedData().getOption3());
        ((RadioButton) mView.findViewById(R.id.option4)).setText(MainActivity.sState.getReceivedData().getOption4());
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(mCheckedButtonId != -1)
//            mOptionGroup.check(mCheckedButtonId);
    }
}
