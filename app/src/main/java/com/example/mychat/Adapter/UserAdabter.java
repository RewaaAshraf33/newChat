package com.example.mychat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mychat.MessageActivity;
import com.example.mychat.Model.Users;
import com.example.mychat.R;

import java.util.List;

public class UserAdabter extends RecyclerView.Adapter <UserAdabter.ViewHolder>{

private Context context;
private List<Users>mUsers;


//constrector


    public UserAdabter(Context context, List<Users> mUsers) {
        this.context = context;
        this.mUsers = mUsers;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);



        return new UserAdabter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Users users = mUsers.get(position);
        holder.username.setText(users.getUsername());
        //////////////////////وقفت هناااااااااااااااااااا
if (users.getImageURL().equals("default"))
{

holder.imageView.setImageResource(R.mipmap.ic_launcher);

}
else {
    //adding glide library
    Glide.with(context).load(users.getImageURL()).into(holder.imageView);

          }
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(context, MessageActivity.class);
        i.putExtra("userid", users.getId());
        context.startActivity(i);

    }
});




    }




    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    // شرح
public class ViewHolder extends RecyclerView.ViewHolder{
           public TextView username;
           public ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.textView30);
            imageView=itemView.findViewById(R.id.imageView);





        }
    }






}
