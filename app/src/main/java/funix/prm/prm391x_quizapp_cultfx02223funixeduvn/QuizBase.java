package funix.prm.prm391x_quizapp_cultfx02223funixeduvn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

// all questions have much extend QuizBase
public abstract class QuizBase {
    // question identifier
    int id;

    // question text
    String question;

    void setId(int id){
        this.id = id;
    }

    // render ui
    public abstract void renderUi(LayoutInflater inflater, ViewGroup parent);

    /**
     * check answers for question
     */
    public abstract boolean checkCorrect();

    /**
     * add selection answers for radio and checkbox question
     */
    public void addSelectionForRadioAndCheckbox(String choice){
        return;
    }

    /**
     * save user answer
     * */
    public void save(Bundle bundle){
        return;
    }

    /**
     * set answer stored for question's answers
     * */
    public void resume(Bundle bundle){
        return;
    }
}
