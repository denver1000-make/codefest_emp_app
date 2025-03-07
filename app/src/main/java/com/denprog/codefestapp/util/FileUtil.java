package com.denprog.codefestapp.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;

public class FileUtil {
    private static final String REGISTER_FILES_LOC = "";

    public static String getFilesExtension(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        String mimeType = contentResolver.getType(uri);
        if (mimeType != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeType);
        }
        return null;
    }

    public static String getFileName (Context context, Uri uri) {
        String name = null;
        String scheme = uri.getScheme();
        if (scheme != null && scheme.equals("file")) {
            name = uri.getLastPathSegment();

        } else {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                        if (nameIndex != -1) {
                            name = cursor.getString(nameIndex);
                        } else {
                            Log.d("Error", "Error Fetching FileName");
                        }
                    }
                }
            }
        }
        return name;
    }

    public static void saveRegistrationFile(Context context, Uri uri) {
        String uriFileName = getFileName(context, uri);
        File savePath = new File(context.getFilesDir(), uriFileName);
    }

    public static String pathExtract(String displayName) {
        String[] split = displayName.split("\\.");
        int indexOfLastSegment = split.length - 1;
        if (indexOfLastSegment != -1) {
            return split[indexOfLastSegment];
        } else {
            return null;
        }
    }


}
