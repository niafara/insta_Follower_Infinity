package ui.utils;


import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

import utility.MyLog;

public class VideoRequestHandler extends RequestHandler {
    public String SCHEME_VIDEO="video";
    public String https_VIDEO="https";
    @Override
    public boolean canHandleRequest(Request data)
    {
        String scheme = data.uri.getScheme();
        return (https_VIDEO.equals(scheme));
    }

    @Override
    public Result load(Request data, int arg1) throws IOException
    {
        Bitmap bm = ThumbnailUtils.createVideoThumbnail(data.uri.toString()/*.getPath()*/, MediaStore.Images.Thumbnails.MINI_KIND);
       MyLog.i("SCHEME_VIDEO", "uri "+data.uri);
       MyLog.i("SCHEME_VIDEO", "uri.toString() "+data.uri.toString());
       MyLog.i("SCHEME_VIDEO", "uri.getPath() "+data.uri.getPath());
       MyLog.i("SCHEME_VIDEO", "uri.getScheme() "+data.uri.getScheme());
       MyLog.i("SCHEME_VIDEO", "uri.getScheme() "+data.uri.getScheme());
       MyLog.i("SCHEME_VIDEO", "uri.getAuthority() "+data.uri.getAuthority());
       MyLog.i("SCHEME_VIDEO", "uri.getQuery() "+data.uri.getQuery());
       MyLog.i("SCHEME_VIDEO", "uri.getUserInfo() " + data.uri.getUserInfo());
        return new Result(bm, Picasso.LoadedFrom.NETWORK);

    }

}
