package com.maxiran.maxiran.tulingdemo;
import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class HttpData extends AsyncTask<String,Void,String> {
    private HttpClient mHttpClient;
    private HttpGet mHtttpGet;
    private String url;
    private HttpResponse mHttpResponse;
    private HttpEntity mHttpEntity;
    private InputStream in;
    private HttpGetDataListener listener;
    public HttpData(String url,HttpGetDataListener listener){
         this.url=url;
         this.listener=listener;
    }
    @Override
    protected String doInBackground(String... params) {
        try{
            mHttpClient = new DefaultHttpClient();
            mHtttpGet = new HttpGet(url);
            mHttpResponse=mHttpClient.execute(mHtttpGet);
            mHttpEntity = mHttpResponse.getEntity();
            in = mHttpEntity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine())!=null){
                sb.append(line);
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.getDataUrl(s);
        super.onPostExecute(s);
    }
}
