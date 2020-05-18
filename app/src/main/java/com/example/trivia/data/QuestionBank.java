package com.example.trivia.data;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    //Using an arrayList to store all info coming from the jsonfile
    ArrayList <Question> questionArrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    //Method that will extract all the info from jSonArrayRequest
    //Taking an argument that will make sure evrything is setup correctly before using
    public List<Question> getQuestion(final AnswerListAsyncResponse callback){

        //Creation of a JSonArrayRequest;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    //Where the extraction is happening
                    @Override
                    public void onResponse(JSONArray response) {
                        //response is now the whole array with content

                        //looping thru the response array and getting question and answer
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                //Setting each value to the question object
                                Question question = new Question();
                                question.setAnswer(response.getJSONArray(i).get(0).toString());
                                question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));


                                //Add question object to our array list
                                questionArrayList.add(question);
                               // Log.d("quesR", "onRes"+ question);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (null != callback) {
                            callback.processFinished(questionArrayList);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );

        //Adding jsonArrayRequest to requestQueue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);


        return questionArrayList;
    }
}
