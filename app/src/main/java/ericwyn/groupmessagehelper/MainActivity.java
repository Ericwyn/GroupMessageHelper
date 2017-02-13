package ericwyn.groupmessagehelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button choose_btn;
    private TextView makerName;
    private TextView issues;
    public final int FILE_SELECT_CODE=10086;
    public String EXCEL_PATH="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choose_btn=(Button)findViewById(R.id.btn_main_choose);
        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });
//        makerName=(TextView)findViewById(R.id.tv_makerName_main);
        issues=(TextView)findViewById(R.id.tv_issues_main);

        issues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/Ericwyn/GroupMessageHelper/issues");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

    }
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult( Intent.createChooser(intent, "选择一个文件管理器"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "请安装一个文件管理器",  Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    EXCEL_PATH = FileUtils.getPath(this, uri);
                    if(EXCEL_PATH.matches("^.*?\\.(xls)$")){
                        Toast.makeText(MainActivity.this,"通讯录读取成功",Toast.LENGTH_SHORT).show();
                        Bundle bundle=new Bundle();
                        bundle.putString("PATH",EXCEL_PATH);
                        Intent intent=new Intent(MainActivity.this,ContextActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else if(EXCEL_PATH.matches("^.*?\\.(xlsx)$")){
                        Toast.makeText(MainActivity.this,"请使用2003及以下版本的Excel表格",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this,"文件格式错误",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}



