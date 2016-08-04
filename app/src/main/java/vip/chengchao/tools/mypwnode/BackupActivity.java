package vip.chengchao.tools.mypwnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import vip.chengchao.tools.mypwnode.utils.Backup;


/**
 * Created by chengchao on 16/7/25.
 */
public class BackupActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

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
    private EditText editBackupPassword;
    private RadioButton cipherRadio;
    private RadioButton clearRadio;
    private TextView textBackupTitle;
    private String backupPassword;
    private boolean isNeedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        editBackupPassword = (EditText) findViewById(R.id.edit_backup_password);
        textBackupTitle = (TextView) findViewById(R.id.text_backup_title);
        cipherRadio = (RadioButton) findViewById(R.id.radio_cipher);
        clearRadio = (RadioButton) findViewById(R.id.radio_clear);
        cipherRadio.setOnCheckedChangeListener(this);
        clearRadio.setOnCheckedChangeListener(this);
        action = getIntent().getAction();
        textBackupTitle.setText(action);
        cipherRadio.setChecked(true);
    }

    protected void startAction() {
        switch (action) {
            case ACTION_IMPORT:
                FileSelectActivity.openFileSelectorForResult(this, BAK_EXTENSION, 0);
                break;
            case ACTION_EXPORT:
                FileSelectActivity.openDirSelectorForResult(this, 0);
                break;
        }
    }

    public void backupOk(View view) {
        backupPassword = editBackupPassword.getText().toString();
        if (isNeedPassword && TextUtils.isEmpty(backupPassword)) {
            toast(R.string.password_cant_empty);
            return;
        }
        startAction();
    }

    protected String getExportFileName() {
        return getApplicationInfo().loadLabel(getPackageManager()).toString() + "_" + System.currentTimeMillis() + "." + BAK_EXTENSION;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            toast(R.string.failed);
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
                backup = Backup.create(path + "/" + getExportFileName(), backupPassword, accountStore);
                result = backup.export();
                break;
            case ACTION_IMPORT:
                backup = Backup.create(path, backupPassword, accountStore);
                result = backup.import$();
                break;
        }
        dialog.dismiss();
        if (result) {
            toast(R.string.success);
        } else {
            toast(R.string.failed);
        }
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (action) {
            case ACTION_EXPORT:
                if (compoundButton.getId() == R.id.radio_clear && b) {
                    isNeedPassword = true;
                    editBackupPassword.setVisibility(View.VISIBLE);
                } else if (compoundButton.getId() == R.id.radio_clear) {
                    isNeedPassword = false;
                    editBackupPassword.setVisibility(View.GONE);
                } else {
                    isNeedPassword = false;
                }
                break;
            case ACTION_IMPORT:
                if (compoundButton.getId() == R.id.radio_cipher && b) {
                    isNeedPassword = true;
                    editBackupPassword.setVisibility(View.VISIBLE);
                } else if (compoundButton.getId() == R.id.radio_cipher) {
                    isNeedPassword = false;
                    editBackupPassword.setVisibility(View.GONE);
                } else {
                    isNeedPassword = false;
                }
                break;
        }
    }
}
