package com.example.talks1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talks1.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    //public static final String TAG = "TAG";
    private EditText mFullName,mEmail,mPassword,mDescription,mUsername;
    private Button mRegisterBtn;
    private TextView mLoginBtn;

    private ImageView ivPicture;
    private Button bPicture;

    FirebaseAuth fAuth;
    ProgressBar progressBar;
    //FirebaseFirestore fStore;
    // String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFullName   = findViewById(R.id.fullName);
        mEmail      = findViewById(R.id.Email);
        mPassword   = findViewById(R.id.password);
        mDescription      = findViewById(R.id.description);
        mRegisterBtn= findViewById(R.id.registerBtn);
        mLoginBtn   = findViewById(R.id.createText);
        mUsername   = findViewById(R.id.username);

        fAuth = FirebaseAuth.getInstance();
        // fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

  /**      if(fAuth.getCurrentUser() != null){
            Toast.makeText(RegisterActivity.this, fAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(),SplashLoginActivity.class));
            finish();
        }**/


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddNewUser(mUsername.getText().toString().trim(), mEmail.getText().toString().trim(), mPassword.getText().toString());

            }
        });



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(getApplicationContext(),LogInActivity.class));
                OpenLogin();
            }
        });

    }

    public void OpenLogin() {
        Intent login = new Intent(RegisterActivity.this,LogInActivity.class);
        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(login);
    }

    public void AddNewUser(String username, String email, String password){

        //final String email = mEmail.getText().toString().trim();
        //String password = mPassword.getText().toString().trim();
        //final String fullName = mFullName.getText().toString();
        //final String phone    = mPhone.getText().toString();

        if(TextUtils.isEmpty(email)){
            mEmail.setError("Email is Required.");
            return;
        }

        if(TextUtils.isEmpty(username)){
            mUsername.setError("Username is Required.");
            return;
        }

        if(TextUtils.isEmpty(password)){
            mPassword.setError("Password is Required.");
            return;
        }

        if(password.length() < 6){
            mPassword.setError("Password Must have at least 6 Characters");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // register the user in firebase

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();

                    final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                    User user = new User();
                    user.setId(fAuth.getCurrentUser().getUid());
                    user.setName(mFullName.getText().toString());
                    user.setEmail(fAuth.getCurrentUser().getEmail().trim());
                    user.setInfo(mDescription.getText().toString().trim());
                    user.setUsername(mUsername.getText().toString().trim());

                    if (ivPicture.getDrawable() != null) {
                        user.setPicture(fAuth.getCurrentUser().getUid());
                        //if there is a picture upload it to the firebase storage
                        UploadPicture(user.getId());
                    }
                    usersRef.child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if(databaseError!=null) {
                                Toast.makeText(RegisterActivity.this, databaseError.toString(),Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(RegisterActivity.this, "Logged in Successfully: USER:" + fAuth.getCurrentUser().getEmail().trim(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),SplashLoginActivity.class));
                            finish();
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Error ! Auth FAIL" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void UploadPicture(String userId) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child(userId);
        ivPicture.setDrawingCacheEnabled(true);
        ivPicture.buildDrawingCache();
        Bitmap bitmap = ivPicture.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(RegisterActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = RegisterActivity.this.getContentResolver().openInputStream(data.getData());
                ivPicture.setImageBitmap(BitmapFactory.decodeStream(inputStream));

            } catch (FileNotFoundException e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static final int PICK_IMAGE = 111;
    public void PickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }
}

