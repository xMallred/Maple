package com.maple.mallr.mongo;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class InfoActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.colorPrimaryDark)
                        .buttonsColor(R.color.colorAccent)
                        .description("")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Tap next to continue");
                    }
                }, "Tutorial"));
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Toast.makeText(this, "Tutorial will now begin", Toast.LENGTH_SHORT).show();
    }
}