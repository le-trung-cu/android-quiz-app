package funix.prm.prm391x_quizapp_cultfx02223funixeduvn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class HomeworkActivity extends AppCompatActivity {
    // store questions
    private QuizDbContext questionList;

    // homework duration 70 seconds
    private int homeworkDuration = 70;
    private TextView timeText;
    private Handler handler = new Handler();
    private String userName = "";

    private LinearLayout layoutQuestion;
    // homework submit
    private Button btnSubmit;

    // questions data file
    private String quizFile = "quiz.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        // user name
        Intent intent = getIntent();
        if(intent != null){
            userName = intent.getStringExtra("userName");
        }
        if(savedInstanceState != null){
            userName = savedInstanceState.getString("userName");
        }
        TextView tvUserName = findViewById(R.id.tv_user_name);
        tvUserName.setText(userName);

        // questions
        layoutQuestion = findViewById(R.id.questionWrap);
        final Context context = getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        List<QuizBase> questions = null;
        timeText = findViewById(R.id.time);

        try {
            questionList = new QuizDbContext();
            questionList.initialQuestionList(this, quizFile);
            questions = questionList.getQuestions();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // set answers for questions if questions is answered
        if (savedInstanceState != null) {
            homeworkDuration = savedInstanceState.getInt("homeworkDuration");
            for(QuizBase question : questions){
                question.resume(savedInstanceState);
            }
        }
        // display questions UI
        for(QuizBase question : questions){
            question.renderUi(inflater, layoutQuestion);
        }

        // homework duration
        handlerHomeworkDuration();
        // submit button
        btnSubmit = findViewById(R.id.submitButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    //
    private void handlerHomeworkDuration(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (homeworkDuration >= 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            timeText.setText(homeworkDuration / 60 + " : " + homeworkDuration % 60);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                        homeworkDuration--;
                    } catch (Exception e) {
                    }
                }
                // handle when homework duration time out
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        submit();
                        btnSubmit.setClickable(false);
                        btnSubmit.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                    }
                });
            }
        }).start();
    }

    private void submit() {
        int correctCount = 0;
        List<QuizBase> questions = questionList.getQuestions();
        for (QuizBase question : questions) {
            if (question.checkCorrect()) {
                correctCount++;
            }
        }
        String toastString = "";
        if(correctCount == questions.size()){
            toastString = getString(R.string.notifySubmitPerfect);
        }else {
            toastString = getString(R.string.notifySubmitTryAgain);
        }
        toastString = String.format(toastString, correctCount, questions.size());
        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userName", userName);
        // save answers for questions
        outState.putInt("homeworkDuration", homeworkDuration);
        List<QuizBase> questions = questionList.getQuestions();
        for (QuizBase question : questions) {
            question.save(outState);
        }
    }
}