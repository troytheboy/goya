package com.example.chowi.goya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.R.attr.defaultValue;
import static android.app.Activity.RESULT_OK;
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

    //PackageManager pm = getActivity().getPackageManager();

    private TextView mTextView;
    private FloatingActionButton mFAB;

    private ImageView mImageView;

    private Bitmap mBitmap = null;

    static final int REQUEST_IMAGE_CAPTURE = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add,
                container, false);


        mTextView = (TextView) view.findViewById(R.id.add_pic);
        mFAB = (FloatingActionButton) view.findViewById(R.id.fab_camera);

        mImageView = (ImageView) view.findViewById(R.id.cam_image);


        mTextView.setVisibility(View.GONE);
        mFAB.setVisibility(View.GONE);


        mImageView.setVisibility(View.VISIBLE);

        Button postButton = (Button) view.findViewById(R.id.button_post);
        postButton.setOnClickListener(this);

        FloatingActionButton camButton = (FloatingActionButton) view.findViewById(R.id.fab_camera);
        camButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("clicking camera!", "camera being pressed");
                // If the device has a camera, open up the camera feature
                //if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                Context context = getActivity();
                PackageManager packageManager = context.getPackageManager();
                if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
                    Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                            .show();
                    return;
                } else {
                    Log.i("cam permission check", "has permission");
                /*if (hasPermissionInManifest(context, "android.permission.CAMERA")) {
                    Log.i("It has the permission", "should be working");
                }*/
                    dispatchTakePictureIntent();
                }
                //dispatchTakePictureIntent();             }

                //}
            }
        });


        return view;
    }

    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mBitmap = imageBitmap;
            mImageView.setImageBitmap(imageBitmap);

            mTextView.setVisibility(View.GONE);
            mFAB.setVisibility(View.GONE);

            mImageView.setVisibility(View.VISIBLE);
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
            String encodedImage = null;
            if (mBitmap != null) {
                encodedImage = BitMapToString(mBitmap);
            }
            mCallback.onPostSelected(titleText, descText, encodedImage);
            titleEditText.setText("");
            descEditText.setText("");
            mTextView.setVisibility(View.VISIBLE);
            mFAB.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
        }
    }

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onPostSelected(String title, String desc, String encodedImage);
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

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}

