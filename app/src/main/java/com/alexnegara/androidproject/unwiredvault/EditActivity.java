package com.alexnegara.androidproject.unwiredvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    // store extra data from intent
    public static final String ACC_ID = "default_id";
    public static final String ACC_PSWD = "default_pswd";
    public static final String ACC_POS = "default_pos";

    // declare views
    private TextInputEditText tietId, tietPswd, tietCPswd;
    private TextInputLayout tilId, tilPswd, tilCPswd;

    // overwrite back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent(this, MainActivity.class);
        startActivity(backIntent);
        overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        // assign the views
        tietId = findViewById(R.id.edit_text_id);
        tilId = findViewById(R.id.edit_layout_id);
        tietPswd = findViewById(R.id.edit_text_pswd);
        tilPswd = findViewById(R.id.edit_layout_pswd);
        tietCPswd = findViewById(R.id.edit_text_confirm_pswd);
        tilCPswd = findViewById(R.id.edit_layout_confirm_pswd);

        // init button views
        Button saveButton = findViewById(R.id.edit_save_btn);
        Button genButton = findViewById(R.id.edit_gen_btn);

        // assign the view from intent data
        tietId.setText(getIntent().getStringExtra(ACC_ID));
        tietPswd.setText(getIntent().getStringExtra(ACC_PSWD));

        // add listener to buttons
        saveButton.setOnClickListener(this);
        genButton.setOnClickListener(this);

        // add focus listener and icon at the end of the text input
        tilId.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
        tilId.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    tilId.setEndIconVisible(true);
                }
                else{
                    tilId.setEndIconVisible(false);
                }
            }
        });
    }

    // define what happens after clicking particular button
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.edit_save_btn:
                String errMessage = validatePassword(tietPswd.getText().toString(), tietCPswd.getText().toString());
                if(!errMessage.isEmpty()){
                    tilCPswd.setError(errMessage);
                }
                else{
                    tilCPswd.setError(null);
                    Intent moveToMain = new Intent(this, MainActivity.class);
                    moveToMain.putExtra(MainActivity.CHANGED_ID, tietId.getText().toString());
                    moveToMain.putExtra(MainActivity.CHANGED_PSWD, tietPswd.getText().toString());
                    moveToMain.putExtra(MainActivity.CHANGED_POS, getIntent().getIntExtra(ACC_POS, -1));
                    moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(moveToMain);
                    overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
                }
                break;
            case R.id.edit_gen_btn:
                String password = PasswordGenerator.generate();
                tietPswd.setText(password);
                tietCPswd.setText(password);
                break;
        }
    }

    // checking both password text field
    public String validatePassword(String pswd, String cPswd){
        if(pswd.equals(cPswd)){
            if(pswd.length()==0) return getString(R.string.err_length_pswd);
            return "";
        }
        else{
            return getString(R.string.err_different_pswd);
        }
    }


}
