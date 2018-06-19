package com.iti.jets.lostchildren.image;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.iti.jets.lostchildren.R;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Shemo on 6/19/2018.
 */

public class ImageSelection {

    static final int CAMERA_PIC_REQUEST = 1111;
    private static final int SELECTED_PICTURE = 1;

    public static void ShowCustomDialog(final Context context, ViewGroup root, final ImageView imgView) {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle("Edit Photo");
//        ViewGroup root = (ViewGroup) ((AppCompatActivity)context).findViewById (R.id.rel);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.image_custom_dialog, root, false);

        ImageButton imBgallary = (ImageButton) v.findViewById(R.id.imBGallary);
        imBgallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ((AppCompatActivity)context).startActivityForResult(i, SELECTED_PICTURE);
                dialog.cancel();

            }
        });

        ImageButton imBCamera = (ImageButton) v.findViewById(R.id.imBGamera);
        imBCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                ((AppCompatActivity)context).startActivityForResult(camera_intent, CAMERA_PIC_REQUEST);
                dialog.cancel();
            }
        });
        ImageButton imBremove = (ImageButton) v.findViewById(R.id.imbRemove);
        imBremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgView.setImageDrawable(null);
                dialog.cancel();
            }
        });

        dialog.setContentView(v);
        dialog.show();


    }

    public static void customOnActivityResult(int arg0, int arg1, Intent arg2, Context context, ImageView imgView) {

        switch (arg0) {
            case SELECTED_PICTURE:
                if (arg1 == RESULT_OK) {

                    Uri uri = arg2.getData();

                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = context.getContentResolver().query(uri, projection,
                            null, null, null);
                    cursor.moveToFirst();

                    int ColumnIndex = cursor.getColumnIndex(projection[0]);
                    String filepath = cursor.getString(ColumnIndex);
                    cursor.close();
                    Bitmap yourselectedimage = BitmapFactory.decodeFile(filepath);
                    Drawable d = new BitmapDrawable(yourselectedimage);
                    imgView.setImageDrawable(d);

                }

                break;
            case CAMERA_PIC_REQUEST:

                if (arg1 == RESULT_OK) {
                    Bitmap thumbnail = (Bitmap) arg2.getExtras().get("data");
                    Drawable d = new BitmapDrawable(thumbnail);
                    imgView.setImageDrawable(d);
                }

            default:
                break;
        }
    }
}
