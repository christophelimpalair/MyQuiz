package uscupstate.edu.myquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends Activity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEATER = "isCheater";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    boolean mIsCheater;

    private TextView mQuestionTextView;

    private TrueFalse[] mAnswerKey = new TrueFalse[]
            {
                    new TrueFalse(R.string.question_oceans, true),
                    new TrueFalse(R.string.question_mideast, false),
                    new TrueFalse(R.string.question_africa, false),
                    new TrueFalse(R.string.question_americas, true),
                    new TrueFalse(R.string.question_asia, true)
            };

    private int mCurrentIndex = 0;
    private int mCounter = 0;

    private void updateQuestion()
    {
        int question = mAnswerKey[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
    }

    public void checkAnswer(boolean userPressedTrue)
    {
        boolean answerIsTrue = mAnswerKey[mCurrentIndex].isTrueQuestion();

        int messageResId = 0;

        if (mAnswerKey[mCurrentIndex].didCheat() || mIsCheater)
        {
            if (userPressedTrue == answerIsTrue)
            {
                messageResId = R.string.judgement_toast;
            }
            else {
                messageResId = R.string.incorrect_judgement_toast;
            }
        }
        else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
    /**
     * Method is called when an instance of the activity subclass is created.
     * This method is called by the OS.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Call setContentView to inflate a layout and put it on screen.
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);
            mAnswerKey[mCurrentIndex].didCheat();
        } else {
            mIsCheater = false;
        }

        // d stands for Debug
        Log.d(TAG, "onCreate() called");

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);

        mTrueButton = (Button)findViewById(R.id.true_button);
        mFalseButton = (Button)findViewById(R.id.false_button);
        mPrevButton = (ImageButton)findViewById(R.id.previous_button);
        mNextButton = (ImageButton)findViewById(R.id.next_button);

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1 ) % mAnswerKey.length;
                updateQuestion();
            }
        });

        // Button Listeners
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // % used to avoid array out of bounds error when you reach end of array
                mCurrentIndex = (mCurrentIndex + 1) % mAnswerKey.length;

                mCounter = 0;

                while (mAnswerKey[mCurrentIndex].didCheat() && mCounter < mAnswerKey.length) {
                    mCurrentIndex = (mCurrentIndex + 1) % mAnswerKey.length;
                    mCounter++;
                }

                if (mAnswerKey[mCurrentIndex].didCheat() && mCounter >= mAnswerKey.length) {
                    Toast.makeText(QuizActivity.this, R.string.end, Toast.LENGTH_SHORT).show();
                    mCounter = 0;
                    return;

                }

                mIsCheater = false;
                updateQuestion();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // % used to avoid array out of bounds error when you reach end of array
                if (mCurrentIndex != 0 )
                {
                    mCurrentIndex = (mCurrentIndex - 1) % mAnswerKey.length;

                    mCounter = 0;

                    while (mAnswerKey[mCurrentIndex].didCheat() && mCounter <= mAnswerKey.length) {
                        if (mCurrentIndex != 0 ) {
                            mCurrentIndex = (mCurrentIndex - 1) % mAnswerKey.length;
                        } else {
                            mCurrentIndex = mAnswerKey.length - 1;
                        }

                        mCounter++;
                    }

                    if (mAnswerKey[mCurrentIndex].didCheat() && mCounter >= mAnswerKey.length) {
                        Toast.makeText(QuizActivity.this, R.string.end, Toast.LENGTH_SHORT).show();
                        mCounter = 0;
                        return;

                    }

                    mIsCheater = false;
                    updateQuestion();
                } else
                {
                    mCurrentIndex = mAnswerKey.length - 1;

                    mCounter= 0;

                    while (mAnswerKey[mCurrentIndex].didCheat() && mCounter <= mAnswerKey.length) {
                        if (mCurrentIndex != 0 ) {
                            mCurrentIndex = (mCurrentIndex - 1) % mAnswerKey.length;
                        } else {
                            mCurrentIndex = mAnswerKey.length - 1;
                        }

                        mCounter++;
                    }

                    if (mAnswerKey[mCurrentIndex].didCheat() && mCounter >= mAnswerKey.length) {
                        Toast.makeText(QuizActivity.this, R.string.end, Toast.LENGTH_SHORT).show();
                        mCounter = 0;
                        return;

                    }

                    mIsCheater = false;
                    updateQuestion();
                }

            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "cheat button clicked");

                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                Log.d(TAG, "intent created");

                boolean answerIsTrue = mAnswerKey[mCurrentIndex].isTrueQuestion();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(i, 0);
            }
        });



        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);

        if (mIsCheater)
            mAnswerKey[mCurrentIndex].cheated();
    }

    @Override
    // This asks the compiler to ensure that the class actually has the method that
    // you are attempting to override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState( Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSavedInstanceState");
        // Bundle object that maps string keys to values
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_CHEATER, mIsCheater);
    }

}
