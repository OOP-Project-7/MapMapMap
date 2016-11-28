package com.example.s535.mapmapmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.name;

/**
 *CreatedbyEunyoungon2016-10-30.
 */

public class LoginActivity extends Activity {
        //로그인작성
        private EditText mEmailInput, mPasswordInput;
        private Button mLoginButton, mRegisterButton, mCancelButton;
        private FirebaseAuth auth;
        private FirebaseAuth.AuthStateListener mAuthListener;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                auth = FirebaseAuth.getInstance();

                setContentView(R.layout.activity_login);

                mEmailInput = (EditText) findViewById(R.id.emailInput);
                mPasswordInput = (EditText) findViewById(R.id.passwordInput);
                mLoginButton = (Button) findViewById(R.id.loginButton);
                mRegisterButton = (Button) findViewById(R.id.registerButton);
                mCancelButton = (Button) findViewById(R.id.cancelButton);

                //어플 완전 종료
                mCancelButton.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                        }
                });
                //사용자 등록
                mRegisterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                startRegister();
                        }
                });
                //로그인 시도
                mLoginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                startSigning();
                        }
                });
        }

        //사용자 등록
        private void startRegister() {

                String email = mEmailInput.getText().toString().trim();
                String password = mPasswordInput.getText().toString().trim();

                if (!validateForm()) {
                        return;
                }

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(LoginActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "createUser failed" + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                }
                        }
                });
/*                mAuthListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email = mEmailInput.getText().toString().trim();
                                String password = mPasswordInput.getText().toString().trim();

                                if (user != null) {
                                        //이메일 확인을 안했다면
                                        if (!user.isEmailVerified()) {
                                                //이메일 발송
                                                user.sendEmailVerification();
                                                Toast.makeText(LoginActivity.this, "Check your email first...", Toast.LENGTH_LONG).show();
                                        }
                                        //이메일을 확인한 사람이면
                                        else {
                                                //계정 만들기
                                                auth.createUserWithEmailAndPassword(email, password)
                                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        Toast.makeText(LoginActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                                        // If sign in fails, display a message to the user. If sign in succeeds
                                                                        // the auth state listener will be notified and logic to handle the
                                                                        // signed in user can be handled in the listener.
                                                                        if (!task.isSuccessful()) {
                                                                                Toast.makeText(LoginActivity.this, "createUser failed." + task.getException(),
                                                                                        Toast.LENGTH_SHORT).show();
                                                                        }
                                                                }
                                                        });
                                        }
                                } else {
                                        // User is signed out
                                }
                        }
                };*/
        }

        //로그인 진행
        private void startSigning() {
                String email = mEmailInput.getText().toString();
                String password = mPasswordInput.getText().toString();

                if (!validateForm()) {
                        return;
                }
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                FirebaseUser user = auth.getCurrentUser();

                                //이메일을 확인하지 않았으면
                                /*if (!user.isEmailVerified()) {
                                        Toast.makeText(LoginActivity.this, "Check your email first...", Toast.LENGTH_LONG).show();
                                } else {//확인했으면*/
                                if (!task.isSuccessful()) {
                                        // there was an error
                                        Toast.makeText(LoginActivity.this, "Sign In Problem", Toast.LENGTH_LONG).show();
                                }
                                //로그인이 성공적으로 진행되면
                                else {
                                        Toast.makeText(LoginActivity.this, "You are in =)", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                                        startActivity(intent);
                                        finish();
                                }

                                //}
                        }
                });
/*                mAuthListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                String email = mEmailInput.getText().toString();
                                String password = mPasswordInput.getText().toString();
                                if (user != null) {
                                        auth.signInWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                                FirebaseUser user = auth.getCurrentUser();

                                                                //이메일을 확인하지 않았으면
                                                                if (!user.isEmailVerified()) {
                                                                        Toast.makeText(LoginActivity.this, "Check your email first...", Toast.LENGTH_LONG).show();
                                                                } else {//확인했으면
                                                                        if (!task.isSuccessful()) {
                                                                                // there was an error
                                                                                Toast.makeText(LoginActivity.this, "Sign In Problem", Toast.LENGTH_LONG).show();
                                                                        }
                                                                        //로그인이 성공적으로 진행되면
                                                                        else {
                                                                                Toast.makeText(LoginActivity.this, "You are in =)", Toast.LENGTH_LONG).show();
                                                                        }
                                                                        Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                }

                                                        }


                                                });
                                }
                        }
                };*/

        }

        private boolean validateForm() {
                boolean valid = true;
                String email = mEmailInput.getText().toString();
                String password = mPasswordInput.getText().toString();

                //이메일 칸이랑 패스워드 칸 비워둔 경우
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter email address and password!", Toast.LENGTH_SHORT).show();
                        valid = false;
                        return valid;
                }
                //이메일 칸 비워둔 경우
                else if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        valid = false;
                        return valid;
                }
                //패스워드 칸 비워둔 경우
                if (!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))) {
                        //비밀 번호가 너무 짧은 경우
                        if (password.length() < 6) {
                                Toast.makeText(getApplicationContext(), "Password is too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                                valid = false;
                                return valid;
                        } else
                                return valid;
                }
                return valid;
        }

}
