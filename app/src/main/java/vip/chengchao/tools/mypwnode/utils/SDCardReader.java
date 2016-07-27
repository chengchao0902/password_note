package vip.chengchao.tools.mypwnode.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by chengchao on 16/7/26.
 * <p/>
 * 必须需要 android.permission.WRITE_EXTERNAL_STORAGE
 * <p/>
 * TODO 完善
 */
public class SDCardReader {

    public static final int TYPE_FILE = 1;
    public static final int TYPE_DIR = 2;
    private static final String ROOT_NAME = "sdcard";

    private String rootPath;
    private File root;
    private File current;

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

    public String root() {
        current = root;
        return ROOT_NAME;
    }

    public String[] list() {
        return current.list(filenameFilter);
    }

    public void back() {
        if (isRoot()) {
            return;
        }
        current = current.getParentFile();
    }

    public File select() {
        return current;
    }

    public boolean isRoot() {
        return current.getAbsolutePath().equals(root.getAbsolutePath());
    }

    /**
     * @return 将原sdcard的绝对路径替换为sdcard, 如果要获取文件绝对路径,调用
     * @see #absolutePath()
     */
    public String path() {
        return absolutePath().replace(rootPath, ROOT_NAME);
    }

    public String absolutePath() {
        return current.getAbsolutePath();
    }

    private FilenameFilter filenameFilter = new FilenameFilter() {
        @Override
        public boolean accept(File file, String name) {
            switch (type) {
                case TYPE_FILE:
                    return TextUtils.isEmpty(extension) || name.endsWith(extension);
                case TYPE_DIR:
                    return file.isDirectory();
                default:
                    return true;
            }

        }
    };

}
