package com.oriensolutions.newtechseller;

import static android.R.layout.simple_spinner_item;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oriensolutions.newtechseller.model.Item;
import com.squareup.picasso.Picasso;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();
    private ImageButton imageButton;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private Uri imagePath;
    ListenerRegistration listenerRegistration;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        imageButton = findViewById(R.id.imageButton);

        firestore.collection("Brands").document("name");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                activityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));
            }
        });


        //Brand Spinner
        final List<String> brandlist = new ArrayList<String>();
        final Spinner brandSelecter = (Spinner) findViewById(R.id.product_brand);

        brandlist.add("Select");
        listenerRegistration = firestore.collection("Brands").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    List<DocumentSnapshot> document = value.getDocuments();
                    document.forEach(d -> {
                        brandlist.add(d.getString("name"));

                    });
                }
            }
        });
        ArrayAdapter<String> adpbrand = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, brandlist);
        adpbrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSelecter.setAdapter(adpbrand);

        //Brand Spinner

        //Model Spinner
        final List<String> modellist = new ArrayList<String>();
        final Spinner modelSelecter = (Spinner) findViewById(R.id.product_model);

        modellist.add("Select");
        listenerRegistration = firestore.collection("Models").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    List<DocumentSnapshot> document = value.getDocuments();
                    document.forEach(d -> {
                        modellist.add(d.getString("name"));

                    });
                }
            }
        });
        ArrayAdapter<String> adpmodel = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, modellist);
        adpmodel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSelecter.setAdapter(adpmodel);
        //Model Spinner


        // Color Spinner
        final List<String> colorlist = new ArrayList<String>();
        final Spinner colorSelecter = (Spinner) findViewById(R.id.product_color);

        colorlist.add("Select");
        listenerRegistration = firestore.collection("Colors").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    List<DocumentSnapshot> document = value.getDocuments();
                    document.forEach(d -> {
                        colorlist.add(d.getString("name"));

                    });
                }
            }
        });
        ArrayAdapter<String> adpcolor = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, colorlist);
        adpcolor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSelecter.setAdapter(adpcolor);
        //Color Spinner


        radioGroup = findViewById(R.id.product_condition);


        findViewById(R.id.addbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextprice = findViewById(R.id.product_price);
                EditText editTextname = findViewById(R.id.product_name);
                EditText editTextqty = findViewById(R.id.product_qty);
                EditText editTextdesc = findViewById(R.id.product_description);

                if (brandSelecter.getSelectedItem() == "Select") {
                    Snackbar.make(v, "Please Select Product Brand..", Snackbar.LENGTH_SHORT).show();
                } else if (modelSelecter.getSelectedItem() == "Select") {
                    Snackbar.make(v, "Please Select Product Model..", Snackbar.LENGTH_SHORT).show();
                } else if (colorSelecter.getSelectedItem() == "Select") {
                    Snackbar.make(v, "Please Select Product Color..", Snackbar.LENGTH_SHORT).show();
                } else if (editTextprice.length() == 0) {
                    Snackbar.make(v, "Please Enter a valid Price..", Snackbar.LENGTH_SHORT).show();
                } else if (editTextname.length() == 0) {
                    Snackbar.make(v, "Please Enter product Title..", Snackbar.LENGTH_SHORT).show();
                } else if (editTextqty.length() == 0) {
                    Snackbar.make(v, "Please Enter Quantity..", Snackbar.LENGTH_SHORT).show();
                } else if (imagePath == null) {
                    Snackbar.make(v, "Please Select Image..", Snackbar.LENGTH_SHORT).show();
                } else {


                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    String condition = null;

                    if (selectedRadioButtonId != -1) {
                        selectedRadioButton = findViewById(selectedRadioButtonId);
                        condition = selectedRadioButton.getText().toString();
//                        Snackbar.make(v, condition, Snackbar.LENGTH_SHORT).show();

                        String brand = brandSelecter.getSelectedItem().toString();
                        String model = modelSelecter.getSelectedItem().toString();
                        String color = colorSelecter.getSelectedItem().toString();
                        double price = Double.parseDouble(editTextprice.getText().toString());


                        String name = editTextname.getText().toString();
                        int qty = Integer.parseInt(editTextqty.getText().toString());
                        String desc = editTextdesc.getText().toString();
                        String imageId = UUID.randomUUID().toString();

                        Item item = new Item(brand, model, color, price, condition, name, qty, desc, imageId, imageId);


                        ProgressDialog dialog = new ProgressDialog(AddProductActivity.this);
                        dialog.setMessage("Adding new item....");
                        dialog.setCancelable(false);
                        dialog.show();

                        firestore.collection("Items").document(imageId).set(item)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.setMessage("Uploading Image.....");

                                        StorageReference reference = storage.getReference("item-images")
                                                .child(imageId);

                                        reference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                dialog.dismiss();
                                                Snackbar.make(v, "Successfully Added Product", Snackbar.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.dismiss();
                                                Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                            }
                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                                dialog.setMessage("Uploading " + (int) progress + "%");
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                });

                        brandSelecter.setSelection(0);
                        modelSelecter.setSelection(0);
                        colorSelecter.setSelection(0);
                        editTextprice.setText("");
                        editTextname.setText("");
                        editTextqty.setText("");
                        editTextdesc.setText("");
                        imageButton.setImageBitmap(null);

                    } else {
                        Snackbar.make(v, "Please Select Product Brand..", Snackbar.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }


    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        imagePath = o.getData().getData();

                        Picasso.get()
                                .load(imagePath)
                                .resize(400, 400)
//                                .centerCrop()
                                .centerInside()
                                .into(imageButton);
                    }
                }
            }
    );

}