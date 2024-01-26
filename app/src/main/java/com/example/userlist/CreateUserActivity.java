package com.example.userlist;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.userlist.libs.APIClient;
import com.example.userlist.libs.APIClientTci;
import com.example.userlist.models.PhotoUploadResponse;
import com.example.userlist.models.User;
import com.example.userlist.services.PhotoService;
import com.example.userlist.services.UserService;
import com.example.userlist.utils.RealPathUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserActivity extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showDialog("External storage", context, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                }, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
            return false;
        } else {
            Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                (dialog, which) -> ActivityCompat.requestPermissions((Activity) context,
                        new String[] { permission },
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE));
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        checkPermissionREAD_EXTERNAL_STORAGE(this);

        EditText inputName = findViewById(R.id.inputName);
        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputJob = findViewById(R.id.inputJob);
        ImageView imageAvatar = findViewById(R.id.avatar);
        EditText inputAvatarName = findViewById(R.id.inputAvatarName);
        Button buttonPickPhoto = findViewById(R.id.buttonPickPhoto);
        Button buttonSave = findViewById(R.id.buttonSubmit);
        final Uri[] photoUri = new Uri[1];

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                Log.d("PhotoPicker", "SDK: " + Build.VERSION.SDK_INT);

                imageAvatar.setVisibility(View.VISIBLE);
                Picasso.get().load(uri).into(imageAvatar);
                inputAvatarName.setText(uri.getPath());
                photoUri[0] = uri;
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        buttonPickPhoto.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });


        UserService userService = APIClient.getClient().create(UserService.class);
        PhotoService photoService = APIClientTci.getClient().create(PhotoService.class);
        buttonSave.setOnClickListener(v -> {
            String path = RealPathUtil.getRealPath(getApplicationContext(), photoUri[0]);
            Log.d("Submit", path);
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MultipartBody.FORM, file);
            MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            RequestBody refNumber = RequestBody.create(MultipartBody.FORM, "ref001");
            Call<PhotoUploadResponse> call = photoService.storePhoto(refNumber, photoPart);

            Log.d("Submit", "Start");
            call.enqueue(new Callback<PhotoUploadResponse>() {
                @Override
                public void onResponse(Call<PhotoUploadResponse> call, Response<PhotoUploadResponse> response) {
                    PhotoUploadResponse photoResult = response.body();
                    Toast.makeText(getApplicationContext(), photoResult.noReference + " " + photoResult.uploadStatus + " " + photoResult.photo, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<PhotoUploadResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Request Fail: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });

            /*User user = new User(inputName.getText().toString(), inputJob.getText().toString());
            Call<User> call = userService.createUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    Toast.makeText(getApplicationContext(), user.name + " " + user.job + " " + user.id + " " + user.createdAt, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Request Fail", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });*/
        });
    }
}