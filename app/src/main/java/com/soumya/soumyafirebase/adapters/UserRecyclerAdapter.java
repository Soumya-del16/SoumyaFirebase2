package com.soumya.soumyafirebase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soumya.soumyafirebase.R;
import com.soumya.soumyafirebase.models.firebasemodels.SampleUserData;

import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.MyUserHolderView> {

    private Context context;
    private List<SampleUserData> list;


    public UserRecyclerAdapter(Context context, List<SampleUserData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserRecyclerAdapter.MyUserHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_data_list,parent,false);

        return new MyUserHolderView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerAdapter.MyUserHolderView holder, int position) {
        SampleUserData person = list.get(position);

        // Bind data to the TextViews
        holder.nameTextView.setText("Name: " + person.getName());
        holder.ageTextView.setText("Age: " + person.getAge());
        holder.emailTextView.setText("Email: " + person.getEmail());
        holder.cityTextView.setText("City: " + person.getCity());
        holder.dateTextView.setText("UID: " + person.getUid());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyUserHolderView extends RecyclerView.ViewHolder {
        TextView nameTextView, ageTextView, emailTextView, cityTextView, dateTextView;

        public MyUserHolderView(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            ageTextView = itemView.findViewById(R.id.textViewAge);
            emailTextView = itemView.findViewById(R.id.textViewEmail);
            cityTextView = itemView.findViewById(R.id.textViewCity);
            dateTextView = itemView.findViewById(R.id.textViewDate);
        }
    }
    public void setData(List<SampleUserData> userList) {
        this.list = userList;
    }

}
