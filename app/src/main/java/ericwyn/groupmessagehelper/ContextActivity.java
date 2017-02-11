package ericwyn.groupmessagehelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContextActivity extends AppCompatActivity {
    private ListView mListView;
    private SimpleAdapter mAdapter;
    private String EXCEL_PATH="";
    private List<Map<String,Object>> list_data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);

        if(getIntent()!=null){
            EXCEL_PATH=getIntent().getStringExtra("PATH");
        }

        mListView=(ListView)findViewById(R.id.listview_context);
        list_data=FileUtils.readExcel(this,EXCEL_PATH);
        mAdapter=new SimpleAdapter(this,
                list_data,
                R.layout.list_item_context,
                new String[]{"Name","Phone"},
                new int[]{R.id.name_list_item_context,R.id.phone_list_item_context});
        mListView.setAdapter(mAdapter);
    }

}
