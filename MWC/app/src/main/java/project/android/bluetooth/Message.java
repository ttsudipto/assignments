package project.android.bluetooth;

public class Message {

    private String mQuestion;
    private String mOption1;
    private String mOption2;
    private String mOption3;
    private String mOption4;

    public Message(String q, String o1, String o2, String o3, String o4) {
        mQuestion = q;
        mOption1 = o1;
        mOption2 = o2;
        mOption3 = o3;
        mOption4 = o4;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getOption1() {
        return mOption1;
    }

    public String getOption2() {
        return mOption2;
    }

    public String getOption3() {
        return mOption3;
    }

    public String getOption4() {
        return mOption4;
    }
}
