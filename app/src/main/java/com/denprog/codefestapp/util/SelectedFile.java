package com.denprog.codefestapp.util;

import android.net.Uri;

public class SelectedFile {
    public Uri uri;
    public String fileName;

    public SelectedFile(Uri uri, String fileName) {
        this.uri = uri;
        this.fileName = fileName;
    }
}
