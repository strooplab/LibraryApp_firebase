package com.example.libraryapp_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddBookActivity extends AppCompatActivity {

    private TextInputEditText bookNameEdt,bookPagesEdt,bookAutorEdt,bookDescEdt,bookImgEdt;
    private Button addBookBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String bookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        bookNameEdt = findViewById(R.id.idEdtBookName);
        bookPagesEdt = findViewById(R.id.idEdtBookPages);
        bookAutorEdt = findViewById(R.id.idEdtBookAutor);
        bookDescEdt = findViewById(R.id.idEdtBookdesc);
        bookImgEdt = findViewById(R.id.idEdtBookImg);
        addBookBtn = findViewById(R.id.idBtnAddBook);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Books");

        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String bookName = bookNameEdt.getText().toString();
                String bookPages = bookPagesEdt.getText().toString();
                String bookAutor = bookAutorEdt.getText().toString();
                String bookDesc= bookDescEdt.getText().toString();
                String bookImg = bookImgEdt.getText().toString();
                bookID = bookName;
                BookRVModal bookRVModal = new BookRVModal(bookName,bookAutor,bookPages,bookDesc,bookImg,bookID);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.child(bookID).setValue(bookRVModal);
                        Toast.makeText(AddBookActivity.this, "Libro Añadido", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddBookActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddBookActivity.this, "Error is "+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}