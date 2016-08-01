package vip.chengchao.tools.mypwnode.utils;

import java.io.File;

/**
 * Created by chengchao on 16/8/1.
 * 主要做导入导出格式适配
 */
public class Backup {
    private static final int EXPORT = 0;
    private static final int IMPORT = 1;

    public static Backup createExport(String path) {
        return new Backup(EXPORT, path);
    }

    public static Backup createImport(String path) {
        return new Backup(IMPORT, path);
    }

    private File file;
    private int action;


    private Backup(int action, String path) {
        this.action = action;
        this.file = FileUtils.createFile(path);
    }

    public boolean run() {
        //TODO
        //TODO
        return true;
    }

}
