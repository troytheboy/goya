package com.example.chowi.goya;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chowi.goya.dummy.DummyContent;
import com.example.chowi.goya.dummy.DummyContent.DummyItem;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.R.attr.entries;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EventItemFragment extends ListFragment {

    private ArrayList<EventItem> mValues;
    private final int LIST_SIZE = 6;
    private String mUsername;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        mUsername = null;
        if (bundle != null) {
            mUsername = bundle.getString("username");
        }

        mValues = new ArrayList<>();
        // Read from the database
        FirebaseDatabase.getInstance().getReference().child("events")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            EventItem eventItem = snapshot.getValue(EventItem.class);
                            mValues.add(eventItem);
                        }

                        if (mValues.size() > 0) {
                            Collections.sort(mValues, new Comparator<EventItem>() {
                                @Override
                                public int compare(final EventItem object1, final EventItem object2) {
                                    return (object2.getGoVotes() - object2.getNoVotes()) - (object1.getGoVotes() - object1.getNoVotes());
                                }
                            });
                        }
                        EventItem[] values = new EventItem[LIST_SIZE];
                        for (int i = 0; i < LIST_SIZE; i++) {
                            if (mValues.get(i) != null) {
                                values[i] = mValues.get(i);
                            }
                        }

                        MyEventItemListAdapter adapter = new MyEventItemListAdapter(getContext(), values);
                        setListAdapter(adapter);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });



    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
        EventItem eventItem = (EventItem) getListAdapter().getItem(position);
        Bundle bundle = new Bundle();
        Log.i("eventitem", eventItem.toString());
        //bundle.putStringArray("data", postData);
        bundle.putParcelable("item", eventItem);
        bundle.putString("username", mUsername);
        // set Fragmentclass Arguments

        SingleEventMapFragment frg1 = new SingleEventMapFragment();


        EventDetailFragment frg2=new EventDetailFragment();//create the fragment instance for the bottom fragment
        frg2.setArguments(bundle);

        FragmentManager manager= getActivity().getSupportFragmentManager();//create an instance of fragment manager

        FragmentTransaction transaction=manager.beginTransaction();//create an instance of Fragment-transaction

        transaction.replace(R.id.container2, frg2, "Frag_Bot");

        transaction.addToBackStack(null);

        transaction.commit();

    }
}
