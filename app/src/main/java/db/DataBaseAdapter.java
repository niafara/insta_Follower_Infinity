package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import utility.MyLog;

/**
 * Created by Ali on 06/27/2016.
 */
public class DataBaseAdapter
{
    protected Context context;
    protected SQLiteDatabase db;
    protected InstaFollowerDB instaFollowerDB;

    public DataBaseAdapter(Context context)
    {
        this.context=context;
        instaFollowerDB = new InstaFollowerDB(context);
    }

    public void open()
    {
        db = instaFollowerDB.getWritableDatabase();
        db.beginTransaction();
    }

    public void close()
{
    if(db!=null) {

        db.setTransactionSuccessful();
        db.endTransaction();

        instaFollowerDB.close();
        db = null;
    }
}
    public void closeFailed()
    {
        if(db!=null) {
            db.endTransaction();

            instaFollowerDB.close();
            db = null;
        }
    }

    //Users
    public Cursor getAllUsers()
    {
        return db.rawQuery("SELECT * FROM users",new String[]{});
    }

    public Cursor getUser(String insta_id)
    {
        return db.rawQuery("SELECT * FROM users WHERE insta_id=?",new String[]{insta_id});
    }

    public void addUser(String insta_id,String username)
    {
        db.execSQL("INSERT INTO users (insta_id,username) VALUES(?,?)"
                ,new String[]{insta_id,username});
    }

    public void addUser(String insta_id,String username,String fullname,String profile_pic)
    {
        if (!userExists(insta_id))
        db.execSQL("INSERT INTO users (insta_id,username,fullname,profile_pic) VALUES(?,?,?,?)"
                ,new String[]{insta_id,username,fullname,profile_pic});
    }

    public void addUser(String insta_id,String username,String fullname,String profile_pic,int likes)
    {
        db.execSQL("INSERT INTO users (insta_id,username,fullname,profile_pic,likes) VALUES(?,?,?,?,?)"
                ,new String[]{insta_id,username,fullname,profile_pic,Integer.toString(likes)});
    }

    public void updateUser(String insta_id,String username,String fullname,String profile_pic,int likes)
    {
        db.execSQL("UPDATE users SET username=?,fullname=?,profile_pic=?,likes=? WHERE insta_id=?"
                ,new String[]{username,fullname,profile_pic,Integer.toString(likes),insta_id});
    }

    public void updateUser(String insta_id,String username,String fullname,String profile_pic)
    {
        db.execSQL("UPDATE users SET username=?,fullname=?,profile_pic=? WHERE insta_id=?"
                ,new String[]{username,fullname,profile_pic,insta_id});
    }

    public void deleteUser(String insta_id)
    {
        db.execSQL("DELETE FROM users WHERE insta_id=?",new String[]{insta_id});
    }

    public void deleteFollower(String insta_id)
    {
        db.execSQL("DELETE FROM followers WHERE insta_id=?",new String[]{insta_id});
    }

    public boolean userExists(String insta_id)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE insta_id=?", new String[]{insta_id});

        boolean exist = cursor.moveToFirst();
        cursor.close();

