package com.example.libraryapp_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookRVAdapter.BookClickInterface {

    private RecyclerView bookRV;
    private ProgressBar loadingPB;
    private FloatingActionButton addFAB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<BookRVModal> bookRVModalArrayList;
    RelativeLayout idContenedor;
    private BookRVAdapter bookRVAdapter;
    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bookRV = findViewById(R.id.idRVBooks);
        loadingPB = findViewById(R.id.idPBLoading);
        addFAB = findViewById(R.id.idAddFAB);
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            databaseReference = firebaseDatabase.getReference("Users").child(uid).child("Books");
        } else {
            Toast.makeText(this, "El usuario no ha iniciado sesion", Toast.LENGTH_SHORT).show();
        }
        bookRVModalArrayList = new ArrayList<>();
        LayoutInflater inflater = getLayoutInflater();
        View dialogInteriorView = inflater.inflate(R.layout.dialogo_inferior, null);
        idContenedor = dialogInteriorView.findViewById(R.id.idRLBSheet);
        mAuth = FirebaseAuth.getInstance();
        bookRVAdapter = new BookRVAdapter(bookRVModalArrayList,this,this);
        bookRV.setLayoutManager(new LinearLayoutManager(this));
        bookRV.setAdapter(bookRVAdapter);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddBookActivity.class));
            }
        });

        getAllBooks();

    }

    private void getAllBooks(){
        bookRVModalArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                bookRVModalArrayList.add(snapshot.getValue(BookRVModal.class));
                bookRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                bookRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                loadingPB.setVisibility(View.GONE);
                bookRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                bookRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBookClick(int position) {
        displayDialogoInferior(bookRVModalArrayList.get(position));

    }

    private void displayDialogoInferior(BookRVModal bookRVModal){
        final BottomSheetDialog Dialogointerior = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.dialogo_inferior,null);
        Dialogointerior.setContentView(layout);
        Dialogointerior.setCancelable(false);
        Dialogointerior.setCanceledOnTouchOutside(true);
        Dialogointerior.show();

        TextView bookNameTV =layout.findViewById(R.id.idTVBookName);
        TextView bookAutorTV =layout.findViewById(R.id.idTVAutor);
        TextView bookDescTV =layout.findViewById(R.id.idTVDescripcion);
        TextView bookpaginasTV =layout.findViewById(R.id.idTVpaginas);
        ImageView bookImage = layout.findViewById(R.id.idImageBookName);
        Button editBTN = layout.findViewById(R.id.EdtBookBtn);
        Button URLBtn= layout.findViewById(R.id.URLBookBtn);

        bookNameTV.setText(bookRVModal.getBookName());
        bookAutorTV.setText(bookRVModal.getBookAutor());
        bookDescTV.setText(bookRVModal.getBookDesc());
        bookpaginasTV.setText(bookRVModal.getBookPages());
        if (!TextUtils.isEmpty(bookRVModal.getBookImg())) {
            Picasso.get().load(bookRVModal.getBookImg()).into(bookImage);
        } else {
            bookImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                intent.putExtra("book", bookRVModal);
                startActivity(intent);

            }
        });

        URLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(bookRVModal.getBookURL()));
                if(intent!=null){
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Link inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        int id = item.getItemId();
        if (id == R.id.idLogout) {
            Toast.makeText(this, "Sesión cerrada con éxito", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }

            return super.onOptionsItemSelected(item);


    }

}