package in.macrocodes.onlineauctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import in.macrocodes.onlineauctionapp.Models.AddEventModel;

public class AddEvents extends AppCompatActivity {
    Uri download_url;
    int i=1;
    private DatabaseReference myRef;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    String uid,poster;
    private FirebaseAuth mAuth;
    String user;
    String name;

    // Global Time Values - Returned
    private int stHour;
    private int stMinute;
    private int enHour;
    private int enMinute;
    private int mYear;
    private int mMonth;
    private int mDay;

    // Calendars
    private Calendar calendarFrom;
    private  Calendar calendarTo;
    private int mill;
    ProgressDialog progressDialog;


    // Widgets
    private Spinner catSpinner,timeSpinner;
    private TextView venueView,heldByView;
    private TextView evName;
    private TextView evDesc;
    EditText department,semster;
    private CalendarView calendarView;
    private TimePicker timePickerFrom;
    private TimePicker timePickerTo;

    String selectedDate,imageid,imagelink;

    DatabaseReference db;
    Button btnupload;
    private Button btnChoose, btnUpload;
    private ImageView uploadImage;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            uid=currentUser.getUid();
            poster=currentUser.getDisplayName();
        }


        setCategorySpinner();
        setTimeSpinner();

        evName = (TextView) findViewById(R.id.evName);
        evDesc = (TextView) findViewById(R.id.evDesc);
        heldByView =(TextView) findViewById(R.id.evHeldBy);
        venueView = (TextView) findViewById(R.id.evVenue);
        department=findViewById(R.id.department);
        semster=findViewById(R.id.semster);
        btnupload=findViewById(R.id.createEvent);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Progress");
        progressDialog.setMessage("Loadings......");
        progressDialog.setCancelable(false);
        //Initialize Views
        btnChoose = (Button) findViewById(R.id.chooseImage);
        btnUpload = (Button) findViewById(R.id.uploadImage);
        uploadImage = (ImageView) findViewById(R.id.imageView2);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadEvents();
            }
        });
        // Calendar
        calendarFrom = Calendar.getInstance();
        calendarTo = Calendar.getInstance();

        // Getting Widget By ID
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        timePickerFrom = (TimePicker) findViewById(R.id.timePicker1);
        timePickerTo = (TimePicker) findViewById(R.id.timePicker4);


        // Setting the time pickers to 24 Hour.
        timePickerFrom.setIs24HourView(true);
        timePickerTo.setIs24HourView(true);

        // Listener for Starting Time - FROM
        timePickerFrom.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                stHour= hourOfDay;
                stMinute = minute;

                calendarFrom.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendarFrom.set(Calendar.MINUTE, minute);

                Toast.makeText(getApplicationContext(), stHour+"  "+stMinute, Toast.LENGTH_LONG).show();

            }
        });

        // Listener for Ending Time - TO
        timePickerTo.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                enHour = hourOfDay;
                enMinute = minute;

                calendarTo.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendarTo.set(Calendar.MINUTE, minute);

                Toast.makeText(getApplicationContext(), enHour + "  " + enMinute, Toast.LENGTH_LONG).show();

            }
        });

        // Listener for Date - CALENDAR
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                calendarFrom.set(year, month, dayOfMonth);

                selectedDate = new StringBuilder().append(mMonth + 1)
                        .append("-").append(mDay).append("-").append(mYear)
                        .append(" ").toString();


                Toast.makeText(getApplicationContext(), selectedDate, Toast.LENGTH_LONG).show();
            }
        });


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            imageid="images/"+ UUID.randomUUID().toString();
            Log.d("imagelink",imageid);

            StorageReference ref = storageReference.child(imageid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddEvents.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();

                            while(!urlTask.isSuccessful()){
                            }
                            download_url = urlTask.getResult();
                            imagelink = String.valueOf(download_url);
                            Log.d("imagelink",imagelink);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddEvents.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    public void setCategorySpinner() {
        catSpinner = (Spinner) findViewById(R.id.categories_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
//         Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//         Apply the adapter to the spinner
        catSpinner.setAdapter(adapter);
    }
    public void setTimeSpinner() {
        timeSpinner = (Spinner) findViewById(R.id.time_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time, android.R.layout.simple_spinner_item);
//         Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//         Apply the adapter to the spinner
        timeSpinner.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            uid = currentUser.getUid();
            String email = currentUser.getEmail();
            name = currentUser.getDisplayName();
            Log.d("UserFound"+uid+email+name,""+currentUser);
            Toast.makeText(this,""+uid+email+name,Toast.LENGTH_LONG);
        }
        else{
            Log.d("NoUserFound",""+currentUser);
        }
    }
    public void UploadEvents(){
        progressDialog.show();
        String category = catSpinner.getSelectedItem().toString();
        String venue = venueView.getText().toString();
        String desc = evDesc.getText().toString();
        String name = evName.getText().toString();
        String time = timeSpinner.getSelectedItem().toString();
        String heldBy = heldByView.getText().toString();
        long startTime = calendarFrom.getTimeInMillis();
        long endTime = calendarTo.getTimeInMillis();
        AddEventModel addEventModel=new AddEventModel(getcurrentDateAndTime(),getcurrentDateAndTime().toString(),endTime,name,venue,category,imagelink,time,poster,desc,heldBy,uid,department.getText().toString(),semster.getText().toString());
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TotalEvents").add(addEventModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddEvents.this, "Data SuccessFully Added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MyCampaigns.class));
                progressDialog.cancel();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(AddEvents.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static String getcurrentDateAndTime(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = simpleDateFormat.format(c);
        return formattedDate;
    }

}
