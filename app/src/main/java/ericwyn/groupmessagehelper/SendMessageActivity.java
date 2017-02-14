package ericwyn.groupmessagehelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendMessageActivity extends AppCompatActivity {;
    private List<Map<String,Object>> list;
    private TextView haveChooseNum;
    private Button btn_chanceSendName;
    private Button btn_sendMessage;
    private EditText mEditText;
    private TextView tv_textNumHaveInput;
    private TextView tv_messageMax;
    private TextView tv_nameShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        list=ContextActivity.getChooseNameList();

        tv_textNumHaveInput=(TextView)findViewById(R.id.tv_textNumHaveInput_sendMActivity);
        tv_messageMax=(TextView)findViewById(R.id.tv_textNumHini_sendMActivity);
        tv_nameShow=(TextView)findViewById(R.id.tv_nameChooseShow_sendMessageActivity);
        tv_nameShow.setText((String)((HashMap)list.get(0)).get("Name")+"同学,");

        haveChooseNum=(TextView)findViewById(R.id.haveChooseNum_sendMessageActivity);
        haveChooseNum.setText("已选择了"+list.size()+"个联系人");
        btn_chanceSendName=(Button)findViewById(R.id.btn_chanceSendName_sendMessageActivity);
        btn_chanceSendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEditText=(EditText)findViewById(R.id.et_messageText_sendMessageActivity);
        btn_sendMessage=(Button)findViewById(R.id.btn_sendMessage_sendMessageActivity);
        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        if(!sp.getString("saveMessageText","000").equals("000")){
            mEditText.setText(sp.getString("saveMessageText",""));
        }
        tv_textNumHaveInput.setText(""+calculateLength(mEditText.getText().toString()));
        tv_messageMax.setText("/"+((calculateLength(mEditText.getText().toString())/70)+1)*70);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_textNumHaveInput.setText(""+calculateLength(mEditText.getText().toString())+"/");
                if((calculateLength(mEditText.getText().toString())%70)==0){
                    tv_messageMax.setText(""+(calculateLength(mEditText.getText().toString())/70)*70);
                }else {
                    tv_messageMax.setText(""+((calculateLength(mEditText.getText().toString())/70)+1)*70);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private int calculateLength(String etstring) {
        etstring=etstring+"陈潇桐同学,";
        char[] ch = etstring.toCharArray();

        int varlength = 0;
        for (int i = 0; i < ch.length; i++) {
            // changed by zyf 0825 , bug 6918，加入中文标点范围 ， TODO 标点范围有待具体化
            if ((ch[i] >= 0x2E80 && ch[i] <= 0xFE4F) || (ch[i] >= 0xA13F && ch[i] <= 0xAA40) || ch[i] >= 0x80) { // 中文字符范围0x4e00 0x9fbb
                varlength = varlength + 2;
            } else {
                varlength++;
            }
        }
        Log.d("TextChanged", "varlength = " + varlength);
        // 这里也可以使用getBytes,更准确嘛
        // varlength = etstring.getBytes(CharSet.forName("GBK")).lenght;// 编码根据自己的需求，注意u8中文占3个字节...
        return varlength;
    }

    private void sendMessage(){
        Bundle bundle=new Bundle();
        bundle.putString("text",mEditText.getText().toString());
        Intent intent=new Intent(SendMessageActivity.this,EnsureSendActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("saveMessageText",mEditText.getText().toString());
        editor.apply();
    }

}
