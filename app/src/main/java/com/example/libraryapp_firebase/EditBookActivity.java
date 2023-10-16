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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class EditBookActivity extends AppCompatActivity {

    private TextInputEditText bookNameEdt,bookPagesEdt,bookAutorEdt,bookDescEdt,bookImgEdt;
    private Button updateBookBtn, deleteBookBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String bookID;
    private BookRVModal bookRVModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        firebaseDatabase = FirebaseDatabase.getInstance();
        bookNameEdt = findViewById(R.id.idEdtBookName);
        bookPagesEdt = findViewById(R.id.idEdtBookPages);
        bookAutorEdt = findViewById(R.id.idEdtBookAutor);
        bookDescEdt = findViewById(R.id.idEdtBookDescripcion);
        bookImgEdt = findViewById(R.id.idEdtBookImg);
        updateBookBtn = findViewById(R.id.idBtnUpdateBook);
        deleteBookBtn = findViewById(R.id.idBtnDeleteBook);
        loadingPB = findViewById(R.id.idPBLoading);
        bookRVModal = getIntent().getParcelableExtra("book");
        if(bookRVModal!=null){
            bookNameEdt.setText(bookRVModal.getBookName());
            bookPagesEdt.setText(bookRVModal.getBookPages());
            bookAutorEdt.setText(bookRVModal.getBookAutor());
            bookImgEdt.setText(bookRVModal.getBookImg());
            bookID = bookRVModal.getBookID();
        }

        databaseReference = firebaseDatabase.getReference("Books").child(bookID);
        updateBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String bookName = bookNameEdt.getText().toString();
                String bookPages = bookPagesEdt.getText().toString();
                String bookAutor = bookAutorEdt.getText().toString();
                String bookDesc= bookDescEdt.getText().toString();
                String bookImg = bookImgEdt.getText().toString();

                Map<String,Object> map = new HashMap<>();
                map.put("bookName",bookName);
                map.put("bookPages",bookPages);
                map.put("bookAutor",bookAutor);
                map.put("bookDescripcion",bookDesc);
                map.put("bookImg",bookImg);
                map.put("bookID",bookID);


                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.updateChildren(map);
                        Toast.makeText(EditBookActivity.this, "Libro Actualizado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditBookActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditBookActivity.this, "Error al actualizar libro", Toast.LENGTH_SHORT).show();
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