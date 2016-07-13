package vip.chengchao.tools.mypwnode.store;

import java.util.List;

import vip.chengchao.tools.mypwnode.model.AccountDesc;

/**
 * Created by chengchao on 16/6/30.
 */
public interface AccountDescStore {

    void encode(String oldPassword, String newPassword);

    void decode(String password);

    void save(AccountDesc account);

    void update(AccountDesc accountDesc);

    void del(String type, String account);

    List<AccountDesc> find(String type);

    List<AccountDesc> listAll();

    AccountDesc find(String type, String account);

}
