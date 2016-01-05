package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.javatuples.Quintet;
import org.ucomplex.ucomplex.Model.Book;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

/**
 * Created by Sermilion on 27/12/2015.
 */
//id, name, edition, quantity, year
public class LibraryAdapter extends ArrayAdapter<Quintet<String,String,String,String, String>> {


    private ArrayList<Quintet<String, String, String, String, String>> values;
    private ArrayList<Book> books;
    private LayoutInflater inflater;
    int type;
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_BOOK= 1;

    public LibraryAdapter(Context context, ArrayList<Quintet<String, String, String, String, String>> values, int type) {
        super(context, -1, values);
        this.values = values;
        this.type = type;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    @Override
    public int getItemViewType(int position) {
        if(this.type==0){
            return TYPE_TITLE;
        }else if(this.type==1){
            return TYPE_BOOK;
        }
        return -1;
    }

    @Override
    public Quintet<String, String, String, String, String> getItem(int position) {
        return this.values.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder = null;
        int viewType = getItemViewType(position);
        if(convertView==null) {
            viewHolder = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            if (viewType == TYPE_TITLE) {
                convertView = inflater.inflate(R.layout.list_item_library_title, null);
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.list_item_library_title_title);
                viewHolder.holderId = TYPE_TITLE;
            } else if (viewType == TYPE_BOOK) {
                convertView = inflater.inflate(R.layout.list_item_library_book, null);
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.list_item_library_book_name);
                viewHolder.editionTextView = (TextView) convertView.findViewById(R.id.list_item_library_book_edition);
                viewHolder.quantityTextView = (TextView) convertView.findViewById(R.id.list_item_library_book_quantity);
                viewHolder.yearTextView = (TextView) convertView.findViewById(R.id.list_item_library_book_year);
                viewHolder.holderId = TYPE_BOOK;
            }
            if (convertView != null) {
                convertView.setTag(viewHolder);
            }
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //id, name, edition, quantity, year
        Quintet<String, String, String, String, String> item = getItem(position);

        if(viewType==TYPE_TITLE) {
            viewHolder.nameTextView.setText(item.getValue1());
        }else if(viewType==TYPE_BOOK){
            viewHolder.nameTextView.setText(item.getValue1());
            viewHolder.editionTextView.setText(item.getValue2());
            viewHolder.quantityTextView.setText("Количество экземпляров: "+String.valueOf(item.getValue3()));
            viewHolder.yearTextView.setText("Год издания: "+String.valueOf(item.getValue4()));
        }
    return convertView;
    }

    public static class ViewHolder {
        int holderId;
        TextView nameTextView;
        TextView editionTextView;
        TextView quantityTextView;
        TextView yearTextView;

        public ViewHolder(){}

    }


}
