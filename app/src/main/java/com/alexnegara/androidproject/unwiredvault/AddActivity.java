package com.alexnegara.androidproject.unwiredvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddActivity extends AppCompatActivity {
    // declare views
    private ImageView logo;
    private TextInputEditText tietId,tietPswd;
    private TextInputLayout tilId, tilAppName, tilPswd;
    private AutoCompleteTextView actvApplication;
    private Button createBtn, genBtn;

    // item in dropdown
    private static final String[] APPS = new String[] {
            "instagram", "facebook", "medium", "youtube", "linkedin", "google plus", "tinder", "reddit", "snapchat", "quora", "other"
    };

    // declare variable
    private String toBeSendAppName, toBeSendId, toBeSendPswd;
    private int photo = R.drawable.ic_baseline_account_circle_24;

    // overwrite back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent(this, MainActivity.class);
        startActivity(backIntent);
        overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);

        // assign the views
        logo = findViewById(R.id.add_logo);
        tietId = findViewById(R.id.add_id);
        tietPswd = findViewById(R.id.add_pswd);
        tilId = findViewById(R.id.add_layout_id);
        tilAppName = findViewById(R.id.add_layout_dropdown);
        tilPswd = findViewById(R.id.add_layout_pswd);
        actvApplication = findViewById(R.id.add_dropdown);
        createBtn = findViewById(R.id.create_vault);
        genBtn = findViewById(R.id.add_gen_btn);

        // array for list item in the dropdown and define what happens after clicking the item
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_app, APPS);
        actvApplication.setAdapter(adapter);
        actvApplication.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toBeSendAppName = parent.getItemAtPosition(position).toString();
                displayPhoto(toBeSendAppName);
                logo.setImageDrawable(getDrawable(photo));
            }
        });

        // set listeners to buttons
        genBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tietPswd.setText(PasswordGenerator.generate());
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(toBeSendAppName == null) {tilAppName.setError(getString(R.string.err_no_selected_item));}
                if(tietId.getText().toString().equals("")) {tilId.setError(getString(R.string.err_no_id));}
                else {toBeSendId = tietId.getText().toString();}
                if(tietPswd.getText().toString().equals("")) {tilPswd.setError(getString(R.string.err_no_pswd));}
                else {toBeSendPswd = tietPswd.getText().toString();}

                if(toBeSendPswd != null && toBeSendId != null && toBeSendAppName != null){
                    Intent moveToMain = new Intent(AddActivity.this, MainActivity.class);
                    moveToMain.putExtra(MainActivity.NEW_ID, toBeSendId);
                    moveToMain.putExtra(MainActivity.NEW_PSWD, toBeSendPswd);
                    moveToMain.putExtra(MainActivity.NEW_APP, toBeSendAppName);
                    moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(moveToMain);
                    overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
                }
            }
        });
    }

    // similar switch method to Account class, to set the photo
    public void displayPhoto(String name){
        switch (name) {
            case "instagram":
                photo = R.drawable.instagram;
                break;
            case "facebook":
                photo = R.drawable.facebook;
                break;
            case "medium":
                photo = R.drawable.medium;
                break;
            case "youtube":
                photo = R.drawable.youtube;
                break;
            case "linkedin":
                photo = R.drawable.linkedin;
                break;
            case "google plus":
                photo = R.drawable.google_plus;
                break;
            case "tinder":
                photo = R.drawable.tinder;
                break;
            case "reddit":
                photo = R.drawable.reddit;
                break;
            case "snapchat":
                photo = R.drawable.snapchat;
                break;
            case "quora":
                photo = R.drawable.quora;
                break;
            default:
                photo = R.drawable.ic_baseline_account_circle_24;
                break;
        }
    }
}
