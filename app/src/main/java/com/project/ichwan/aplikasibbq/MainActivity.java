package com.project.ichwan.aplikasibbq;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 102;
    private ImageView imageAdd;
    private EditText inputName;
    private TextView txProgress;
    private ProgressBar progress;
    Uri imageUri;
    boolean isImageAdded = false;

    DatabaseReference dataRef;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageAdd = findViewById(R.id.imageViewAdd);
        inputName = findViewById(R.id.inputImageName);
        txProgress = findViewById(R.id.tvProgress);
        progress = findViewById(R.id.progressBar);
        Button uploadBtn = findViewById(R.id.btnUpload);

        txProgress.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);

        dataRef = FirebaseDatabase.getInstance().getReference().child("Activity");
        storageRef = FirebaseStorage.getInstance().getReference().child("ActImage");

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String imageName = inputName.getText().toString();
                if (isImageAdded && imageName!=null){
                    uploadImage(imageName);
                }
            }
        });
    }

    private void uploadImage(final String imageName){
        txProgress.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);

        final String key = dataRef.push().getKey();
        storageRef.child(key+".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("ActName",imageName);
                        hashMap.put("ImageUrl",uri.toString());

                        dataRef.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                //Toast.makeText(MainActivity.this,"Data Sukses Diupload",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
               double progressUpload = (taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
               progress.setProgress((int)progressUpload);
               txProgress.setText(progressUpload+" %");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null){
            imageUri = data.getData();
            isImageAdded = true;
            imageAdd.setImageURI(imageUri);
        }
    }
}


