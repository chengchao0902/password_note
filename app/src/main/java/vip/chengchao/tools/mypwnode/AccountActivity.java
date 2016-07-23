package vip.chengchao.tools.mypwnode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import vip.chengchao.tools.mypwnode.cipher.AES;
import vip.chengchao.tools.mypwnode.model.AccountDesc;

/**
 * Created by chengchao on 16/7/6.
 */
public class AccountActivity extends BaseActivity {

    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DESC = "desc";

    public static void startActivityForEdit(Context activity, AccountDesc accountDesc) {
        Intent intent = new Intent(activity, AccountActivity.class);
        intent.putExtra(KEY_ID, accountDesc.getId());
        intent.putExtra(KEY_TYPE, accountDesc.getType());
        intent.putExtra(KEY_NAME, accountDesc.getAccount());
        intent.putExtra(KEY_PASSWORD, accountDesc.getPassword());
        intent.putExtra(KEY_DESC, accountDesc.getDesc());
        activity.startActivity(intent);
    }

    public static void startActivityForAdd(Context activity) {
        Intent intent = new Intent(activity, AccountActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        initView();
        Intent intent = getIntent();
        id = intent.getIntExtra(KEY_ID, 0);
        if (id == 0) return;
        editTextType.setText(intent.getStringExtra(KEY_TYPE));
        editTextAccount.setText(intent.getStringExtra(KEY_NAME));
        editTextPassword.setText(intent.getStringExtra(KEY_PASSWORD));
        editTextDesc.setText(intent.getStringExtra(KEY_DESC));
    }

    private int id;
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
                if (id == 0) {
                    this.accountStore.save(accountDesc);
                } else {
                    accountDesc.setId(id);
                    this.accountStore.update(accountDesc);
                }
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            case R.id.button_cancel:
                finish();
                break;
            default:
        }
    }


}
