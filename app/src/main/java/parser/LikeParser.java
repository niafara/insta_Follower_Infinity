package parser;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import data.InstagramLike;
import data.InstagramUser;
import data.UserData;
import utility.MyLog;

public class LikeParser {

    String TAG = "LikeParser";

    public InstagramLike parsLike(JSONObject response, boolean isDataObject){

        InstagramLike like = new InstagramLike();

        try {
            JSONObject dataObject;
            if (isDataObject)
                dataObject = response;
            else
                dataObject = response.getJSONObject("data");
            like.setUser_id(dataObject.getString("id"));
            like.setUsername(dataObject.getString("username"));
            like.setFull_name(dataObject.getString("full_name"));
            like.setProfile_picture(dataObject.getString("profile_picture"));

        } catch (JSONException e) {
            e.printStackTrace();
            MyLog.i(TAG, "user.getUserName() ee " + e.getMessage());
        }
        return like;
    }

}


