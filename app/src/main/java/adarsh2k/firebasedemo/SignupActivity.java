package adarsh2k.firebasedemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends Activity {

    EditText userEmail, userPass;
    TextView forgotPassword, login;
    Button signUp;
    String email, password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        //initialising the firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        //gives the color to the statusbar if the sdk>=21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.setStatusBarColor(ContextCompat.getColor(this, R.color.signUpOrLoginActivityColor));
        }

        //binding views to their ids
        userEmail = (EditText) findViewById(R.id.userEmail);
        userPass = (EditText) findViewById(R.id.userPass);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        login = (TextView) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.registerButton);

        //firebase auth listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // User is signed in
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {

                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");
                }
            }
        };


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                //startActivity(i);
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i = new Intent(SignupActivity.this,ForgotPasswordActivity.class);
                //startActivity(i);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = userEmail.getText().toString();
                password = userPass.getText().toString();
                userSignUp(email,password);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void userSignUp(String userEmail, String userPassword) {

        if(userEmail.equals("")&&userPassword.equals("")){
            //fields cant be empty
            return;
        }
        if(!userEmail.matches(emailPattern)) {
            //invalid email
            return;
        } else if(userPassword.length() < 6){
            //password must be greater then 6
            return;
        }

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Signing Up, Please Wait...");
        progressDialog.show();

        //firebase auth complete listener

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            //signup task failed

                            progressDialog.hide();
                            //please try after sometime
                        }
                        if (task.isSuccessful()) {

                            //signup successful

                            progressDialog.hide();
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"Signup Completed Successfully",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
    }
}
