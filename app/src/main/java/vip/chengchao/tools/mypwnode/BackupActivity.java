package vip.chengchao.tools.mypwnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

import vip.chengchao.tools.mypwnode.utils.Backup;
import vip.chengchao.tools.mypwnode.utils.FileUtils;
import vip.chengchao.tools.mypwnode.utils.SDCardReader;

/**
 * Created by chengchao on 16/7/25.
 */
public class BackupActivity extends BaseActivity {

    private static final String TAG = "BackupActivity";
    public static final String ACTION_EXPORT = "export";
    public static final String ACTION_IMPORT = "import";
    private static final String BAK_EXTENSION = "txt";

    public static void startActivity(Context context, String action) {
        Intent intent = new Intent(context, BackupActivity.class);
        intent.setAction(action);
        context.startActivity(intent);
    }

    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        action = getIntent().getAction();
        switchAction();
    }

    protected void switchAction() {
        switch (action) {
            case ACTION_IMPORT:
                FileSelectActivity.openFileSelectorForResult(this, BAK_EXTENSION, 0);
                break;
            case ACTION_EXPORT:
                FileSelectActivity.openDirSelectorForResult(this, 0);
                break;
        }
    }

    protected String getExportFileName() {
        return getApplicationInfo().loadLabel(getPackageManager()).toString() + "_" + System.currentTimeMillis() + "." + BAK_EXTENSION;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            toast("failed");//TODO 改为strings文件
            finish();
            return;
        }
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage(action);
        dialog.show();
        String path = data.getStringExtra(FileSelectActivity.KEY_FILE_PATH);
        boolean result = false;
        Backup backup;
        switch (action) {
            case ACTION_EXPORT:
                backup = Backup.create(path + "/" + getExportFileName(), password, accountStore);
                result = backup.export();
                break;
            case ACTION_IMPORT:
                backup = Backup.create(path, password, accountStore);
                result = backup.import$();
                break;
        }
        dialog.dismiss();
        if (result) {//TODO 改为strings文件
            toast("success");
        } else {
            toast("failed");
        }
        finish();
    }
}
