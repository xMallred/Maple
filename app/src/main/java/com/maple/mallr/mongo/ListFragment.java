package com.maple.mallr.mongo;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.StitchClient;
import com.mongodb.stitch.android.StitchClientFactory;
import com.mongodb.stitch.android.auth.anonymous.AnonymousAuthProvider;
import com.mongodb.stitch.android.services.mongodb.MongoClient;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    ArrayList<String> listOfEvent;

    StitchClient stitchClient;
    MongoClient client;
    MongoClient.Collection coll;
    ArrayList<String> list_event = new ArrayList<>();
    ArrayList<String> displayEventList = new ArrayList<>();
    ArrayList<String> nameOfEvents = new ArrayList<>();

    public ListFragment() {
        // Required empty public constructor
    }


    ListView lv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Task<StitchClient> stitchClientTask = StitchClientFactory.create(getActivity(), "eventfinder-wkdhy");
        stitchClient = stitchClientTask.getResult();

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        lv = view.findViewById(R.id.lv);
            //listOfEvents = savedInstanceState.getStringArrayList("eventList");

        login();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String event = list_event.get(position);
                mListener.onFragmentInteraction(event);
            }
        });
         return view;
    }

    public void login()
    {
        client = new MongoClient(stitchClient, "mongodb-atlas");
        coll = client.getDatabase("Maple").getCollection("Events");

        stitchClient.logInWithProvider(new AnonymousAuthProvider()).addOnCompleteListener(new OnCompleteListener<String>(){
            @Override
            public void onComplete(@NonNull final Task<String> task) {
                if (task.isSuccessful()) {
                    Log.d("stitch", "logged in anonymously as user " + task.getResult());
                } else {
                    Log.e("stitch", "failed to log in anonymously", task.getException());
                }
                refreshList();
            }
        });
    }

    public String parse(String str)
    {
        String ret = str.replaceAll("Document", "");
        String ret1 = ret.replaceAll("\\{", "");
        String[] tokens = ret1.split("_");
        for(int i = 1; i < tokens.length ; i++)
        {
            String temp = tokens[i];

            String id = StringUtils.substringBetween(temp, "id=", ",");
            String title = StringUtils.substringBetween(temp, "title=", ",");
            String venue = StringUtils.substringBetween(temp, "venue=", ",");
            String address = StringUtils.substringBetween(temp, "Address=", ",");
            String latitude = StringUtils.substringBetween(temp, "latitude=", ",");
            String longitude = StringUtils.substringBetween(temp, "longitude=", ",");
            String eventtype = StringUtils.substringBetween(temp, "EventType=", ",");
            String age = StringUtils.substringBetween(temp, "Age=", ",");
            String date = StringUtils.substringBetween(temp, "Date=", "}");
            displayEventList.add(title + " @ " + venue + "\nEventType: " + eventtype + "\nAddress: " + address + "\nDate: " + date);


            if(!nameOfEvents.contains(title))
            {
                nameOfEvents.add(title);
                String event_info = "Event: " + title + ", EventType: " + eventtype + ", Address: " + address + ", Venue: " + venue + ", Date: " + date+ ", Latitude: " + latitude + ", Longitude: " + longitude+";";
                list_event.add(event_info);
            }



        }
        return ret1;

    };


    public void refreshList()
    {
        stitchClient.executeFunction("finddocs").addOnCompleteListener(new OnCompleteListener<Object>() {
            @Override
            public void onComplete(@NonNull Task task){
                if(task.isSuccessful()){
                    //Log.d("Stitch", "Number of collections: " + task.getResult());
                    String t = task.getResult().toString();
                    String obj = parse(t);

                        final ArrayAdapter<String> listViewArrayAdapter;
                    List<String> eventList = new ArrayList<>(list_event);
                    if(!eventList.isEmpty()) {
                        listViewArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, displayEventList);

                        lv.setAdapter(listViewArrayAdapter);

                        lv.invalidateViews();
                    }
                }
                else {
                    Log.e("stitch", "failed to get number of collections", task.getException());
                }

            }


        });
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String string);
    }
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
