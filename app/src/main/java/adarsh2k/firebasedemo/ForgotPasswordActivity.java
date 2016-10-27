package com.osgrip.demofirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText userEmail;
    TextView signUp;
    Button resetPassword;
    String email;
    private FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //initialising the firebase Auth instance
        auth = FirebaseAuth.getInstance();

        //gives color to status bar if sdk>=21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.forgotPasswordActivityColor));
        }

        //binding views to their respective ids
        userEmail = (EditText) findViewById(R.id.userEmail);
        signUp = (TextView) findViewById(R.id.signUP);
        resetPassword = (Button) findViewById(R.id.resetPassword);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPasswordActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                email = userEmail.getText().toString();
                if(email.equals("")){
                    Snackbar.make(v, "Field can't be empty", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }

                if(!email.matches(emailPattern)){
                    Snackbar.make(v, "Enter valid email id", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }

                //showing progress dialog
                progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
                progressDialog.setMessage("Sending request, Please Wait...");
                progressDialog.show();

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    //reset email sent

                                    progressDialog.hide();
                                    Snackbar.make(v, "Follow your mail to reset your password", 3000)
                                            .setAction("Action", null).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                                        }
                                    },3000 );
                                } else {

                                    //error in the email provided with the signup email id

                                    progressDialog.hide();
                                    Snackbar.make(v, "Wrong email address", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                }
                            }
                        });
            }
        });
    }


}
