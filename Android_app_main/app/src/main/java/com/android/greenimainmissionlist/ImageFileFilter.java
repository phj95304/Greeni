package com.android.greenimainmissionlist;

import java.io.File;
import java.io.FilenameFilter;

public class ImageFileFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String filename) {
        if (filename.endsWith(".jpg"))
            return true;
        else if (filename.endsWith(".JPG"))
            return true;
        else if (filename.endsWith(".png"))
            return true;
        else if (filename.endsWith(".PNG"))
            return true;
        else
            return false;
    }
}
