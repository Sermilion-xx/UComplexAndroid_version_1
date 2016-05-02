package org.ucomplex.ucomplex.Adaptors;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.javatuples.Triplet;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.MyFilesActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.CourseMaterialsFragment;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Sermilion on 07/12/2015.
 */
public class CourseMaterialsAdapter extends ArrayAdapter<File> {

    protected Context mContext;
    Activity activity;
    MyFilesActivity myFilesActivity;
    private CourseMaterialsFragment fragment;
    private List<File> mItems;
    protected boolean myFiles;
    public int level = 0;
    public ArrayList<ArrayList<File>> stackFiles = new ArrayList<>();
    private LayoutInflater inflater;
    private String previousName;
    private User user;
    private ArrayList<Triplet<Integer, String, Integer>> materialsShareGroups = new ArrayList<>();


    public void setMyFilesActivity(MyFilesActivity myFilesActivity) {
        this.myFilesActivity = myFilesActivity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public CourseMaterialsAdapter(Context mContext, List<File> items, boolean myFiles, CourseMaterialsFragment fragment) {
        super(mContext, R.layout.list_item_course_material_folder, items);
        this.mContext = mContext;
        this.fragment = fragment;
        this.myFiles = myFiles;
        this.mItems = items;
        this.stackFiles = new ArrayList<>();
        user = Common.getUserDataFromPref(mContext);
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
        if (mItems.size() > 0) {
            File file = getItem(position);
            if (file.getType().equals("f")) {
                return TYPE_FOLDER;
            } else {
                return TYPE_FILE;
            }
        } else {
            return -1;
        }

    }

    private View createHolder(ViewHolder viewHolder, View convertView, int viewType, Context context, ViewGroup parent, int position) {
        viewHolder = new ViewHolder(context, this);
        if (viewType == TYPE_FILE) {
            convertView = inflater.inflate(R.layout.list_item_course_material_file, parent, false);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_name);
            viewHolder.weightTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_weight);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_time);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.course_material_listview_item_image_file);
            viewHolder.setButton((Button) convertView.findViewById(R.id.list_materials_item_file_menu_button_file), position);
            viewHolder.holderId = TYPE_FILE;

        } else if (viewType == TYPE_FOLDER) {
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
        return mItems.size() > 0 ? mItems.size() : 1;
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
        if (mItems.size() == 0) {
            if (!Common.isNetworkConnected(getContext())) {
                convertView = inflater.inflate(R.layout.list_item_no_internet, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.list_item_no_content, parent, false);
            }
            return convertView;
        }
        if (convertView == null) {
            convertView = createHolder(viewHolder, convertView, viewType, mContext, parent, position);
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.holderId != viewType) {
                convertView = createHolder(viewHolder, convertView, viewType, mContext, parent, position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }

        File file = getItem(position);
        viewHolder.nameTextView.setTypeface(robotoFont);
        viewHolder.nameTextView.setText(file.getName());
        if (viewType == TYPE_FOLDER) {
            viewHolder.textView2.setTypeface(robotoFont);
            viewHolder.textView2.setText(Common.makeDate(file.getTime(), true));
        } else if (viewType == TYPE_FILE) {
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
        ArrayList<String> groupsActionsArrayList = new ArrayList<>();


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
                                    if (user.getType() == 3 && isMyFiles()) {
                                        actionsArrayList.add("Поделиться");
                                    }

                                    AlertDialog.Builder build = new AlertDialog.Builder(context);
                                    build.setItems(actionsArrayList.toArray(new String[actionsArrayList.size()]), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:
                                                    showInputDialog(position, nameTextView.getText().toString());
                                                    break;
                                                case 1:
                                                    if (Common.isNetworkConnected(context)) {
                                                        removeItem(position);
                                                    } else {
                                                        Toast.makeText(context, "Проверте интернет соединение.", Toast.LENGTH_LONG)
                                                                .show();
                                                    }
                                                    break;
                                                case 2:
                                                    new AsyncTask<String, Void, ArrayList<Triplet<Integer, String, Integer>>>() {

                                                        ProgressDialog progressDialog = ProgressDialog.show(mContext, "",
                                                                "Идет запрос", true);

                                                        @Override
                                                        protected void onPreExecute() {
                                                            super.onPreExecute();
                                                            progressDialog.show();
                                                        }

                                                        @Override
                                                        protected ArrayList<Triplet<Integer, String, Integer>> doInBackground(String... params) {
                                                            String urlString = "https://ucomplex.org/teacher/my_files/get_access?json";
                                                            HashMap<String, String> postParams = new HashMap<>();
                                                            postParams.put("file", params[0]);
                                                            String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext), postParams);
                                                            if (jsonData != null) {
                                                                return getGroupsFromJson(jsonData);
                                                            }
                                                            return new ArrayList<>();
                                                        }

                                                        @Override
                                                        protected void onPostExecute(ArrayList<Triplet<Integer, String, Integer>> triplets) {
                                                            super.onPostExecute(triplets);
                                                            materialsShareGroups = triplets;
                                                            showRadioButtonDialog(materialsShareGroups, position);
                                                            progressDialog.dismiss();

                                                        }

                                                        private ArrayList<Triplet<Integer, String, Integer>> getGroupsFromJson(String jsonData) {
                                                            JSONObject shareGroupsJson;
                                                            ArrayList<Triplet<Integer, String, Integer>> groupsArrayList = new ArrayList<>();
                                                            try {
                                                                shareGroupsJson = new JSONObject(jsonData);
                                                                JSONObject groupsJson = shareGroupsJson.getJSONObject("groups");
                                                                JSONObject allGroupsJson = groupsJson.getJSONObject("all");
                                                                ArrayList<String> keys = Common.getKeys(allGroupsJson);
                                                                for (int i = 0; i < keys.size(); i++) {
                                                                    Triplet<Integer, String, Integer> groupItem =
                                                                            new Triplet<>(Integer.valueOf(
                                                                                    keys.get(i)),
                                                                                    allGroupsJson.getJSONObject(keys.get(i)).getString("name"),
                                                                                    allGroupsJson.getJSONObject(keys.get(i)).getInt("year"));
                                                                    groupsArrayList.add(groupItem);

                                                                }
                                                                try {
                                                                    JSONObject access = groupsJson.getJSONObject("access");
                                                                    ArrayList<String> accessKeys = Common.getKeys(access);
                                                                    for (int i = 0; i < accessKeys.size(); i++) {
                                                                        Triplet<Integer, String, Integer> accessItem =
                                                                                new Triplet<>(Integer.valueOf(
                                                                                        accessKeys.get(i)),
                                                                                        access.getString(accessKeys.get(i)),
                                                                                        -1);
                                                                        groupsArrayList.add(accessItem);

                                                                    }
                                                                } catch (JSONException ignored) {
                                                                }

                                                                JSONObject fileJson = groupsJson.getJSONObject("file");
                                                                File file = new File();
                                                                file.setId(fileJson.getString("id"));
                                                                file.setName(fileJson.getString("name"));
                                                                file.setAddress(fileJson.getString("address"));
                                                                file.setType(fileJson.getString("type"));
                                                                file.setSize(fileJson.getInt("size"));
                                                                try {
                                                                    file.setFolder(fileJson.getString("folder"));
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                file.setData(fileJson.getString("data"));
                                                                file.setOwner(user);
                                                                file.setTime(fileJson.getString("type"));
                                                                try {
                                                                    file.setCheckTime(fileJson.getString("check_time"));
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                file.setID(fileJson.getInt("ID"));
                                                                return groupsArrayList;
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                            return new ArrayList<>();
                                                        }
                                                    }.execute(mItems.get(position).getAddress());
                                                    break;
                                            }
                                        }
                                    });
                                    AlertDialog alert = build.create();
                                    alert.show();
                                }
                            }
                        }
                );
            } else {
                menuButton.setVisibility(View.GONE);
            }
        }

        private void showRadioButtonDialog(final ArrayList<Triplet<Integer, String, Integer>> triplets, final int position) {

            ArrayList<Triplet<Integer, String, Integer>> accessTriplets = new ArrayList<>();

            List<CharSequence> list1 = new ArrayList<>();
            for (int i = 0; i < triplets.size(); i++) {
                if (triplets.get(i).getValue2() != -1) {
                    list1.add(triplets.get(i).getValue1());
                }else{
                    accessTriplets.add(triplets.get(i));
                }
            }

            final CharSequence[] dialogList = list1.toArray(new CharSequence[list1.size()]);
            final android.app.AlertDialog.Builder builderDialog = new android.app.AlertDialog.Builder(mContext);
            builderDialog.setTitle("Выберите группы");
            int count = dialogList.length;
            boolean[] is_checked = new boolean[count];

            for (int i = 0; i < list1.size(); i++) {
                for(int j = 0; j<accessTriplets.size(); j++){
                    if(list1.get(i).equals(accessTriplets.get(j).getValue1())){
                        is_checked[i]= true;
                    }
                }
            }

            // Creating multiple selection by using setMutliChoiceItem method
            builderDialog.setMultiChoiceItems(dialogList, is_checked,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton, boolean isChecked) {

                        }
                    });

            builderDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ListView list = ((android.app.AlertDialog) dialog).getListView();
                            ArrayList<Integer> ids = new ArrayList<>();
                            // make selected item in the comma seprated string
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < list.getCount(); i++) {
                                boolean checked = list.isItemChecked(i);
                                if (checked) {
                                    if (stringBuilder.length() > 0) stringBuilder.append(",");
                                    stringBuilder.append(triplets.get(i).getValue0());
                                    ids.add(triplets.get(i).getValue0());
                                }else {
                                    ids.add(-1);
                                }
                            }

                            new AsyncTask<ArrayList, Void, String>() {

                                @Override
                                protected String doInBackground(ArrayList... params) {
                                    String urlString = "https://ucomplex.org/teacher/my_files/give_access?json";
                                    HashMap<String, String> postParams = new HashMap<>();
                                    postParams.put("file", mItems.get(position).getAddress());
                                    for (int i = 0; i < params[0].size(); i++) {
                                        String id = String.valueOf(params[0].get(i));
                                        if(id.equals("-1")){
                                            id = "";
                                        }
                                        postParams.put("groups[" + i + "]", id);
                                    }
                                    String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext), postParams);
                                    return jsonData;
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                }
                            }.execute(ids);


                        }
                    });

            builderDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            android.app.AlertDialog alert = builderDialog.create();
            alert.show();
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
                            if (Common.isNetworkConnected(context)) {
                                String newName = editText.getText().toString();
                                if (!newName.equals("")) {
                                    renameItem(position, newName);
                                    previousName = adapter.mItems.get(position).getName();
                                    adapter.mItems.get(position).setName(newName);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Название не может быть пустым.", Toast.LENGTH_LONG).show();
                                }

                            } else {
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
//            alert.getWindow().setBackgroundDrawableResource(android.R.color.background_light);
            alert.show();
        }

        private void removeItem(final int pos) {

            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String url = "http://you.com.ru/student/my_files/delete_file?mobile=1";
                    HashMap<String, String> httpParams = new HashMap();
                    if (adapter.mItems.size() > 0) {
                        httpParams.put("file", adapter.mItems.get(pos).getAddress());
                        return Common.httpPost(url, Common.getLoginDataFromPref(context), httpParams);
                    }
                    return "";

                }

                @Override
                protected void onPostExecute(String newFile) {
                    super.onPostExecute(newFile);
                    try {
                        JSONObject jsonObject = new JSONObject(newFile);
                        if (jsonObject.getBoolean("general")) {
                            adapter.mItems.remove(pos);
                            adapter.notifyDataSetChanged();
                            if (adapter.mItems.size() == 0) {
                                fragment.getListView().setDivider(null);
                            }
                        } else {
                            Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show();
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

                    } else {
                        adapter.mItems.get(pos).setName(previousName);
                        Snackbar snackbar = Snackbar
                                .make(null, "Ошибка", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }
            }.execute();
        }
    }
}
