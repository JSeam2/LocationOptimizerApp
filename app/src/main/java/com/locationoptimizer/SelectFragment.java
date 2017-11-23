package com.locationoptimizer;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> placesSelected = new ArrayList<>();
    private String listAll;
    TextView selectedPlacesA;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    OnHeadlineSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(ArrayList<String> placesSelected);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
//
//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        // Send the event to the host activity
//        mCallback.onArticleSelected(position);
//    }



    public SelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectFragment newInstance(String param1, String param2) {
        SelectFragment fragment = new SelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);

            }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_select, container, false);
        selectedPlacesA = (TextView) rootview.findViewById(R.id.selectedPlaces);
        selectedPlacesA.setVisibility(View.VISIBLE);
        // get your ToggleButton
        ToggleButton bt1 = (ToggleButton) rootview.findViewById(R.id.toggleButton1);
        ToggleButton bt2 = (ToggleButton) rootview.findViewById(R.id.toggleButton2);
        ToggleButton bt3 = (ToggleButton) rootview.findViewById(R.id.toggleButton3);
        ToggleButton bt4 = (ToggleButton) rootview.findViewById(R.id.toggleButton4);
        ToggleButton bt5 = (ToggleButton) rootview.findViewById(R.id.toggleButton5);

//        Button saveButton = (Button) rootview.findViewById(R.id.saveButton);

        // attach an OnClickListener
        bt1.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // your click actions go here
                Toggled1(v);
            }
        });

        bt2.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // your click actions go here
                Toggled2(v);
            }
        });

        bt3.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // your click actions go here
                Toggled3(v);
            }
        });

        bt4.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // your click actions go here
                Toggled4(v);
            }
        });

        bt5.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // your click actions go here
                Toggled5(v);
            }
        });

//        saveButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveIt(view);
//            }
//        });


        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    public void Toggled1(View view){
        boolean checked = ((ToggleButton)view).isChecked();
        if(checked) {
            placesSelected.add(this.getString(R.string.tb1));
            Toast.makeText(getContext(), "Toggled On", Toast.LENGTH_SHORT).show();

        }else{
            placesSelected.remove(this.getString(R.string.tb1));
            Toast.makeText(getContext(), "Toggled Off", Toast.LENGTH_SHORT).show();

        }
        updateListAll();
    }
    public void Toggled2(View view){
        boolean checked = ((ToggleButton)view).isChecked();
        if(checked) {
            placesSelected.add(this.getString(R.string.tb2));
        }else{
            placesSelected.remove(this.getString(R.string.tb2));
        }
        updateListAll();
    }
    public void Toggled3(View view){
        boolean checked = ((ToggleButton)view).isChecked();
        if(checked) {
            placesSelected.add(this.getString(R.string.tb3));
        }else{
            placesSelected.remove(this.getString(R.string.tb3));
        }
        updateListAll();
    }
    public void Toggled4(View view){
        boolean checked = ((ToggleButton)view).isChecked();
        if(checked) {
            placesSelected.add(this.getString(R.string.tb4));
        }else{
            placesSelected.remove(this.getString(R.string.tb4));
        }
        updateListAll();
    }
    public void Toggled5(View view){
        boolean checked = ((ToggleButton)view).isChecked();
        if(checked) {
            placesSelected.add(this.getString(R.string.tb5));
        }else{
            placesSelected.remove(this.getString(R.string.tb5));
        }
        updateListAll();
    }

    private void updateListAll() {
        listAll = "";
        for(String i : placesSelected){
            listAll = listAll + "\n" + i;
        }
        selectedPlacesA.setText(listAll);

        mCallback.onArticleSelected(placesSelected);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "Select fragment attached", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
//
//    public interface OnFragmentInteractionListener {
//        public void onFragmentSetStudents(ArrayList<String>);
//    }


}
