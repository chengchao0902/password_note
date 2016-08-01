package vip.chengchao.tools.mypwnode.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by chengchao on 16/8/1.
 */
public final class FileUtils {


    public static final File createFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) return null;
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        }
        try {
            if (file.createNewFile()) {
                return file;
            }
        } catch (IOException e) {
            throw new RuntimeException("错误的地址:" + filePath, e);
        }
        throw new RuntimeException("创建失败:" + filePath);
    }

    public static final File isFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile()) {
            return file;
        }
        throw new RuntimeException("不是文件:" + filePath);
    }


    public static final File createDir(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) return null;
        File file = new File(dirPath);
        if (file.exists() && file.isDirectory()) {
            return file;
        }
        if (file.mkdirs()) {
            return file;
        }
        throw new RuntimeException("创建失败:" + dirPath);
    }

}
