import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hakansahin on 03/01/16.
 */
public class QuestionEquivalence {

    private final String TRAINING_DATA_PATH = "TrainingData/";
    private final String ASKUBUNTU_DATA_PATH = TRAINING_DATA_PATH + "AskUbuntu";
    private final String ENGLISH_DATA_PATH = TRAINING_DATA_PATH + "English";

    private List<Question> askUbuntuQuestionList, englishQuestionList;
    public QuestionEquivalence() throws IOException {

        readQuestions();
    }

    public void readQuestions() throws IOException {

        askUbuntuQuestionList = new ArrayList<Question>();

        File[] listOfFiles = (new File(ASKUBUNTU_DATA_PATH)).listFiles();
        for(File file : (listOfFiles != null ? listOfFiles : new File[]{})){

            BufferedReader br = new BufferedReader(new FileReader(file));
            String questionJSON = br.readLine();

            Question question = new Question(questionJSON);

            System.out.println(question.getTitle());
        }
    }

    public static void main(String[] args){

        try {
            QuestionEquivalence qe = new QuestionEquivalence();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
