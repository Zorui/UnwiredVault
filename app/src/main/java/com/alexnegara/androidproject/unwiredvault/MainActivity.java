package com.alexnegara.androidproject.unwiredvault;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // declare views
    private RecyclerView rvVaults;
    private Button addButton;
    private LinearLayout noAccountLayout;

    // init list, file path, and variables related to Account
    public static final String FILE_NAME = "vault.json";
    public static final String CHANGED_POS = "default_pos";
    public static final String CHANGED_PSWD = "default_pswd";
    public static final String CHANGED_ID = "default_id";
    public static final String NEW_ID = "default_new_id";
    public static final String NEW_PSWD = "default_new_pswd";
    public static final String NEW_APP = "default_new_app";
    private ArrayList<Account> list = new ArrayList<>();

    // when created do this
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign views
        rvVaults = findViewById(R.id.rv_vault);
        noAccountLayout = findViewById(R.id.no_account_layout);

        // set fixed size for optimized RecycleView
        // the child of RecycleView is not changing in size
        rvVaults.setHasFixedSize(true);

        // add custom fab with click listener
        addButton = findViewById(R.id.custom_fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            // define what happens after clicking
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(addIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        // init gotten intent data every time MainActivity is created
        String changedPswd = getIntent().getStringExtra(CHANGED_PSWD);
        int posToChange = getIntent().getIntExtra(CHANGED_POS, -1);
        String changedId = getIntent().getStringExtra(CHANGED_ID);
        String newId = getIntent().getStringExtra(NEW_ID);
        String newPswd = getIntent().getStringExtra(NEW_PSWD);
        String newApp = getIntent().getStringExtra(NEW_APP);

        // read the json file then assign to list
        jsonAssign(jsonRead());

        // check whether new account is created
        if (newId != null && newPswd != null && newApp != null) {
            setNewAccountToList(newId, newPswd, newApp);
        }

        // check whether id is changed
        if (changedId != null) {
            list.get(posToChange).setId(changedId);
        }

        // check whether password is changed
        if (changedPswd != null) {
            list.get(posToChange).setPswd(changedPswd);
        }

        // write json to file, then clear list to update with new json file
        try {
            jsonWrite(setListToJson());
            list.clear();
            jsonAssign(jsonRead());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // show configured recycler
        showRecyclerList();
    }


    // create option, use inflate for possible future option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // what happen after clicking option, use switch for possible future option
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.password_setting:
                Intent settingIntent = new Intent(MainActivity.this, GeneratorActivity.class);
                startActivity(settingIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // configure recycler adapter and layout
    private void showRecyclerList() {
        rvVaults.setLayoutManager(new LinearLayoutManager(this));
        ListAdapter listVaultAdapter = new ListAdapter(list);
        rvVaults.setAdapter(listVaultAdapter);

        // check list empty or not
        onEmptyList(list.size());

        // define what interface do
        listVaultAdapter.setOnClickCallback(new ListAdapter.OnClickCallback() {
            @Override
            public void onEditClicked(Account data, int pos) {
                // move to EditActivity with data
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(EditActivity.ACC_ID, data.getId());
                intent.putExtra(EditActivity.ACC_PSWD, data.getPswd());
                intent.putExtra(EditActivity.ACC_POS, pos);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }

            @Override
            public void onCopyClicked(String password) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password", password);
                clipboard.setPrimaryClip(clip);
            }

            @Override
            public void onDeleteClicked(int pos) {
                // get the position and remove on the list
                list.remove(pos);

                // rewrite json
                try {
                    jsonWrite(setListToJson());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // delete item in adapter without reread json file due to getting inherited animation
                listVaultAdapter.notifyItemRemoved(pos);

                // display empty layout if no more item left
                onEmptyList(list.size());
            }
        });
    }

    // check empty to set layout visibility
    private void onEmptyList(int size){
        if(size == 0){
            noAccountLayout.setVisibility(View.VISIBLE);
            rvVaults.setVisibility(View.GONE);
        }
        else {
            noAccountLayout.setVisibility(View.GONE);
            rvVaults.setVisibility(View.VISIBLE);
        }
    }

    // add new data to list
    private void setNewAccountToList(String id, String pswd, String app) {
        Account newAcc = new Account();
        newAcc.setName(app);
        newAcc.setId(id);
        newAcc.setPswd(pswd);
        list.add(newAcc);
    }

    // set account object with value from readed json file
    private Account setAccValue(JSONObject jsonObj) throws JSONException {
        Account accObj = new Account();
        accObj.setId(jsonObj.getString("id"));
        accObj.setName(jsonObj.getString("name"));
        accObj.setPswd(jsonObj.getString("pswd"));
        accObj.setPhoto(accObj.getName());
        return accObj;
    }

    // set json from list
    private JSONObject setListToJson() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject curObj = new JSONObject();
            curObj.put("name", list.get(i).getName());
            curObj.put("id", list.get(i).getId());
            curObj.put("pswd", list.get(i).getPswd());
            jsonArray.put(curObj);
        }
        return new JSONObject().put("vault", jsonArray);
    }

    // write json object into json file (no append)
    public void jsonWrite(JSONObject jsonObject) {
        // convert json to string Format
        String accString = jsonObject.toString();

        // open file and write
        File file = new File(getFilesDir(), FILE_NAME);
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(accString);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read json file into string
    public String jsonRead() {
        File file = new File(getFilesDir(), FILE_NAME);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    // assign read string and convert it to json object and Account object added to the list
    public void jsonAssign(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            JSONArray objArr = obj.getJSONArray("vault");
            for (int i = 0; i < objArr.length(); i++) {
                JSONObject currentObj = objArr.getJSONObject(i);
                list.add(setAccValue(currentObj));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}