//package ServerAPI;
//
//import com.onesignal.OneSignal;
//
//import org.apache.commons.codec.binary.Hex;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.DecimalFormat;
//
//import data.UserData;
//import instaAPI.InstagramApi;
//import utility.MyLog;
//
//public class ServerApi22 {
//
//    private SecureHttpApi secureHttpApi = SecureHttpApi.GetInstance();
//    private UserData userData= UserData.getInstance();
//    public static ServerApi22 _Instance;
//
//    public static ServerApi22 getInstance() {
//        if (_Instance == null)
//            _Instance = new ServerApi22();
//        return _Instance;
//    }
//
//    public interface JsonHandler
//    {
//        public void SuccessHandle(JSONObject response);
//        public void FailHandle(JSONObject response);
//    }
//
//    public void syncUser() throws JSONException {
//        String uri = "/v1/user/sync";
//        JSONObject object = new JSONObject();
//        object.put("id", userData.getSelf_user().getUserId());
//        object.put("username", userData.getSelf_user().getUserName());
//        object.put("password", "dummy");
//        object.put("token", userData.getSelf_user().getToken());
//        MyLog.i(";Ser start  ", "OnSuccess" + userData.getSelf_user().getToken()
//                        + userData.getSelf_user().getUserName()
//                        + userData.getSelf_user().getUserId()
//                        + userData.getSelf_user().getPassword()
//        );
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser  ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";Ser  ", "OnFailure" + errorResponse);
//
//            }
//        });
//
//
//        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
//            @Override
//            public void idsAvailable(String userId, String registrationId) {
//                try {
//                    syncNotifToken(userData.getSelf_user().getUserId(),
//                            userId);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                MyLog.d(";d;debug", "User:" + userId);
//                if (registrationId != null)
//                    MyLog.d(";d;debug", "registrationId:" + registrationId);
//            }
//        });
//
//    }
//
//    public void syncUser(final SecureHttpApi.ResponseHandler handler) throws JSONException {
//        String uri = "/v1/user/sync";
//        JSONObject object = new JSONObject();
//        object.put("id", userData.getSelf_user().getUserId());
//        object.put("username", userData.getSelf_user().getUserName());
//        object.put("password", "dummy");
//        object.put("token", userData.getSelf_user().getToken());
//        MyLog.i(";Ser0 start  ", "Token " + userData.getSelf_user().getToken()
//                        + "UserName " + userData.getSelf_user().getUserName()
//                        + "UserId " + userData.getSelf_user().getUserId()
//                        + "Password " + userData.getSelf_user().getPassword()
//        );
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser0  ", "OnSuccess" + response);
//                handler.OnSuccess(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";Ser0  ", "OnFailure" + errorResponse);
//                handler.OnFailure(statusCode, throwable, errorResponse);
//
//            }
//        });
//
//
//        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
//            @Override
//            public void idsAvailable(String userId, String registrationId) {
//                try {
//                    syncNotifToken(userData.getSelf_user().getUserId(),
//                            userId);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                MyLog.d(";d;debug", "User:" + userId);
//                if (registrationId != null)
//                    MyLog.d(";d;debug", "registrationId:" + registrationId);
//            }
//        });
//
//    }
//
//    public void syncSex(boolean isMale) throws JSONException {
//        String uri = "/v1/user/gender";
//
//        JSONObject object = new JSONObject();
//
//        object.put("id", userData.getSelf_user().getUserId());
//        object.put("male", isMale ? 1 : 0);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser -5  ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";Ser -5  ", "OnFailure" + errorResponse);
//
//            }
//        });
//
//
//        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
//            @Override
//            public void idsAvailable(String userId, String registrationId) {
//                try {
//                    syncNotifToken(userData.getSelf_user().getUserId(),
//                            userId);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                MyLog.d(";d;debug", "User:" + userId);
//                if (registrationId != null)
//                    MyLog.d(";d;debug", "registrationId:" + registrationId);
//            }
//        });
//
//    }
//
//    public void syncLocation(String id, double lat, double lng) throws JSONException {
//        String uri = "/v1/user/location";
//        final JSONObject object = new JSONObject();
//        object.put("id", id);
//        lat = Math.toRadians(lat);
//        DecimalFormat format = new DecimalFormat("#.############");
//
//        object.put("latitude",format.format(lat));
//        lng = Math.toRadians(lng);
//        object.put("longitude", format.format(lng));
//
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i("Location Sync", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.e("Location Sync", "OnFailure" + errorResponse);
//            }
//        });
//
//    }
//
//    public void syncNotifToken(String id, String notif_token) throws JSONException {
//        MyLog.d(";d;debug", "User:" + id);
//        MyLog.d(";d;debug", "notif_token:" + notif_token);
//
//        String uri = "/v1/user/notif";
//        JSONObject object = new JSONObject();
//        object.put("id", id);
//        object.put("notif_token", notif_token);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser 4  ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";Ser 4 ", "OnFailure" + errorResponse);
//
//            }
//        });
//
//    }
//
//    public void GetUserInfo(String id, final JsonHandler handler) throws JSONException {
//        String uri = "/v1/user/info/"+id;
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                handler.SuccessHandle(response);
//                MyLog.i(";Ser 5  ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";Ser 5 ", "OnFailure" + errorResponse);
//
//            }
//        });
//    }
//
//    public void SetUserGemCount(String id, int gem) throws JSONException {
//        String uri = "/v1/user/setgem";
//
//        JSONObject object = new JSONObject();
//        object.put("id", id);
//        object.put("gem", gem);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser 6  ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";Ser 6 ", "OnFailure" + errorResponse);
//
//            }
//        });
//    }
//
//    public void SetUserCoinCount(String id, int coin, final JsonHandler jsonHandler) throws JSONException {
//        String uri = "/v1/user/setcoin";
//
//        JSONObject object = new JSONObject();
//        object.put("id", id);
//        object.put("coin", coin);
//
//
//        secureHttpApi.Post(uri, object , new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser 7  ", "OnSuccess" + response);
//
//                jsonHandler.SuccessHandle(response);
//
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";Ser 7 ", "OnFailure" + errorResponse);
//                jsonHandler.FailHandle(errorResponse);
//
//            }
//        });
//    }
//
//    public void CreateLikeRequest(String id, String media_id, long count,
//                                  int price
//            , String image_url ,String caption
//            ,int sex
//            , final JsonHandler jsonHandler) throws JSONException {
//        String uri = "/v3/like/request";
//
//        JSONObject object = new JSONObject();
//            object.put("id", id);
//        object.put("media_id", media_id);
//        object.put("count", count);
//        object.put("price", price);
//        object.put("image_url", image_url);
//        object.put("token", InstagramApi.getInstance().getToken());
//
//        if (caption!=null)
//            object.put("caption", caption);
//        if (sex ==0 | sex==1)
//            object.put("male", sex);
//        MyLog.i(";Ser 81  ", "object" + object);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                jsonHandler.SuccessHandle(response);
//                MyLog.i(";Ser 8  ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";Ser 8 ", "OnFailure" + errorResponse);
//                jsonHandler.FailHandle(errorResponse);
//
//            }
//        });
//    }
//
//    public void GetLikeRequest(String insta_id,String male,boolean containInactives, final String max_id, final JsonHandler jsonHandler) throws JSONException {
//        String uri = "/v3/like/request?instaId="+insta_id+"&male="+male;
//
//        if(containInactives)
//            uri += "&inactives=1";
//
//        if (max_id!= null)
//            uri += "&max_id="+max_id;
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser-9  ", "OnSuccess "+max_id +" "+ response);
//
//                jsonHandler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";Ser-9 ", "OnFailure" + errorResponse);
//                jsonHandler.FailHandle(null);
//
//            }
//        });
//    }
//
//
//    public void GetUserCreatedLR(String id, String max_id , final JsonHandler handler) throws JSONException {
//        String uri = "/v2/like/request/"+id;
//
//        if (max_id!=null)
//            uri += "/?max_id="+max_id;
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";Ser22 ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";Ser22 ", "OnFailure" + errorResponse);
//
//            }
//        });
//    }
//
//    public void GetUserCreatedFR(String id, String max_id , final JsonHandler handler) throws JSONException {
//        String uri = "/v2/follow/request/"+id;
//
//        if (max_id!=null)
//            uri += "?max_id="+max_id;
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser23 ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";Ser23 ", "OnFailure" + errorResponse);
//
//            }
//        });
//    }
//
//    public void Postlike(String id, String media_id, String req_id, final JsonHandler handler) throws JSONException {
//        String uri = "/v1/like/done";
//
//        JSONObject object = new JSONObject();
//        object.put("id", id);
//        object.put("media_id", media_id);
//        object.put("req_id", req_id);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser 10  ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";Ser 10 ", "OnFailure" + errorResponse);
//
//            }
//        });
//    }
//
//    public void GetCreatedLR(String id, String max_id , final JsonHandler handler) throws JSONException {
//        String uri = "/v2/like/request/"+id;
//
//        if (max_id!=null)
//            uri += "?max_id="+max_id;
//
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser 10  ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";Ser 10 ", "OnFailure" + errorResponse);
//
//            }
//        });
//    }
//
//    public void GetLRDone(String id, String max_id, final JsonHandler handler) throws JSONException {
//        String uri = "/v1/like/done/"+id;
//
//        if (max_id!=null)
//            uri += "?max_id="+max_id;
//
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser 10 ---- ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";Ser 10 ----- ", "OnFailure" + errorResponse);
//
//            }
//        });
//    }
//
//    public void CreateFollowRequest(String id,  int count,
//                                  Float price
//            ,String caption
//            ,int sex
//            , final JsonHandler jsonHandler) throws JSONException {
//        String uri = "/v3/follow/request/unique";
//        MyLog.i(";Ser 10 ", "----- price " + price);
//        MyLog.i(";Ser 10 ", "----- count " + count);
//
//        JSONObject object = new JSONObject();
//        //user who received followers
//        object.put("id", id);
//
//        //user who create request
//        object.put("payer_id", UserData.getInstance().getSelf_user().getUserId());
//        object.put("count", count);
//        object.put("price", price);
//        object.put("token", InstagramApi.getInstance().getToken());
//
//        if (caption!=null)
//            object.put("caption", caption);
//        if (sex ==0 | sex==1)
//            object.put("male", sex);
//        MyLog.i(";Ser f81  ", "object" + object);
//
//            secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//                @Override
//                public void OnSuccess(JSONObject response) {
//
//                    jsonHandler.SuccessHandle(response);
//                    MyLog.i(";Ser f8  ", "OnSuccess" + response);
//                }
//
//                @Override
//                public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                    MyLog.i(";Ser f8 ", "OnFailure" + errorResponse);
//                    jsonHandler.FailHandle(errorResponse);
//
//                }
//
//            });
//    }
//
//    public void GetFollowRequest(final Integer max_req_id, final JsonHandler jsonHandler) throws JSONException {
//        String uri = "/v4/follow/request";
//        uri += "?instaId=" + UserData.getInstance().getSelf_user().getUserId();
//
//        if (max_req_id!= null) {
//            uri += "&max_req_id=" + max_req_id;
//        }
//
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//
//                jsonHandler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";Ser f9 ", "OnFailure" + errorResponse);
//                jsonHandler.FailHandle(new JSONObject());
//
//            }
//        });
//    }
//
//
//
//    public void GetAllFollowRequest(final JsonHandler jsonHandler) throws JSONException {
//        String uri = "/v5/follow/request";
//
//        uri += "?instaId=" + UserData.getInstance().getSelf_user().getUserId();
//        uri += "&male=" + UserData.getInstance().getMale();
//
//        MyLog.i(";Serfe0 ", "uri" + uri);
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";Serfe0 ", "OnSuccess" + response);
//                jsonHandler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";Serfe0 ", "OnFailure" + errorResponse);
//                jsonHandler.FailHandle(new JSONObject());
//
//            }
//        });
//    }
//
//    public void GetFollowRequestDetails(String [] req_ids, final JsonHandler jsonHandler) throws JSONException {
//        String uri = "/v5/follow/request/detail";
//
//        final JSONObject object = new JSONObject();
//        JSONArray array = new JSONArray();
//
//        for (String req_id:
//                req_ids) {
//            array.put(req_id);
//            MyLog.i(";GetFollowRequestDetails req_id  ", " " +req_id);
//        }
//
//        object.put("req_ids", array);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Serfe1 ", "OnSuccess" + response);
//                jsonHandler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";Sere1 ", "OnFailure 1 " + errorResponse);
//                MyLog.i(";Sere1 ", "OnFailure 2 " + object);
//                jsonHandler.FailHandle(new JSONObject());
//
//            }
//        });
//    }
//
//
//    public void PostFollow(String id , String req_id, final JsonHandler handler) throws JSONException {
//        String uri = "/v1/follow/done";
//
//        JSONObject object = new JSONObject();
//        object.put("insta_id", id);
//        object.put("req_id", req_id);
//        MyLog.i(";PostFollow", "; " + object);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser f10  ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";Ser f10 ", "OnFailure" + errorResponse);
//
//            }
//        });
//    }
//
//    public void GetFRDone(String id, final JsonHandler handler) throws JSONException {
//        String uri = "/v1/follow/done/"+id;
//
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser f10 ---- ", "OnSuccess " + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";Ser f10 ----- ", "OnFailure " + errorResponse);
//
//            }
//        });
//    }
//
//    public void RefoundFollow(JSONObject object, final JsonHandler handler) throws JSONException {
//        String uri = "/v1/follow/refund";
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i(";Ser ff10 ---- ", "OnSuccess " + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";Ser ff10 ----- ", "OnFailure " + errorResponse);
//
//            }
//        });
//    }
//    public void GetVersion(String market_name,  final JsonHandler handler) throws JSONException {
//        String uri = "/v1/version"+"?market="+market_name;
//
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//
//                MyLog.i("Version ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";Ser f30 ----- ", "OnFailure" + errorResponse);
//
//            }
//
//
//        });
//    }
//
//    public void countOfGemRefunded(final JsonHandler handler){
//        String uri = "/v1/follow/refund/"+userData.getSelf_user().getUserId();
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser65 ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";ser65 ----- ", "OnFailure" + errorResponse);
//            }
//        });
//
//    }
//
//    public void deleteCountOfGemRefunded(final JsonHandler handler) throws JSONException {
//        String uri = "/v1/follow/refund/delete";
//
//        JSONObject object = new JSONObject();
//        object.put("instaId", UserData.getInstance().getSelf_user().getUserId());
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser613 ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";ser613 ----- ", "OnFailure" + errorResponse);
//            }
//        });
//
//    }
//
//    public void getOffPercent(final JsonHandler handler)
//    {
//        String uri = "/v1/off";
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                try {
//                    JSONObject object = response.getJSONObject("data");
//                    String hash = response.getString("hash");
//
//                    String hashCalculated = new String(Hex.encodeHex(DigestUtils.sha1(object.toString() + SecureHttpApi.SECURITY_KEY)));
//
//                    if (hash.equals(hashCalculated))
//                        handler.SuccessHandle(response);
//                    else
//                        handler.FailHandle(response);
//
//                } catch (Exception e) {
//                    MyLog.i("OFF ", "exc: " + e.getMessage());
//                    handler.FailHandle(response);
//                }
//
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//            }
//        });
//
//    }
//
//    public void getLastTimeAward(final JsonHandler handler){
//        String uri = "/v1/dailyaward/"+userData.getSelf_user().getUserId();
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                handler.SuccessHandle(response);
//
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//            }
//        });
//    }
//
//    public void giveDailyAward(boolean Double, final JsonHandler handler) throws JSONException {
//        String uri = "/v2/dailyaward";
//
//        JSONObject object = new JSONObject();
//        object.put("id", UserData.getInstance().getSelf_user().getUserId());
//        object.put("double",Double);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser2727 ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";ser2727 ----- ", "OnFailure" + errorResponse);
//            }
//        });
//    }
//
//    public void deactivateRequest(String req_id, final JsonHandler handler) throws JSONException {
//        String uri = "/v2/like/request/deactivate";
//
//        JSONObject object = new JSONObject();
//        object.put("req_id", req_id);
//
//        MyLog.i("Deactive", "req_id: " + req_id);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser99--- ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";ser99------ ", "OnFailure" + errorResponse);
//            }
//        });
//
//    }
//    public void activateRequest(String req_id, final JsonHandler handler) throws JSONException {
//        String uri = "/v1/like/request/activate";
//
//        JSONObject object = new JSONObject();
//        object.put("req_id", req_id);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser99--- ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";ser99------ ", "OnFailure" + errorResponse);
//            }
//        });
//
//    } public void syncPhoneNumber(String instaId, String phoneNumber) throws JSONException {
//        String uri = "/v1/user/phone";
//
//        JSONObject object = new JSONObject();
//        object.put("instaId", instaId);
//        object.put("phone", phoneNumber);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser9009--- ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";ser9009------ ", "OnFailure" + errorResponse);
//            }
//        });
//
//    }
//
//    public void transfer(String instaId_sender, String instaId_receiver , String type,
//                         int count, final JsonHandler handler) throws JSONException {
//        String uri = "/v2/transfer";
//
//        JSONObject object = new JSONObject();
//        object.put("insta_id", instaId_sender);
//        object.put("type", type);
//        object.put("count", count);
//        object.put("receiver", instaId_receiver);
//        object.put("token", UserData.getInstance().getSelf_user().getToken());
//        //or
////        object.put("token", InstagramApi.getInstance().getToken());
//
//        MyLog.i(";ser77777 ", "transfer object" + object);
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";ser77777 ", "transfer OnFailure" + errorResponse);
//                handler.FailHandle(errorResponse);
//            }
//
//        });
//
//    }
//
//    public void getLikeLeaderBoard(final JsonHandler handler){
//        String uri = "/v1/leaderboard/like";
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser77777 ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";ser7667676 ----- ", "OnFailure" + errorResponse);
//            }
//        });
//
//    }
//
//
//    public void getFollowLeaderBoard(final JsonHandler handler){
//        String uri = "/v1/leaderboard/follow";
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser65423 ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";ser654324 ----- ", "OnFailure" + errorResponse);
//            }
//        });
//
//    }
//
//    public void getUserLikeCount(final JsonHandler handler, String insta_id){
//        String uri = "/v1/leaderboard/like/"+insta_id;
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser65322 ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";ser65322 ----- ", "OnFailure" + errorResponse);
//            }
//        });
//
//    }
//
//    public void getUserFollowCount(final JsonHandler handler, String insta_id){
//        String uri = "/v1/leaderboard/follow/"+insta_id;
//
//        secureHttpApi.Get(uri, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser65342 ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";ser65342 ----- ", "OnFailure" + errorResponse);
//            }
//        });
//
//    }
//
//    public void getSelfUserLikeCount(final JsonHandler handler){
//        getUserLikeCount(handler, userData.getSelf_user().getUserId());
//    }
//
//    public void getSelfUserFollowCount(final JsonHandler handler){
//        getUserFollowCount(handler, userData.getSelf_user().getUserId());
//    }
//
//    public void setUserProfilePic(String insta_id, String pic_url) throws JSONException {
//        String uri = "/v1/user/pic";
//
//        JSONObject object = new JSONObject();
//        object.put("instaId", insta_id);
//        object.put("profile_pic", pic_url);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser099--- ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";ser099------ ", "OnFailure" + errorResponse);
//            }
//        });
//    }
//
//    public void reportLike(String req_id, String insta_id) throws JSONException {
//        String uri = "/v1/like/report";
//
//        JSONObject object = new JSONObject();
//        object.put("req_id", req_id);
//        object.put("insta_id", insta_id);
//        MyLog.i(";ser7458--- ", "req_id" + req_id);
//        MyLog.i(";ser7458--- ", "insta_id" + insta_id);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser7458--- ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";ser7458------ ", "OnFailure" + errorResponse);
//            }
//        });
//    }
//
//    public void reportFollow(String req_id, String insta_id) throws JSONException {
//        String uri = "/v1/follow/report";
//
//        JSONObject object = new JSONObject();
//        object.put("req_id", req_id);
//        object.put("insta_id", insta_id);
//        MyLog.i(";ser7459--- ", "req_id" + req_id);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser7459--- ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";ser7459------ ", "OnFailure" + errorResponse);
//            }
//        });
//    }
//
//    public void addCoin(String insta_id, int count, final JsonHandler handler) throws JSONException {
//        String uri = "/v1/user/inccoin";
//
//        JSONObject object = new JSONObject();
//        object.put("insta_id", insta_id);
//        object.put("count", count);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                MyLog.i(";ser33322--- ", "OnSuccess" + response);
//                handler.SuccessHandle(response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                handler.FailHandle(errorResponse);
//                MyLog.i(";ser33323------ ", "OnFailure" + errorResponse);
//            }
//        });
//    }
//    public void addGem(String insta_id, int count, final JsonHandler handler) throws JSONException {
//        String uri = "/v1/user/incgem";
//
//        JSONObject object = new JSONObject();
//        object.put("insta_id", insta_id);
//        object.put("count", count);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                handler.SuccessHandle(response);
//                MyLog.i(";ser33344--- ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";ser33345------ ", "OnFailure" + errorResponse);
//                handler.FailHandle(errorResponse);
//            }
//        });
//    }
//
//    public void giveFollowUsAward(String insta_id, int coin_count
//            , int gem_count, final JsonHandler handler) throws JSONException {
//        String uri = "/v1/award/follow";
//
//        JSONObject object = new JSONObject();
//        object.put("insta_id", insta_id);
//        object.put("coin_count", coin_count);
//        object.put("gem_count", gem_count);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                handler.SuccessHandle(response);
//                MyLog.i(";ser33344--- ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";ser33345------ ", "OnFailure" + errorResponse);
//                handler.FailHandle(errorResponse);
//            }
//        });
//    }
//
//    public void giveInstallAnalyzerAward(String insta_id, int coin_count
//            , int gem_count, final JsonHandler handler) throws JSONException {
//        String uri = "/v1/award/install";
//
//        JSONObject object = new JSONObject();
//        object.put("insta_id", insta_id);
//        object.put("coin_count", coin_count);
//        object.put("gem_count", gem_count);
//
//        secureHttpApi.Post(uri, object, new SecureHttpApi.ResponseHandler() {
//            @Override
//            public void OnSuccess(JSONObject response) {
//                handler.SuccessHandle(response);
//                MyLog.i(";ser33344--- ", "OnSuccess" + response);
//            }
//
//            @Override
//            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
//                MyLog.i(";ser33345------ ", "OnFailure" + errorResponse);
//                handler.FailHandle(errorResponse);
//            }
//        });
//    }
//}
