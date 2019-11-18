package com.example.androidpl4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Button call;
    private TextView out;
    private EditText takeName;
    private final String BASE_URL = "https://api.github.com" ;
    private Retrofit client;
    private GithubAPIInerface api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
       api = createRetrofit();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResponse();
            }
        });
    }

    public GithubAPIInerface createRetrofit(){
        client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return client.create(GithubAPIInerface.class);
    }

    public void initViews(){
        call = findViewById(R.id.button);
        out = findViewById(R.id.textView);
        takeName = findViewById(R.id.editText);
        out.setMovementMethod(new ScrollingMovementMethod());

    }

    private void dowloadGitUser(Call<List<GitReqestModel>> call) throws IOException {
        call.enqueue(new Callback<List<GitReqestModel>>() {
            @Override
            public void onResponse(Call<List<GitReqestModel>> call, Response<List<GitReqestModel>> response) {
                if (response.isSuccessful()) {
                    GitReqestModel curRetrofitModel = null;
                    for (int i = 0; i < response.body().size(); i++) {
                        curRetrofitModel = response.body().get(i);
                        out.append("\nName = " + curRetrofitModel.getName() +
                                "\nURL " + curRetrofitModel.getHtmlUrl() +
                                "\n-----------------");
                    }
                } else if (response.code()==404){out.setText("Данного пользователя не существует");}

                else {
                    System.out.println("onResponse error: " + response.code());
                    out.setText("onResponse error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<GitReqestModel>> call, Throwable t) {
                System.out.println("onFailure " + t);
                out.setText("onFailure " + t.getMessage());
            }
        });

    }

    public void getResponse(){
        out.setText("");
        Call<List<GitReqestModel>> call = api.getUser(takeName.getText().toString());
        if (isNetworkSuccess()) {
            try {
                dowloadGitUser(call);
            } catch (IOException e) {
                e.printStackTrace();
                out.setText(e.getMessage());
            }
        } else {
            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean isNetworkSuccess() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isConnected();
    }
}
