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
    private boolean myFiles;
    public int level = 0;
    public ArrayList<ArrayList<File>> stackFiles = new ArrayList<>();

    public CourseMaterialsAdapter(Context context, List<File> items, boolean myFiles, CourseMaterialsFragment fragment) {
        super(context, R.layout.list_item_course_material_folder, items);
        this.context = context;
        this.fragment = fragment;
        this.myFiles = myFiles;
        mItems = items;
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
        if (getItem(position).getType().equals("f")) {
            return TYPE_FOLDER;
        } else {
            return TYPE_FILE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int viewType = getItemViewType(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            viewHolder = new ViewHolder(context, this);
            if (viewType == TYPE_FILE) {
                convertView = inflater.inflate(R.layout.list_item_course_material_file, parent, false);
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_name);
                viewHolder.weightTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_weight);
                viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_time);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.course_material_listview_item_image);
                viewHolder.setButton((Button) convertView.findViewById(R.id.list_materials_item_file_menu_button_file), position);

            } else {
                convertView = inflater.inflate(R.layout.list_item_course_material_folder, parent, false);
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.list_course_material_item_name);
                viewHolder.textView2 = (TextView) convertView.findViewById(R.id.list_course_material_item_filecount);
                viewHolder.setButton((Button) convertView.findViewById(R.id.list_materials_item_folder_menu_button_folder), position);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        File file = getItem(position);
        viewHolder.nameTextView.setTypeface(robotoFont);
        viewHolder.nameTextView.setText(file.getName());
        if (viewType == TYPE_FOLDER) {
            viewHolder.textView2.setTypeface(robotoFont);
            viewHolder.textView2.setText(Common.makeDate(file.getTime(), true));
        } else {
            viewHolder.weightTextView.setTypeface(robotoFont);
            viewHolder.weightTextView.setText(String.valueOf(Common.readableFileSize(file.getSize(), false)));
            viewHolder.timeTextView.setTypeface(robotoFont);
            viewHolder.timeTextView.setText(Common.makeDate(file.getTime()));
        }
        return convertView;
    }

    public static class ViewHolder {
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
                                                showInputDialog(position);
                                                break;
                                            case 1:
                                                removeItem(position);
                                                break;
                                        }
                                    }
                                }).create().show();
                            }
                        }
                    }
            );
        }

        public ViewHolder(final Context context, final CourseMaterialsAdapter adapter) {
            this.context = context;
            this.adapter = adapter;
        }


        protected void showInputDialog(final int position) {
            // get prompts.xml view
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptView = layoutInflater.inflate(R.layout.dialog_input, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptView);

            final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String newName = editText.getText().toString();
                            renameItem(position, newName);
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
