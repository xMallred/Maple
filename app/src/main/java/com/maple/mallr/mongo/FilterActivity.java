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

        import java.util.List;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }
}
