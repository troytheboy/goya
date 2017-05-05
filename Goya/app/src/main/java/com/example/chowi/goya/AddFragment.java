package com.example.chowi.goya;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.os.Build.VERSION_CODES.M;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment implements View.OnClickListener {
    public static final String EXTRA_URL ="url";

    OnHeadlineSelectedListener mCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add,
                container, false);


        Button postButton = (Button) view.findViewById(R.id.button_post);
        postButton.setOnClickListener(this);

        FloatingActionButton camButton = (FloatingActionButton) view.findViewById(R.id.fab_camera);
        camButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("clicking camera!", "camera being pressed");
            }
        });

        return view;
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

    @Override
    public void onClick(View v) {
        Log.i("hello","hello");
        EditText titleEditText = (EditText) getActivity().findViewById(R.id.title_text);
        String titleText = titleEditText.getText().toString();

        EditText descEditText = (EditText) getActivity().findViewById(R.id.desc_text);
        String descText = descEditText.getText().toString();

        // if the number is empty, dont send and toast error message
        if (titleText.isEmpty()) {
            Toast.makeText(getContext(), "Enter a title", Toast.LENGTH_SHORT).show();
            // if the text body is empty, dont send and toast error message
        } else if (descText.isEmpty()) {
            Toast.makeText(getContext(), "Enter a description", Toast.LENGTH_SHORT).show();
            // send text and reset field values
        } else {
            mCallback.onPostSelected(titleText, descText);
            titleEditText.setText("");
            descEditText.setText("");
        }
    }

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onPostSelected(String title, String desc);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

}

