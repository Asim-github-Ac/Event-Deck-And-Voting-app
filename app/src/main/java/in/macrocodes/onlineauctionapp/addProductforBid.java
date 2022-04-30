package in.macrocodes.onlineauctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.macrocodes.onlineauctionapp.Adapter.AdapterClass;
import in.macrocodes.onlineauctionapp.Adapter.AllImageAdapte;
import in.macrocodes.onlineauctionapp.Models.ImageUpload;
import in.macrocodes.onlineauctionapp.Models.Products;

public class addProductforBid extends AppCompatActivity {

    EditText pName,pDesc,pBid,pDays;
    Button addProduct,addImage;
    String name,desc,bid,days;
    DatabaseReference reference;
    private static final int GALLERY = 1;
    String fileRealPath;
    ImageView allImagesView;
    AllImageAdapte mAdapter;
    Uri mImageUri;

    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;
    String uid;
    List<Uri> images = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_productfor_bid);
        pName = (EditText) findViewById(R.id.productName);
        pDesc = (EditText) findViewById(R.id.description);
        pBid = (EditText) findViewById(R.id.bid);
        pDays = (EditText) findViewById(R.id.days);
        addProduct = (Button) findViewById(R.id.addProduct);
        addImage = (Button) findViewById(R.id.addImage);
        allImagesView = findViewById(R.id.Allimages);



        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();

            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY);
            }
        });


    }

    private void addProduct() {
        name = pName.getText().toString();
        desc = pDesc.getText().toString();
        bid = pBid.getText().toString();
        days = pDays.getText().toString().trim();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String expireDate = null;
        String todayDate = formatter.format(date);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(formatter.parse(todayDate));
            c.add(Calendar.DATE, Integer.parseInt(days));  // number of days to add
            expireDate = formatter.format(c.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        uploadFile(name,desc,"abc123",days,uid,expireDate,mImageUri);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                mImageUri = data.getData();
                Glide.with(this)
                        .load(mImageUri)
                        .centerCrop()
                        .placeholder(R.drawable.default_send_image)
                        .into(allImagesView);
            }
        }
}
    private String getFileExtension(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadFile(String name,String des,String bids,String day,String userid,String date,Uri imgurl) {
        reference = FirebaseDatabase.getInstance().getReference().child("Selected_Canidate");
        String push_id = FirebaseDatabase.getInstance().getReference().child("Products").push().getKey();
        mStorageRef = FirebaseStorage.getInstance().getReference("Candidate");
       // mDatabaseRef = FirebaseDatabase.getInstance().getReference("Products");
        if (mImageUri != null) {
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref =
                    mStorageRef.child("Products" + System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            ref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Products products=new Products(name,des,day,userid,bids,date,"running",uri.toString());
                            ImageUpload upload = new ImageUpload(uri.toString());
                            //adding an upload to firebase database
                            String uploadId = reference.push().getKey();
                            reference.child(name).setValue(products);
                            Intent intent=new Intent(addProductforBid.this,HomeActivity.class);
                                startActivity(intent);
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(addProductforBid.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        } else {

        }

    }
}