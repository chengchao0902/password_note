package vip.chengchao.tools.mypwnode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import vip.chengchao.tools.mypwnode.cipher.AES;
import vip.chengchao.tools.mypwnode.model.AccountDesc;
import vip.chengchao.tools.mypwnode.store.DBAccountStore;

/**
 * Created by chengchao on 16/7/6.
 */
public class AddActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        initView();
    }


    private EditText editTextType;
    private EditText editTextAccount;
    private EditText editTextPassword;
    private EditText editTextDesc;

    private void initView() {
        editTextType = (EditText) findViewById(R.id.edittext_type);
        editTextAccount = (EditText) findViewById(R.id.edittext_account);
        editTextPassword = (EditText) findViewById(R.id.edittext_password);
        editTextDesc = (EditText) findViewById(R.id.edittext_desc);
    }

    public void okCancelClicked(View view) {
        switch (view.getId()) {
            case R.id.button_ok:
                String type = editTextType.getText().toString();
                if (TextUtils.isEmpty(type)) {
                    toast(R.string.type_cant_empty);
                    return;
                }
                String account = editTextAccount.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    toast(R.string.account_cant_empty);
                    return;
                }
                String password = editTextPassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    toast(R.string.password_cant_empty);
                    return;
                }
                String desc = editTextDesc.getText().toString();
                AccountDesc accountDesc = new AccountDesc(type, account, password, desc);
                if (isOpenPasswordProtection()) {
                    //此处两个password不要搞混淆了,一个是类的,一个Account的
                    accountDesc.setAccount(AES.encrypt(BaseActivity.password, account));
                    accountDesc.setPassword(AES.encrypt(BaseActivity.password, password));
                }
                this.accountStore.save(accountDesc);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            case R.id.button_cancel:
                finish();
                break;
            default:
        }
    }


}
