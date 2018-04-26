package com.maple.mallr.mongo;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.colorPrimaryDark)
                        .buttonsColor(R.color.colorAccent)
                        .image(R.drawable.logo1)
                        .title("Welcome To Maple!")
                        .description("")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Finding events near you");
                    }
                }, "Maple"));
        addSlide(new CustomSlide());
        //        addSlide(new SlideFragmentBuilder()
//                        .backgroundColor(R.color.colorPrimaryDark)
//                        .buttonsColor(R.color.colorAccent)
//                        //.possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
//                        //.neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
//                        .image(R.drawable.check)
//                        .title("Continue")
//                        .description("")
//                        .build(),
//                new MessageButtonBehaviour(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showMessage("");
//                    }
//                }, "Maple"));
        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.colorPrimaryDark)
                        .buttonsColor(R.color.colorAccent)
                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
                        .neededPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION})
                        .image(R.drawable.location)
                        .title("Location")
                        .description("")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Thank you");
                    }
                }, "Maple"));
//        addSlide(new SlideFragmentBuilder()
//                        .backgroundColor(R.color.colorPrimaryDark)
//                        .buttonsColor(R.color.colorAccent)
//                        //.possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
//                        //.neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
//                        .image(R.drawable.check)
//                        .title("Continue")
//                        .description("")
//                        .build(),
//                new MessageButtonBehaviour(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showMessage("");
//                    }
//                }, "Maple"));
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Toast.makeText(this, "Welcome To Maple", Toast.LENGTH_SHORT).show();
    }
}