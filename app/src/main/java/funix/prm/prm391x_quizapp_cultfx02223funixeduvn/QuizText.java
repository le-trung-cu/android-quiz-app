package funix.prm.prm391x_quizapp_cultfx02223funixeduvn;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

// câu hỏi dạng text
public class QuizText extends QuizBase {

    private List<String> answers;
    private String userAnswer;

    public QuizText(String title, List<String> answers) {
        super();
        question = title;
        this.answers = answers;
        userAnswer = "";
    }

    @Override
    public boolean checkCorrect() {
        if(userAnswer != null){
            for(String answer : answers){
                if(answer.equalsIgnoreCase(userAnswer)){
                    return  true;
                }
            }
        }
        return false;
    }

    @Override
    public void renderUi(LayoutInflater inflater, ViewGroup parent) {
        LinearLayout viewQuestion = (LinearLayout) inflater.inflate(R.layout.quiz_component,
                parent, false);

        TextView questionTitle = viewQuestion.findViewById(R.id.questionTitle);
        questionTitle.setText(this.question);

        EditText editText = (EditText) inflater.inflate(R.layout.quiz_text_component,
                viewQuestion, false);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setUserAnswer(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(!userAnswer.isEmpty()){
            editText.setText(userAnswer);
        }

        viewQuestion.addView(editText);
        parent.addView(viewQuestion);
    }

    @Override
    public void save(Bundle bundle) {
        bundle.remove("question_"+id);
        bundle.putString("question_" + id, userAnswer);
    }

    @Override
    public void resume(Bundle bundle) {
        userAnswer = bundle.getString("question_" + id);
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer.trim();
    }
}
