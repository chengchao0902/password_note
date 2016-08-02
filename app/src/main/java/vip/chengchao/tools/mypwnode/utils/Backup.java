package vip.chengchao.tools.mypwnode.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import vip.chengchao.tools.mypwnode.cipher.AES;
import vip.chengchao.tools.mypwnode.model.AccountDesc;
import vip.chengchao.tools.mypwnode.store.AccountDescStore;

/**
 * Created by chengchao on 16/8/1.
 * 主要做导入导出格式适配
 */
public class Backup {

    private static final String TAG = "Backup";
    private static final String LINE_SEPARATOR = "#################################";

    public static Backup create(String path, String password, AccountDescStore store) {
        return new Backup(path, password, store);
    }

    private File file;
    private AccountDescStore store;
    private String password;

    private Backup(String path, String password, AccountDescStore store) {
        this.file = FileUtils.createFile(path);
        this.store = store;
        this.password = password;
    }

    public boolean export() {
        List<AccountDesc> accountDescs = store.listAll();
        StringBuilder builder = new StringBuilder();
        for (AccountDesc accountDesc : accountDescs) {
            accountDesc.setAccount(AES.decrypt(password, accountDesc.getAccount()));
            accountDesc.setPassword(AES.decrypt(password, accountDesc.getPassword()));
            builder.append("type:").append(accountDesc.getType()).append("\n");
            builder.append("account:").append(accountDesc.getAccount()).append("\n");
            builder.append("password:").append(accountDesc.getPassword()).append("\n");
            builder.append("desc:").append(accountDesc.getDesc()).append("\n");
            builder.append(LINE_SEPARATOR).append("\n");
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(builder.toString().getBytes());
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                //ignore
            }
        }
        return true;
    }


    public boolean import$() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            String line;
            AccountDesc accountDesc = null;
            int filedCount = 0;
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                if (LINE_SEPARATOR.equals(line) && accountDesc != null) {
                    if (filedCount == 4) {
                        store.save(accountDesc);
                    } else {
                        Log.w(TAG, accountDesc.toString());
                    }
                    filedCount = 0;
                    continue;
                }
                if (line.startsWith("type:")) {
                    filedCount++;
                    accountDesc = new AccountDesc();
                    accountDesc.setType(line.replace("type:", ""));
                } else if (line.startsWith("account:")) {
                    filedCount++;
                    accountDesc.setAccount(line.replace("account:", ""));
                } else if (line.startsWith("password:")) {
                    filedCount++;
                    accountDesc.setPassword(line.replace("password:", ""));
                } else if (line.startsWith("desc:")) {
                    filedCount++;
                    accountDesc.setDesc(line.replace("desc:", ""));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }
        return true;
    }

}
