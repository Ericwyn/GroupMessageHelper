package ericwyn.groupmessagehelper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

/**
 * 文件操作的工具类
 * Created by Ericwyn on 2017/2/11.
 */

public class FileUtils {
    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * 这个类
     * @param context 上下文
     * @param path 通讯录文件路径
     * @return 返回一个List<Map>，其中name标识姓名，Num标识电话号码
     */
    public static List<Map<String ,Object>> readExcel(Context context, String path){
//        path="/storage/emulated/0/test.xls";
        List<Map<String ,Object>> data=new ArrayList<>();
        try {
            InputStream is = new FileInputStream(path);
            Workbook book = Workbook.getWorkbook(is);
            int num = book.getNumberOfSheets();
//            txt.setText("the num of sheets is " + num+ "\n");
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();         //行
            int Cols = sheet.getColumns();      //列
            int colsOfName=0;
            int rolsOfName=0;
            int colsOfNum=0;
            int rolsOfNum=0;
            Log.i("File操作日志","查找姓名属性");
            for (int i = 0; i < Cols; ++i) {
                for (int j = 0; j < Rows; ++j) {
                    // getCell(Col"列",Row"行")获得单元格的值
                    if(sheet.getCell(i,j).getContents().equals("姓名")
                            ||sheet.getCell(i,j).getContents().equals("名字")
                            ||sheet.getCell(i,j).getContents().equals("名称")){
                        colsOfName=i;
                        rolsOfName=j;
                        Log.i("File操作日志","已经找到姓名属性");
                        break;
                    }
                }
            }
            Log.i("File操作日志","查找电话属性");
            for (int i = 0; i < Cols; ++i) {
                for (int j = 0; j < Rows; ++j) {
                    // getCell(Col,Row)获得单元格的值
                    if(sheet.getCell(i,j).getContents().equals("电话")
                            ||sheet.getCell(i,j).getContents().equals("联系电话")
                            ||sheet.getCell(i,j).getContents().equals("电话号码")
                            ||sheet.getCell(i,j).getContents().equals("手机")
                            ||sheet.getCell(i,j).getContents().equals("手机号码")){
                        colsOfNum=i;
                        rolsOfNum=j;
                        Log.i("File操作日志","已经找到电话属性");
                        break;
                    }
                }
            }
            for(int i1=rolsOfName+1,i2=rolsOfNum+1;i1<(Rows-rolsOfName)+1;i1++,i2++){
                if(!sheet.getCell(colsOfName,i1).getContents().equals("") && !sheet.getCell(colsOfNum,i2).getContents().equals("")){
                    Map<String,Object> map=new HashMap<>();
                    map.put("Name",sheet.getCell(colsOfName,i1).getContents());
                    map.put("Phone",sheet.getCell(colsOfNum,i2).getContents());
                    map.put("haveChoose","1");
                    Log.i("File操作日志",sheet.getCell(colsOfName,i1).getContents()+sheet.getCell(colsOfNum,i2).getContents());
                    data.add(map);
                }

            }
            book.close();
        } catch (Exception e) {
//            Toast.makeText(context,"通讯录读取错误",Toast.LENGTH_SHORT).show();
        }
        return data;
    }
}