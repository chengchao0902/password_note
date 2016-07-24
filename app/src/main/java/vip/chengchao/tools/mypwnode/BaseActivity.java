package vip.chengchao.tools.mypwnode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vip.chengchao.tools.mypwnode.menu.MoveTouchMenu;
import vip.chengchao.tools.mypwnode.store.DBAccountStore;

/**
 * Created by chengchao on 16/7/6.
 */
public class BaseActivity extends Activity {
    public static final String PASSWORD_KEY = "password";
    private static final String KEY_SHOW_HOVER_MENU = "show_hover_menu";

    private static List<Activity> activities;

    protected static DBAccountStore accountStore;

    protected static ClipboardManager clipboardManager;
    protected static MoveTouchMenu moveTouchMenu;
    protected static View menuView;
    private boolean openPasswordProtection = true;
    protected static SharedPreferences sharedPreferences;
    protected static String md5Password;
    protected static String password;
    protected static ImageView lockImageView;
    protected static boolean locked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initField();
        Log.i(this.getClass().getSimpleName(), "onCreate");
        if (!(this instanceof ProtectionActivity)) {
            if (isOpenPasswordProtection() && TextUtils.isEmpty(md5Password)) {
                ProtectionActivity.startActivityForResult(this, ProtectionActivity.ACTION_CHANGE);
                finish();
            }
            if (isOpenPasswordProtection() && !TextUtils.isEmpty(md5Password) && TextUtils.isEmpty(BaseActivity.password)) {
                ProtectionActivity.startActivityForResult(this, ProtectionActivity.ACTION_CONFIRM);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(this.getClass().getSimpleName(), "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(getClass().getSimpleName(), "onStart");
    }

    protected void initField() {
        synchronized (BaseActivity.class) {
            accountStore = accountStore == null ? new DBAccountStore(this) : accountStore;
            clipboardManager = clipboardManager == null ? (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE) : clipboardManager;
            sharedPreferences = sharedPreferences == null ? getSharedPreferences("setting", Context.MODE_PRIVATE) : sharedPreferences;
            activities = activities == null ? new ArrayList<Activity>() : activities;
            openPasswordProtection = sharedPreferences.getBoolean("open_password_protection", openPasswordProtection);
            if (moveTouchMenu == null || moveTouchMenu.isDestroyed()) {
                menuView = LayoutInflater.from(this).inflate(R.layout.menu_view, null);
                moveTouchMenu = new MoveTouchMenu(this)
                        .setTouchView(R.layout.menu_flag)
                        .addMenuView(menuView).setBottom(500).setRight(200);
                moveTouchMenu.show();

            }
            md5Password = sharedPreferences.getString(PASSWORD_KEY, null);
            locked = TextUtils.isEmpty(password);
            lockImageView = (ImageView) menuView.findViewById(R.id.image_lock_menu);
            setLockImageViewVisible();
            changeLockImageView();
            activities.add(this);
        }
    }

    public void setLockImageViewVisible() {
        if (openPasswordProtection) {
            lockImageView.setVisibility(View.VISIBLE);
        } else {
            lockImageView.setVisibility(View.GONE);
        }
    }

    public void changeLockImageView() {
        lockImageView.setImageResource(locked ? R.drawable.locked_96 : R.drawable.unlocked_96);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locked = TextUtils.isEmpty(password);
        changeLockImageView();
    }

    public void onMenuClick(View view) {
        switch (view.getId()) {
            case R.id.image_lock_menu:
                finishAll();
                if (locked) {
                    ProtectionActivity.startActivityForResult(this, ProtectionActivity.ACTION_UNLOCK);
                } else {
                    password = null;
                    ProtectionActivity.startActivityForResult(this, ProtectionActivity.ACTION_LOCK);
                }
                closeMenu();
                break;
            case R.id.textview_main:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                closeMenu();
                break;
            case R.id.textview_add:
                AccountActivity.startActivityForAdd(this);
                closeMenu();
                break;
            case R.id.textview_hidden:
                closeMenu();
                break;
            case R.id.textview_exit:
                closeMenu();
                finishAll();
                moveTouchMenu.destroy();
                break;
            case R.id.textview_setting:
                intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                closeMenu();
            default:
        }
    }

    protected void closeMenu() {
        moveTouchMenu.removeMenuView();
        finish();
    }

    protected void finishAll() {
        for (Activity activity : activities) {
            activity.finish();
        }
        activities.clear();
    }

    @Override
    protected void onDestroy() {
        activities.remove(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("open_password_protection", openPasswordProtection);
        editor.commit();
        super.onPause();
    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void toast(@StringRes int resouce) {
        Toast.makeText(this, resouce, Toast.LENGTH_SHORT).show();
    }

    protected void copyToClipboard(CharSequence content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }

        ClipData clipData = ClipData.newPlainText(content, content);
        clipboardManager.setPrimaryClip(clipData);
    }

    protected CharSequence pasteFromClipboard() {
        return clipboardManager.getPrimaryClip().getItemAt(0).getText();
    }

    public boolean isOpenPasswordProtection() {
        return openPasswordProtection;
    }

    protected void setOpenPasswordProtection(boolean openPasswordProtection) {
        this.openPasswordProtection = openPasswordProtection;
    }
}
