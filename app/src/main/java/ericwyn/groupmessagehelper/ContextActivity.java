package ericwyn.groupmessagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContextActivity extends AppCompatActivity{
    private ListView mListView;
    private SimpleAdapter mAdapter;
    private static boolean[] listOfChooseNum;
    private int haveChooseNum=0;
    private String EXCEL_PATH="";
    private static List<Map<String,Object>> list_data;

    private Button successChoose;
    private ImageView chooseAll;
    private RelativeLayout relativeLayoutChooseAll;
    private Boolean haveChooseAll=false;
    private TextView haveChoose;
    private TextView numOfContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);
        if (getIntent() != null) {
            EXCEL_PATH = getIntent().getStringExtra("PATH");
        }

        mListView = (ListView) findViewById(R.id.listview_contextActivity);
        haveChoose = (TextView) findViewById(R.id.tv_numofContextChoose_contextActivity);
        numOfContext = (TextView) findViewById(R.id.tv_numofContext_contextActivity);
        successChoose = (Button) findViewById(R.id.btn_successsChoose_context);
        chooseAll=(ImageView)findViewById(R.id.cb_chooseAll_contextActivity);
        relativeLayoutChooseAll=(RelativeLayout)findViewById(R.id.chooseAllRelativeLayout_contextActivity);
        successChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getChooseNameList().size()==0){
                    Toast.makeText(ContextActivity.this,"你还未选择任何的联系人",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(ContextActivity.this,SendMessageActivity.class);
                    startActivity(intent);
                }
            }
        });
        relativeLayoutChooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(haveChooseAll){
                    unChooseAll();
                }else {
                    chooseAll();
                }
            }
        });

        list_data = FileUtils.readExcel(this, EXCEL_PATH);
        listOfChooseNum=new boolean[list_data.size()];
        for(int i=0;i<listOfChooseNum.length;i++){
            listOfChooseNum[i]=false;
        }
        mAdapter = new SimpleAdapter(this,
                list_data,
                R.layout.list_item_context,
                new String[]{"Name", "Phone"},
                new int[]{R.id.name_list_item_context, R.id.phone_list_item_context});

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView cb=(ImageView)view.findViewById(R.id.cb_list_item_context);
                if(listOfChooseNum[position]){        //已经选中
                    cb.setImageResource(R.drawable.checkbox_uncheck);
                    listOfChooseNum[position]=false;
                }else {                                         //未选中
                    cb.setImageResource(R.drawable.checkbox_check);
                    listOfChooseNum[position]=true;
                }
                haveChooseNum=0;
                for(int i=0;i<listOfChooseNum.length;i++){
                    if(listOfChooseNum[i]){
                        haveChooseNum++;
                    }
                }
                haveChoose.setText(""+haveChooseNum);
            }
        });
        numOfContext.setText("" + list_data.size());
        haveChoose.setText(""+haveChooseNum);

    }
    private void chooseAll(){
        for(int i=0;i<mListView.getChildCount();i++){
            View view=mListView.getChildAt(i);
            ImageView cb=(ImageView)view.findViewById(R.id.cb_list_item_context);
            cb.setImageResource(R.drawable.checkbox_check);
            listOfChooseNum[i]=true;
        }
        haveChoose.setText(""+listOfChooseNum.length);
        chooseAll.setImageResource(R.drawable.checkbox_check);
        haveChooseAll=true;
    }
    private void unChooseAll(){
        for(int i=0;i<mListView.getChildCount();i++){
            View view=mListView.getChildAt(i);
            ImageView cb=(ImageView)view.findViewById(R.id.cb_list_item_context);
            cb.setImageResource(R.drawable.checkbox_uncheck);
            listOfChooseNum[i]=false;
        }
        haveChoose.setText(""+0);
        chooseAll.setImageResource(R.drawable.checkbox_uncheck);
        haveChooseAll=false;
    }
    public static List<Map<String,Object>> getChooseNameList(){
        List<Map<String,Object>> listToSend=new ArrayList<>();
        for(int i=0;i<listOfChooseNum.length;i++){
            if(listOfChooseNum[i]){
                listToSend.add(list_data.get(i));
            }
        }
        return listToSend;
    }

}
