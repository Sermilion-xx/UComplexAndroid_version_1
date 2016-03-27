package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.CourseMaterialsFragment;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sermilion on 07/12/2015.
 */
public class CourseMaterialsAdapter extends ArrayAdapter<File> {

    private Context context;
    private CourseMaterialsFragment fragment;
    private List<File> mItems;
    protected boolean myFiles;
    public int level = 0;
    public ArrayList<ArrayList<File>> stackFiles = new ArrayList<>();
    private LayoutInflater inflater;

    public CourseMaterialsAdapter(Context context, List<File> items, boolean myFiles, CourseMaterialsFragment fragment) {
        super(context, R.layout.list_item_course_material_folder, items);
        this.context = context;
        this.fragment = fragment;
        this.myFiles = myFiles;
        mItems = items;
        this.stackFiles = new ArrayList<>();
    }

    public void setmItems(List<File> mItems) {
        this.mItems = mItems;
    }

    public boolean isMyFiles() {
        return myFiles;
    }

    public void setLevel(int level) {
        this.level = level;
        mItems = new ArrayList<>(stackFiles.get(level));
    }

    public ArrayList<ArrayList<File>> getStackFiles() {
        return stackFiles;
    }

    public void addStack(ArrayList<File> files) {

        this.stackFiles.add(new ArrayList<>(files));
    }

    public void levelUp() {
        this.level++;
    }

    public void levelDown() {
        this.level--;
    }

    public int getLevel() {
        return level;
    }

    Typeface robotoFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");

    private static final int TYPE_FILE = 0;
    private static final int TYPE_FOLDER = 1;

    @Override
    public int getItemViewType(int position) {
        if(mItems.size()>0){
            File file = getItem(position);
            if (file.getType().equals("f")) {
                return TYPE_FOLDER;
            } else {
                return TYPE_FILE;
            }
        }else{
            return -1;
        }

    }

