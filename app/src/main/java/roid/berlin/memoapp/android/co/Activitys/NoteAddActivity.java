package roid.berlin.memoapp.android.co.Activitys;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import roid.berlin.memoapp.android.co.Database.Note;
import roid.berlin.memoapp.android.co.Fragments.DrawingFragment;
import roid.berlin.memoapp.android.co.Other.RuntimePermissionsActivity;
import roid.berlin.memoapp.android.co.R;
import roid.berlin.memoapp.android.co.Utils.AppUtils;

import static roid.berlin.memoapp.android.co.Utils.AppConstants.INTENT_DESC;
import static roid.berlin.memoapp.android.co.Utils.AppConstants.INTENT_ENCRYPT;
import static roid.berlin.memoapp.android.co.Utils.AppConstants.INTENT_IMAGE;
import static roid.berlin.memoapp.android.co.Utils.AppConstants.INTENT_PWD;
import static roid.berlin.memoapp.android.co.Utils.AppConstants.INTENT_TASK;
import static roid.berlin.memoapp.android.co.Utils.AppConstants.INTENT_TITLE;


public class NoteAddActivity extends RuntimePermissionsActivity {


    private static final int CAMERA_REQUEST = 1;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 12;
    ImageView addImage;
    LinearLayout audioRecorder_layout, createPassLayout, addBottomSheetLayout;
    Animation anim;
    EditText edTitle, edMemo;
    TextView txtShow_title, txtShow_memo, txtTime;
    Note note;
    //for create pass
    CheckBox checkBox;
    TextView txt_createPass;
    EditText ed_pwd;
    ImageView img_showPass;
    private String imagePath = Environment.getExternalStorageDirectory() + "/Yaddasht" + "/picture";
    private String FileName = System.currentTimeMillis() + "pic.png";
    File dest = new File(imagePath, FileName);
    private BottomSheetDialog AddBottomSheetDialog, MoreBottomSheetDialog;
    private int PICK_IMAGE_REQUEST = 2;
    private int GALLERY_REQUEST_CODE = 1234;
    //audio Recorder
    private ImageView imageViewRecord, imageViewPlay, imageViewStop;
    private Chronometer chronometer;
    private SeekBar seekBar;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null;
    private String filePic = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run()
        {
            seekUpdation();
        }
    };
    private int RECORD_AUDIO_REQUEST_CODE = 123;
    private boolean isPlaying = false;
    private boolean pwdVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        edTitle = findViewById(R.id.addNote_editText_title);
        edMemo = findViewById(R.id.Ed_Memo);
        txtShow_title = findViewById(R.id.texShow_title);
        txtShow_memo = findViewById(R.id.txtShow_memo);
        txtTime = findViewById(R.id.text_Time);
        checkBox = findViewById(R.id.more_bSh_checkBax);
        ed_pwd = findViewById(R.id.edit_pwd);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            getPermissionToRecordAudio();
        }

        initVoiceRecorderViews();
        initCreatePass();

        NoteAddActivity.super.requestAppPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
        NoteAddActivity.super.requestAppPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);

        addImage = findViewById(R.id.noteActivity_imageView);
        addImage.setVisibility(View.GONE);


        //save note

        note = (Note) getIntent().getSerializableExtra(INTENT_TASK);
        if (note == null)
        {

            txtTime.setText(AppUtils.getFormattedDateString(AppUtils.getCurrentDateTime()));

        }
        else
        {

            if (note.getTitle() != null && !note.getTitle().isEmpty())
            {
                edTitle.setText(note.getTitle());
                edTitle.setSelection(edTitle.getText().length());
            }
            if (note.getDescription() != null && !note.getDescription().isEmpty())
            {
                edMemo.setText(note.getDescription());
                edMemo.setSelection(edMemo.getText().length());
            }
            if (note.getCreatedAt() != null)
            {
                txtTime.setText(AppUtils.getFormattedDateString(note.getCreatedAt()));
            }
            if (note.getPassword() != null && !note.getPassword().isEmpty())
            {
                ed_pwd.setText(note.getPassword());
                ed_pwd.setSelection(ed_pwd.getText().length());
            }
            checkBox.setChecked(note.isEncrypt());
        }

        AppUtils.openKeyboard(getApplicationContext());
    }

    private void initCreatePass()
    {

        checkBox = findViewById(R.id.more_bSh_checkBax);
        ed_pwd = findViewById(R.id.edit_pwd);
        txt_createPass = findViewById(R.id.txt_createPass);
        img_showPass = findViewById(R.id.img_showPass);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {

                if (b)
                {
                    ed_pwd.setVisibility(View.VISIBLE);
                    txt_createPass.setVisibility(View.GONE);
                    img_showPass.setVisibility(View.VISIBLE);
                    ed_pwd.setFocusable(true);
                }
                else
                {
                    ed_pwd.setVisibility(View.GONE);
                    img_showPass.setVisibility(View.GONE);
                    txt_createPass.setVisibility(View.VISIBLE);
                }

            }
        });

        img_showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!pwdVisible)
                {
                    pwdVisible = Boolean.TRUE;
                    ed_pwd.setTransformationMethod(null);
                    img_showPass.setImageResource(R.drawable.ic_visibility_off);
                }
                else
                {
                    pwdVisible = Boolean.FALSE;
                    ed_pwd.setTransformationMethod(new PasswordTransformationMethod());
                    img_showPass.setImageResource(R.drawable.ic_visibility);
                }
                ed_pwd.setSelection(ed_pwd.length());
            }
        });

    }

    @Override
    public void onPermissionsGranted(int requestCode)
    {
        if (requestCode == PERMISSION_WRITE_EXTERNAL_STORAGE)
        {
            makeDirectory();
        }

    }

    @Override
    public void onPermissionsDeny(int requestCode)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST)
        {
            try
            {
                Bundle extras = data.getExtras();

                if (extras != null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    addImage.setVisibility(View.VISIBLE);
                    Glide.with(this).load(photo).into(addImage);
                    FileOutputStream outputStream = new FileOutputStream(dest);
                    photo.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            Uri uri = data.getData();

            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                addImage.setVisibility(View.VISIBLE);
                addImage.setImageBitmap(bitmap);
                FileOutputStream out = new FileOutputStream(dest);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void showAddBottomSheetDialog()
    {


        final View view = getLayoutInflater().inflate(R.layout.add_bottoms_heet_dialog, null);


        view.findViewById(R.id.addPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                OpenCamera();
                AddBottomSheetDialog.dismiss();
            }
        });

        view.findViewById(R.id.choose_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                ChoosePhoto();
                AddBottomSheetDialog.dismiss();

            }

        });

        view.findViewById(R.id.drawing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                DrawingFragment drawingFragment = new DrawingFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.drawingFragment_frame, drawingFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                AddBottomSheetDialog.dismiss();
            }
        });
        view.findViewById(R.id.record_audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                audioRecorder_layout = findViewById(R.id.audioRecorder_layout);
                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                audioRecorder_layout.setAnimation(anim);
                audioRecorder_layout.setVisibility(View.VISIBLE);

                AddBottomSheetDialog.dismiss();
            }
        });


        AddBottomSheetDialog = new BottomSheetDialog(this);
        AddBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            AddBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        AddBottomSheetDialog.show();
        AddBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                AddBottomSheetDialog = null;

            }
        });


    }

    private void showMorBottomSheetDialog()
    {

        final View view = getLayoutInflater().inflate(R.layout.more_bottoms_heet_dialog, null);
        view.findViewById(R.id.addPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                createPassLayout = findViewById(R.id.create_pass_layout);
                createPassLayout.setVisibility(View.VISIBLE);
                MoreBottomSheetDialog.dismiss();
            }
        });

        AppCompatSeekBar seekBar;
        seekBar = view.findViewById(R.id.seekBar_fontSize);
        final AppCompatTextView textView_fontSize;
        textView_fontSize = view.findViewById(R.id.textView_fontSize);

        final SharedPreferences sp = this.getSharedPreferences("pref", MODE_PRIVATE);
        int s = sp.getInt("size", 15);
        textView_fontSize.setTextSize(s);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                if (i < 15)
                {
                    return;
                }
                textView_fontSize.setTextSize(i);
                edMemo.setTextSize(i);

                sp.edit().putInt("size", i).apply();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });


        MoreBottomSheetDialog = new BottomSheetDialog(this);
        MoreBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            MoreBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        MoreBottomSheetDialog.show();
        MoreBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                MoreBottomSheetDialog = null;

            }
        });
    }

    public void onClick_addBottomSheetDialog(View view)
    {
        showAddBottomSheetDialog();
    }

    public void onClick_morBottomSheetDialog(View view)
    {

        showMorBottomSheetDialog();
    }

    public void OpenCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
        try
        {
            intent.putExtra("return-data", true);
            startActivityForResult(intent, CAMERA_REQUEST);

        }
        catch (ActivityNotFoundException e)
        {
        }
    }

    private void makeDirectory()
    {
        try
        {
            File directory = new File(imagePath);
            if (!directory.isDirectory())
            {
                directory.mkdirs();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "دایرکتوری ساخته نشد", Toast.LENGTH_SHORT).show();
        }
    }

    public void ChoosePhoto()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


    }

    private void initVoiceRecorderViews()
    {


        chronometer = findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        imageViewRecord = findViewById(R.id.imageViewRecord);
        imageViewStop = findViewById(R.id.imageViewStop);
        imageViewPlay = findViewById(R.id.imageViewPlay);
        seekBar = findViewById(R.id.seekBar);

        imageViewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                prepareforRecording();
                startRecording();

            }
        });
        imageViewStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                prepareforStop();
                stopRecording();
                imageViewRecord.setVisibility(View.INVISIBLE);
            }
        });
        imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!isPlaying && fileName != null)
                {
                    isPlaying = true;
                    startPlaying();
                }
                else
                {
                    isPlaying = false;
                    stopPlaying();
                }

            }
        });

    }

    private void prepareforStop()
    {

        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.INVISIBLE);
        imageViewPlay.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);

    }

    private void prepareforRecording()
    {

        imageViewRecord.setVisibility(View.INVISIBLE);
        imageViewStop.setVisibility(View.VISIBLE);
        imageViewPlay.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.INVISIBLE);
    }

    private void stopPlaying()
    {
        try
        {
            mPlayer.release();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mPlayer = null;
        imageViewPlay.setImageResource(R.drawable.ic_play);
        chronometer.stop();

    }

    private void startRecording()
    {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/Yaddasht/audios");
        if (!file.exists())
        {
            file.mkdirs();
        }

        fileName = root.getAbsolutePath() + "/Yaddasht/audios/" + System.currentTimeMillis() + ".mp3";
        Log.d("filename", fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try
        {
            mRecorder.prepare();
            mRecorder.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        lastProgress = 0;
        seekBar.setProgress(0);
        stopPlaying();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void stopRecording()
    {

        try
        {
            mRecorder.stop();
            mRecorder.release();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mRecorder = null;
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        Toast.makeText(this, "saved.", Toast.LENGTH_SHORT).show();
    }

    private void startPlaying()
    {
        mPlayer = new MediaPlayer();
        try
        {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        }
        catch (IOException e)
        {
            Log.e("LOG_TAG", "prepare() failed");
        }
        imageViewPlay.setImageResource(R.drawable.ic_pause);

        seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        seekBar.setMax(mPlayer.getDuration());
        seekUpdation();
        chronometer.start();


        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                imageViewPlay.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                chronometer.stop();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (mPlayer != null && fromUser)
                {
                    mPlayer.seekTo(progress);
                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    private void seekUpdation()
    {
        if (mPlayer != null)
        {
            int mCurrentPosition = mPlayer.getCurrentPosition();
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RECORD_AUDIO_REQUEST_CODE);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == RECORD_AUDIO_REQUEST_CODE)
        {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED)
            {

            }
            else
            {
                Toast.makeText(this, "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }

    }

    public void noteCardsViewBackground()
    {

        addBottomSheetLayout = findViewById(R.id.addBottomSheetLayout);
        addBottomSheetLayout.setBackgroundColor(Color.parseColor("#353131"));
    }

    public void addNoteActivity_onClick(View view)
    {
        int id = view.getId();

        if (id == R.id.addNote_btn_save)
        {


            AppUtils.hideKeyboard(this);

            Intent intent = getIntent();
            if (note != null)
            {
                note.setTitle(edTitle.getText().toString());
                note.setDescription(edMemo.getText().toString());
                note.setImagePath(imagePath);
                note.setEncrypt(checkBox.isChecked());
                note.setPassword(ed_pwd.getText().toString());
                intent.putExtra(INTENT_TASK, note);

            }
            else
            {
                intent.putExtra(INTENT_TITLE, edTitle.getText().toString());
                intent.putExtra(INTENT_DESC, edMemo.getText().toString());
                intent.putExtra(INTENT_ENCRYPT, checkBox.isChecked());
                intent.putExtra(INTENT_PWD, ed_pwd.getText().toString());
                intent.putExtra(INTENT_IMAGE, dest.getAbsolutePath());
            }
            setResult(Activity.RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.stay, R.anim.side);
        }
    }


}
