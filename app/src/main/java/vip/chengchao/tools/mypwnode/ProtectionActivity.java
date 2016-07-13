package vip.chengchao.tools.mypwnode;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import vip.chengchao.tools.mypwnode.cipher.MD5;

/**
 * Created by chengchao on 16/7/10.
 */
public class ProtectionActivity extends BaseActivity {
    public static final String ACTION_CONFIRM = "confirm_password";
    public static final String ACTION_CHANGE = "change_password";
    public static final String ACTION_LOCK = "screen_lock";
    public static final String ACTION_UNLOCK = "screen_unlock";

    /**
     * @param from   from Activity
     * @param action see ProtectionActivity.ACTION_CONFIRM and ProtectionActivity.ACTION_CHANGE
     */
    public static void startActivityForResult(Activity from, String action) {
        Intent intent = new Intent(from, ProtectionActivity.class);
        intent.setAction(action);
        from.startActivityForResult(intent, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_protection);
        action = getIntent().getAction();
        initView();
    }

    private View confirmPasswordView;
    private View changePasswordView;

    private EditText editTextConfirmPassword;
    private EditText editTextChangePassword1;
    private EditText editTextChangePassword2;
    private ImageView imageViewLock;

    private String action;

    private void initView() {
        confirmPasswordView = findViewById(R.id.layout_confirm_password);
        changePasswordView = findViewById(R.id.layout_change_password);
        editTextConfirmPassword = (EditText) findViewById(R.id.edittext_confirm_password);
        editTextChangePassword1 = (EditText) findViewById(R.id.edittext_change_password_1);
        editTextChangePassword2 = (EditText) findViewById(R.id.edittext_change_password_2);
        imageViewLock = (ImageView) findViewById(R.id.image_lock);
        if (TextUtils.isEmpty(md5Password)) {
            confirmPasswordView.setVisibility(View.GONE);
            changePasswordView.setVisibility(View.VISIBLE);
        }
        if (ACTION_LOCK.equals(action)) {
            imageViewLock.setVisibility(View.VISIBLE);
            confirmPasswordView.setVisibility(View.GONE);
            changePasswordView.setVisibility(View.GONE);
        }
    }

    public void confirmPasswordClick(View view) {
        password = editTextConfirmPassword.getText().toString();
        if (!MD5.isEquals(password, md5Password)) {
            toast(R.string.confirm_password_faild);
            return;
        }
        locked = true;
        if (ACTION_CONFIRM.equals(action)) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (ACTION_CHANGE.equals(action)) {
            confirmPasswordView.setVisibility(View.GONE);
            changePasswordView.setVisibility(View.VISIBLE);
        }
        if (ACTION_UNLOCK.equals(action)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void changePasswordClick(View view) {
        String newPassword1 = editTextChangePassword1.getText().toString();
        if (TextUtils.isEmpty(newPassword1)) {
            toast(R.string.new_password_cant_empty);
            return;
        }
        String newPassword2 = editTextChangePassword2.getText().toString();
        if (!newPassword1.equals(newPassword2)) {
            toast(R.string.two_password_not_equal);
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        md5Password = MD5.get(newPassword1);
        editor.putString(PASSWORD_KEY, md5Password);
        editor.commit();
        accountStore.encode(password, newPassword1);
        password = newPassword1;
        setResult(RESULT_OK);
        finish();
        toast(R.string.set_password_success);
    }

    public void unlock(View view) {
        ((ImageView) view).setImageResource(R.drawable.unlocked_512);
        view.setVisibility(View.GONE);
        confirmPasswordView.setVisibility(View.VISIBLE);
        action = ACTION_UNLOCK;
    }
}
