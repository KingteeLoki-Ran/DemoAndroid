package edu.zju.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button upload;
    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upload = findViewById(R.id.upload);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        upload.setOnClickListener(new OnUploadClickListener());
    }

    class OnUploadClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("begin post");
                    loginByPost(userName.getText().toString(),password.getText().toString());
                }
            }).start();
        }
    }

    public String loginByPost(String username,String password){
        //数据准备
        String data = "username="+username+"&password="+password;
        String path = "http://192.168.38.199:8888/user/login3";
        path = path+"?"+data;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            //至少要设置的两个请求头
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Content-Length", data.length()+"");
            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            //获得结果码
            int responseCode = connection.getResponseCode();
            if(responseCode ==200){
                //请求成功
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is));
                String resultString = "";
                String line = "";
                while (null != (line = reader.readLine()))
                {
                    resultString += line;
                }
                System.out.println(resultString);
            }else {
                //请求失败
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}