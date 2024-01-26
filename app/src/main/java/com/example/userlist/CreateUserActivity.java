package com.example.userlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.userlist.libs.APIClient;
import com.example.userlist.models.User;
import com.example.userlist.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        EditText inputName = findViewById(R.id.inputName);
        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputJob = findViewById(R.id.inputJob);
        Button buttonSave = findViewById(R.id.buttonSubmit);

        UserService userService = APIClient.getClient().create(UserService.class);
        buttonSave.setOnClickListener(v -> {
            User user = new User(inputName.getText().toString(), inputJob.getText().toString());
            Call<User> call = userService.createUser(user);
            call.enqueue(new Callback<User>() {

                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    Toast.makeText(getApplicationContext(), user.name + " " + user.job + " " + user.id + " " + user.createdAt, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        });
    }
}