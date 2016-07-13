package vip.chengchao.tools.mypwnode.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import vip.chengchao.tools.mypwnode.R;
import vip.chengchao.tools.mypwnode.model.AccountDesc;

/**
 * Created by chengchao on 16/7/1.
 */
public class AccountListAdapter extends ArrayAdapter<AccountDesc> {

    private static final int resource = R.layout.list_account_item;

    public AccountListAdapter(Context context, List<AccountDesc> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        ViewHolder holder = null;
        if (layout == null) {
            layout = LayoutInflater.from(getContext()).inflate(resource, null);
            holder = new ViewHolder();
            holder.textViewAccountType = (TextView) layout.findViewById(R.id.textview_account_type);
            holder.textViewAccountName = (TextView) layout.findViewById(R.id.textview_account);
            holder.textViewPassword = (TextView) layout.findViewById(R.id.textview_password);
            holder.textViewDesc = (TextView) layout.findViewById(R.id.textview_desc);
            holder.copyAccount = layout.findViewById(R.id.view_copy_account);
            holder.copyPassword = layout.findViewById(R.id.view_copy_password);
            holder.copyDesc = layout.findViewById(R.id.view_copy_desc);
            holder.layoutDescParent = (LinearLayout) layout.findViewById(R.id.layout_desc_parent);
            layout.setTag(holder);
        } else {
            holder = (ViewHolder) layout.getTag();
        }
        AccountDesc accountDesc = getItem(position);
        holder.textViewAccountType.setText(accountDesc.getType());
        holder.textViewAccountName.setText(accountDesc.getAccount());
        holder.textViewPassword.setText(accountDesc.getPassword());
        holder.textViewDesc.setText(accountDesc.getDesc());
        holder.copyAccount.setTag(position);
        holder.copyPassword.setTag(position);
        holder.copyDesc.setTag(position);
        if (TextUtils.isEmpty(accountDesc.getDesc())) {
            holder.layoutDescParent.setVisibility(View.GONE);
        }
        return layout;
    }

    private static class ViewHolder {
        LinearLayout layoutDescParent;
        TextView textViewAccountType;
        TextView textViewAccountName;
        TextView textViewPassword;
        TextView textViewDesc;
        View copyAccount;
        View copyPassword;
        View copyDesc;
    }
}
