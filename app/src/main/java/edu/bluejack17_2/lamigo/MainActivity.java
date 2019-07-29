package edu.bluejack17_2.lamigo;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity {

    Button registerButton,loginButton,loginFB , loginGoogle;
    EditText txtemail,txtpassword;
    AnimationDrawable gradient;
    ConstraintLayout constraintLayout;
    private String email , password;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();



   // GoogleSignInClient gsc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
//                requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail().build();
//
//        gsc = GoogleSignIn.getClient(this, gso);
        setListener();
    }

    public void setListener(){
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        txtemail = (EditText) findViewById(R.id.usernameTxt);
        txtpassword = (EditText) findViewById(R.id.passswordTxt);

        registerButton = findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                register();
            }
        });

//        SignInButton signInButton = findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
//
//        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                switch (view.getId())
////                {
////                    case R.id.sign_in_button:
////                        signIn();
////                        //login();
////                        //Toast.makeText(MainActivity.this, account.getGivenName() , Toast.LENGTH_SHORT).show();
////                        break;
////                }
//                signIn();
//            }
//        });


        loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = txtemail.getText().toString();
                password = txtpassword.getText().toString();

                if(email.equals("") || password.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Email and Password must be filled", Toast.LENGTH_SHORT).show();
                }else {

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // kalo berhasil login
                                login();
                            } else {
                                // kalo gagal login
                                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

//        loginFB  = findViewById(R.id.facebookBtn);
//        loginFB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loginFacebook();
//            }
//        });
    }

    public void register(){
        Intent toRegister = new Intent(this,RegisterActivity.class);
        startActivity(toRegister);
        //finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // kalo sudah ada yang login, result nya akan non null, kalo engga ada yang login akan return null
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(mAuth.getCurrentUser() != null)
        {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            login();
        }
//        else{
//            Toast.makeText(this, "belom ada yang login google", Toast.LENGTH_SHORT).show();
//
//        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == 123) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try{
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//                Toast.makeText(this, account.getDisplayName(), Toast.LENGTH_SHORT).show();
//                //login();;
//                System.out.print("ini 1");
//            }catch (ApiException e){
//                Log.w("google sign in failed" , e);
//                Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show();
//                System.out.print("ini 2");
//            }
//            handleSignInResult(task);
//        }
//    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            login();
//            //updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w("GOOGLE ADA ERROR NIH : ", "signInResult:failed code=" + e.getStatusCode());
//            //updateUI(null);
//        }
//    }

//    private void signIn() {
//        Intent signInIntent = gsc.getSignInIntent();
//        startActivityForResult(signInIntent, 123);
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d("acc id : ", "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("sukses : ", "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            login();
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("gagal", "signInWithCredential:failure", task.getException());
//                           // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }
    public void login(){
        Intent toForum = new Intent(this,ForumActivity.class);
        startActivity(toForum);
        finish();

    }

    public void loginFacebook(){

    }
}
