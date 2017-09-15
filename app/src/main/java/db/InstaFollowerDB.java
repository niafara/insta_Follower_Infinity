package db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Ali on 06/27/2016.
 */
public class InstaFollowerDB extends SQLiteAssetHelper
{

    private static final String DATABASE_NAME = "insta_follower.db";
    private static final int DATABASE_VERSION = 1;


    public InstaFollowerDB(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

}
