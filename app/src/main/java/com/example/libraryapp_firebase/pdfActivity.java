package com.example.libraryapp_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

public class pdfActivity extends AppCompatActivity {

    private PDFView pdfView;
    private String pdfUriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfView = findViewById(R.id.pdfView);
        pdfUriString = getIntent().getStringExtra("pdfFilePath");

        pdfView.fromUri(Uri.parse(pdfUriString))
                .defaultPage(0)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        Toast.makeText(pdfActivity.this, "Pdf cargado correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

}