package com.maple.mallr.mongo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmailActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Button send=(Button)findViewById(R.id.sendMail);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EmailActivity.this,"Opening mail app...",          Toast.LENGTH_SHORT).show();
                EditText subj = findViewById(R.id.subj);
                EditText fdbk = findViewById(R.id.feedback);
                EditText name = findViewById(R.id.name);
                EditText phone = findViewById(R.id.phone);
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:m5972@tamu.edu"));
                intent.putExtra(Intent.EXTRA_SUBJECT, subj.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, fdbk.getText().toString() + " \n\nSent from: " + name.getText().toString() + "\nPhone: " + phone.getText().toString() + "\nSent from the Maple App.");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);

                }

            }
        });
    }
}
