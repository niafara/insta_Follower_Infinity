package activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import data.UserData;
import afm.niafara.instagram.R;
import utility.Utility;

/**
 * Created by pc on 7/8/2016.
 */
public class CoinTransfer extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    LinearLayout parent ;
    EditText et_username;
    EditText et_coin_count;
    TextView tv_rules;
    Button btn_transfer;
    TextView tv_count;
    ImageView iv_back;
    private Spinner sp_coin_type;

    final String TYPE_GEM = "gem";
    final String TYPE_COIN = "coin";
    String type = TYPE_COIN ;

//    ServerApi serverApi = ServerApi.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.coin_transfer_layout);

        parent = (LinearLayout) findViewById(R.id.cl_main);
        et_username = (EditText) findViewById(R.id.et_username);
        et_coin_count = (EditText) findViewById(R.id.et_coin_count);
        tv_rules = (TextView) findViewById(R.id.tv_rules);
        btn_transfer = (Button) findViewById(R.id.btn_transfer);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        sp_coin_type = (Spinner) findViewById(R.id.sp_coin_type);
        tv_count = (TextView) findViewById(R.id.tv_coin_count);

        Utility.overrideFont(this, parent);
        Utility.changeNotificationColor(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_transfer.setOnClickListener(this);
        tv_rules.setOnClickListener(this);

        addItemsOnSpinner();
        sp_coin_type.setOnItemSelectedListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void addItemsOnSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("سکه لایک");
        list.add("سکه فالـُـور ");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
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
//        if (type.equals(TYPE_COIN))
            content += "سکه ";
//        else
//            content += "الماس ";

        content += "به ";
        content += username + " ";
        content += "مطمئنید؟ ";

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
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


       /* try {
            serverApi.transfer(UserData.getInstance().getSelf_user().getUserId(),
                    username, type, coin_count, new ServerApi.JsonHandler() {
                        @Override
                        public void SuccessHandle(JSONObject response) {
                            cancelProgressDialog();
                            displayAlert(R.string.transfer_success);
                            Utility.closeKeyboard(CoinTransfer.this);
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
                                    Toast.makeText(CoinTransfer.this,
                                            R.string.CONNECTION_ERROR , Toast.LENGTH_SHORT).show();
                                }

                            }else
                                Toast.makeText(CoinTransfer.this,
                                        R.string.CONNECTION_ERROR , Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
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
        Toast.makeText(this, R.string.enter_values, Toast.LENGTH_LONG).show();
    }


    ProgressDialog dialog;
    private void displayProgressDialog() {
        dialog = new ProgressDialog(this);
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
}
