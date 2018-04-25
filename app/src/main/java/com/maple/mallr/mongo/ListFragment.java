package com.maple.mallr.mongo;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {


    public ListFragment() {
        // Required empty public constructor
    }
    ListView lv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View view = inflater.inflate(R.layout.fragment_list, container, false);


        lv = view.findViewById(R.id.lv);
        String[] eventText = {"Event 1", "Event 2", "event 3",
                "event 4", "etc"};

        List<String> eventList = new ArrayList<>(Arrays.asList(eventText));
        final ArrayAdapter<String> listViewArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, eventList);

        lv.setAdapter(listViewArrayAdapter);

        lv.invalidateViews();

        return view;
    }



}
