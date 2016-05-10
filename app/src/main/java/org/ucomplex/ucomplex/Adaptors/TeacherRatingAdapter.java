package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.javatuples.Pair;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.TeacherRating;
import org.ucomplex.ucomplex.Model.Votes;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 30/04/16.
 */
public class TeacherRatingAdapter extends ArrayAdapter<Votes> {

    private ArrayList<String> colors = new ArrayList();
    private LayoutInflater inflater;
    public ArrayList<Votes> mItems = new ArrayList<>();
    private int teacher;
    private boolean myTeacher;
    private ArrayList<Pair<Integer, Integer>> questionHint = new ArrayList<>();
    private HashMap<String, Integer> givenMarks = new HashMap<>();
    ViewHolder viewHolder;
    public static boolean voted;

    public ArrayList<Votes> getmItems() {
        return mItems;
    }

    public TeacherRatingAdapter(Context context, TeacherRating teacherRating) {
        super(context, -1, teacherRating.getVotes());
        mItems = teacherRating.getVotes();
        if (mItems.size() == 0) {
            for (int i = 0; i < 10; i++) {
                mItems.add(new Votes());
            }
        }
        for (int i = 0; i < teacherRating.getVotes().size(); i++) {
            givenMarks.put(String.valueOf(i + 1), teacherRating.getVotes().get(i).getAll().get(i));
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

        colors.add("#E77272");
        colors.add("#E77D72");
        colors.add("#E78D72");
        colors.add("#E7A472");
        colors.add("#E8B472");
        colors.add("#E8C272");
        colors.add("#E8C272");
        colors.add("#E6E773");
        colors.add("#c3e874");
        colors.add("#89e874");
    }

    @Override
    public int getCount() {
        return mItems.size() > 0 ? mItems.size() : 1;
    }

    @Override
    public boolean isEnabled(int position) {
        return mItems.size() != 0;
    }

    private View createHolder(ViewGroup parent, int pos) {
        viewHolder = new ViewHolder();

        viewHolder.holderId = pos;
        View convertView = inflater.inflate(R.layout.list_item_teacher_rating2, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.mQuestionTextView = (TextView) convertView.findViewById(R.id.question);
        viewHolder.mHintTextView = (TextView) convertView.findViewById(R.id.question_hint);
        viewHolder.button1 = (RadioButton) convertView.findViewById(R.id.question_button_1);
        viewHolder.button2 = (RadioButton) convertView.findViewById(R.id.question_button_2);
        viewHolder.button3 = (RadioButton) convertView.findViewById(R.id.question_button_3);
        viewHolder.button4 = (RadioButton) convertView.findViewById(R.id.question_button_4);
        viewHolder.button5 = (RadioButton) convertView.findViewById(R.id.question_button_5);
        viewHolder.button6 = (RadioButton) convertView.findViewById(R.id.question_button_6);
        viewHolder.button7 = (RadioButton) convertView.findViewById(R.id.question_button_7);
        viewHolder.button8 = (RadioButton) convertView.findViewById(R.id.question_button_8);
        viewHolder.button9 = (RadioButton) convertView.findViewById(R.id.question_button_9);
        viewHolder.button10 = (RadioButton) convertView.findViewById(R.id.question_button_10);

        viewHolder.setListener(viewHolder.button1);
        viewHolder.setListener(viewHolder.button2);
        viewHolder.setListener(viewHolder.button3);
        viewHolder.setListener(viewHolder.button4);
        viewHolder.setListener(viewHolder.button5);
        viewHolder.setListener(viewHolder.button6);
        viewHolder.setListener(viewHolder.button7);
        viewHolder.setListener(viewHolder.button8);
        viewHolder.setListener(viewHolder.button9);
        viewHolder.setListener(viewHolder.button10);

        viewHolder.button1.setTag(pos);
        viewHolder.button2.setTag(pos);
        viewHolder.button3.setTag(pos);
        viewHolder.button4.setTag(pos);
        viewHolder.button5.setTag(pos);
        viewHolder.button6.setTag(pos);
        viewHolder.button7.setTag(pos);
        viewHolder.button8.setTag(pos);
        viewHolder.button9.setTag(pos);
        viewHolder.button10.setTag(pos);

        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
                convertView = createHolder(parent, position);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (viewHolder.holderId != position) {
                convertView = createHolder(parent, position);
                if (mItems.get(position).getChecked() != -1) {
                    viewHolder.buttons.get(mItems.get(position).getChecked()).setChecked(true);
                }
            }
            viewHolder.mQuestionTextView.setText(questionHint.get(position).getValue0());
            viewHolder.mHintTextView.setText(questionHint.get(position).getValue1());
            Votes item = getItem(position);

            int count = 0;
            double score = 0;
            for (int i = 0; i < item.getAll().size(); i++) {
                score += (i + 1) * item.getAll().get(i);
                count += item.getAll().get(i);
            }
            if (count > 0) {
                score = score / count;
            }
            if (score == 0) {
                score = 10;
            }
            int wholeScore = (int) Math.round(score);

            if(mItems.get(position).getChecked()>0){
                viewHolder.buttons.get(mItems.get(position).getChecked() - 1).setChecked(true);
            }else{
                viewHolder.buttons.get(wholeScore-1).setChecked(true);
            }
        }
        return convertView;
    }

//    private void colorButton(long givenMark, ViewHolder viewHolder){
//        for(int i=0; i<buttons.size();i++){
//            buttons.get(i).setBackgroundColor(Color.WHITE);
//        }
//        for (int j = 0; j < givenMark; j++) {
//            Button button = buttons.get(j);
//            button.setBackgroundColor(Color.parseColor(colors.get(j)));
//        }
//    }

    public class ViewHolder {
        int holderId;
        int givenMark = 0;
        TextView mQuestionTextView;
        TextView mHintTextView;
        RadioButton button1;
        RadioButton button2;
        RadioButton button3;
        RadioButton button4;
        RadioButton button5;
        RadioButton button6;
        RadioButton button7;
        RadioButton button8;
        RadioButton button9;
        RadioButton button10;

        private ArrayList<RadioButton> buttons = new ArrayList<>();


        View.OnClickListener radioListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) v;
                switch (rb.getId()) {
                    case R.id.question_button_1:
                        givenMark = 1;
                        break;
                    case R.id.question_button_2:
                        givenMark = 2;
                        break;
                    case R.id.question_button_3:
                        givenMark = 3;
                        break;
                    case R.id.question_button_4:
                        givenMark = 4;
                        break;
                    case R.id.question_button_5:
                        givenMark = 5;
                        break;
                    case R.id.question_button_6:
                        givenMark = 6;
                        break;
                    case R.id.question_button_7:
                        givenMark = 7;
                        break;
                    case R.id.question_button_8:
                        givenMark = 8;
                        break;
                    case R.id.question_button_9:
                        givenMark = 9;
                        break;
                    case R.id.question_button_10:
                        givenMark = 10;
                        break;
                    default:
                        break;
                }
                int pos = (int) v.getTag();
                mItems.get(pos).setChecked(givenMark - 1);
                voted = true;
                System.out.println();
            }
        };

        public void setListener(RadioButton button) {
            button.setOnClickListener(radioListener);
            buttons.add(button);
        }

    }
}
