package com.example.mychat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.mychat.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    ImageView imageView;
    ImageButton btn_send;
    EditText text_send;
    EditText msg_editText;
    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        imageView =findViewById(R.id.imageView);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.send_message_btn);
        //msg_editText = findViewById(R.id.sender_messsage_text);

        intent=getIntent();



        final String userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send= findViewById(R.id.send_message_btn);

        imageView = findViewById(R.id.imageView);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user =dataSnapshot .getValue(Users.class);
                username.setText(user.getUsername());

                if (user.getImageURL().equals("default"))
                {
                    imageView.setImageResource(R.mipmap.ic_launcher);

                }else {

                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(imageView);
                 }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(), userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");

            }
        });
    }

    private void sendMessage(String sender, String receiver, String MessageID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", MessageID);

        reference.child("Chats").push().setValue(hashMap);
    }

      //private void readMessages(String sender, String receiver, String message){
       //Chat = new ArrayList<>();
       //reference = FirebaseDatabase.getInstance().getReference(ChatActivity);
    //}
}