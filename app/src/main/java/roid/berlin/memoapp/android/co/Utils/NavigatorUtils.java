package roid.berlin.memoapp.android.co.Utils;


import android.app.Activity;
import android.content.Intent;

import roid.berlin.memoapp.android.co.Activitys.NoteAddActivity;
import roid.berlin.memoapp.android.co.Database.Note;

public class NavigatorUtils implements AppConstants {


    public static void redirectToEditTaskScreen(Activity activity,
                                                Note note)
    {
        Intent intent = new Intent(activity, NoteAddActivity.class);
        intent.putExtra(INTENT_TASK, note);
        activity.startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }

    public static void redirectToViewNoteScreen(Activity activity,
                                                Note note)
    {
        Intent intent = new Intent(activity, NoteAddActivity.class);
        intent.putExtra(INTENT_TASK, note);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        activity.startActivity(intent);
        activity.finish();
    }
}
