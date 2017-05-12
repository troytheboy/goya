package com.example.chowi.goya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.defaultValue;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION_CODES.M;
import static java.security.AccessController.getContext;


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

    private Bundle bundle = this.getArguments();

    private URI mImageUri;

    static final int REQUEST_TAKE_PHOTO = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add,
                container, false);


        mTextView = (TextView) view.findViewById(R.id.add_pic);
        mFAB = (FloatingActionButton) view.findViewById(R.id.fab_camera);

        mImageView = (ImageView) view.findViewById(R.id.cam_image);




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
                    //captureCameraImage();
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


    private Uri imageToUploadUri;


    private void dispatchTakePictureIntent() {
        /*
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        */
        //new code
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.chowi.fileprovider",
                        photoFile);
                imageToUploadUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }


    }

    private static final int CAMERA_PHOTO = 111;


    private void captureCameraImage() {
        Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imageToUploadUri = Uri.fromFile(f);
        startActivityForResult(chooserIntent, CAMERA_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            if (imageToUploadUri != null) {
                Uri selectedImage = imageToUploadUri;


                Bitmap imageBitmap = uriToBitmap(selectedImage);

                mBitmap = imageBitmap;
                mImageView.setImageBitmap(imageBitmap);

                mTextView.setVisibility(View.GONE);
                mFAB.setVisibility(View.GONE);

                mImageView.setVisibility(View.VISIBLE);
            }

        }

    }



    private Bitmap uriToBitmap(Uri selectedFileUri) {
        Bitmap image = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContext().getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);


            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContext().getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContext().getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
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
            Uri encodedImage = null;
            if (imageToUploadUri != null) {
                encodedImage = imageToUploadUri;
                imageToUploadUri = null;
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
        public void onPostSelected(String title, String desc, Uri encodedImage);
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

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



}

