import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.medallia.word2vec.Searcher;
import com.medallia.word2vec.Word2VecExamples;
import com.medallia.word2vec.Word2VecModel;
import com.oracle.tools.packager.Log;
import org.jblas.DoubleMatrix;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hakansahin on 03/01/16.
 */
public class QuestionEquivalence {

    private final String TRAINING_DATA_PATH = "TrainingData/";
    private final String ASKUBUNTU_DATA_PATH = TRAINING_DATA_PATH + "AskUbuntu";
    private final String ENGLISH_DATA_PATH = TRAINING_DATA_PATH + "English";
    private final int WINDOW_SIZE = 3, CONV_SIZE = 200 , CLU = 250;
    private DoubleMatrix W1, b;

    private List<Question> askUbuntuQuestionList, englishQuestionList;

    public static Word2VecModel model;

    public QuestionEquivalence() throws IOException, Searcher.UnknownWordException {
        readQuestions();

        W1 = DoubleMatrix.rand(CLU, WINDOW_SIZE * CONV_SIZE);
        b = DoubleMatrix.rand(CLU,1).mul(-1);

        try {
            model = Word2VecModel.fromBinFile(new File("vectors.bin"));
//
//            int limit = 0;
//            for (String token: model.getVocab()) {
//                if (limit % 25 == 0)
//                    System.out.println("");
//                System.out.print(token);
//                limit++;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Question q1 = englishQuestionList.get(0);
        DoubleMatrix rConv1 = rConv(q1.getAllTokens());

        Question q2 = q1.getRelatedQuestions()[0];
        DoubleMatrix rConv2 = rConv(q2.getAllTokens());

        Question q3 = englishQuestionList.get(1);
        DoubleMatrix rConv3 = rConv(q3.getAllTokens());

        Question q4 = q3.getRelatedQuestions()[0];
        DoubleMatrix rConv4 = rConv(q4.getAllTokens());

        double sim11 = computeCosineSimilarity(rConv1,rConv1);
        double sim12 = computeCosineSimilarity(rConv1,rConv2);
        double sim34 = computeCosineSimilarity(rConv3,rConv4);
        double sim13 = computeCosineSimilarity(rConv1,rConv3);

        System.out.println("Similarity1-1: " + sim11 / sim11);
        System.out.println("Similarity1-2: " + sim12 / sim11);
        System.out.println("Similarity3-4: " + sim34 / sim11);
        System.out.println("Similarity1-3: " + sim13 / sim11);

    }

    public void readQuestions() throws IOException {
        // Reads questions of 'AskUbuntu'.
//        askUbuntuQuestionList = new ArrayList<Question>();
        File[] listOfFiles = (new File(ASKUBUNTU_DATA_PATH)).listFiles();
//        for(File file : (listOfFiles != null ? listOfFiles : new File[]{})){
//            if(!file.getName().endsWith(".txt")) continue;
//            askUbuntuQuestionList.add(new Question((new BufferedReader(new FileReader(file))).readLine()));
//        }

        // Reads questions of 'English'.
        englishQuestionList = new ArrayList<Question>();
        listOfFiles = (new File(ENGLISH_DATA_PATH)).listFiles();
        for(File file : (listOfFiles != null ? listOfFiles : new File[]{})){
            if(!file.getName().endsWith(".txt")) continue;
            englishQuestionList.add(new Question((new BufferedReader(new FileReader(file))).readLine()));
        }
    }

    public DoubleMatrix rConv(List<String> tokens) throws Searcher.UnknownWordException {
        DoubleMatrix qemb = new DoubleMatrix(tokens.size(),CONV_SIZE);

        Searcher searcher = model.forSearch();
        for (int i=0; i<tokens.size(); i++) {
            String token = tokens.get(i);
            if (searcher.contains(token)) {
                List<Double> vector = searcher.getRawVector(token);

                for(int j=0; j<vector.size(); j++)
                    qemb.put(i,j, vector.get(j));
            }
        }

        System.out.println("\n");

        DoubleMatrix ave = new DoubleMatrix(CLU, tokens.size() - (WINDOW_SIZE-1));

        for(int i=0 ; i<tokens.size() - (WINDOW_SIZE-1); i++){

            DoubleMatrix z = new DoubleMatrix(WINDOW_SIZE * CONV_SIZE,1);

            for(int j=0; j<WINDOW_SIZE; j++)
                for(int k=0; k<CONV_SIZE; k++)
                    z.put(j*CONV_SIZE + k, qemb.get(j+i,k));

            DoubleMatrix d = W1.mmul(z).add(b);
            ave.putColumn(i, d);
        }

        return ave.rowMaxs();
    }

    public double computeCosineSimilarity(DoubleMatrix d1, DoubleMatrix d2){
//        DoubleMatrix normalized1 = d1.div(d1.distance2(DoubleMatrix.zeros(d1.getLength())));
//        DoubleMatrix normalized2 = d2.div(d2.distance2(DoubleMatrix.zeros(d2.getLength())));
//        return normalized1.mmul(normalized2.transpose()).get(0);
        return (d1.mmul(d2.transpose()).get(0) /
                (d1.distance2(DoubleMatrix.zeros(d1.getLength())) * d2.distance2(DoubleMatrix.zeros(d2.getLength()))));
    }

    public static void main(String[] args) throws IOException, Searcher.UnknownWordException {
        QuestionEquivalence qe = new QuestionEquivalence();

    }
}
