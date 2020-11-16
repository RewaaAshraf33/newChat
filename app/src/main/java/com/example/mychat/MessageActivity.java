package com.example.mychat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mychat.Adapter.MessageAdapter;
import com.example.mychat.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.type.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    ImageView imageView;
    ImageButton btn_send;
    EditText text_send;
    //EditText msg_editText;
    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;
    MessageAdapter adapter;
    ArrayList<Messages> messageList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.MessageViewHolder);
        messageList = new ArrayList<>();
        imageView =findViewById(R.id.imageView);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.send_message_btn);
        text_send = findViewById(R.id.input_message);
        //msg_editText = findViewById(R.id.sender_messsage_text);
        intent=getIntent();



        final String userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send= findViewById(R.id.send_message_btn);

        imageView = findViewById(R.id.imageView);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("MyUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user =dataSnapshot .getValue(Users.class);
                username.setText(user.getUsername());

                if (true)
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

        reference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Messages message = snapshot.getValue(Messages.class);
                if(message != null) {
                    if(message.getFrom() == FirebaseAuth.getInstance().getCurrentUser().getUid()){
                        if(message.getTo() == userid){
                            messageList.add(message);
                            adapter.notifyDataSetChanged();
                        }
                    }else if(message.getTo() ==FirebaseAuth.getInstance().getCurrentUser().getUid()){
                        if(message.getFrom()==userid){
                            messageList.add(message);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    Log.i("Message",message.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Message",error.getMessage());
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
                text_send.setText("");//
            }
        });
        adapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void sendMessage(String sender, String receiver, String MessageID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("sender", sender);
//        hashMap.put("receiver", receiver);
//        hashMap.put("message", MessageID);

        Messages message = new Messages();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setDate(Calendar.getInstance().toString());
        message.setMessage(MessageID);
        reference.child("Chats").setValue(message);
    }

      //private void readMessages(String sender, String receiver, String message){
       //Chat = new ArrayList<>();
       //reference = FirebaseDatabase.getInstance().getReference(ChatActivity);
    //}
}