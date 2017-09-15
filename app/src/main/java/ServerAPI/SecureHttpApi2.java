//package ServerAPI;
//
//import org.apache.commons.codec.binary.Hex;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import utility.MyLog;
//
//public class SecureHttpApi2
//{
//  //  private final static String BaseUrl = "http://164.138.19.140";
//    //private final static String BaseUrl = "";
//   // private final static String BaseUrl = "http://192.168.43.200:8080/Follower/default/Php";
//    private final static String BaseUrl = "http://192.168.43.200:8080/Follower/";
//
//    private OkHttpClient httpClient;
//
//    private static SecureHttpApi2 instance=null;
//
//    public static final String SECURITY_KEY = "masoudXSWV56732PE";
//
//    private static final boolean Enabled=true;
//
//    public static final MediaType JSON
//            = MediaType.parse("application/json; charset=utf-8");
//
//    public interface ResponseHandler
//    {
//        public void OnSuccess(JSONObject response);
//
//        public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse);
//    }
//
//    public static SecureHttpApi2 GetInstance()
//    {
//        if(instance==null)
//            instance = new SecureHttpApi2();
//
//        return instance;
//    }
//
//    private SecureHttpApi2()
//    {
//        httpClient =  new OkHttpClient.Builder()
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(15, TimeUnit.SECONDS)
//                .readTimeout(120, TimeUnit.SECONDS)
//                .build();
//    }
//
//    public void Get(String uri,final ResponseHandler handler)
//    {
//
//        if(Enabled) {
//
//            String pathPart = uri;
//            int indexOfQuestionMark = uri.indexOf('?');
//
//            MyLog.i(SecureHttpApi2.class.getName(), "index: " + indexOfQuestionMark);
//
//            if (indexOfQuestionMark != -1) {//there are query parameters
//                pathPart = pathPart.substring(0, indexOfQuestionMark);
//            }
//
//            MyLog.i(SecureHttpApi2.class.getName(), "path: " + pathPart);
//
//            String hash = new String(Hex.encodeHex(DigestUtils.sha1(pathPart + SECURITY_KEY)));
//
//            MyLog.i(SecureHttpApi2.class.getName(), "hash: " + hash);
//
//            if (indexOfQuestionMark != -1) {//has query parameters at hash at then end
//                uri += "&hash=" + hash;
//            } else {//it doens't have query part , add it
//                uri += "?hash=" + hash;
//            }
//        }
//
//        MyLog.i(SecureHttpApi2.class.getName(), "uri: " + uri);
//
//
//        Request request = new Request.Builder().url(BaseUrl+uri).build();
//
//        httpClient.newCall(request).enqueue(new CustomOkHttpResponseHandler(handler));
//
//    }
//
//    public void Post(String uri,JSONObject jsonObject,final ResponseHandler handler)
//    {
//        JSONObject sentJson=new JSONObject();
//
//        if(Enabled) {
//
//            try {
//                sentJson.put("data", jsonObject);
//            } catch (JSONException e) {
//                MyLog.e(SecureHttpApi2.class.getName(), e.getMessage());
//            }
//
//            String hash = new String(Hex.encodeHex(DigestUtils.sha1(jsonObject.toString() + SECURITY_KEY)));
//
//            try {
//                sentJson.put("hash", hash);
//            } catch (JSONException e) {
//                MyLog.e(SecureHttpApi2.class.getName(), e.getMessage());
//            }
//
//        }
//        else
//        {
//            sentJson = jsonObject;
//        }
//
//        RequestBody requestBody = RequestBody.create(JSON, sentJson.toString());
//        Request request = new Request.Builder().url(BaseUrl+uri).post(requestBody).build();
//
//        httpClient.newCall(request).enqueue(new CustomOkHttpResponseHandler(handler));
//    }
//
//}
