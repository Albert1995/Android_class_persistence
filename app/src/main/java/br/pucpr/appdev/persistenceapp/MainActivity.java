package br.pucpr.appdev.persistenceapp;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private TextView txtInfo;
    private EditText txtName, txtCity;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInfo = findViewById(R.id.txtInfo);
        txtName = findViewById(R.id.txtName);
        txtCity = findViewById(R.id.txtCity);

        SharedPreferences preferences = getSharedPreferences("preferencias", MODE_PRIVATE);
        count = preferences.getInt("count", 0);
        txtInfo.setText(getString(R.string.txt_info_text) + ++count);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("count", count);
        editor.commit();

        loadData();
    }

    public void loadData() {
        if (loadDataFromInternalStorage()) {
            Toast.makeText(this, "Dados internos carregados", Toast.LENGTH_LONG).show();
        } else {
            loadDataFromAssetsFolder();
            Toast.makeText(this, "Dados carregados da pasta Assets", Toast.LENGTH_LONG).show();
        }
    }

    public boolean loadDataFromInternalStorage() {

        InputStream in = null;
        InputStreamReader streamReader = null;
        BufferedReader br = null;

        try {
            in = openFileInput("db.txt");
            streamReader = new InputStreamReader(in);
            br = new BufferedReader(streamReader);

            txtName.setText(br.readLine());
            txtCity.setText(br.readLine());

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();

                if (streamReader != null)
                    streamReader.close();

                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public void loadDataFromAssetsFolder() {
        AssetManager manager = getAssets();

        InputStream in = null;
        InputStreamReader streamReader = null;
        BufferedReader br = null;

        try {
            in = manager.open("db.txt");
            streamReader = new InputStreamReader(in);
            br = new BufferedReader(streamReader);
            txtName.setText(br.readLine());
            txtCity.setText(br.readLine());

            StringBuilder sb = new StringBuilder();
            sb.append(txtName.getText().toString())
                    .append("\n")
                    .append(txtCity.getText().toString());

            saveDataFromInternalStorage(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();

                if (streamReader != null)
                    streamReader.close();

                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean saveDataFromInternalStorage(String s) {
        OutputStream out = null;
        OutputStreamWriter streamWriter = null;


        try {
            out = openFileOutput("db.txt", MODE_PRIVATE);
            streamWriter = new OutputStreamWriter(out);

            streamWriter.write(s);
            streamWriter.flush();

            return true;
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            try {
                if (streamWriter != null)
                    streamWriter.close();

                if (out != null)
                    out.close();
            } catch (IOException e) {

            }
        }

        return false;
    }

    public void btnSaveOnClick(View v) {
        StringBuilder sb = new StringBuilder();
        sb.append(txtName.getText().toString())
                .append("\n")
                .append(txtCity.getText().toString());

        saveDataFromInternalStorage(sb.toString());
    }

    public void btnLoadOnClick(View v) {
        loadDataFromInternalStorage();
    }

    public void btnClearOnClick(View v) {
        txtCity.setText("");
        txtName.setText("");
        txtName.requestFocus();
    }
}
