package com.example.aufa18.praktikumdelapan;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    ListView lvdata;
    ArrayList<HashMap<String, String>> datalist;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = "http://localhost/tes/getdata.php";
        lvdata = (ListView) findViewById(R.id.lvdata);
        datalist = new ArrayList<>();

        new GetData().execute();
    }

    private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Mohon tunggu");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String JsonStr = sh.makeServiceCall(url);
            Log.e("Condition", "Respone From Url: " + JsonStr);

            if (JsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(JsonStr);
                    JSONArray dataPool = jsonObject.getJSONArray("sample");
                    for (int i = 0; i < dataPool.length(); i++) {
                        JSONObject d = dataPool.getJSONObject(i);
                        String nama = d.getString("nama");
                        String nim = d.getString("nim");
                        String jurusan = d.getString("jurusan");
                        HashMap<String, String> sampleData = new HashMap<>();

                        sampleData.put("nama", nama);
                        sampleData.put("nim", nim);
                        sampleData.put("jurusan", jurusan);
                        datalist.add(sampleData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("Information: ", "\nCouldn't get json from server");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing())
                pDialog.dismiss();
                ListAdapter adapter = new SimpleAdapter(MainActivity.this, datalist, R.layout.getdatapull,
                        new String[]{"nama", "nim", "jurusan"}, new int[]{R.id.name, R.id.nim, R.id.jurusan});
                lvdata.setAdapter(adapter);
        }
    }
}
