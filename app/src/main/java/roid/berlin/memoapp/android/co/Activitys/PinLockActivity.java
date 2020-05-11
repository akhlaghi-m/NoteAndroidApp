package roid.berlin.memoapp.android.co.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import roid.berlin.memoapp.android.co.R;

public class PinLockActivity extends AppCompatActivity {


    private static final String True_Code = "1111";
    private static final int max_lenght = 4;
    @BindViews({
            R.id.btn_0,
            R.id.btn_1,
            R.id.btn_2,
            R.id.btn_3,
            R.id.btn_4,
            R.id.btn_5,
            R.id.btn_6,
            R.id.btn_7,
            R.id.btn_8,
            R.id.btn_9,})
    List<View> btnNumpad;
    @BindViews({
            R.id.dot_1,
            R.id.dot_2,
            R.id.dot_3,
            R.id.dot_4,
    })

    List<ImageView> dots;
    private String codeString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_screen);


        ButterKnife.bind(this);

        ImageView clear = findViewById(R.id.img_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (codeString.length() > 0)
                {
                    codeString = removeLastChar(codeString);
                    setImageDot_State();
                }
            }
        });


    }

    @OnClick({
            R.id.btn_0,
            R.id.btn_1,
            R.id.btn_2,
            R.id.btn_3,
            R.id.btn_4,
            R.id.btn_5,
            R.id.btn_6,
            R.id.btn_7,
            R.id.btn_8,
            R.id.btn_9,
    })

    public void onClick(Button button)
    {

        getStringCode(button.getId());
        if (codeString.length() == max_lenght)
        {

            if (codeString.equals(True_Code))
            {
                Toast.makeText(this, "کد وارد شده صحیح می باشد", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PinLockActivity.this, MainActivity.class));


            }
            else
            {
                animation();
                Toast.makeText(this, "کد وارد شده صحیح نمیباشد!", Toast.LENGTH_SHORT).show();

            }
        }
        else if (codeString.length() > max_lenght)
        {
            codeString = "";
            getStringCode(button.getId());


        }
        setImageDot_State();
    }

    private void getStringCode(int ButtonId)
    {

        switch (ButtonId)
        {

            case R.id.btn_0:
                codeString += "0";
                break;

            case R.id.btn_1:
                codeString += "1";
                break;
            case R.id.btn_2:
                codeString += "2";
                break;
            case R.id.btn_3:
                codeString += "3";
                break;
            case R.id.btn_4:
                codeString += "4";
                break;
            case R.id.btn_5:
                codeString += "5";
                break;
            case R.id.btn_6:
                codeString += "6";
                break;
            case R.id.btn_7:
                codeString += "7";
                break;
            case R.id.btn_8:
                codeString += "8";
                break;
            case R.id.btn_9:
                codeString += "9";
                break;
        }

    }

    private void setImageDot_State()
    {
        for (int i = 0; i < codeString.length(); i++)
        {
            dots.get(i).setImageResource(R.drawable.ic_add);
        }

        if (codeString.length() < 4)
        {
            for (int j = codeString.length(); j < 4; j++)
            {
                dots.get(j).setImageResource(R.drawable.ic_mic);

            }
        }
    }

    private String removeLastChar(String s)
    {

        if (s == null || s.length() == 0)
        {
            return s;
        }
        return s.substring(0, s.length() - 1);
    }

    private void animation()
    {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.pin_animation);
        findViewById(R.id.dot_layout).startAnimation(anim);

    }

    private void savePass()
    {

        SharedPreferences.Editor editor = getSharedPreferences("PASS_CODE", MODE_PRIVATE).edit();
        editor.putBoolean("is_pass", true);
        editor.apply();

    }


}
