package ericwyn.groupmessagehelper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnsureSendActivity extends AppCompatActivity {
    private EditText ensureName;
    private EditText ensureText;
    private TextView moneyGuesst;
    private Button ensureSend;
    private List<Map<String,Object>> list;
    private final int REQUEST_CODE_ASK_CALL_SENSMS=10086;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensure_send);

        list=ContextActivity.getChooseNameList();

        ensureName=(EditText)findViewById(R.id.et_ensureName);
        ensureText=(EditText)findViewById(R.id.et_ensureText);
        moneyGuesst=(TextView)findViewById(R.id.tv_sendMoney);
        ensureSend=(Button)findViewById(R.id.ensureSend);
        if(getIntent()!=null){
            String ensure_text=getIntent().getStringExtra("text");
            ensureText.setText((String)list.get(0).get("Name")+"同学,"+ensure_text);
        }
        String ensure_name="";
        for(int i=0;i<list.size();i++){
            HashMap map=(HashMap) list.get(i);
            String name1=(String )map.get("Name");
            ensure_name=ensure_name+name1+",";
            if(i%5==0 && i!=0){
                ensure_name=ensure_name+"\n";
            }
        }
        ensureName.setText(ensure_name);
        int msmNum=(calculateLength(ensureText.getText().toString())/70)+1;
        double money=msmNum*0.1*list.size();

        moneyGuesst.setText("PS:本次群发预计花费"+money+"元");
        ensureSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ensureSend();
                Toast.makeText(EnsureSendActivity.this,"短信群发助手群发操作完毕",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClassName("com.android.mms","com.android.mms.ui.ConversationList");
                startActivity(intent);
                finish();
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

    /**
     * 调用系统发短信的接口
     * @param phoneNumber 电话号码
     * @param message   短信内容
     */
    private void sendSMS(String phoneNumber, String message) {
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        }
    }

    private void ensureSend(){
        if(Build.VERSION.SDK_INT >= 23){
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},REQUEST_CODE_ASK_CALL_SENSMS);
                return;
            }else{
                //上面已经写好的拨号方法
                sendSms();
            }
        }else {
            sendSms();
        }

    }

    private void sendSms(){
        for(Map map:list){
            String phone=(String)map.get("Phone");
            String name=(String)map.get("Name");
            String ensure_text=getIntent().getStringExtra("text");
            String text=name+"同学,"+ensure_text;
            sendSMS(phone,text);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_SENSMS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    sendSms();
                } else {
                    // Permission Denied
                    Toast.makeText(EnsureSendActivity.this, "无法权限获取，短信发送失败", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
