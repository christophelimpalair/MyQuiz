package uscupstate.edu.myquiz;

/**
 * Created by Christophe on 1/20/2015.
 */
public class TrueFalse {
    // variable will hold a resource ID for a string
    private int mQuestion;
    private boolean mTrueQuestion;
    private boolean mCheated = false;

    public TrueFalse(int question, boolean trueQuestion)
    {
        mQuestion = question;
        mTrueQuestion = trueQuestion;
    }

    public int getQuestion()
    {
        return mQuestion;
    }

    public void setQuestion( int question )
    {
        mQuestion = question;
    }

    public boolean isTrueQuestion()
    {
        return mTrueQuestion;
    }

    public void setTrueQuestion( boolean trueQuestion)
    {
        mTrueQuestion = trueQuestion;
    }

    public void cheated(){ mCheated = true; }

    public boolean didCheat() { return mCheated; }
}

