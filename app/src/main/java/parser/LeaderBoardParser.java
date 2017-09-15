package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

import data.FriendStatus;
import data.InstagramUser;
import data.LeaderBoard;
import utility.MyLog;
import utility.Utility;

public class LeaderBoardParser {

    private static final String TAG = ";LeaderBoardParser";

    public static Stack <LeaderBoard> parsFriendStatus(JSONObject response) throws JSONException {
        Stack <LeaderBoard>  leaderBoards = new Stack<>();

        JSONArray userJsonArray = response.getJSONArray("users");
        for (int i=0; i<userJsonArray.length() ; i++){
            JSONObject userObject = userJsonArray.getJSONObject(i);
            LeaderBoard leaderBoard = new LeaderBoard();
            leaderBoard.setCount(userObject.getInt("count"));
            leaderBoard.setInsta_id(userObject.getString("insta_id"));
            leaderBoard.setName(userObject.getString("name"));
            leaderBoards.push(leaderBoard);
        }
        // sort users , because we use stack
//        Collections.sort(leaderBoards, new Comparator<LeaderBoard>() {
//            @Override
//            public int compare(LeaderBoard lhs, LeaderBoard rhs) {
//                return lhs.getCount().compareTo(rhs.getCount());
//            }
//        });

        // reverse users , because we use stack
        Collections.reverse(leaderBoards);

        return leaderBoards;
    }

    public static ArrayList <InstagramUser> parsFriendStatusAsUser(JSONObject response,
                                                                   boolean isLike) throws JSONException {
        ArrayList <InstagramUser>  leaderBoards = new ArrayList<>();
        MyLog.d(TAG, "res : "+response);

        JSONArray userJsonArray = response.getJSONArray("users");
        for (int i=0; i<userJsonArray.length() ; i++){
            JSONObject userObject = userJsonArray.getJSONObject(i);
            InstagramUser user = new InstagramUser();
            if (isLike)
                user.setUserFullName(Utility
                        .toPersianNumber(userObject.getInt("count") + " لایک"));
            else
                user.setUserFullName(Utility
                        .toPersianNumber((userObject.getInt("count") + " فالو")));
            user.setUserId(userObject.getString("insta_id"));
            user.setUserName(userObject.getString("name"));
            try{
                if (!userObject.has("profile_pic") || userObject.get("profile_pic")==null
                        || userObject.getString("profile_pic") == null
                        || userObject.getString("profile_pic").equalsIgnoreCase("null")) {
                    user.setProfilePicture("temp");
                }
                else
                    user.setProfilePicture(userObject.getString("profile_pic"));

            }catch (Exception e){
                MyLog.d(TAG, "res : ee "+e.getMessage());
                e.printStackTrace();
                user.setProfilePicture("temp");
            }
            leaderBoards.add(user);
        }
        // sort users , because we use stack
//        Collections.sort(leaderBoards, new Comparator<LeaderBoard>() {
//            @Override
//            public int compare(LeaderBoard lhs, LeaderBoard rhs) {
//                return lhs.getCount().compareTo(rhs.getCount());
//            }
//        });

        // reverse users , because we use stack
//        Collections.reverse(leaderBoards);

        return leaderBoards;
    }
}
