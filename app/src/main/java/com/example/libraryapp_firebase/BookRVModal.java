package com.example.libraryapp_firebase;

import android.os.Parcel;
import android.os.Parcelable;

public class BookRVModal implements Parcelable {
    private String bookName;
    private String bookAutor;
    private String bookPages;
    private String bookDesc;
    private String bookURL;
    private String bookImg;
    private String bookID;
    private String uid;

    public BookRVModal(){

    }

    protected BookRVModal(Parcel in) {
        bookName = in.readString();
        bookAutor = in.readString();
        bookPages = in.readString();
        bookDesc = in.readString();
        bookURL = in.readString();
        bookImg = in.readString();
        bookID = in.readString();
    }

    public static final Creator<BookRVModal> CREATOR = new Creator<BookRVModal>() {
        @Override
        public BookRVModal createFromParcel(Parcel in) {
            return new BookRVModal(in);
        }

        @Override
        public BookRVModal[] newArray(int size) {
            return new BookRVModal[size];
        }
    };

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAutor() {
        return bookAutor;
    }

    public void setBookAutor(String bookAutor) {
        this.bookAutor = bookAutor;
    }

    public String getBookPages() {
        return bookPages;
    }

    public void setBookPages(String bookPages) {
        this.bookPages = bookPages;
    }

    public String getBookDesc(){
        return bookDesc;
    }

    public void setBookDesc(String bookDesc){
        this.bookDesc = bookDesc;
    }

    public String getBookURL(){
        return bookURL;
    }

    public void setBookURL(String bookURL){
        this.bookURL = bookURL;
    }

    public String getBookImg() {
        return bookImg;
    }

    public void setBookImg(String bookImg) {
        this.bookImg = bookImg;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public BookRVModal(String bookName, String bookAutor, String bookPages, String bookDesc,String bookURL, String bookImg, String bookID, String uid) {
        this.bookName = bookName;
        this.bookAutor = bookAutor;
        this.bookPages = bookPages;
        this.bookDesc=bookDesc;
        this.bookURL=bookURL;
        this.bookImg = bookImg;
        this.bookID = bookID;
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookName);
        dest.writeString(bookAutor);
        dest.writeString(bookPages);
        dest.writeString(bookDesc);
        dest.writeString(bookURL);
        dest.writeString(bookImg);
        dest.writeString(bookID);
    }
}
