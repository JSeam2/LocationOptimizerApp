package com.locationoptimizer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SelectFragment extends Fragment {
    // Recycler stuff
    RecyclerView recyclerView;
    LocationAdapter adapter;

    // Locations ArrayList
    public static ArrayList<String> locations = new ArrayList<>();

    // TODO Selected Locations



    private OnFragmentInteractionListener mListener;

    public SelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                locations = getArguments().getStringArrayList("locations");
            }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null){
            adapter = (LocationAdapter) savedInstanceState.getSerializable("adapter");
        } else {
            adapter = new LocationAdapter(getContext(), locations);
        }


        View rootview = inflater.inflate(R.layout.fragment_select, container, false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycle_1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootview;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
