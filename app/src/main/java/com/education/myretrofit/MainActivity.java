package com.education.myretrofit;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.button_get_no_params)
    Button buttonGetNoParams;
    @InjectView(R.id.button_get_params)
    Button buttonGetParams;
    @InjectView(R.id.button_post)
    Button buttonPost;
    @InjectView(R.id.button_download)
    Button buttonDownload;
    @InjectView(R.id.button_upload)
    Button buttonUpload;
    RetrofitServiceTest serviceTest;

    //    http://apicloud.mob.com/v1/weather/query?province=江苏&key=14b49edf0cfb1&city=南京
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apicloud.mob.com/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        serviceTest = retrofit.create(RetrofitServiceTest.class);
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("必须同意该权限")
                .setPositiveButton("打开所有权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        Intent intent2 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent2.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent2);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @OnClick({R.id.button_get_no_params, R.id.button_get_params, R.id.button_post, R.id.button_download, R.id.button_upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_get_no_params:
                getNoParams();
                break;
            case R.id.button_get_params:
                getWeatherParams();
                break;
            case R.id.button_post:
                getWeatherObj();
                break;
            case R.id.button_download:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Call<ResponseBody> call = serviceTest.download("http://www.mingxingku.com/Data/Images/Articles/2016-03-01/4176145740.jpg");
                        try {
                            Response<ResponseBody> response = call.execute();
                            InputStream inputStream = response.body().byteStream();
                            File file = new File(Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + ".jpg");
                            file.createNewFile();
                            FileOutputStream outputStream = new FileOutputStream(file);
                            byte[] array = new byte[1024];
                            int index = inputStream.read(array);
                            while (index != -1) {
                                outputStream.write(array, 0, index);
                                index = inputStream.read(array);
                            }
                            Log.d("-------", "---------down sucess");
                            outputStream.flush();
                            outputStream.close();
                            inputStream.close();
                        } catch (IOException e) {
                            Log.d("-------", "---------down fail");
                            e.printStackTrace();
                        }
                    }
                }).start();

//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        InputStream inputStream = response.body().byteStream();
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                    }
//                });
                break;
            case R.id.button_upload:
                break;
        }
    }

    private void getWeatherObj() {
        Call<Weather> call = serviceTest.getWeatherJson();
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Log.d("---------", "---------" + response.body().getMsg());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

            }
        });
    }

    private void getWeatherParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("province", "江苏");
        map.put("key", "14b49edf0cfb1");
        map.put("city", "南京");
        Call<ResponseBody> call = serviceTest.getWeatherParams("v1/weather/query", map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("---------", "-------sucess" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("-----------", "---------失败");
            }
        });
    }

    private void getNoParams() {
        Call<ResponseBody> call = serviceTest.getWeatherString();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("---------", "-------sucess" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("-----------", "---------失败");
            }
        });
    }
}
