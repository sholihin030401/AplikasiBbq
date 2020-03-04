package com.project.ichwan.aplikasibbq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity {

    private ImageView image;
    TextView textView;
    Button button;

    DatabaseReference ref,DataRef;
    StorageReference storRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        image = findViewById(R.id.image_item_view);
        textView = findViewById(R.id.tx_itemview);
        button = findViewById(R.id.btnDelete);
        ref = FirebaseDatabase.getInstance().getReference().child("Activity");
        String ActKey = getIntent().getStringExtra("ActivityKey");
        DataRef = FirebaseDatabase.getInstance().getReference().child("Activity").child(ActKey);
        storRef = FirebaseStorage.getInstance().getReference().child("ActIamge").child(ActKey+".jpg");

        ref.child(ActKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String actName = dataSnapshot.child("ActName").getValue().toString();
                    String imageUrl = dataSnapshot.child("ImageUrl").getValue().toString();

                    Picasso.get().load(imageUrl).into(image);
                    textView.setText(actName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    }
                });
            }
        });
    }
}
