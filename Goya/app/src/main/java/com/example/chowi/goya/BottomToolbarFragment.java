package com.example.chowi.goya;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BottomToolbarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BottomToolbarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomToolbarFragment extends Fragment implements View.OnClickListener {
        public static final String EXTRA_URL ="url";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_bottom_toolbar,
                    container, false);

            FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.fab_add);
            addButton.setOnClickListener(this);

            return view;
        }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.fab_add:
                Log.i("pressing this button", "pressing it");
                AddFragment frg2=new AddFragment();//create the fragment instance for the bottom fragment

                FragmentManager manager= getActivity().getSupportFragmentManager();//create an instance of fragment manager

                FragmentTransaction transaction=manager.beginTransaction();//create an instance of Fragment-transaction

                transaction.add(R.id.container2, frg2, "Frag_Bot");

                transaction.addToBackStack(null);

                transaction.commit();

        }
    }



        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Bundle bundle = getArguments();
            if (bundle != null) {
                String link = bundle.getString("url");
                setText("hello world!!!!");
            }
        }

        public void setText(String url) {
            TextView view = (TextView) getView().findViewById(R.id.desc_text);
            view.setText(url);
        }
    }