package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.javatuples.Pair;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.TeacherRating;
import org.ucomplex.ucomplex.Model.Votes;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

/**
 * Created by Sermilion on 30/04/16.
 */
public class TeacherInfoAdapter extends ArrayAdapter<Votes> {

    private ArrayList<Button> buttons = new ArrayList<>();
    private LayoutInflater inflater;
    private ArrayList<Votes> mItems = new ArrayList<>();
    private int teacher;
    private boolean myTeacher;
    private ArrayList<Pair<Integer, Integer>> questionHint = new ArrayList<>();

    public TeacherInfoAdapter(Context context, TeacherRating teacherRating) {
        super(context, -1, teacherRating.getVotes());
        mItems = teacherRating.getVotes();
        if(mItems.size()==0){
            for(int i = 0; i<10; i++){
                mItems.add(new Votes());
            }
        }
        teacher = teacherRating.getTeacher();
        myTeacher = teacherRating.isMy_teacher();

        questionHint.add(new Pair<>(R.string.question1, R.string.questionHint1));
        questionHint.add(new Pair<>(R.string.question2, R.string.questionHint2));
        questionHint.add(new Pair<>(R.string.question3, R.string.questionHint3));
        questionHint.add(new Pair<>(R.string.question4, R.string.questionHint4));
        questionHint.add(new Pair<>(R.string.question5, R.string.questionHint5));
        questionHint.add(new Pair<>(R.string.question6, R.string.questionHint6));
        questionHint.add(new Pair<>(R.string.question7, R.string.questionHint7));
        questionHint.add(new Pair<>(R.string.question8, R.string.questionHint8));
        questionHint.add(new Pair<>(R.string.question9, R.string.questionHint9));
        questionHint.add(new Pair<>(R.string.question10, R.string.questionHint10));
    }

    @Override
    public int getCount() {
        return mItems.size() > 0 ? mItems.size() : 1;
    }

    @Override
    public boolean isEnabled(int position) {
        return mItems.size() != 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        inflater = LayoutInflater.from(getContext());
        if (mItems.size() == 0) {
            if (!Common.isNetworkConnected(getContext())) {
                convertView = inflater.inflate(R.layout.list_item_no_internet, null, false);
            } else {
                convertView = inflater.inflate(R.layout.list_item_no_content, null, false);
            }
            return convertView;
        } else {
            if (convertView == null) {
                buttons.clear();
                convertView = inflater.inflate(R.layout.list_item_teacher_rating, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mQuestionTextView = (TextView) convertView.findViewById(R.id.question);
                viewHolder.mHintTextView = (TextView) convertView.findViewById(R.id.question_hint);
                viewHolder.button1 = (Button) convertView.findViewById(R.id.question_button_1);
                viewHolder.button2 = (Button) convertView.findViewById(R.id.question_button_2);
                viewHolder.button3 = (Button) convertView.findViewById(R.id.question_button_3);
                viewHolder.button4 = (Button) convertView.findViewById(R.id.question_button_4);
                viewHolder.button5 = (Button) convertView.findViewById(R.id.question_button_5);
                viewHolder.button6 = (Button) convertView.findViewById(R.id.question_button_6);
                viewHolder.button7 = (Button) convertView.findViewById(R.id.question_button_7);
                viewHolder.button8 = (Button) convertView.findViewById(R.id.question_button_8);
                viewHolder.button9 = (Button) convertView.findViewById(R.id.question_button_9);
                viewHolder.button10 = (Button) convertView.findViewById(R.id.question_button_10);
                buttons.add(viewHolder.button1);
                buttons.add(viewHolder.button2);
                buttons.add(viewHolder.button3);
                buttons.add(viewHolder.button4);
                buttons.add(viewHolder.button5);
                buttons.add(viewHolder.button6);
                buttons.add(viewHolder.button7);
                buttons.add(viewHolder.button8);
                buttons.add(viewHolder.button9);
                buttons.add(viewHolder.button10);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mQuestionTextView.setText(questionHint.get(position).getValue0());
            viewHolder.mHintTextView.setText(questionHint.get(position).getValue1());
            Votes item = getItem(position);

            int count = 0;
            double score = 0;
            for(int i = 0; i<item.getAll().size(); i++){
                score += (i+1) * item.getAll().get(i);
                count +=item.getAll().get(i);
            }
            if(count>0){
                score = score/count;
            }
            if(score==0){
                score = 10;
            }
            double diff = (10-score);
            for (int j = 9; j >= diff; j--) {
                Button button = buttons.get(j);
                button.setBackgroundColor(Color.WHITE);
                button.setTextColor(Color.BLACK);
            }

        }
        return convertView;
    }

    private static class ViewHolder {
        TextView mQuestionTextView;
        TextView mHintTextView;
        Button button1;
        Button button2;
        Button button3;
        Button button4;
        Button button5;
        Button button6;
        Button button7;
        Button button8;
        Button button9;
        Button button10;
    }
}
