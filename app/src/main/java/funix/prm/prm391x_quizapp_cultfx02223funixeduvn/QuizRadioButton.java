package funix.prm.prm391x_quizapp_cultfx02223funixeduvn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuizRadioButton extends QuizBase {
    // selectionList
    private List<String> selectionList;
    // correct answer
    private int answerIndex;
    // user answer
    private int userAnswerIndex = -1;

    public QuizRadioButton(String title, int answerIndex) {
        super();
        question = title;
        selectionList = new ArrayList<>();
        this.answerIndex = answerIndex;
    }

    @Override
    public boolean checkCorrect() {
        return answerIndex == userAnswerIndex;
    }

    @Override
    public void renderUi(LayoutInflater inflater, ViewGroup parent) {
        LinearLayout viewQuestion = (LinearLayout) inflater.inflate(R.layout.quiz_component,
                parent, false);

        TextView questionTitle = viewQuestion.findViewById(R.id.questionTitle);
        questionTitle.setText(this.question);

        RadioGroup radioGroup = (RadioGroup) inflater.inflate(R.layout.quiz_radio_group_component, viewQuestion, false);

        int size = this.selectionList.size();

        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.quiz_radio_button_component,
                    radioGroup, false);
            radioButton.setText(this.selectionList.get(i));


            final int userCheckedIndex = i;

            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        userAnswerIndex = userCheckedIndex;
                    }
                }
            });

            if(userAnswerIndex == i){
                radioButton.setChecked(true);
            }
            radioGroup.addView(radioButton);
        }
        viewQuestion.addView(radioGroup);
        parent.addView(viewQuestion);
    }

    @Override
    public void addSelectionForRadioAndCheckbox(String choice) {
        selectionList.add(choice);
    }

    @Override
    public void save(Bundle bundle) {
        bundle.remove("question_"+id);
        bundle.putInt("question_" + id, userAnswerIndex);
    }

    @Override
    public void resume(Bundle bundle) {
        userAnswerIndex = bundle.getInt("question_" + id);
    }
}