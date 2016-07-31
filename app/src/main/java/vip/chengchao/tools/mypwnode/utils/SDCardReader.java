package vip.chengchao.tools.mypwnode.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.List;

/**
 * Created by chengchao on 16/7/26.
 * <p/>
 * 必须需要 android.permission.WRITE_EXTERNAL_STORAGE
 * <p/>
 */
public class SDCardReader {

    public static final int TYPE_FILE = 1;
    public static final int TYPE_DIR = 2;

    private String rootPath;
    private File root;
    private File current;
    private File[] children;
    private int type;
    private String extension;

    {
        root = Environment.getExternalStorageDirectory();
        rootPath = root.getAbsolutePath();
        current = root;
    }

    public SDCardReader() {
        this.type = TYPE_FILE;
    }

    public SDCardReader(int type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    public SDCardReader(int type) {
        this.type = type;
    }

    public SDCardReader(String extension) {
        this.type = TYPE_FILE;
        this.extension = extension;
    }

    public File root() {
        current = root;
        return current;
    }

    public File[] list() {
        children = current.listFiles(fileFilter);
        return children;
    }

    public void back() {
        if (isRoot()) {
            return;
        }
        current = current.getParentFile();
    }

    public File select(int index) {
        if (children == null || children.length == 0) return null;
        if (index < 0 || index > children.length) return null;
        current = children[index];
        children = current.listFiles(fileFilter);
        return current;
    }

    public File current() {
        return current;
    }

    public String getReplacedPath(String replacement) {
        return current.getAbsolutePath().replace(rootPath, replacement);
    }

    public boolean isRoot() {
        return current.getAbsolutePath().equals(root.getAbsolutePath());
    }


    public String rootPath() {
        return rootPath;
    }

    private FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            String name = file.getName();
            switch (type) {
                case TYPE_FILE:
                    return TextUtils.isEmpty(extension) || file.isDirectory() || name.endsWith(extension);
                case TYPE_DIR:
                    return file.isDirectory();
                default:
                    return true;
            }
        }

    };

}
