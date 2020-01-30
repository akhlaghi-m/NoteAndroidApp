package roid.berlin.memoapp.android.co.Activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bignerdranch.android.multiselector.MultiSelector;


import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;

import roid.berlin.memoapp.android.co.Adapters.Adapter;
import roid.berlin.memoapp.android.co.Adapters.DrawerAdapter;
import roid.berlin.memoapp.android.co.Database.Note;
import roid.berlin.memoapp.android.co.Drawer.Drawer;
import roid.berlin.memoapp.android.co.Fragments.SettingFragment;
import roid.berlin.memoapp.android.co.R;
import roid.berlin.memoapp.android.co.ReminderUtils.AlarmReceiver;
import roid.berlin.memoapp.android.co.SQLDatabaseReminder.ReminderDatabase;
import roid.berlin.memoapp.android.co.Utils.AppConstants;
import roid.berlin.memoapp.android.co.Utils.AppUtils;
import roid.berlin.memoapp.android.co.Utils.NavigatorUtils;
import roid.berlin.memoapp.android.co.Utils.NoteRepository;
import roid.berlin.memoapp.android.co.Utils.RecyclerItemClickListener;


public class MainActivity extends AppCompatActivity
        implements
        RecyclerItemClickListener.OnRecyclerViewItemClickListener,
        AppConstants {

    private RecyclerView recyclerView;
    private Adapter notesListAdapter;
    NoteRepository noteRepository;
    final Context context = this;
    Note note;
    private boolean pwdVisible;

    Animation anim;
    CardView cardViewSearchBar, cardViewMenuBar;
    DrawerLayout drawer;
    EditText main_editText_search;
    BottomNavigationView bottomNavigation;

    //Reminder
    private RecyclerView mList;
    private Toolbar mToolbar;
    private TextView mNoRiminderTextView;
    private int mTempPost;
    private LinkedHashMap<Integer, Integer> IDmap = new LinkedHashMap<>();
    private ReminderDatabase rb;
    private MultiSelector mMultiSelector = new MultiSelector();
    private AlarmReceiver mAlarmReceiver;

    //drawer
    private DrawerLayout drawerLayout;
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            exitStatus = false;
        }
    };
    public View drawerHolder;
    private boolean exitStatus = false;

    private SettingFragment settingFragment;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardViewSearchBar = findViewById(R.id.cardViewSearch_bar);
        cardViewMenuBar = findViewById(R.id.cardViewMenu_bar);
        drawer = findViewById(R.id.drawer_layout);
        main_editText_search = findViewById(R.id.main_editText_search);
        bottomNavigation = findViewById(R.id.navigation);

        initToolbar();
        //initNavigationMenu();
        setupDrawer();
        initViews();



        noteRepository = new NoteRepository(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        note = (Note) getIntent().getSerializableExtra(INTENT_TASK);


        updateTaskList();


    }


    private void initToolbar() {
        ActionBar actionBar;
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("");
    }

    private void setupDrawer() {
        // Set date in drawer
      // ((TextView) findViewById(R.id.drawer_date)).setText(AppUtils.getFormattedDateString(AppUtils.getCurrentDateTime()));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerHolder = findViewById(R.id.drawer_holder);
        ListView drawerList = (ListView) findViewById(R.id.drawer_list);

        // Navigation menu button
        findViewById(R.id.nav_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // Settings button
        findViewById(R.id.settings_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDrawer(Drawer.TYPE_SETTINGS);
            }
        });

        // Set adapter of drawer
        drawerList.setAdapter(new DrawerAdapter(
                getApplicationContext(),
                new DrawerAdapter.ClickListener() {
                    @Override
                    public void onClick(int type) {
                        onClickDrawer(type);
                    }
                }
        ));
    }


    private void onClickDrawer(final int type) {
        drawerLayout.closeDrawers();

        try {
            handler.removeCallbacks(runnable);
        } catch (Exception ignored) {}

        new Thread() {
            @Override
            public void run() {
                try {
                    // wait for completion of drawer animation
                    sleep(500);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (type) {
                                case Drawer.TYPE_ABOUT:
                                    new MaterialDialog.Builder(MainActivity.this)
                                            .title(R.string.app_name)
                                            .content(R.string.app_name)
                                            .positiveText("ok")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                    break;
                                case Drawer.TYPE_REMINDER:
                                    Intent intent = new Intent(MainActivity.this, ReminderAddActivity.class);
                                    startActivity(intent);
                                    break;
                                case Drawer.TYPE_RESTORE:
                                    Toast.makeText(MainActivity.this,"restoreData()",Toast.LENGTH_SHORT).show();

                                    break;
                                case Drawer.TYPE_SETTINGS:
                                    SettingFragment settingFragment = new SettingFragment();
                       FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.settingFragment_frame, settingFragment);
                        fragmentTransaction.addToBackStack(null);fragmentTransaction.commit();
                                    break;
                            }
                        }
                    });

                    interrupt();
                } catch (Exception ignored) {
                }
            }
        }.start();
    }

    private void initViews() {
        ImageButton main_btn_search;
        main_btn_search = findViewById(R.id.main_btn_search);
        cardViewSearchBar.setVisibility(View.GONE);
        main_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim;
                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_one);
                cardViewSearchBar.setAnimation(anim);
                cardViewSearchBar.setVisibility(View.VISIBLE);
                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_two);
                cardViewMenuBar.setAnimation(anim);
                cardViewMenuBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onBackPressed() {

        if (cardViewMenuBar.getVisibility() == View.GONE) {

            anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_one);
            cardViewMenuBar.setAnimation(anim);
            cardViewMenuBar.setVisibility(View.VISIBLE);
            anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_two);
            cardViewSearchBar.setAnimation(anim);
            cardViewSearchBar.setVisibility(View.GONE);

        }

        if (drawerLayout.isDrawerOpen(drawerHolder)) {
            drawerLayout.closeDrawers();
            return;
        }

        super.onBackPressed();
    }

    public void cardViewSearchOnClick(View view) {

        int id = view.getId();

        if (id == R.id.main_btn_clear) {

            main_editText_search.setText("");

        } else if (id == R.id.main_btn_back) {

            anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_two);
            cardViewSearchBar.setAnimation(anim);
            cardViewSearchBar.setVisibility(View.GONE);
            anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_one);
            cardViewMenuBar.setAnimation(anim);
            cardViewMenuBar.setVisibility(View.VISIBLE);

            main_editText_search.setText("");
        }


    }

    public void onClick_toAddNoteActivity(View view) {

        Intent intent = new Intent(MainActivity.this, NoteAddActivity.class);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }



    public void customeFont(Context context, String defultfontName, String customeFontFileName) {

        try {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), customeFontFileName);
            Field defultFontTypeFace = Typeface.class.getDeclaredField(defultfontName);
            defultFontTypeFace.setAccessible(true);
            defultFontTypeFace.set(null, typeface);

        } catch (Exception e) {
            Toast.makeText(this, "ccc", Toast.LENGTH_SHORT).show();
        }

    }

    public void customeFontTwo(Context context, String defultFontNameTwo, String customeFontFieldNameTwo) {

        try {
            Typeface typefaceTwo = Typeface.createFromAsset(context.getAssets(), customeFontFieldNameTwo);
            Field defultFontTypeFaceTwo = Typeface.class.getDeclaredField(defultFontNameTwo);
            defultFontTypeFaceTwo.setAccessible(true);
            defultFontTypeFaceTwo.set(null, typefaceTwo);
        } catch (Exception e) {
            Toast.makeText(this, "ccc", Toast.LENGTH_SHORT).show();

        }
    }


    private void updateTaskList() {
        noteRepository.getTasks().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if (notes.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    if (notesListAdapter == null) {
                        notesListAdapter = new Adapter(notes);
                        recyclerView.setAdapter(notesListAdapter);

                    } else notesListAdapter.addTasks(notes);
                } else updateEmptyView();

            }

        });
    }


    private void updateEmptyView() {
        // recyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (data.hasExtra(INTENT_TASK)) {
                if (data.hasExtra(INTENT_DELETE)) {
                    noteRepository.deleteTask((Note) data.getSerializableExtra(INTENT_TASK));

                } else {
                    noteRepository.updateTask((Note) data.getSerializableExtra(INTENT_TASK));
                }
            } else {
                String title = data.getStringExtra(INTENT_TITLE);
                String desc = data.getStringExtra(INTENT_DESC);
                String pwd = data.getStringExtra(INTENT_PWD);
                boolean encrypt = data.getBooleanExtra(INTENT_ENCRYPT, false);
                noteRepository.insertTask(title, desc, encrypt, pwd);
            }
            updateTaskList();
        }

    }


    @Override
    public void onItemClick(View parentView, View childView, int position) {
        final Note note = notesListAdapter.getItem(position);
        if (note.isEncrypt()) {

            // get pwd.xml view
            LayoutInflater li = LayoutInflater.from(context);
            View view = li.inflate(R.layout.dialog_pwd, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);
            alertDialogBuilder.setTitle("رمز خود را وارد کنید");
            alertDialogBuilder.setIcon(R.drawable.ic_lock_outline);
            alertDialogBuilder.setView(view);

            final ImageView img_showPass = view.findViewById(R.id.img_showPass);
            final EditText userInput =view.findViewById(R.id.edit_pwd);
            img_showPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!pwdVisible) {
                        pwdVisible = Boolean.TRUE;
                        userInput.setTransformationMethod(null);
                        img_showPass.setImageResource(R.drawable.ic_visibility_off);
                    } else {
                        pwdVisible = Boolean.FALSE;
                        userInput.setTransformationMethod(new PasswordTransformationMethod());
                        img_showPass.setImageResource(R.drawable.ic_visibility);
                    }
                    userInput.setSelection(userInput.length());
                }
            });

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("تایید",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Evaluate the password
                                    if ( note.getPassword().equals(AppUtils.generateHash(userInput.getText().toString()))) {

                                        NavigatorUtils.redirectToViewNoteScreen(MainActivity.this, note);

                                    } else
                                        AppUtils.showMessage(getApplicationContext(), getString(R.string.error_pwd));
                                }
                                {
                            }
        })
                    .setNegativeButton("انصراف",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    } else

    {
        NavigatorUtils.redirectToEditTaskScreen(this, note);
    }
}

}
