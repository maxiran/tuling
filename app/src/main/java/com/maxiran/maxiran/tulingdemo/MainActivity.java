package com.maxiran.maxiran.tulingdemo;
import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements HttpGetDataListener,View.OnClickListener{
    private HttpData httpData;
    private List<ListData> lists;
    private ListView lv;
    private EditText sendText;
    private Button send_btn;
    private String content_str;
    private TextAdapter adapter;
    private String[] welcome_arry;
    private double currentTime,oldTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }
   private void initView(){
       lists = new ArrayList<ListData>();
       sendText = (EditText) findViewById(R.id.senText);
       send_btn = (Button) findViewById(R.id.send_btn);
       lv = (ListView) findViewById(R.id.lv );
       send_btn.setOnClickListener(this);
       adapter = new TextAdapter(lists,this);
       lv.setAdapter(adapter);
       ListData listData;
       listData = new ListData(getRandomWelcomeTips(),ListData.RECEIVER,getTime());
       lists.add(listData);

   }
    private String getRandomWelcomeTips(){
         String welcome_tip = null;
          welcome_arry = this.getResources().getStringArray(R.array.welcome);
          int index = (int) (Math.random()*(welcome_arry.length-1));
          welcome_tip = welcome_arry[index];
          return welcome_tip;
    }
    @Override
    public void getDataUrl(String data) {
          Log.d("MainActivity",data);
          parseText(data);

    }

    public void parseText(String str){
        try {
            JSONObject jb = new JSONObject(str);

            ListData listData;
            listData = new ListData(jb.getString("text"),ListData.RECEIVER,getTime());
            lists.add(listData);
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        getTime();
        content_str = sendText.getText().toString();
        String dropK = content_str.replace(" ","");
        String dropH = dropK.replace("\n","");
        ListData listData;
        listData = new ListData(content_str,ListData.SEND,getTime());
        lists.add(listData);
        sendText.setText("");
        if (lists.size()>20){
            for (int i = 0;i < lists.size();i++){
                lists.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        httpData =(HttpData) new HttpData("http://www.tuling123.com/openapi/api?key=aeb60f1b8fc09d6c82175f5c19fe4bd1&info="+dropH,this).execute();
    }

    public String  getTime(){
        currentTime = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        if (currentTime - oldTime > 5*60*1000){
            oldTime = currentTime;
            return str;
        }else {
            return "";
        }
    }
}
