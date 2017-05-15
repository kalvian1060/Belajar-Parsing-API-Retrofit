package com.example.win.api;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.example.win.api.API.ApiService;
import com.example.win.api.Model.ModelData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainEdit extends AppCompatActivity {

String ID_MAHASISWA;
    EditText et_id,et_nama,et_kelas;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_detail);

        ID_MAHASISWA = getIntent().getStringExtra(ModelData.id_mahasiswa);

        et_id = (EditText) findViewById(R.id.tampil_id);
        et_nama = (EditText) findViewById(R.id.tampil_nama);
        et_kelas = (EditText) findViewById(R.id.tampil_kelas);

        bindData();

    }

    public void bindData(){
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MainActivity.ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<List<ModelData>> call= service.getSingleData(ID_MAHASISWA);
        call.enqueue(new Callback<List<ModelData>>() {


            @Override
            public void onResponse(Call<List<ModelData>> call, final Response<List<ModelData>> response) {
                if (response.isSuccessful()) {
                    try{

                        int jumlah = response.body().size();

                        for (int i = 0; i < jumlah; i++) {
                            final int finalI = i;
                            //menggunakan run on Ui Thread karena et berada di UI sedangkan proses download di background
                            //jadi harus buat thread yang berbeda
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    et_id.setText(response.body().get(finalI).getidMahasiswa());
                                    et_nama.setText(response.body().get(finalI).getNama());
                                    et_kelas.setText(response.body().get(finalI).getKelas_mhs());
                                }
                            });
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    }
            }

            @Override
            public void onFailure(Call<List<ModelData>> call, Throwable t) {


            }

        });
    }

}
