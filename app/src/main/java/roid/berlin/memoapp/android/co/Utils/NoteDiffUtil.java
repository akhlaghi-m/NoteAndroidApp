package roid.berlin.memoapp.android.co.Utils;


import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import roid.berlin.memoapp.android.co.Database.Note;

public class NoteDiffUtil extends DiffUtil.Callback {

    List<Note> oldNoteList;
    List<Note> newNoteList;

    public NoteDiffUtil(List<Note> oldNoteList, List<Note> newNoteList)
    {
        this.oldNoteList = oldNoteList;
        this.newNoteList = newNoteList;
    }

    @Override
    public int getOldListSize()
    {
        return oldNoteList.size();
    }

    @Override
    public int getNewListSize()
    {
        return newNoteList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition)
    {
        return oldNoteList.get(oldItemPosition).getId() == newNoteList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition)
    {
        return oldNoteList.get(oldItemPosition).equals(newNoteList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition)
    {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
