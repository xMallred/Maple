package com.maple.mallr.mongo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    ArrayList<String> fb= new ArrayList<>();
    String name;
    String email;
    String picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            email = extras.getString("email");
            picture = extras.getString("picture");
        }



        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        ImageView mIcon = (ImageView) findViewById(R.id.ivProfile);
        Picasso.with(this).load(picture).placeholder(R.drawable.defprofile).resize(500,500).transform(transformation).into(mIcon);


        Button mFollow = (Button) findViewById(R.id.btnFollow);
            if (mFollow.getText().toString().equalsIgnoreCase("View Events")) {
                // mFollow.setText("Following");
            } else {
                // mFollow.setText("Follow me");
            }

            // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.userName);
        textView.setText(name);
        textView.setTextColor(Color.BLACK);

        TextView textView2 = (TextView) findViewById(R.id.userDescription);
        textView2.setText(email);
        textView2.setTextColor(Color.BLACK);



        }
    }