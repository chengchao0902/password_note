package vip.chengchao.tools.mypwnode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import vip.chengchao.tools.mypwnode.adapter.AccountListAdapter;
import vip.chengchao.tools.mypwnode.cipher.AES;
import vip.chengchao.tools.mypwnode.menu.MoveTouchMenu;
import vip.chengchao.tools.mypwnode.model.AccountDesc;


public class MainActivity extends BaseActivity implements AdapterView.OnItemLongClickListener {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAccount(BaseActivity.password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            toast(R.string.access_permission_denied);
            finish();
        }

        loadAccount(password);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ListView listViewAccount;
    private AccountListAdapter listAdapter;
    private List<AccountDesc> accountDescs;

    private void initView() {
        listViewAccount = (ListView) findViewById(R.id.listview_account);
    }

    private void loadAccount(String password) {
        accountDescs = accountStore.listAll();
        if (!TextUtils.isEmpty(password)) {
            for (AccountDesc accountDesc : accountDescs) {
                accountDesc.setAccount(AES.decrypt(password, accountDesc.getAccount()));
                accountDesc.setPassword(AES.decrypt(password, accountDesc.getPassword()));
            }
        }
        listAdapter = new AccountListAdapter(accountStore, this, accountDescs);
        listViewAccount.setAdapter(listAdapter);
        listViewAccount.setOnItemLongClickListener(this);
    }

    public void copyAccount(View view) {
        int position = (int) view.getTag();
        AccountDesc desc = accountDescs.get(position);
        copyToClipboard(desc.getAccount());
        toast(R.string.copy_account_success);
    }

    public void copyPassword(View view) {
        int position = (int) view.getTag();
        AccountDesc desc = accountDescs.get(position);
        copyToClipboard(desc.getPassword());
        toast(R.string.copy_password_success);
    }

    public void copyDesc(View view) {
        int position = (int) view.getTag();
        AccountDesc desc = accountDescs.get(position);
        copyToClipboard(desc.getDesc());
        toast(R.string.copy_desc_success);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "On Item Long Click");
        view = listAdapter.getView(i, null, null);
        Button closeButton = (Button) view.findViewById(R.id.button_close_item);
        closeButton.setVisibility(View.VISIBLE);
        final MoveTouchMenu moveTouchMenu = new MoveTouchMenu(this).setTouchView(view);
        moveTouchMenu.show();
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTouchMenu.destroy();
            }
        });
        return false;
    }
}
