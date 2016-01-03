import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

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
        // Reads questions of 'AskUbuntu'.
        askUbuntuQuestionList = new ArrayList<Question>();
        File[] listOfFiles = (new File(ASKUBUNTU_DATA_PATH)).listFiles();
        for(File file : (listOfFiles != null ? listOfFiles : new File[]{})){
            if(!file.getName().endsWith(".txt")) continue;
            askUbuntuQuestionList.add(new Question((new BufferedReader(new FileReader(file))).readLine()));
        }

        // Reads questions of 'English'.
        englishQuestionList = new ArrayList<Question>();
        listOfFiles = (new File(ENGLISH_DATA_PATH)).listFiles();
        for(File file : (listOfFiles != null ? listOfFiles : new File[]{})){
            if(!file.getName().endsWith(".txt")) continue;
            englishQuestionList.add(new Question((new BufferedReader(new FileReader(file))).readLine()));
        }
    }

    public static void main(String[] args) throws IOException {
        QuestionEquivalence qe = new QuestionEquivalence();
    }
}
