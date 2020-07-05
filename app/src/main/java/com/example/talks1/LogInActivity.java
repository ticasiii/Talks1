package com.example.talks1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LogInActivity extends AppCompatActivity {

    private EditText mEmail,mPassword;
    private Button mLoginBtn;
    private TextView mCreateBtn;
    private ProgressBar progressBar;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener authStateListener; //This could be removed after
                                                            //you make sure login works fine



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.Email);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.createText);


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
                //sendCreateEventRequest();
            }
        });



        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });


    }

    public void loginUser(){

        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            mEmail.setError("Email is Required.");
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

        // authenticate the user

        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LogInActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),SplashLoginActivity.class));
                    finish();
                }else {
                    Toast.makeText(LogInActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }

    public void openRegisterActivity(){

        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
        finish();
    }

    public void sendCreateEventRequest() {
        //URL of the request we are sending
        String url = "http://192.168.43.206:3000/secure/createnewevent";

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "event.getName()");
        params.put("latitude", "1");
        params.put("longitude", "Double.toString(event.getLng())");
        params.put("headspeaker", "event.getHeadSpeaker()");
        params.put("date", "event.composeDateString()");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONPost", response.toString());
                        try {
                            String id = response.getString("_id");
                            //UploadPicture(event.getId());
                        }
                        catch (JSONException e) {
                            Log.e("Json exception",e.toString());
                        }
                        Toast.makeText(LogInActivity.this, "Event has been saved successfully !", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LogInActivity.this, "ERROR JSON POST!", Toast.LENGTH_SHORT).show();

                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        })
        {
            //
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers = new HashMap<>();
                //      Consider using some kind of authentication
                //      String credentials = USERNAME+":"+PASSWORD;
                //      String auth = "Basic "
                //        + Base64.encodeToString(credentials.getBytes(),
                //        Base64.NO_WRAP);
                //       headers.put("Authorization", auth);
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };
        // Adding the request to the queue along with a unique string tag
        ServerRequesterSingleton.getInstance(this).addToRequestQueue(jsonObjReq);

    }
}