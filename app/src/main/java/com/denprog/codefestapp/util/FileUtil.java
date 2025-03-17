package com.denprog.codefestapp.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    private static final String REGISTER_FILES_LOC = "";
    public static final String APPLICATION_FILE_LOC = "applications";
    public static final String APPLICATION_FILES_DOWNLOAD_LOC_EXTERNAL = "ApplicationFiles";

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

    public static String insertUriToInternalDirectory(String fileName, String folder, String employeeId, Uri uri, Context context) throws RuntimeException{
        ContentResolver contentResolver = context.getContentResolver();
        File folderObj = new File(context.getFilesDir(), folder);
        folderObj.mkdir();
        File employeeFolder = new File(folderObj, employeeId);
        employeeFolder.mkdir();
        File actualFile = new File(employeeFolder, fileName);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            FileOutputStream fos = new FileOutputStream(actualFile);

            if (inputStream == null) {
                throw new FileNotFoundException("Uri Not Found");
            }

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            inputStream.close();
            fos.close();

            return folder + File.separator + employeeId + File.separator + actualFile.getName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void transFileToPublicDownloadFolderFromInternalStorage(String internalStorageFilePath, int employeeId, Context context) {
        File actualFile = new File(context.getFilesDir(), internalStorageFilePath);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, stripFilePath(internalStorageFilePath));
        contentValues.put(MediaStore.Files.FileColumns.MIME_TYPE, MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension(internalStorageFilePath)));
        contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS +
                        File.separator +
                        APPLICATION_FILES_DOWNLOAD_LOC_EXTERNAL +
                        File.separator +
                        employeeId);

        Uri externalUri = MediaStore.Files.getContentUri("external");
        ContentResolver contentResolver = context.getContentResolver();
        Uri fileUri = contentResolver.insert(externalUri, contentValues);

        if (fileUri == null) {
            throw new RuntimeException("File Not Downloaded");
        }

        try (OutputStream outputStream = contentResolver.openOutputStream(fileUri)) {
            FileInputStream fis = new FileInputStream(actualFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static String getExtension(String displayName) {
        String[] split = displayName.split("\\.");
        int indexOfLastSegment = split.length - 1;
        if (indexOfLastSegment >= 0) {
            return split[indexOfLastSegment];
        } else {
            return null;
        }
    }

    public static String stripFilePath(String filePath) {
        String[] segments = filePath.split("/");
        if (segments.length > 1) {
            return segments[segments.length - 1];
        } else {
            return filePath;
        }
    }


}
