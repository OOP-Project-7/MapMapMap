package com.example.s535.mapmapmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

/**
 * Created by eunyoung on 2016-12-02.
 */
public class RegisterActivity extends Activity implements
        View.OnClickListener {
    private SoundPool sound_pool;
    private int sound_error;
    private void initSound(){
        sound_pool = new SoundPool(5, AudioManager.STREAM_ALARM,0);
        sound_error = sound_pool.load(this,R.raw.error,1);
    }
    public void playSound(){
        sound_pool.play(sound_error, 1f, 1f,0,0,1.5f);
    }
    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private ProgressDialog mProgress;
    //  private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initSound();
        mProgress = new ProgressDialog(this);
        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        // Views
        mEmailField = (EditText) findViewById(R.id.emailInput);
        mPasswordField = (EditText) findViewById(R.id.passwordInput);

        // Buttons
        findViewById(R.id.nextButton).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        // [END auth_state_listener]
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    private void createAccount(String email, String password) {
        mProgress.setMessage("Checking...");
        mProgress.show();
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            mProgress.dismiss();
            playSound();
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    //추가한부분

                    //추가한부분끝
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            mProgress.dismiss();
                            Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            RegisterActivity.this.finish();

                        }
                        else{
                            mProgress.dismiss();
                            Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, SettingActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + Pattern.quote("postech.ac.kr") + "$";

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            //추가시작
            if(email.contains("postech.ac.kr")){
                mEmailField.setError(null);
            }
            else{
                Toast.makeText(getApplicationContext(), "Use yourPovisID@postech.ac.kr", Toast.LENGTH_SHORT).show();
                mEmailField.setError("Required.");
                valid = false;
            }
            //추가끝
            //mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();

        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            if (password.length() < 6) {
                mPasswordField.setError("Required.");
                Toast.makeText(getApplicationContext(), "Password is too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                valid = false;
            } else {
                mPasswordField.setError(null);
            }
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.nextButton) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }
}