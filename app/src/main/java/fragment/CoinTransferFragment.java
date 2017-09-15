package fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//import ServerAPI.ServerApi;
import butterknife.ButterKnife;
import data.UserData;
import instaAPI.InstagramApi;
import afm.niafara.instagram.R;
import utility.MyLog;
import utility.Utility;

public class CoinTransferFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final int THIS_TAB_NUMBER = 4;
    UserData userData = UserData.getInstance();


    private static String TAG = "SearchFragment";
    private int fragment_layout;



    final String TYPE_GEM = "gem";
    final String TYPE_COIN = "coin";
    String type = TYPE_COIN ;

//    ServerApi serverApi = ServerApi.getInstance();
    //--------------------------- components

    LinearLayout parent ;
    EditText et_username;
    EditText et_coin_count;
    TextView tv_rules;
    Button btn_transfer;
    TextView tv_count;
    private Spinner sp_coin_type;

    //---------------------------

    private Activity activity;
    private Context context;
    private InstagramApi api = InstagramApi.getInstance();

    public static final String MEDIA_DATA_BUNDLE_COIN_TRANSFER_LAYOUT_TAG =
            "MEDIA_DATA_BUNDLE_COIN_TRANSFER_LAYOUT_TAG";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.coin_transfer_frag_layout, container, false);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        activity = getActivity();
        context = activity.getApplicationContext();
