package vip.chengchao.tools.mypwnode.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by chengchao on 16/7/26.
 * <p/>
 * 必须需要 android.permission.WRITE_EXTERNAL_STORAGE
 *
 * TODO 完善
 */
public class SDCardReader {

    public static final int TYPE_FILE = 1;
    public static final int TYPE_DIR = 2;
    private static final String ROOT_NAME = "sdcard";
    private File root;
    private File current;

    private int type;
    private String extension;

    {
        root = Environment.getExternalStorageDirectory();
        current = root;
    }

    public SDCardReader() {
        this.type = TYPE_FILE;
    }

    public SDCardReader(int type) {
        this.type = type;
    }

    public SDCardReader(String extension) {
        this.type = TYPE_FILE;
        this.extension = TextUtils.isEmpty(extension) ? null : extension;
    }

    public String root() {
        current = root;
        return ROOT_NAME;
    }

    public String[] list() {
        return current.list();
    }

    public void back() {
        current = current.getParentFile();
    }

    public File select() {
        return current;
    }

}
