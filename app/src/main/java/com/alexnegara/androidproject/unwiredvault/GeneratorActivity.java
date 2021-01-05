package com.alexnegara.androidproject.unwiredvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

public class GeneratorActivity extends AppCompatActivity {
    // declare views
    private Slider sDigit, sLowChar, sUpChar, sSymbol;
    private TextView tvPswdLength;
    private Button btnSaveConf;

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
        setContentView(R.layout.generator_layout);

        // assign views
        sDigit = findViewById(R.id.digit_slider);
        sLowChar = findViewById(R.id.low_char_slider);
        sUpChar = findViewById(R.id.up_char_slider);
        sSymbol = findViewById(R.id.symbol_slider);
        tvPswdLength = findViewById(R.id.pswd_length);
        btnSaveConf = findViewById(R.id.save_configuration);

        // set the value of views
        sDigit.setValue((float)PasswordGenerator.getDigitLength());
        sLowChar.setValue((float)PasswordGenerator.getLowCharLength());
        sUpChar.setValue((float)PasswordGenerator.getUpCharLength());
        sSymbol.setValue((float)PasswordGenerator.getSymbolLength());
        tvPswdLength.setText(getString(R.string.password_length)+ " " + countLength(sDigit.getValue(), sLowChar.getValue(), sUpChar.getValue(), sSymbol.getValue()));

        // set click event listener
        btnSaveConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordGenerator.setDigitLength((int) sDigit.getValue());
                PasswordGenerator.setLowCharLength((int) sLowChar.getValue());
                PasswordGenerator.setUpCharLength((int) sUpChar.getValue());
                PasswordGenerator.setSymbolLength((int) sSymbol.getValue());
                Intent moveToMain = new Intent(GeneratorActivity.this, MainActivity.class);
                moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(moveToMain);
                overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
            }
        });

        // set sliders listener
        sDigit.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                tvPswdLength.setText(getString(R.string.password_length) + " " + countLength(sDigit.getValue(), sLowChar.getValue(), sUpChar.getValue(), sSymbol.getValue()));
            }
        });
        sLowChar.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                tvPswdLength.setText(getString(R.string.password_length) + " " + countLength(sDigit.getValue(), sLowChar.getValue(), sUpChar.getValue(), sSymbol.getValue()));
            }
        });
        sUpChar.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                tvPswdLength.setText(getString(R.string.password_length) + countLength(sDigit.getValue(), sLowChar.getValue(), sUpChar.getValue(), sSymbol.getValue()));
            }
        });
        sSymbol.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                tvPswdLength.setText(getString(R.string.password_length) + " " + countLength(sDigit.getValue(), sLowChar.getValue(), sUpChar.getValue(), sSymbol.getValue()));
            }
        });

    }

    // count the length of character combinations
    private String countLength(float dLen, float lLen, float uLen, float sLen){
        float pswdLength = dLen + lLen + uLen + sLen;
        return String.valueOf(pswdLength);
    }
}
