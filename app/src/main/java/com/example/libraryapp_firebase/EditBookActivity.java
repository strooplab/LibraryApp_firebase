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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class EditBookActivity extends AppCompatActivity {

    private TextInputEditText bookNameEdt,bookPagesEdt,bookAutorEdt,bookDescEdt,bookImgEdt,bookURLEdt;
    private Button updateBookBtn, deleteBookBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String bookID;
    private BookRVModal bookRVModal;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        FirebaseApp.initializeApp(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        bookNameEdt = findViewById(R.id.idEdtBookName);
        bookPagesEdt = findViewById(R.id.idEdtBookPages);
        bookAutorEdt = findViewById(R.id.idEdtBookAutor);
        bookDescEdt = findViewById(R.id.idEdtBookDescripcion);
        bookURLEdt = findViewById(R.id.idEdtBookURL);
        bookImgEdt = findViewById(R.id.idEdtBookImg);
        updateBookBtn = findViewById(R.id.idBtnUpdateBook);
        deleteBookBtn = findViewById(R.id.idBtnDeleteBook);
        loadingPB = findViewById(R.id.idPBLoading);
        bookRVModal = getIntent().getParcelableExtra("book");
        if(bookRVModal!=null){
            bookNameEdt.setText(bookRVModal.getBookName());
            bookPagesEdt.setText(bookRVModal.getBookPages());
            bookAutorEdt.setText(bookRVModal.getBookAutor());
            bookDescEdt.setText(bookRVModal.getBookDesc());
            bookURLEdt.setText(bookRVModal.getBookURL());
            bookImgEdt.setText(bookRVModal.getBookImg());
            bookID = bookRVModal.getBookID();
        }

        databaseReference = firebaseDatabase.getReference("Users").child(uid).child("Books").child(bookID);

        updateBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String bookName = bookNameEdt.getText().toString();
                String bookPages = bookPagesEdt.getText().toString();
                String bookAutor = bookAutorEdt.getText().toString();
                String bookDescripcion= bookDescEdt.getText().toString();
                String bookURL = bookURLEdt.getText().toString();
                String bookImg = bookImgEdt.getText().toString();

                Map<String,Object> map = new HashMap<>();
                map.put("bookName",bookName);
                map.put("bookPages",bookPages);
                map.put("bookAutor",bookAutor);
                map.put("bookDesc",bookDescripcion);
                map.put("bookURL",bookURL);
                map.put("bookImg",bookImg);
                map.put("bookID",bookID);
                map.put("propietario", uid);


                databaseReference.updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, DatabaseReference ref) {
                        loadingPB.setVisibility(View.GONE);
                        if (error == null) {
                            Toast.makeText(EditBookActivity.this, "Libro Actualizado", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditBookActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(EditBookActivity.this, "Error al actualizar libro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        deleteBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBook();
            }
        });
    }
    private void deleteBook(){
        databaseReference.removeValue();
        Toast.makeText(this, "Libro borrado", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditBookActivity.this, MainActivity.class));
    }
}