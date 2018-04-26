package com.maple.mallr.mongo;

        import android.annotation.TargetApi;
        import android.content.Context;
        import android.content.Intent;
        import android.content.res.Configuration;
        import android.media.Ringtone;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.preference.ListPreference;
        import android.preference.Preference;
        import android.preference.PreferenceActivity;
        import android.support.v7.app.ActionBar;
        import android.preference.PreferenceFragment;
        import android.preference.PreferenceManager;
        import android.preference.RingtonePreference;
        import android.support.v7.app.AppCompatActivity;
        import android.text.TextUtils;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Switch;

        import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private Switch switchMusic;
    private Switch switchSports;
    private Switch switchOther;
    private Button refreshButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        switchMusic = findViewById(R.id.mySwitchMusic);
        switchSports = findViewById(R.id.mySwitchSports);
        switchOther = findViewById(R.id.mySwitchOther);
        refreshButton = findViewById(R.id.refreshButton);



        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean music = false;
                Boolean sports = false;
                Boolean other = false;
                if(switchMusic.isChecked())
                {
                    Intent i = new Intent();
                    music = true;
                    i.putExtra("music", music);
                    i.putExtra("sports", sports);
                    i.putExtra("other", other);
                    setResult(RESULT_OK, i);
                    finish();
                }
                if(switchSports.isChecked())
                {
                    Intent i = new Intent();
                    sports = true;
                    i.putExtra("music", music);
                    i.putExtra("sports", sports);
                    i.putExtra("other", other);
                    setResult(RESULT_OK, i);
                    finish();
                }
                if(switchOther.isChecked())
                {
                    Intent i = new Intent();
                    other = true;
                    i.putExtra("music", music);
                    i.putExtra("sports", sports);
                    i.putExtra("other", other);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });

    }
}
