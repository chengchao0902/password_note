package vip.chengchao.tools.mypwnode.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vip.chengchao.tools.mypwnode.AccountActivity;
import vip.chengchao.tools.mypwnode.R;
import vip.chengchao.tools.mypwnode.model.AccountDesc;
import vip.chengchao.tools.mypwnode.store.DBAccountStore;

/**
 * Created by chengchao on 16/7/1.
 */
public class AccountListAdapter extends ArrayAdapter<AccountDesc> {

    private static final int resource = R.layout.list_account_item;
    private DBAccountStore accountStore;

    public AccountListAdapter(DBAccountStore accountStore, Context context, List<AccountDesc> objects) {
        super(context, resource, objects);
        this.accountStore = accountStore;
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
            holder.viewDelete = layout.findViewById(R.id.view_delete_account);
            holder.viewEdit = layout.findViewById(R.id.view_edit);
            holder.viewDelete.setVisibility(View.VISIBLE);
            holder.viewEdit.setVisibility(View.VISIBLE);
            ItemOperatorListener listener = new ItemOperatorListener(holder);
            holder.viewDelete.setOnClickListener(listener);
            holder.viewEdit.setOnClickListener(listener);
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
        holder.position = position;
        if (TextUtils.isEmpty(accountDesc.getDesc())) {
            holder.layoutDescParent.setVisibility(View.GONE);
        } else {
            holder.layoutDescParent.setVisibility(View.VISIBLE);
        }
        return layout;
    }

    private static class ViewHolder {
        int position;
        LinearLayout layoutDescParent;
        TextView textViewAccountType;
        TextView textViewAccountName;
        TextView textViewPassword;
        TextView textViewDesc;
        View copyAccount;
        View copyPassword;
        View copyDesc;
        View viewDelete;
        View viewEdit;

    }

    class ItemOperatorListener implements View.OnClickListener {

        private ViewHolder viewHolder;

        public ItemOperatorListener(ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View view) {
            if (viewHolder == null) return;
            final AccountDesc accountDesc = getItem(viewHolder.position);
            switch (view.getId()) {
                case R.id.view_delete_account:
                    new AlertDialog.Builder(getContext()).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int count = accountStore.del(accountDesc.getId());
                            if (count == 1) {
                                remove(getItem(viewHolder.position));
                                notifyDataSetChanged();
                            }
                            dialogInterface.dismiss();
                        }
                    }).setTitle(R.string.warn).setMessage(R.string.delete_warn).show();
                    break;
                case R.id.view_edit:
                    AccountActivity.startActivityForEdit(getContext(), getItem(viewHolder.position));
                    break;
                default:
            }

        }
    }
}
