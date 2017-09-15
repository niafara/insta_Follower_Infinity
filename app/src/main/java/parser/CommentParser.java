package parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import data.InstagramComment;
import utility.MyLog;

public class CommentParser {

    private String TAG = "CommentParser";

    public ArrayList<InstagramComment> parseComments(JSONObject response){

        ArrayList <InstagramComment> instagramComments = new ArrayList<>();
        //                JSONObject
        JSONArray commentsJsonArray = null;
        try {
            commentsJsonArray = response.getJSONArray("data");
            for (int i=0; i<commentsJsonArray.length() ;i++){
                JSONObject commentJsonObject = commentsJsonArray.getJSONObject(i);
                InstagramComment comment = new InstagramComment();

                comment.setText(commentJsonObject.getString("text"));
                comment.setCreated_time(commentJsonObject.getString("created_time"));
                comment.setFull_name(commentJsonObject.getJSONObject("from").getString("full_name"));
                comment.setProfile_picture(commentJsonObject.getJSONObject("from").getString("profile_picture"));
                comment.setUser_id(commentJsonObject.getJSONObject("from").getString("id"));
                comment.setUsername(commentJsonObject.getJSONObject("from").getString("username"));
                comment.setComment_id(commentJsonObject.getInt("id"));

                MyLog.i("from.setUser_id ", comment.getUser_id());
                instagramComments.add(comment);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            MyLog.i(TAG, "error  " + e.getMessage());
        }

        return instagramComments;
    }
}
