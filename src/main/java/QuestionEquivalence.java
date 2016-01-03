import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.medallia.word2vec.Searcher;
import com.medallia.word2vec.Word2VecExamples;
import com.medallia.word2vec.Word2VecModel;
import com.oracle.tools.packager.Log;

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

    public static Word2VecModel model;

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
//        QuestionEquivalence qe = new QuestionEquivalence();
        try {
            model = Word2VecModel.fromBinFile(new File("vectors.bin"));

            int limit = 0;
            for (String token: model.getVocab()) {
                if (limit % 25 == 0)
                    System.out.println("");
                System.out.print(token);
                limit++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Double> rConv(List<String> tokens) {
        try {
            List<List<Double>> qemb = new ArrayList<List<Double>>();

            Searcher searcher = model.forSearch();
            for (String token: tokens) {
                if (searcher.contains(token)) {
                    List<Double> vector = null;
                        vector = searcher.getRawVector(token);
                    qemb.add(vector);
                }
            }


        } catch (Searcher.UnknownWordException e) {
            e.printStackTrace();
        }

        return null;
    }
}