//        serverApi = ServerApi.getInstance();
        ButterKnife.inject(activity);

       MyLog.i(TAG, "starting onCreateView 22");

        //---------------------------

        parent = (LinearLayout) rootView.findViewById(R.id.cl_main);
        et_username = (EditText) rootView.findViewById(R.id.et_username);
        et_coin_count = (EditText) rootView.findViewById(R.id.et_coin_count);
        tv_rules = (TextView) rootView.findViewById(R.id.tv_rules);
        btn_transfer = (Button) rootView.findViewById(R.id.btn_transfer);
        sp_coin_type = (Spinner) rootView.findViewById(R.id.sp_coin_type);
        tv_count = (TextView) rootView.findViewById(R.id.tv_coin_count);

        Utility.changeNotificationColor(getActivity());

        btn_transfer.setOnClickListener(this);
        tv_rules.setOnClickListener(this);

        addItemsOnSpinner();
        sp_coin_type.setOnItemSelectedListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void addItemsOnSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("سکه لایک");
        list.add("سکه فالـُـور ");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_coin_type.setAdapter(dataAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_rules:
                displayRules();
                break;
            case R.id.btn_transfer:
                if (inputIsValid())//{
                    displayTransferAlert();
//                }else
//                    toastEnterValue();
                break;
        }
    }

    private void displayTransferAlert() {
        int coin_count  = Integer.parseInt(et_coin_count.getText().toString());
        String username = et_username.getText().toString();

        String  content = "آیا از انتقال";
        content += coin_count + " ";
        if (type.equals(TYPE_COIN))
            content += "سکه ";
//        else
//            content += "الماس ";

        content += "به ";
        content += username + " ";
        content += "مطمئنید؟ ";

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setMessage(Utility.toPersianNumber(content));
        mBuilder.setCancelable(true);
        mBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                transfer();
            }
        });
        mBuilder.setNegativeButton(R.string.bikhial, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mBuilder.show();
    }

    private void transfer() {
        int coin_count  = Integer.parseInt(et_coin_count.getText().toString());
        String username = et_username.getText().toString();
//        try {
//            username = URLEncoder.encode(username, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        displayProgressDialog();


      /*  try {
            serverApi.transfer(UserData.getInstance().getSelf_user().getUserId(),
                    username, type, coin_count, new ServerApi.JsonHandler() {
                        @Override
                        public void SuccessHandle(JSONObject response) {
                            cancelProgressDialog();
                            displayAlert(R.string.transfer_success);
                            fetchUserInfo();
                            Utility.closeKeyboard(getActivity());
                        }

                        @Override
                        public void FailHandle(JSONObject errorResponse) {
                            cancelProgressDialog();
                            if (errorResponse!=null && errorResponse.has("message")){

                                try {
                                    if (errorResponse.getString("message").equals("cant transfer more than twice a day")){
                                        twiceAday();
                                    }else if  (errorResponse.getString("message").equals("cant transfer more than 1000")){
                                        morThan1000();
                                    }else if (errorResponse.getString("message").equals("receiver does not exists")){
                                        receiverDoesNotExists();
                                    }else if (errorResponse.getString("message").equals("not enough budget")){
                                        notEnoughBudget();
                                    }else
                                        error();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showToast();
                                }

                            }else
                                showToast();

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    private boolean inputIsValid() {
        if (et_username.getText()!=null && et_username.getText().length() >0
                && et_coin_count.getText()!=null && et_coin_count.getText().length() >0){
            String str_coin_count  = et_coin_count.getText().toString();
            try {
                int coin_count = Integer.valueOf(str_coin_count);
                if (coin_count > 1000) {
                    morThan1000();
                    return false;
                } else if (coin_count <= 0) {
                    lessThan0();
                    return false;
                } else
                    return true;
            }catch (Exception e){
                e.printStackTrace();
                toastEnterValue();
                return false;
            }
        }else {
            toastEnterValue();
            return false;
        }

    }

    private void lessThan0() {
        displayAlert(R.string.less_than);
    }

    private void displayRules() {
        displayAlert(R.string.rules_des);
    }
    public void notEnoughBudget() {
        displayAlert(R.string.not_enough_budget);
    }
    public void receiverDoesNotExists() {
        displayAlert(R.string.not_exist);
    }
    public void morThan1000() {
        displayAlert(R.string.more_than);
    }
    public void twiceAday() {
        displayAlert(R.string.twice_a_day);
    }

    private void error() {
        displayAlert(R.string.transfer_error);
    }

    private void displayAlert(int res) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setMessage(Utility.toPersianNumber(getString(res)));
        mBuilder.setCancelable(true);
        mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mBuilder.show();
    }

    public void toastEnterValue(){
        if (UserData.currentTab == THIS_TAB_NUMBER)
            Toast.makeText(getActivity().getApplicationContext(), R.string.enter_values, Toast.LENGTH_LONG).show();
    }


    ProgressDialog dialog;
    private void displayProgressDialog() {
        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.PLEASE_WAIT));
        dialog.show();
    }

    private void cancelProgressDialog() {
        if (dialog!=null && dialog.isShowing()){
            try{
                dialog.cancel();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            type = TYPE_COIN;
            tv_count.setText(R.string.coins_count);
        }
        else {
            type = TYPE_GEM;
            tv_count.setText(R.string.gem_count);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //--------------------------------------------------------------------
    Bundle savedState;
    final String USER_PROFILE_DATA_BUNDLE__LEADER_BOARD_LAYOUT_TAG =
            "USER_PROFILE_DATA_BUNDLE__LEADER_BOARD_LAYOUT_TAG";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Restore State Here
        boolean restored = false;
        try {
            restored = restoreStateFromArguments();
        }catch (Exception e){
            e.printStackTrace();
            restored = false;
        }
        if (!restored) {
            try {
                Bundle b = getArguments();
//          ArrayList <InstagramMedia> medias= (ArrayList<InstagramMedia>) b.getSerializable(USER_PROFILE_DATA_BUNDLE_POPULAR_MEDIAS_TAG);
                fragment_layout = getArguments().getInt(MEDIA_DATA_BUNDLE_COIN_TRANSFER_LAYOUT_TAG, R.id.my_leader_board_root_frame);
            } catch (Exception e) {
                e.printStackTrace();
               MyLog.i(TAG, "2 userId error 2 " + e.getMessage());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Save State Here
        saveStateToArguments();
    }

    private static final String SAVED_BUNDLE_COIN_TRANSFER_TAG =
            "SAVED_BUNDLE_COIN_TRANSFER_TAG";

    private static final String SAVED_BUNDLE_COIN_TRANSFER_LAYOUT_TAG =
            "SAVED_BUNDLE_COIN_TRANSFER_LAYOUT_TAG";

    private void saveStateToArguments() {
       MyLog.i(TAG, "-savedState 00 " + savedState);
        savedState = saveState();
       MyLog.i(TAG, "-savedState 01" + savedState);
        if (savedState != null) {
//            Bundle b /*= getArguments()*/;
//            b= new Bundle();
            try {
                Bundle b = getArguments();
                b.putBundle(SAVED_BUNDLE_COIN_TRANSFER_TAG, savedState);
               MyLog.i(TAG, "-savedState 10" + savedState);

            } catch (Exception e) {
                e.printStackTrace();
               MyLog.i(TAG, "-savedState 11" + savedState);
            }

        }
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
       MyLog.i(TAG, "Bundle_b  " + b);
        try {
            savedState = b.getBundle(SAVED_BUNDLE_COIN_TRANSFER_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

/////////////////////////////////
// Restore Instance State Here
/////////////////////////////////

    private void restoreState() {
        if (savedState != null) {
            // For Example
            //tv1.setText(savedState.getString(as text));
            fragment_layout = savedState.getInt(SAVED_BUNDLE_COIN_TRANSFER_LAYOUT_TAG,
                    R.id.my_popular_root_frame);

        }
    }

    //////////////////////////////
// Save Instance State Here
//////////////////////////////
    private Bundle saveState() {

        Bundle state = new Bundle();

//        state.putParcelableArrayList(SAVED_BUNDLE_LEADER_BOARD_USER_FOLLOW_TAG, users_follow);
        state.putInt(SAVED_BUNDLE_COIN_TRANSFER_LAYOUT_TAG, fragment_layout);
        return state;
    }

    //-------------------------------------------------------
    private static final Field sChildFragmentManagerField;

    static {
        Field f = null;
        try {
            f = Fragment.class.getDeclaredField("mChildFragmentManager");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
           MyLog.e("LOGTAG", "Error getting mChildFragmentManager field", e);
        }
        sChildFragmentManagerField = f;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (sChildFragmentManagerField != null) {
            try {
                sChildFragmentManagerField.set(this, null);
            } catch (Exception e) {
               MyLog.e("LOGTAG", "Error setting mChildFragmentManager field", e);
            }
        }
    }
    //---------------------------------------------

    public void showToast() {
        if (UserData.currentTab == THIS_TAB_NUMBER)
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.CONNECTION_ERROR), Toast.LENGTH_LONG).show();

    }

    public void showToast(String text) {
        if (UserData.currentTab == THIS_TAB_NUMBER)
            Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_LONG).show();

    }

    private void fetchUserInfo() {
//        ServerApi serverApi = ServerApi.getInstance();
        userData = UserData.getInstance();
       /* try {
            serverApi.GetUserInfo(userData.getSelf_user().getUserId(), new ServerApi.JsonHandler() {
                @Override
                public void SuccessHandle(JSONObject response) {

                    try {
                        JSONObject userObject = response.getJSONObject("user");
                        int gem = userObject.getInt("gem");
                        int coin = userObject.getInt("coin");

                        userData.setUser_coin(coin);
                        userData.setUser_gem(gem);

                    } catch (JSONException e) {
                        showToast();
                        e.printStackTrace();
                    }
                }

                @Override
                public void FailHandle(JSONObject response) {
                    showToast();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
}
