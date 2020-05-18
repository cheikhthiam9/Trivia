package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MESSAGE_ID = "messages_prefs";
    private TextView questionTextView;
    private TextView counterTextView;
    private TextView scoreTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private int currentIndexQuestion = 0;
    private int currentScore = 0;
    private int highestScore;
    private List <Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.question_text_view);
        counterTextView = findViewById(R.id.counter_text);
        scoreTextView = findViewById(R.id.score_text_view);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);


        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);


        questionList = new QuestionBank().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                questionTextView.setText(questionArrayList.get(currentIndexQuestion).getAnswer());
                counterTextView.setText(currentIndexQuestion+ "/" + questionArrayList.size());
                getPref();

               // Log.d("Main",  "OnCreate"+ questionArrayList);

            }
        });

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.prev_button:
                currentIndexQuestion--;
                updateQuestion();
                break;
            case R.id.next_button:
                currentIndexQuestion++;
                updateQuestion();
                break;
            case R.id.true_button:
                checkAnswer (true);
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer (false);
                updateQuestion();
                break;
            default:
                break;

        }

    }

    private void checkAnswer(boolean b) {

        int toastMessageId = 0 ;

        if (questionList.get(currentIndexQuestion).isAnswerTrue() == b) {
            currentScore++;
            fadeView();
            //currentIndexQuestion++;
            //updateQuestion();

            toastMessageId = R.string.correct_answer;
        } else {
            currentScore--;
            shakeAnimation();
            toastMessageId = R.string.incorrect_answer;

        }
        savePref();
        Toast.makeText(MainActivity.this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion() {

        if (currentIndexQuestion < 0){
            currentIndexQuestion = 0;
        }

        if (currentScore < 0) {
            currentScore =0;
        }

        if (currentIndexQuestion < questionList.size())
                {
                    questionTextView.setText(questionList.get(currentIndexQuestion).getAnswer());
                    counterTextView.setText(currentIndexQuestion+ "/" + questionList.size());
                    scoreTextView.setText("Highest Score: " + currentScore);
                }

    }

    //ANIMATION USING CODE
    private void fadeView (){

        //The view we are going to add the animation in
        final CardView cardView = findViewById(R.id.cardView);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //ANIMATION USING XML
    private void  shakeAnimation () {
        //importing our animation located in the anim folder
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);

        //The view we are going to add the animation in
         final CardView cardView = findViewById(R.id.cardView);

        //Calling setAnimation to action the shake animation on the card view
        cardView.setAnimation(shake);

        //Handling events when the shake animation is active
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void savePref () {

        if (currentScore > highestScore )
        {
           // highestScore = currentScore;
            SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("score", currentScore);
            editor.apply();
        }

    }

    private void getPref () {
        //Getting stuff back from SharedPreferences
        SharedPreferences getSharedPrefs = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        highestScore = getSharedPrefs.getInt("score", 0);
        scoreTextView.setText("Highest Score: " + highestScore);
    }
}
