package vip.chengchao.tools.mypwnode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;

/**
 * Created by chengchao on 16/7/9.
 */
public class SettingActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private Switch switchHoverMenu;
    private Switch switchPasswordProtection;

    private void initView() {
        switchHoverMenu = (Switch) findViewById(R.id.switch_show_hover_menu);
        switchPasswordProtection = (Switch) findViewById(R.id.switch_open_password_protection);
        switchHoverMenu.setChecked(isShowHoverMenu());
        switchPasswordProtection.setChecked(isOpenPasswordProtection());
    }

    public void switchClicked(View view) {
        switch (view.getId()) {
            case R.id.switch_show_hover_menu:
                setShowHoverMenu(isShowHoverMenu() ? false : true);
                break;
            case R.id.switch_open_password_protection:
                if (isOpenPasswordProtection()) {
                    ProtectionActivity.startActivityForResult(this, ProtectionActivity.ACTION_CONFIRM);
                    return;
                }
                if (TextUtils.isEmpty(md5Password)) {
                    ProtectionActivity.startActivityForResult(this, ProtectionActivity.ACTION_CHANGE);
                } else {
                    setOpenPasswordProtection(true);
                }
                break;
        }
    }

    public void changePassword(View view) {
        ProtectionActivity.startActivityForResult(this, ProtectionActivity.ACTION_CHANGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setOpenPasswordProtection(isOpenPasswordProtection() ? false : true);
        }
        switchPasswordProtection.setChecked(isOpenPasswordProtection());
        super.onActivityResult(requestCode, resultCode, data);
    }
}
