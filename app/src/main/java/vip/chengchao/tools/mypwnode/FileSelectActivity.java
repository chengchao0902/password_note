package vip.chengchao.tools.mypwnode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vip.chengchao.tools.mypwnode.adapter.FileSelectorAdaptor;
import vip.chengchao.tools.mypwnode.model.FileDesc;
import vip.chengchao.tools.mypwnode.utils.SDCardReader;

/**
 * Created by chengchao on 16/7/28.
 */
public class FileSelectActivity extends Activity {

    private static final String ACTION_SELECT_DIR = "dir";
    private static final String ACTION_SELECT_FILE = "file";
    private static final String KEY_FILE_EXT = "ext";
    public static final String KEY_FILE_PATH = "filePath";
    private static final String SDCARD = "sdcard";

    public static void openFileSelectorForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, FileSelectActivity.class);
        intent.setAction(ACTION_SELECT_FILE);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openDirSelectorForResult(Activity activity, String extension, int requestCode) {
        Intent intent = new Intent(activity, FileSelectActivity.class);
        intent.setAction(ACTION_SELECT_DIR);
        intent.putExtra(KEY_FILE_EXT, extension);
        activity.startActivityForResult(intent, requestCode);
    }


    private String action;
    private String extension;
    private SDCardReader sdCardReader;
    private ListView listViewFile;
    private FileSelectorAdaptor adapter;
    private List<FileDesc> fileDescList;
    private Button buttonSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdcard_selector);
        Intent intent = getIntent();
        action = intent.getAction();
        action = ACTION_SELECT_FILE; //TODO TEST
        extension = intent.getStringExtra(KEY_FILE_EXT);
        buttonSelect = (Button) findViewById(R.id.button_select);
        listViewFile = (ListView) findViewById(R.id.listview_file_list);
        fileDescList = new ArrayList<>();
        adapter = new FileSelectorAdaptor(this, fileDescList);
        listViewFile.setAdapter(adapter);
        listViewFile.setOnItemClickListener(clickListener);
        sdCardReader = new SDCardReader(mapAction(), extension);
        flush();
    }

    public void back(View view) {
        if (sdCardReader.isRoot()) {
            return;
        }
        sdCardReader.back();
        flush();
    }

    public void selected(View view) {
        Intent intent = new Intent();
        intent.putExtra(KEY_FILE_PATH, sdCardReader.current().getAbsolutePath());
        setResult(RESULT_OK, intent);
        forceClose(view);
    }

    public void flush() {
        fileDescList.clear();
        adapter.notifyDataSetChanged();
        File[] children = sdCardReader.list();
        if (children == null || children.length == 0) return;
        for (int i = 0; i < children.length; i++) {
            fileDescList.add(getFileDesc(children[i]));
        }
        adapter.notifyDataSetChanged();
    }

    private FileDesc getFileDesc(File file) {
        int type;
        if (file.isFile()) {
            type = R.drawable.file;
        } else {
            String[] list = file.list();
            type = list == null || list.length == 0 ? R.drawable.folder_empty : R.drawable.folder_fill;
        }
        return new FileDesc(type, file.getName());
    }

    public void forceClose(View view) {
        action = null;
        extension = null;
        sdCardReader = null;
        finish();
    }

    public void clickRoot(View view) {
        sdCardReader.root();
        flush();
    }

    private int mapAction() {
        switch (action) {
            case ACTION_SELECT_DIR:
                buttonSelect.setVisibility(View.VISIBLE);
                return SDCardReader.TYPE_DIR;
            case ACTION_SELECT_FILE:
                buttonSelect.setVisibility(View.GONE);
                return SDCardReader.TYPE_FILE;
            default:
                return SDCardReader.TYPE_FILE;
        }
    }

    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            sdCardReader.select(i);
            flush();
        }

    };
}