        return exist;
    }

    public boolean followerExists(String insta_id)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM followers WHERE insta_id=?", new String[]{insta_id});

        boolean exist = cursor.moveToFirst();
        cursor.close();

        return exist;
    }
    public boolean followingExists(String insta_id)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM following WHERE insta_id=?", new String[]{insta_id});

        boolean exist = cursor.moveToFirst();
        cursor.close();

        return exist;
    }

    public Cursor getMaxLiker(int page)
    {
        return db.rawQuery("SELECT * FROM users ORDER BY likes DESC  LIMIT ?,10",new String[]{Integer.toString(page)});
    }

    public Cursor getMinLiker(int page)
    {
        return db.rawQuery("SELECT * FROM users ORDER BY likes  LIMIT ?,10",new String[]{Integer.toString(page)});
    }

    //

    //followers

    public Cursor getAllFollowers()
    {
        return db.rawQuery("SELECT * FROM followers",new String[]{});
    }

    public Cursor getFollower(String insta_id)
    {
        return db.rawQuery("SELECT * FROM followers WHERE insta_id=?",new String[]{insta_id});
    }

    public void updateFollower(String insta_id,int refreshTime,int enabled)
    {
        db.execSQL("UPDATE followers SET refresh_time=?,enabled=? WHERE insta_id=?",
                new String[]{Integer.toString(refreshTime),Integer.toString(enabled),insta_id});
    }

    public void addFollower(String insta_id,int refreshTime)
    {
        if (!followerExists(insta_id))
            db.execSQL("INSERT INTO followers (insta_id,refresh_time) VALUES(?,?)",new String[]{insta_id, Integer.toString(refreshTime)});
        MyLog.d(";ddddd323", insta_id + "  " + refreshTime);
    }

    public int getFollowersCountUnequalRefreshTime(int excluding_refresh_time)
    {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM followers WHERE refresh_time!=?",new String[]{Integer.toString(excluding_refresh_time)});

        if(cursor.moveToFirst())
        {
            int result = cursor.getInt(0);
            cursor.close();

            if(cursor.moveToFirst())
            {
                do {
                    MyLog.i("DB", cursor.getString(0));
                }
                while (cursor.moveToNext());
            }

            return  result;
        }
        else
        {
            cursor.close();
            return 0;
        }

    }

    public int getFollowersGreaterRefreshTime(int max_refresh_time)
    {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM followers WHERE refresh_time>?",new String[]{Integer.toString(max_refresh_time)});

        if(cursor.moveToFirst())
        {
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }
        else
        {
            cursor.close();
            return 0;
        }

    }

    public int getFollowersMaxRefreshTime()
    {
        Cursor cursor = db.rawQuery("SELECT MAX(refresh_time) FROM followers",new String[]{});
        if(cursor.moveToFirst())
        {
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }
        else
        {
            cursor.close();
            return 0;
        }
    }

    //

    //following

    public Cursor getAllFollowings()
    {
        return db.rawQuery("SELECT * FROM following",new String[]{});
    }

    public Cursor getFollowing(String insta_id)
    {
        return db.rawQuery("SELECT * FROM following WHERE insta_id=?",new String[]{insta_id});
    }

    public void updateFollowing(String insta_id,int refreshTime,int enabled)
    {
        db.execSQL("UPDATE following SET refresh_time=?,enabled=? WHERE insta_id=?",
                new String[]{Integer.toString(refreshTime),Integer.toString(enabled),insta_id});
    }

    public void addFollowing(String insta_id,int refreshTime)
    {
        if (!followingExists(insta_id))
            db.execSQL("INSERT INTO following (insta_id,refresh_time) VALUES(?,?)",new String[]{insta_id,Integer.toString(refreshTime)});
    }

    public int getFollowingCountUnequalRefreshTime(int excluding_refresh_time)
    {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM following WHERE refresh_time!=?",new String[]{Integer.toString(excluding_refresh_time)});

        if(cursor.moveToFirst())
        {
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }
        else
        {
            cursor.close();
            return 0;
        }

    }

    public int getFollowingGreaterRefreshTime(int max_refresh_time)
    {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM following WHERE refresh_time>?",new String[]{Integer.toString(max_refresh_time)});

        if(cursor.moveToFirst())
        {
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }
        else
        {
            cursor.close();
            return 0;
        }

    }

    public int getFollowingMaxRefreshTime()
    {
        Cursor cursor = db.rawQuery("SELECT MAX(refresh_time) FROM following ",new String[]{});
        if(cursor.moveToFirst())
        {
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }
        else
        {
            cursor.close();
            return 0;
        }
    }

    //

    public void clearFollowers(){
        ClearTable("followers");
    }

    public void clearFollowing(){
        ClearTable("following");
    }

    public void clearUsers(){
        ClearTable("users");
    }

    public void clearAndCloseDatabase()
    {

        ClearTable("followers");
        ClearTable("following");
        ClearTable("users");

        db.setTransactionSuccessful();
        db.endTransaction();

        db.execSQL("VACUUM;");

        instaFollowerDB.close();
        db = null;
    }

    private void ClearTable(String tableName)
    {
        db.execSQL("DELETE FROM "+tableName);
//        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = ?;",new String[]{tableName});
    }

}
