package com.maple.mallr.mongo;

import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.StitchClient;
import com.mongodb.stitch.android.StitchClientFactory;
import com.mongodb.stitch.android.auth.AvailableAuthProviders;
import com.mongodb.stitch.android.auth.anonymous.AnonymousAuthProvider;
import com.mongodb.stitch.android.services.mongodb.MongoClient;

import junit.framework.TestResult;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import javax.json.Json;

import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {

    CallbackManager _callbackManager;
    String email;
    String first_name;
    String facebook_id;
    String profilepicURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Tutorial First run


        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();

        }



        ///




        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);


        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Thread thread = new Thread() {
                   @Override
                   public void run() {
                       _callbackManager = CallbackManager.Factory.create();
                       LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "public_profile"));

                       LoginManager.getInstance().registerCallback(_callbackManager, new FacebookCallback<LoginResult>() {
                           @Override
                           public void onSuccess(LoginResult loginResult) {
                               System.out.println("i am right here");
                               AccessToken accToken = loginResult.getAccessToken();
                               GraphRequest request = GraphRequest.newMeRequest(
                                       accToken,
                                       new GraphRequest.GraphJSONObjectCallback() {
                                           @Override
                                           public void onCompleted(JSONObject object, GraphResponse response) {
                                               Log.v("LoginActivity Response ", response.toString());
                                               Log.e("object", object.toString());
                                               try {
                                                   email = object.getString("email");
                                                   first_name = object.getString("name");
                                                   facebook_id = object.getString("id");
                                                   profilepicURL = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                               } catch (JSONException e) {
                                                   e.printStackTrace();
                                               }
                                               profilepicURL = "graph.facebook.com/" + facebook_id + "/picture?type=large";
                                           }
                                       });
                               Bundle parameters = new Bundle();
                               parameters.putString("fields", "id,name,email,picture.type(large)");
                               request.setParameters(parameters);
                               request.executeAsync();

                               Intent myintent = new Intent(MainActivity.this, MapsActivity.class);
                               Bundle bun = new Bundle();
                               bun.putString("name", first_name);
                               bun.putString("email", email);
                               bun.putString("picture", profilepicURL);
                               bun.putString("fb", loginResult.getAccessToken().getToken());

                               myintent.putExtras(bun);
                               MainActivity.this.startActivity(myintent);

                           }

                           @Override
                           public void onCancel() {
                               System.out.println("i was canceled");
                           }

                           @Override
                           public void onError(FacebookException error) {
                               Intent myintent = new Intent(MainActivity.this, MapsActivity.class);
                               MainActivity.this.startActivity(myintent);
                           }
                       });
                   }
               };
               thread.start();
               try {
                   thread.join();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        _callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
