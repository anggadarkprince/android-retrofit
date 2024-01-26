package com.example.userlist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userlist.R;
import com.example.userlist.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.viewName.setText(user.firstName + " " + user.lastName);
        holder.viewEmail.setText(user.email);
        Picasso.get().load(user.avatar).into(holder.imageAvatar);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView viewName;
        public final TextView viewEmail;
        public final ImageView imageAvatar;

        public ViewHolder(View view) {
            super(view);
            viewName = view.findViewById(R.id.name);
            viewEmail = view.findViewById(R.id.email);
            imageAvatar = view.findViewById(R.id.avatar);
        }
    }

}
