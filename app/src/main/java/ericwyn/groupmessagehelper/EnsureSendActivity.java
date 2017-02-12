package ericwyn.groupmessagehelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        if(getIntent()!=null){
            String ensure_text=getIntent().getStringExtra("text");
            ensureText.setText((String)list.get(0).get("Name")+"同学,"+ensure_text);
        }
        String ensure_name="";
        for(int i=0;i<list.size();i++){
            ensure_name=ensureName+(String)list.get(i).get("Name");
        }
        ensureName.setText(ensure_name);
        double money=getIntent().getIntExtra("money",0);
        money=(money/70)*0.1;

        moneyGuesst.setText("PS:本次群发预计花费"+money+"元");
        ensureSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
