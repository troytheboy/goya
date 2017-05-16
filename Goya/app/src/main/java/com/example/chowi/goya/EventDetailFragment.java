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
import android.provider.ContactsContract;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

import static android.R.attr.data;
import static android.R.attr.password;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.POWER_SERVICE;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


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
    private TextView mAuthorText;
    private TextView mVoteText;

    private ImageView mImageView;

    private StorageReference mStorageRef;

    private DatabaseReference mDatabase;

    private EventItem mEventItem;

    private Bitmap mBitmap = null;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Bundle bundle = this.getArguments();

    private String mUsername = "chow";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail,
                container, false);


        mTitleText = (TextView) view.findViewById(R.id.title_text);
        mAuthorText = (TextView) view.findViewById(R.id.event_author);
        mVoteText = (TextView) view.findViewById(R.id.event_votes);

        mImageView = (ImageView) view.findViewById(R.id.cam_image);



        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        mEventItem = null;
        if (bundle != null) {
            mEventItem = bundle.getParcelable("item");
        }

        mTitleText.setText(mEventItem.getTitle());
        mAuthorText.setText(mEventItem.getUsername());
        mVoteText.setText(mEventItem.getGoVotes() - mEventItem.getNoVotes() + " GoVotes");

        if (mEventItem.getImage() != null) {
            mStorageRef = FirebaseStorage.getInstance().getReference();

            StorageReference currRef = mStorageRef.child(mEventItem.getImage());

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



        ImageButton buttonGovote = (ImageButton) getActivity().findViewById(R.id.btn_govote);
        buttonGovote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("clicking govote!", "govote being pressed");

                mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("accounts").child(mUsername).child("voted").child(mEventItem.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.getValue() != null) {
                            Long votes = (Long) snapshot.getValue();
                            if (votes == -1) {
                                mEventItem.setGoVotes(mEventItem.getGoVotes() + 2);
                                mVoteText.setText(mEventItem.getGoVotes() - mEventItem.getNoVotes() + " GoVotes");
                                mDatabase.child("events").child(mEventItem.getId()).child("goVotes").setValue(mEventItem.getGoVotes());
                                mDatabase.child("accounts").child(mUsername).child("voted").child(mEventItem.getId()).setValue(1);
                            } else if (votes == 0) {
                                mEventItem.setGoVotes(mEventItem.getGoVotes() + 1);
                                mVoteText.setText(mEventItem.getGoVotes() - mEventItem.getNoVotes() + " GoVotes");
                                mDatabase.child("events").child(mEventItem.getId()).child("goVotes").setValue(mEventItem.getGoVotes());
                                mDatabase.child("accounts").child(mUsername).child("voted").child(mEventItem.getId()).setValue(1);
                            } else if (votes == 1) {
                                mEventItem.setGoVotes(mEventItem.getGoVotes() - 1);
                                mVoteText.setText(mEventItem.getGoVotes() - mEventItem.getNoVotes() + " GoVotes");
                                mDatabase.child("events").child(mEventItem.getId()).child("goVotes").setValue(mEventItem.getGoVotes());
                                mDatabase.child("accounts").child(mUsername).child("voted").child(mEventItem.getId()).setValue(0);
                            }
                        } else {
                            mEventItem.setGoVotes(mEventItem.getGoVotes() + 1);
                            mVoteText.setText(mEventItem.getGoVotes() - mEventItem.getNoVotes() + " GoVotes");
                            mDatabase.child("events").child(mEventItem.getId()).child("goVotes").setValue(mEventItem.getGoVotes());
                            mDatabase.child("accounts").child(mUsername).child("voted").child(mEventItem.getId()).setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });






            }
        });

        ImageButton buttonNovote = (ImageButton) getActivity().findViewById(R.id.btn_novote);
        buttonNovote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("clicking novote!", "novote being pressed");

                mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("accounts").child(mUsername).child("voted").child(mEventItem.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.getValue() != null) {
                            Long votes = (Long) snapshot.getValue();
                            if (votes == -1) {
                                mEventItem.setGoVotes(mEventItem.getGoVotes() + 1);
                                mVoteText.setText(mEventItem.getGoVotes() - mEventItem.getNoVotes() + " GoVotes");
                                mDatabase.child("events").child(mEventItem.getId()).child("goVotes").setValue(mEventItem.getGoVotes());
                                mDatabase.child("accounts").child(mUsername).child("voted").child(mEventItem.getId()).setValue(0);
                            } else if (votes == 0) {
                                mEventItem.setGoVotes(mEventItem.getGoVotes() - 1);
                                mVoteText.setText(mEventItem.getGoVotes() - mEventItem.getNoVotes() + " GoVotes");
                                mDatabase.child("events").child(mEventItem.getId()).child("goVotes").setValue(mEventItem.getGoVotes());
                                mDatabase.child("accounts").child(mUsername).child("voted").child(mEventItem.getId()).setValue(-1);
                            } else if (votes == 1) {
                                mEventItem.setGoVotes(mEventItem.getGoVotes() - 2);
                                mVoteText.setText(mEventItem.getGoVotes() - mEventItem.getNoVotes() + " GoVotes");
                                mDatabase.child("events").child(mEventItem.getId()).child("goVotes").setValue(mEventItem.getGoVotes());
                                mDatabase.child("accounts").child(mUsername).child("voted").child(mEventItem.getId()).setValue(-1);
                            }
                        } else {
                            mEventItem.setGoVotes(mEventItem.getGoVotes() - 1);
                            mVoteText.setText(mEventItem.getGoVotes() - mEventItem.getNoVotes() + " GoVotes");
                            mDatabase.child("events").child(mEventItem.getId()).child("goVotes").setValue(mEventItem.getGoVotes());
                            mDatabase.child("accounts").child(mUsername).child("voted").child(mEventItem.getId()).setValue(-1);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });



            }

        });




    }

    public static void setImageViewWithByteArray(ImageView view, byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);


        //ExifInterface exif = new ExifInterface(data);
        //orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

        /*
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        view.setImageBitmap(rotatedBitmap);*/
        view.setImageBitmap(bitmap);
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