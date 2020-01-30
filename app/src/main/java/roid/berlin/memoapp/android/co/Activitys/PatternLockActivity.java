package roid.berlin.memoapp.android.co.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import io.paperdb.Paper;
import roid.berlin.memoapp.android.co.R;

public class PatternLockActivity extends AppCompatActivity {
    String save_pattern_key="pattern-lock";
    String final_pattern="";
    PatternLockView patternLockView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Paper.init(this);

        final String save_pattern=Paper.book().read(save_pattern_key);
        if (save_pattern!=null && !save_pattern.equals("null")){
            setContentView(R.layout.pattern_screen);
            patternLockView=findViewById(R.id.pattern_lockView);
            patternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {

                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {

                    final_pattern= PatternLockUtils.patternToString(patternLockView,pattern);

                    if (final_pattern.equals(save_pattern)){
                        Toast.makeText(PatternLockActivity.this,"pass is true",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PatternLockActivity.this,MainActivity.class));


                    }else{
                        Toast.makeText(PatternLockActivity.this,"pass is false",Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCleared() {

                }
            });
        }else {

            setContentView(R.layout.activity_pattern_lock);
            patternLockView=findViewById(R.id.pattern_lockView);
            patternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {

                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {

                    final_pattern=PatternLockUtils.patternToString(patternLockView,pattern);

                }

                @Override
                public void onCleared() {

                }
            });


            Button btn_setPattern=findViewById(R.id.btn_set_pattern);
            btn_setPattern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Paper.book().write(save_pattern_key,final_pattern);
                    Toast.makeText(PatternLockActivity.this,"pass is saved",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PatternLockActivity.this,MainActivity.class));

                }
            });
        }


    }
}
