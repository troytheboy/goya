package com.example.chowi.goya;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.chowi.goya.EventDetailFragment.setImageViewWithByteArray;

/**
 * Created by chowi on 5/25/2017.
 */

public class MyEventItemListAdapter extends ArrayAdapter<EventItem> {

    private final Context context;
    private final EventItem[] values;
    private StorageReference mStorageRef;

    public MyEventItemListAdapter(Context context, EventItem[] values) {
        super(context, R.layout.fragment_eventitem, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listItem = inflater.inflate(R.layout.fragment_eventitem, parent, false);
        final ImageView imageView = (ImageView) listItem.findViewById(R.id.itemImage);
        TextView titleText = (TextView) listItem.findViewById(R.id.itemTitle);
        TextView authorText = (TextView) listItem.findViewById(R.id.author);
        TextView voteText = (TextView) listItem.findViewById(R.id.text_votes);

        titleText.setText(values[position].getTitle());
        authorText.setText(values[position].getUsername());
        voteText.setText("" + (values[position].getGoVotes() - values[position].getNoVotes()));

        if (values[position].getImage() != null) {
            mStorageRef = FirebaseStorage.getInstance().getReference();

            StorageReference currRef = mStorageRef.child(values[position].getImage());

            final long ONE_MEGABYTE = 1024 * 1024;
            currRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.i("found in the database", "displaying it now");
                    // Data for "images/island.jpg" is returns, use this as needed

                    setImageView(imageView, bytes);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.i("did not find in db", "error");
                }
            });
        }





        return listItem;
    }

    public static void setImageView(ImageView view, byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
        view.setImageBitmap(bitmap);
    }
}
