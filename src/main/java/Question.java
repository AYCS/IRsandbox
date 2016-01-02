import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by hakansahin on 03/01/16.
 */
public class Question{

    private int id;
    private String title, body;
    private Question[] relatedQuestions;
    public Question(String json){

        JSONObject jsonObject = (new JSONArray(json)).getJSONObject(0);
        init(jsonObject);

        JSONArray relatedQuestions = jsonObject.optJSONArray("related_questions");
        this.relatedQuestions = new Question[relatedQuestions.length()];
        for (int i = 0;i < this.relatedQuestions.length;i++) {
            JSONObject relatedQuestion = relatedQuestions.getJSONObject(i);
            this.relatedQuestions[i] = new Question(relatedQuestion);
        }
    }

    public Question(JSONObject jsonObject) {
        init(jsonObject);
    }

    public void init(JSONObject json){
        this.id = json.getInt("id");
        this.title = json.getString("title");
        this.body = json.getString("body");
    }

    public int getID(){ return this.id; }
    public String getTitle(){ return this.title; }
    public String getBody(){ return this.body; }
    public Question[] getRelatedQuestions(){ return this.relatedQuestions; }

}
