package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ucomplex.ucomplex.Activities.CalendarActivity;
import org.ucomplex.ucomplex.Activities.EventsActivity;
import org.ucomplex.ucomplex.Activities.LibraryActivity;
import org.ucomplex.ucomplex.Activities.LoginActivity;
import org.ucomplex.ucomplex.Activities.MyFilesActivity;
import org.ucomplex.ucomplex.Activities.ReferenceActivity;
import org.ucomplex.ucomplex.Activities.SubjectsActivity;
import org.ucomplex.ucomplex.Activities.UsersActivity;
import org.ucomplex.ucomplex.Activities.WebViewActivity;
import org.ucomplex.ucomplex.R;

/**
 * Created by hp1 on 28-12-2014.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>  {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the viaew under inflation and population is list_item_menu_header or Item
    private static final int TYPE_ITEM = 1;
    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java
    private String name;        //String Resource for list_item_menu_header View Name
    private int profile;        //int Resource for list_item_menu_header view profile picture
    private String email;       //String Resource for list_item_menu_header view email
    private Context context;



    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;
        Context contxt;


        public ViewHolder(View itemView,int ViewType,Context c) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{
                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from list_item_menu_header.xmlheader.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from list_menu_headerenu_header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from list_item_menu_header.xmlheader.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type list_item_menu_header view
            }
        }

        @Override
        public void onClick(View v) {

            if(getAdapterPosition()==1){
                Intent intent = new Intent(contxt, EventsActivity.class);
                contxt.startActivity(intent);
            }else if(getAdapterPosition()==2){
                Intent intent = new Intent(contxt, WebViewActivity.class);
                contxt.startActivity(intent);
            }else if(getAdapterPosition()==3){
                Intent intent = new Intent(contxt, SubjectsActivity.class);
                contxt.startActivity(intent);
            }else if(getAdapterPosition()==4){
                Intent intent = new Intent(contxt, MyFilesActivity.class);
                contxt.startActivity(intent);
            }else if(getAdapterPosition()==5){
                Intent intent = new Intent(contxt, ReferenceActivity.class);
                contxt.startActivity(intent);
            }else if(getAdapterPosition()==6){
                Intent intent = new Intent(contxt, UsersActivity.class);
                contxt.startActivity(intent);
            }else if(getAdapterPosition()==7){
                Intent intent = new Intent(contxt, LibraryActivity.class);
                contxt.startActivity(intent);
            }else if(getAdapterPosition()==8){
                Intent intent = new Intent(contxt, CalendarActivity.class);
                contxt.startActivity(intent);
            }
            else if(getAdapterPosition()==9){
                this.logout();
            }

        }

        private void logout(){
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(contxt).edit();
            editor.putString("pLogin", null);
            editor.apply();
            Intent intent = new Intent(contxt, LoginActivity.class);
            contxt.startActivity(intent);
        }
    }




    public MenuAdapter(String Titles[], int Icons[], String Name, String Email, int Profile, Context passedContext){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        name = Name;
        email = Email;
        profile = Profile;                     //here we assign those passed values to the values we declared here
        this.context = passedContext;
    }



    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate list_menu_headerenu_header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu,parent,false); //Inflating the layout
            return new ViewHolder(v,viewType,context); // Returning the created object
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu_header,parent,false); //Inflating the layout
            return new ViewHolder(v,viewType,context); //returning the object created
        }
        return null;

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {                              // as the list view is going to be called after the list_item_menu_header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons
        }else{
            holder.profile.setImageResource(profile);           // Similarly we set the resources for list_item_menu_header view
            holder.Name.setText(name);

        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the list_item_menu_header view.
    }

    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}