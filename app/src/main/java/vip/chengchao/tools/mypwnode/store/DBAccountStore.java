package vip.chengchao.tools.mypwnode.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import vip.chengchao.tools.mypwnode.cipher.AES;
import vip.chengchao.tools.mypwnode.model.AccountDesc;
import vip.chengchao.tools.mypwnode.utils.DateUtil;

/**
 * Created by chengchao on 16/6/30.
 */
public class DBAccountStore implements AccountDescStore {

    public static final String DB_NAME = "my_pw_node_db";
    public static final String TABLE_NAME = "account";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_ACCOUNT = "account";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_CREATE_TIME = "create_time";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DBAccountStore(Context context) {
        dbHelper = new DBHelper(context, DB_NAME);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void encode(String oldPassword, String newPassword) {
        if (TextUtils.isEmpty(newPassword))
            return;
        List<AccountDesc> accountDescs = listAll();
        if (!TextUtils.isEmpty(oldPassword)) {
            for (AccountDesc accountDesc : accountDescs) {
                accountDesc.setAccount(AES.decrypt(oldPassword, accountDesc.getAccount()));
                accountDesc.setPassword(AES.decrypt(oldPassword, accountDesc.getPassword()));
            }
        }
        for (AccountDesc accountDesc : accountDescs) {
            accountDesc.setAccount(AES.encrypt(newPassword, accountDesc.getAccount()));
            accountDesc.setPassword(AES.encrypt(newPassword, accountDesc.getPassword()));
            update(accountDesc);
        }

    }

    @Override
    public void decode(String password) {
        if (TextUtils.isEmpty(password))
            return;
        List<AccountDesc> accountDescs = listAll();
        for (AccountDesc accountDesc : accountDescs) {
            accountDesc.setAccount(AES.decrypt(password, accountDesc.getAccount()));
            accountDesc.setPassword(AES.decrypt(password, accountDesc.getPassword()));
            update(accountDesc);
        }
    }

    @Override
    public void save(AccountDesc account) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, account.getType());
        values.put(COLUMN_ACCOUNT, account.getAccount());
        values.put(COLUMN_PASSWORD, account.getPassword());
        values.put(COLUMN_DESC, account.getDesc());
        values.put(COLUMN_CREATE_TIME, DateUtil.formatDate(account.getCreateTime()));
        values.put(COLUMN_UPDATE_TIME, DateUtil.formatDate(account.getUpdateTime()));
        database.insert(TABLE_NAME, null, values);
    }

    @Override
    public void update(AccountDesc account) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, account.getType());
        values.put(COLUMN_ACCOUNT, account.getAccount());
        values.put(COLUMN_PASSWORD, account.getPassword());
        values.put(COLUMN_DESC, account.getDesc());
        values.put(COLUMN_CREATE_TIME, DateUtil.formatDate(account.getCreateTime()));
        values.put(COLUMN_UPDATE_TIME, DateUtil.formatDate(account.getUpdateTime()));
        database.update(TABLE_NAME, values, "id=?", new String[]{Integer.toString(account.getId())});
    }

    @Override
    public void del(String type, String account) {
        database.delete(TABLE_NAME, COLUMN_TYPE + "=? and " + COLUMN_ACCOUNT + "=?", new String[]{type, account});
    }

    @Override
    public List<AccountDesc> find(String type) {
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_TYPE + "=?", new String[]{type}, null, null, null);
        return parse(cursor);
    }

    @Override
    public List<AccountDesc> listAll() {
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        return parse(cursor);
    }

    @Override
    public AccountDesc find(String type, String account) {
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_TYPE + "=? and " + COLUMN_ACCOUNT + "=?", new String[]{type, account}, null, null, null);
        List<AccountDesc> accountDescs = parse(cursor);
        if (accountDescs.size() > 0) {
            return accountDescs.get(0);
        }
        return null;
    }

    private List<AccountDesc> parse(Cursor cursor) {
        List<AccountDesc> accountDescs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                AccountDesc desc = new AccountDesc();
                desc.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                desc.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                desc.setAccount(cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT)));
                desc.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                desc.setDesc(cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
                desc.setCreateTime(DateUtil.parseDate(cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_TIME))));
                desc.setUpdateTime(DateUtil.parseDate(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_TIME))));
                accountDescs.add(desc);
            } while (cursor.moveToNext());
        }
        return accountDescs;
    }
}
