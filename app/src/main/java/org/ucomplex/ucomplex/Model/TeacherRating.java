package org.ucomplex.ucomplex.Model;

import java.util.ArrayList;

/**
 * Created by Sermilion on 22/03/16.
 */
public class TeacherRating {

    private int teacher;
    private boolean my_teacher;
    private ArrayList<Votes> votes;

    public TeacherRating() {
    }

    public void addVote(Votes vote){
        votes.add(vote);
    }

    public ArrayList<Votes> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<Votes> votes) {
        this.votes = votes;
    }

    public boolean isMy_teacher() {
        return my_teacher;
    }

    public void setMy_teacher(boolean my_teacher) {
        this.my_teacher = my_teacher;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }
}
