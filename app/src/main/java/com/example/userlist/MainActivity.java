package com.example.userlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userlist.adapters.UserAdapter;
import com.example.userlist.libs.APIClient;
import com.example.userlist.models.PaginatedResource;
import com.example.userlist.models.User;
import com.example.userlist.services.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    UserService userService;
    private ProgressBar progressBar;
    private RecyclerView userList;
    private UserAdapter userAdapter;
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userList = findViewById(R.id.userListView);
        progressBar = findViewById(R.id.progressbar);

        Button buttonCreate = findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateUserActivity.class);
            startActivity(intent);
        });

        userAdapter = new UserAdapter(users);
        userList.setAdapter(userAdapter);
        userList.setLayoutManager(new LinearLayoutManager(this));

        userService = APIClient.getClient().create(UserService.class);
        fetchUserList();
    }

    private void fetchUserList() {
        Call<PaginatedResource<User>> call = userService.getUserList("1");
        call.enqueue(new Callback<PaginatedResource<User>>() {
            @Override
            public void onResponse(Call<PaginatedResource<User>> call, Response<PaginatedResource<User>> response) {
                Log.d("AppUser","Response code: " + response.code());

                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);

                    PaginatedResource<User> resource = response.body();
                    Integer page = resource.page;
                    Integer perPage = resource.perPage;
                    Integer total = resource.total;
                    Integer totalPages = resource.totalPages;

                    int lastIndex = userAdapter.getItemCount();
                    users.addAll(resource.data);
                    userAdapter.notifyItemInserted(lastIndex);

                    /*String displayResponse = "";
                    displayResponse += text + " page\n" + total + " Total\n" + totalPages + " Total Pages\n";
                    for (User user: users) {
                        displayResponse += user.id + " " + user.firstName + " " + user.lastName + " " + user.email + "\n";
                    }
                    responseText.setText(displayResponse);*/
                } else if (response.errorBody() != null) {
                    Log.d("ApiCall", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<PaginatedResource<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Request Fail", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }
}