    private View createHolder(ViewHolder viewHolder, View convertView, int viewType, Context context, ViewGroup parent, int position){
        viewHolder = new ViewHolder(context, this);
        if(viewType==TYPE_FILE ){
            convertView = inflater.inflate(R.layout.list_item_course_material_file, parent, false);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_name);
            viewHolder.weightTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_weight);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_time);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.course_material_listview_item_image_file);
            viewHolder.setButton((Button) convertView.findViewById(R.id.list_materials_item_file_menu_button_file), position);
            viewHolder.holderId = TYPE_FILE;

        }else if(viewType==TYPE_FOLDER){
            convertView = inflater.inflate(R.layout.list_item_course_material_folder, parent, false);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.list_course_material_item_name);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.list_course_material_item_filecount);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.list_course_material_item_imageview_folder);
            viewHolder.setButton((Button) convertView.findViewById(R.id.list_materials_item_folder_menu_button_folder), position);
            viewHolder.holderId = TYPE_FOLDER;
        }
        if (convertView != null) {
            convertView.setTag(viewHolder);
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return mItems.size() != 0;
    }

    @Override
    public int getCount() {
        return mItems.size()>0?mItems.size():1;
    }

    @Override
    public File getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        int viewType = getItemViewType(position);
        inflater = LayoutInflater.from(getContext());
        if(mItems.size()==0){
            if(!Common.isNetworkConnected(getContext())){
                convertView = inflater.inflate(R.layout.list_item_no_internet, parent, false);
            }else{
                convertView = inflater.inflate(R.layout.list_item_no_content, parent, false);
            }
            return convertView;
        }
        if (convertView == null) {
            convertView = createHolder(viewHolder, convertView, viewType, context, parent, position);
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if(viewHolder.holderId != viewType){
                convertView = createHolder(viewHolder, convertView, viewType, context, parent, position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }

        File file = getItem(position);
        viewHolder.nameTextView.setTypeface(robotoFont);
        viewHolder.nameTextView.setText(file.getName());
        if (viewType == TYPE_FOLDER) {
            viewHolder.textView2.setTypeface(robotoFont);
            viewHolder.textView2.setText(Common.makeDate(file.getTime(), true));
        } else if (viewType == TYPE_FILE){
            viewHolder.weightTextView.setTypeface(robotoFont);
            viewHolder.weightTextView.setText(String.valueOf(Common.readableFileSize(file.getSize(), false)));
            viewHolder.timeTextView.setTypeface(robotoFont);
            viewHolder.timeTextView.setText(Common.makeDate(file.getTime()));
        }
        return convertView;
    }

    public class ViewHolder {
        int holderId;
        ImageView imageView;
        Button menuButton;
        TextView nameTextView;
        TextView textView2;
        TextView weightTextView;
        TextView timeTextView;

        Context context;
        CourseMaterialsAdapter adapter;
        ArrayList<String> actionsArrayList = new ArrayList<>();


        public void setButton(Button button, final int position) {
            this.menuButton = button;
            if (myFiles) {
                menuButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                actionsArrayList.clear();
                                if (adapter.myFiles) {
                                    actionsArrayList.add("Переименовать");
                                    actionsArrayList.add("Удалить");

                                    AlertDialog.Builder build = new AlertDialog.Builder(context);
                                    build.setItems(actionsArrayList.toArray(new String[actionsArrayList.size()]), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:
                                                    showInputDialog(position, nameTextView.getText().toString());
                                                    break;
                                                case 1:
                                                    if(Common.isNetworkConnected(context)){
                                                        removeItem(position);
                                                    }else {
                                                        Toast.makeText(context, "Проверте интернет соединение.", Toast.LENGTH_LONG)
                                                                .show();
                                                    }

                                                    break;
                                            }
                                        }
                                    }).create().show();
                                }
                            }
                        }
                );
            }else{
                menuButton.setVisibility(View.GONE);
            }
        }

        public ViewHolder(final Context context, final CourseMaterialsAdapter adapter) {
            this.context = context;
            this.adapter = adapter;
        }


        protected void showInputDialog(final int position, String oldName) {
            // get prompts.xml view
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptView = layoutInflater.inflate(R.layout.dialog_input, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptView);

            final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
            editText.setText(oldName);
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(Common.isNetworkConnected(context)){
                                String newName = editText.getText().toString();
                                if(!newName.equals("")){
                                    renameItem(position, newName);
                                }else{
                                    Toast.makeText(context, "Название не может быть пустым.", Toast.LENGTH_LONG).show();
                                }

                            }else {
                                Toast.makeText(context, "Проверте интернет соединение.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

        private void removeItem(final int pos) {

            new AsyncTask<Void, Void, ArrayList>() {
                @Override
                protected ArrayList doInBackground(Void... params) {
                    String url = "http://you.com.ru/student/my_files/delete_file?mobile=1";
                    HashMap<String, String> httpParams = new HashMap();
                    httpParams.put("file", adapter.mItems.get(pos).getAddress());
                    Common.httpPost(url, Common.getLoginDataFromPref(context), httpParams);
                    return null;
                }

                @Override
                protected void onPostExecute(ArrayList newFile) {
                    super.onPostExecute(newFile);
                    adapter.mItems.remove(pos);
                    adapter.notifyDataSetChanged();
                    if(adapter.mItems.size()==0){
                        fragment.getListView().setDivider(null);
                    }
                }
            }.execute();
        }

        private void renameItem(final int pos, final String newName) {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String jsonData;
                    String url = "http://you.com.ru/student/my_files/rename_file?mobile=1";
                    HashMap<String, String> httpParams = new HashMap();
                    httpParams.put("file", adapter.mItems.get(pos).getAddress());
                    httpParams.put("name", newName);
                    jsonData = Common.httpPost(url, Common.getLoginDataFromPref(context), httpParams);
                    return jsonData;
                }

                @Override
                protected void onPostExecute(String newFile) {
                    super.onPostExecute(newFile);
                    if (newFile != null) {
                        adapter.mItems.get(pos).setName(newName);
                        adapter.notifyDataSetChanged();
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(null, "Ошибка", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }
            }.execute();
        }
    }
}
