package roid.berlin.memoapp.android.co.Adapters;

import android.icu.text.RelativeDateTimeFormatter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import roid.berlin.memoapp.android.co.Activitys.MainActivity;
import roid.berlin.memoapp.android.co.Database.Note;
import roid.berlin.memoapp.android.co.R;
import roid.berlin.memoapp.android.co.Utils.AppUtils;
import roid.berlin.memoapp.android.co.Utils.NoteDiffUtil;

public class Adapter extends RecyclerView.Adapter<Adapter.NoteViewHolder> {


    private MainActivity mainActivity;
    private List<Note> notes;

    public Adapter(MainActivity mainActivity, List<Note> notes) {
        this.mainActivity = mainActivity;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item, null);
        NoteViewHolder viewHolder = new NoteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note note = getItem(position);

        holder.itemTitle.setText(note.getTitle());
        holder.itemTime.setText(AppUtils.getFormattedDateString(note.getCreatedAt()));

        if(note.isEncrypt()) {
            holder.itemTime.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_lock_outline, 0);
            holder.itemDescription.setText("یادداشت محافظت شده");
        } else {
            holder.itemTime.setCompoundDrawablesWithIntrinsicBounds(0,0, 0, 0);
            holder.itemDescription.setText(note.getDescription());
        }

        Glide.with(mainActivity).load(new File(note.getImagePath())).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Note getItem(int position) {
        return notes.get(position);
    }


    public void addTasks(List<Note> newNotes) {
        NoteDiffUtil noteDiffUtil = new NoteDiffUtil(notes, newNotes);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(noteDiffUtil);
        notes.clear();
        notes.addAll(newNotes);
        diffResult.dispatchUpdatesTo(this);
    }

    protected class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTitle,itemDescription, itemTime;
        private ImageView itemImage;

        public NoteViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDescription=itemView.findViewById(R.id.item_description);
            itemTime = itemView.findViewById(R.id.item_desc);
            itemImage=itemView.findViewById(R.id.note_items_ImageView);

        }
    }
}
