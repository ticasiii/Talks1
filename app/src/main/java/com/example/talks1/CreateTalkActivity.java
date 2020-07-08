package com.example.talks1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.talks1.Models.Talk;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateTalkActivity extends AppCompatActivity {

    private final int MAPS_PICKER_ACTIVITY_RESULT = 1;
    private boolean placeSet;
    private boolean dateSet;
    private boolean timeSet;

    ImageView ivPicture;

    EditText etName;
    EditText etDescription;
    EditText etHeadSpeaker;
    EditText etCategory;

    TextView etDate;
    TextView etTime;

    Button bPicture;
    Button bCreate;
    Button bCancel;

    Talk talk;
    String talkID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_talk);

        talk = new Talk();
        placeSet = false;
        dateSet = false;
        timeSet = false;

        ivPicture = findViewById(R.id.activity_create_talk_picture);
        etName = findViewById(R.id.activity_create_title);
        etDescription = findViewById(R.id.activity_create_talk_description);
        etCategory = findViewById(R.id.activity_create_talk_category);
        //etHeadSpeaker = (EditText)findViewById(R.id.activity_create_lecture_headspeaker);

        bPicture = findViewById(R.id.activity_create_lecture_picture_button);
        bCreate = findViewById(R.id.activity_create_talk_create_button);
        bCancel = findViewById(R.id.activity_create_talk_cancel_button);

        etTime = findViewById(R.id.activity_create_talk_time);
        etDate = findViewById(R.id.activity_create_talk_date);

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etName.getText().toString().isEmpty()) {

                    talk.setTitle(etName.getText().toString());
                    if (!etDescription.getText().toString().isEmpty()) {
                        talk.setDescription(etDescription.getText().toString());
                    }
                    talk.setCategory(etCategory.getText().toString());
                    //FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    talk.setSpeaker(FirebaseAuth.getInstance().getUid());

                    if (!placeSet) {
                        showPlacePicker();
                    }
                    if (!dateSet) {
                        showDatePicker();
                    }
                    else {
                        showTimePicker();
                    }
                    FirebaseDatabase.getInstance().getReference("talks").push().setValue(talk, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseReference != null) {

                                talkID = databaseReference.getKey();


                                databaseReference.child("id").setValue(talkID);
                                databaseReference.child("picture").setValue(talkID);


                                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("events").child(talkID);
                                ivPicture.setDrawingCacheEnabled(true);
                                ivPicture.buildDrawingCache();
                                Bitmap bitmap = ivPicture.getDrawingCache();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();

                                UploadTask uploadTask = storageReference.putBytes(data);
                                uploadTask.addOnFailureListener(CreateTalkActivity.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateTalkActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                                Intent intent = new Intent(CreateTalkActivity.this, MainActivity.class);
                                intent.putExtra("talkID", talkID);

                                Toast.makeText(CreateTalkActivity.this, "Event successfully created" + talkID, Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(CreateTalkActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(CreateTalkActivity.this, "Name your event.", Toast.LENGTH_SHORT).show();
                }
                //openTalk();

            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTalkActivity.this.finish();
            }
        });

        bPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showTimePicker();
            }
        });

        etDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDatePicker();
            }
        });
    }

    public void showPlacePicker() {
        Intent intent = new Intent(CreateTalkActivity.this, MapsPickerActivity.class);
        startActivityForResult(intent,MAPS_PICKER_ACTIVITY_RESULT);
    }

    private void openTalk(){
        startActivity(new Intent(CreateTalkActivity.this, SpeakerDetailsActivity.class));

    }

    public void showDatePicker() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                talk.setYear(selectedYear);
                talk.setMonth(selectedMonth + 1); //vraca od 0 do 11
                talk.setDay(selectedDay);


                etDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);

                dateSet = true;
//                showTimePicker();
            }
        };
        DatePickerDialog dpd = new DatePickerDialog(CreateTalkActivity.this, dateOnDateSetListener,now.get(Calendar.YEAR),
                now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    public void showTimePicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                talk.setHour(hourOfDay);
                talk.setMinute(minute);

                etTime.setText(hourOfDay + ":" + minute);


                talk.setPast(false);
                timeSet=true;

                /**FirebaseDatabase.getInstance().getReference("talks").push().setValue(talk, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseReference != null) {

                            databaseReference.child("id").setValue(databaseReference.getKey());
                            databaseReference.child("picture").setValue(databaseReference.getKey());

                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("eventImages").child(databaseReference.getKey());
                            ivPicture.setDrawingCacheEnabled(true);
                            ivPicture.buildDrawingCache();
                            Bitmap bitmap = ivPicture.getDrawingCache();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = storageReference.putBytes(data);
                            uploadTask.addOnFailureListener(CreateTalkActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateTalkActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                            Intent intent = new Intent(CreateTalkActivity.this, MainActivity.class);
                            Toast.makeText(CreateTalkActivity.this, "Event successfully created", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(CreateTalkActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });**/
                //ArrayList<String> attended =  new ArrayList<>();
                //attended.add()
                //event.set
                //At this point event has all fields
                //Send a json request to save a previously created event to the mongo database
                //In order to get id from db
                //sendCreateEventRequest(event);
            }
        };
        TimePickerDialog tmd = new TimePickerDialog(CreateTalkActivity.this,onTimeSetListener,now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
        tmd.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAPS_PICKER_ACTIVITY_RESULT) {
            if(resultCode == RESULT_OK) {
                LatLng selectedPlace = data.getParcelableExtra("selected_place");
                String address = data.getStringExtra("selected_address");

                talk.setLat(selectedPlace.latitude);
                talk.setLng(selectedPlace.longitude);
                talk.setAddress(address);

                placeSet = true;
            }
        }
        else if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = CreateTalkActivity.this.getContentResolver().openInputStream(data.getData());
                ivPicture.setImageBitmap(BitmapFactory.decodeStream(inputStream));

            } catch (FileNotFoundException e) {
                Toast.makeText(CreateTalkActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }



    public static final int PICK_IMAGE = 111;
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void UploadPicture(String eventId) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("events").child(eventId);
        ivPicture.setDrawingCacheEnabled(true);
        ivPicture.buildDrawingCache();
        Bitmap bitmap = ivPicture.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(CreateTalkActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateTalkActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendCreateEventRequest(final Talk talk) {
        //URL of the request we are sending
        String url = "http://192.168.43.228:3000/secure/createnewevent";

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", talk.getTitle());
        params.put("latitude", Double.toString(talk.getLat()));
        params.put("longitude", Double.toString(talk.getLng()));
        params.put("headspeaker", talk.getSpeaker());
        params.put("date", talk.composeDateString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONPost", response.toString());
                        try {
                            String id = response.getString("_id");
                            UploadPicture(talk.getId());
                        }
                        catch (JSONException e) {
                            Log.e("Json exception",e.toString());
                        }
                        Toast.makeText(CreateTalkActivity.this, "Event has been saved successfully !", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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