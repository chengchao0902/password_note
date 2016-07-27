package vip.chengchao.tools.mypwnode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import vip.chengchao.tools.mypwnode.utils.SDCardReader;

/**
 * Created by chengchao on 16/7/28.
 */
public class FileSelectActivity extends Activity {

    private static final String ACTION_SELECT_DIR = "dir";
    private static final String ACTION_SELECT_FILE = "file";
    private static final String KEY_FILE_EXT = "ext";

    public static void openFileSelectorForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, FileSelectActivity.class);
        intent.setAction(ACTION_SELECT_FILE);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openDirSelectorForResult(Activity activity, String extension, int requestCode) {
        Intent intent = new Intent(activity, FileSelectActivity.class);
        intent.setAction(ACTION_SELECT_DIR);
        intent.putExtra(KEY_FILE_EXT, extension);
        activity.startActivityForResult(intent, requestCode);
    }


    private String action;
    private String extension;
    private SDCardReader sdCardReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        action = intent.getAction();
        extension = intent.getStringExtra(KEY_FILE_EXT);
        sdCardReader = new SDCardReader(mapAction(), extension);
    }

    public void back(View view) {
        if (sdCardReader.isRoot()) {
            //TODO 改到string文件里面去
            Toast.makeText(this, "is root", Toast.LENGTH_SHORT).show();
            return;
        }
        sdCardReader.back();
    }

    public void selectedBack(View view) {
        //TODO
    }

    public void forceClose(View view) {
        action = null;
        extension = null;
        sdCardReader = null;
        finish();
    }

    private int mapAction() {
        switch (action) {
            case ACTION_SELECT_DIR:
                return SDCardReader.TYPE_DIR;
            case ACTION_SELECT_FILE:
                return SDCardReader.TYPE_FILE;
            default:
                return SDCardReader.TYPE_FILE;
        }
    }
}
