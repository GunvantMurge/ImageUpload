package com.example.admin.imageupload;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.imageupload.camera.ImageChooserUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RCBOOK_IMAGE = 1;
    ImageView UpImage;
    String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView Up = (TextView) findViewById(R.id.upload);
        Up.setOnClickListener(this);
        UpImage = (ImageView) findViewById(R.id.upImage);
    }

    @Override
    public void onClick(View v) {

        Intent chooseFirst = ImageChooserUtility.getPickImageIntent(this);
        startActivityForResult(chooseFirst, RCBOOK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("file ", "onActivityResult   == " + requestCode);
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == RCBOOK_IMAGE) {
                Bitmap bitmap = null;
                try {
                    bitmap = ImageChooserUtility.getImageFromResult(this, resultCode, data);
                    saveBitmapToFile(bitmap, requestCode);
                } catch (Exception e) {
                    e.printStackTrace();
               /*     AlertDialogUtils alertDialogUtils = new AlertDialogUtils();
                    alertDialogUtils.errorAlert(getActivity(), "Camera Application Error", "There seems to be some issue " +
                            "with the Camera Application.\nPlease select the image file from Photo Gallery");*/
                }
            }
        }
    }

    private void saveBitmapToFile(final Bitmap bitmap, final int position) throws Exception {
        Log.d("file ", "saveBitmapToFile   == " + bitmap);

        final String fileName = position + "_" + System.currentTimeMillis() + ".jpeg";

        File file = new File(this.getCacheDir(), fileName);
        file.createNewFile();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();
        UpImage.setImageBitmap(bitmap);
        Log.d("file ", "file   == " + file);

        //  RequestBody requestBody = RequestBody.create(MediaType.parse("**/*//*"), file);
    /*    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        IRetrofitWebservice getResponse = AppConfig.getRetrofit().create(IRetrofitWebservice.class);
        Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, filename);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });*/
    }

}
