    package com.example.libraryapp_firebase;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ProgressBar;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.android.material.textfield.TextInputEditText;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    public class AddBookActivity extends AppCompatActivity {

        private TextInputEditText bookNameEdt, bookPagesEdt, bookAutorEdt, bookDescEdt,bookURLEdt,bookImgEdt;
        private Button addBookBtn;
        private ProgressBar loadingPB;
        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference databaseReference;
        private String bookID, uid;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_book);
            bookNameEdt = findViewById(R.id.idEdtBookName);
            bookPagesEdt = findViewById(R.id.idEdtBookPages);
            bookAutorEdt = findViewById(R.id.idEdtBookAutor);
            bookDescEdt = findViewById(R.id.idEdtBookdesc);
            bookURLEdt = findViewById(R.id.idEdtBookURL);
            bookImgEdt = findViewById(R.id.idEdtBookImg);
            addBookBtn = findViewById(R.id.idBtnAddBook);
            loadingPB = findViewById(R.id.idPBLoading);
            firebaseDatabase = FirebaseDatabase.getInstance();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                uid = user.getUid();

                DatabaseReference userBooksRef = firebaseDatabase.getReference("Users").child(uid).child("Books");
                userBooksRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            addBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String bookName = bookNameEdt.getText().toString();
                    String bookPages = bookPagesEdt.getText().toString();
                    String bookAutor = bookAutorEdt.getText().toString();
                    String bookURL = bookURLEdt.getText().toString();
                    String bookDesc = bookDescEdt.getText().toString();
                    String bookImg = bookImgEdt.getText().toString();
                    if (TextUtils.isEmpty(bookImg)){
                        Toast.makeText(AddBookActivity.this, "Sin enlace de imagen", Toast.LENGTH_SHORT).show();
                    }else{
                        bookID = bookName;
                        BookRVModal bookRVModal = new BookRVModal(bookName, bookAutor, bookPages, bookDesc, bookURL ,bookImg, bookID, uid);

                        databaseReference.child(bookID).setValue(bookRVModal)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        loadingPB.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            // El libro se agregó correctamente
                                            Toast.makeText(AddBookActivity.this, "Libro Añadido", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AddBookActivity.this, MainActivity.class));
                                        } else {
                                            // Hubo un error al agregar el libro
                                            Toast.makeText(AddBookActivity.this, "Error al agregar el libro", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                }
            }
        });
    }
}
