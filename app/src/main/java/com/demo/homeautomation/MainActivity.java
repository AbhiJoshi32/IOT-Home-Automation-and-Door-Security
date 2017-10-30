package com.demo.homeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main activity";
    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference l1Ref;
    DatabaseReference l2Ref;
    DatabaseReference f1Ref;

    ValueEventListener l1EventListener;
    ValueEventListener l2EventListener;
    ValueEventListener f1EventListener;

    @BindView(R.id.modeSelectionGroup)
    RadioGroup modeSelectionGroup;
    @BindView(R.id.l1switch)
    Switch l1switch;
    @BindView(R.id.l2switch)
    Switch l2switch;
    @BindView(R.id.speakBtn)
    ImageButton speakBtn;
    @BindView(R.id.speakText)
    TextView speakText;
    @BindView(R.id.fanSeekBar)
    SeekBar fanSeekBar;
    @BindView(R.id.manualRadio)
    RadioButton manualRadio;
    @BindView(R.id.automaticRadio)
    RadioButton automaticRadio;
    @BindView(R.id.textView2)
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        l1Ref = firebaseDatabase.getReference("light1");
        l2Ref = firebaseDatabase.getReference("light2");
        f1Ref = firebaseDatabase.getReference("fan1");
        if (firebaseAuth.getCurrentUser() == null) {
            openLoginActivity();
        }
        enableManual();
        fanSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                fan1Change(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onResume() {
//        startListeners();
        super.onResume();
    }

    private void fan1Change(int progress) {
        f1Ref.setValue(progress);
    }

    private void startListeners() {
        removeListeners();
        l1EventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.getValue(Boolean.class);
                if (value != null) {
                    if (value) {
                        l1switch.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        l1Ref.addValueEventListener(l1EventListener);

    }

    private void removeListeners() {
        if (l1EventListener != null) {
            l1Ref.removeEventListener(l1EventListener);
        }
        if (l2EventListener != null) {
            l2Ref.removeEventListener(l2EventListener);
        }
        if (f1EventListener != null) {
            f1Ref.removeEventListener(f1EventListener);
        }
    }

    @OnClick({R.id.manualRadio,R.id.automaticRadio, R.id.l1switch, R.id.l2switch, R.id.speakBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.manualRadio:
                enableManual();
                break;
            case R.id.automaticRadio:
                disableManual();
                break;
            case R.id.l1switch:
                if (l1switch.isChecked())
                    swtichl1On();
                else
                    swtichl1Off();
                break;
            case R.id.l2switch:
                if (l2switch.isChecked())
                    swtichl2On();
                else
                    swtichl2Off();
                break;
            case R.id.speakBtn:
                startSpeechToText();
                break;
        }
    }

    private void startSpeechToText() {

    }

    private void swtichl2Off() {
        l2Ref.setValue(false);
    }

    private void swtichl2On() {
        l2Ref.setValue(true);
    }

    private void swtichl1Off() {
        l1Ref.setValue(false);
    }

    private void swtichl1On() {
        l1Ref.setValue(true);
    }

    private void enableManual() {
        speakBtn.setEnabled(true);
        speakText.setEnabled(true);
        l1switch.setEnabled(true);
        l2switch.setEnabled(true);
        fanSeekBar.setEnabled(true);
    }

    private void disableManual() {
        speakBtn.setEnabled(false);
        speakText.setEnabled(false);
        l1switch.setEnabled(false);
        l2switch.setEnabled(false);
        fanSeekBar.setEnabled(false);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        removeListeners();
        super.onPause();
    }
}
