package com.example.chowi.goya;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.POWER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailFragment extends Fragment {
    public static final String EXTRA_URL ="url";

    //OnHeadlineSelectedListener mCallback;

    //PackageManager pm = getActivity().getPackageManager();

    private TextView mTitleText;
    private TextView mDescText;

    private ImageView mImageView;

    private StorageReference mStorageRef;

    private Bitmap mBitmap = null;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Bundle bundle = this.getArguments();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail,
                container, false);


        mTitleText = (TextView) view.findViewById(R.id.title_text);
        mDescText = (TextView) view.findViewById(R.id.desc_text);

        mImageView = (ImageView) view.findViewById(R.id.cam_image);



        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String[] data = bundle.getStringArray("data");

            mTitleText.setText(data[0]);
            //mDescText.setText(data[1]);

            if (data[2] != null) {
                Log.i(data[2], "here is data2");
                // Create a storage reference from our app
                mStorageRef = FirebaseStorage.getInstance().getReference();

                StorageReference currRef = mStorageRef.child(data[2]);


                /*
                mStorageRef.child(data[2]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Log.i("got the download uri", "here it is");
                        mImageView.setImageURI(uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i("uri retrieval failure", "could not get it");
                        // Handle any errors
                    }
                });*/

                // working but slow byte method for donwload
                final long ONE_MEGABYTE = 1024 * 1024;
                currRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.i("found in the database", "displaying it now");
                        // Data for "images/island.jpg" is returns, use this as needed

                        setImageViewWithByteArray(mImageView, bytes);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Log.i("did not find in db", "error");
                    }
                });



            }

        }
    }

    public static void setImageViewWithByteArray(ImageView view, byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);


        //ExifInterface exif = new ExifInterface(data);
        //orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);


        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        view.setImageBitmap(rotatedBitmap);
        //view.setImageBitmap(bitmap);
    }

    public void setText(String url) {
        TextView view = (TextView) getView().findViewById(R.id.desc_text);
        view.setText(url);
    }

    /*
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

    }*/

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