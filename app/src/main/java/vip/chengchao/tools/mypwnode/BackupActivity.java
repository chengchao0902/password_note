package vip.chengchao.tools.mypwnode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import vip.chengchao.tools.mypwnode.utils.SDCardReader;

/**
 * Created by chengchao on 16/7/25.
 */
public class BackupActivity extends BaseActivity {

    private static final String TAG = "BackupActivity";
    public static final String ACTION_EXPORT = "export";
    public static final String ACTION_IMPORT = "import";
    private static final String BAK_EXTENSION = "pw";

    public static void startActivity(Context context, String action) {
        Intent intent = new Intent(context, BackupActivity.class);
        intent.setAction(action);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        switchAction();
    }

    protected void switchAction() {
        switch (getIntent().getAction()) {
            case ACTION_IMPORT:
                FileSelectActivity.openFileSelectorForResult(this, BAK_EXTENSION, 0);
                importBackup();
                break;
            case ACTION_EXPORT:
                FileSelectActivity.openDirSelectorForResult(this, 0);
                exportBackup();
                break;
        }
        finish();
    }

    protected String selectBy(String action) {
        return null;
    }

    protected void exportBackup() {
        //TODO
    }

    protected void importBackup() {
        //TODO
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO
    }
}
