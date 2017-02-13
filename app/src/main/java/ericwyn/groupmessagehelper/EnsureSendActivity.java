package ericwyn.groupmessagehelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnsureSendActivity extends AppCompatActivity {
    private EditText ensureName;
    private EditText ensureText;
    private TextView moneyGuesst;
    private Button ensureSend;
    private List<Map<String,Object>> list;
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
}
