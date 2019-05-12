package com.ansaridroid.csacademy.util.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseDBUtility {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;


    public FirebaseDBUtility() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

    }



}
