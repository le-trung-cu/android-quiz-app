package funix.prm.prm391x_quizapp_cultfx02223funixeduvn;

import android.app.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuizDbContext {

    // Id increased
    private static int increasedId = 0;
    // // REGEX for question (begin with number)
    private static final String REGEX_IS_QUESTION = "^\\d+.*";
    // REGEX for radio question
    private static final String REGEX_IS_RADIO_QUESTION = ".*#\\d+.*";
    // REGEX for checkbox question
    private static final String REGEX_IS_CHECKBOX_QUESTION = ".*#\\d+.* and #\\d+.*";
    // REGEX for text answer (contains pattern [...]
    private static final String REGEX_TEX_ANSWER = "\\(([\\w\\s]+)\\)+|\\\"([\\w\\s]+)\\\"+";
    // REGEX for radio answer or checkbox answer
    final static String REGEX_INDEX_ANSWER = "#(\\d+)";

    private List<QuizBase> questions = new ArrayList<>();
    public List<QuizBase> getQuestions(){
        return questions;
    }

    private void addQuestion(QuizBase question){
        increasedId++;
        question.setId(increasedId);
        questions.add(question);
    }

    public QuizDbContext(){
    }

    public void initialQuestionList(Activity activity, String fileQuiz) throws IOException {
        Scanner scanner = null;
        increasedId = 0; // reset incrementId
        try{
            scanner = new Scanner( activity.getAssets().open(fileQuiz));
            QuizBase question = null;
            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();
                if (line.matches(REGEX_IS_QUESTION)) {
                    question = null;
                    // nhap cau hoi
                    int indexOf = line.indexOf("(");
                    String questionTitle = line.substring(0, indexOf);

                    String answerString = line.substring(indexOf);

                    // match checkbox question
                    if (answerString.matches(REGEX_IS_CHECKBOX_QUESTION)) {

                        // match answers for checkbox question
                        final Pattern pattern = Pattern.compile(REGEX_INDEX_ANSWER);
                        final Matcher matcher = pattern.matcher(answerString);

                        List<Integer> answers = new ArrayList<>();
                        while (matcher.find()) {
                            String answerMatcher = matcher.group(1);
                            answers.add(Integer.parseInt(answerMatcher) - 1);
                        }
                        question = new QuizCheckbox(questionTitle, answers);
                    }
                    // match radio question
                    else if (answerString.matches(REGEX_IS_RADIO_QUESTION)) {

                        // match answer for radio question
                        final Pattern pattern = Pattern.compile(REGEX_INDEX_ANSWER);
                        final Matcher matcher = pattern.matcher(answerString);
                        String answerMatcher="";
                        if(matcher.find()){
                             answerMatcher = matcher.group(1);
                        }
                        question = new QuizRadioButton(questionTitle, Integer.parseInt(answerMatcher) - 1);
                    }
                    // match text question
                    else {
                        // match answer for text question
                        final Pattern pattern = Pattern.compile(REGEX_TEX_ANSWER);
                        final Matcher matcher = pattern.matcher(answerString);
                        List<String> answers = new ArrayList<>();

                        while (matcher.find()) {
                            answers.add(matcher.group(2));
                        }

                        question = new QuizText(questionTitle, answers);

                    }
                    addQuestion(question);
                }
                // if question is radio or checkbox, add selection answer
                else if (question != null && !line.isEmpty()) {
                    question.addSelectionForRadioAndCheckbox(line.trim());
                }
            }
        }catch (Exception e) {
            System.out.println("Error read file assets");
            throw e;
        }finally {
            if(scanner != null){
                scanner.close();
            }
        }
    }

}
