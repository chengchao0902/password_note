package vip.chengchao.tools.mypwnode.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vip.chengchao.tools.mypwnode.R;
import vip.chengchao.tools.mypwnode.model.FileDesc;

/**
 * Created by chengchao on 16/7/28.
 */
public class FileSelectorAdaptor extends ArrayAdapter<FileDesc> {
    private static final int RES_ID = R.layout.file_list_item;

    public FileSelectorAdaptor(Context context, List<FileDesc> objects) {
        super(context, RES_ID, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = convertView;
        ViewHolder holder = new ViewHolder();
        if (root == null) {
            root = LayoutInflater.from(getContext()).inflate(RES_ID, null);
            holder.imageViewIcon = (ImageView) root.findViewById(R.id.imageview_file_icon);
            holder.textViewPathName = (TextView) root.findViewById(R.id.textview_file_path);
            root.setTag(holder);
        } else {
            holder = (ViewHolder) root.getTag();
        }
        FileDesc fileDesc = getItem(position);
        holder.imageViewIcon.setImageResource(fileDesc.getType());
        holder.textViewPathName.setText(fileDesc.getPathName());
        return root;
    }

    class ViewHolder {
        ImageView imageViewIcon;
        TextView textViewPathName;
    }
}
