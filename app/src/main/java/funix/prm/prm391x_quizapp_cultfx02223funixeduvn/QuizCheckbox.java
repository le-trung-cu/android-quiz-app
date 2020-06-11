package funix.prm.prm391x_quizapp_cultfx02223funixeduvn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// câu hỏi dạng checkbox
public class QuizCheckbox extends QuizBase {
    // selection list
    private List<String> selectionList;
    // correct answers
    private List<Integer> answerList;
    // user answers
    private List<Integer> userAnswerList;

    public QuizCheckbox(String title, List<Integer> answerList) {
        super();
        question = title;
        this.answerList = answerList;
        userAnswerList = new ArrayList<>();
        this.selectionList = new ArrayList<>();
    }

    // true when all user answers is correct
    @Override
    public boolean checkCorrect() {
        if (userAnswerList.size() == answerList.size()) {
            for (Integer answer : userAnswerList) {
                if (!answerList.contains(answer)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void renderUi(LayoutInflater inflater, ViewGroup parent) {
        LinearLayout viewQuestion = (LinearLayout) inflater.inflate(R.layout.quiz_component,
                parent, false);

        TextView questionTitle = viewQuestion.findViewById(R.id.questionTitle);
        questionTitle.setText(this.question);

        int size = this.selectionList.size();

        for (int i = 0; i < size; i++) {
            CheckBox checkBoxAnswer = (CheckBox) inflater.inflate(R.layout.checkbox_answer_component,
                    viewQuestion, false);

            checkBoxAnswer.setText(this.selectionList.get(i));

            final int indexAnswer = i;

            checkBoxAnswer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if(!userAnswerList.contains(indexAnswer)){
                            userAnswerList.add(indexAnswer);
                        }
                    } else {
                        int index = userAnswerList.indexOf(indexAnswer);
                        userAnswerList.remove(index);
                    }
                }
            });

            if(userAnswerList.contains(i)){
                checkBoxAnswer.setChecked(true);
            }
            viewQuestion.addView(checkBoxAnswer);
        }
        parent.addView(viewQuestion);
    }

    @Override
    public void addSelectionForRadioAndCheckbox(String choice) {
        selectionList.add(choice);
    }

    @Override
    public void save(Bundle bundle) {
        bundle.remove("question_"+id);
        bundle.putIntegerArrayList("question_" + id, (ArrayList<Integer>) userAnswerList);
    }

    @Override
    public void resume(Bundle bundle) {

        List<Integer> arrUserAnswer = bundle.getIntegerArrayList("question_"+id);

        if(arrUserAnswer != null){
            userAnswerList.clear();
            for(int value : arrUserAnswer){
                userAnswerList.add(value);
            }
        }
    }
}
