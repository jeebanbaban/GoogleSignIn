package com.ingreens.googlesignin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private EditText etname,etnickname;
    private Button register;
    DbInterface dbInterface;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences=getSharedPreferences(AllKeys.SP_INSTANCE_NAME,MODE_PRIVATE);
        dbInterface=new DbInterface(this);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        //btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        //btnRevokeAccess.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());

    }


    private void signIn() {
        System.out.println("********************* SignIn()");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                        System.out.println("logout status=="+status.getStatus());
                        System.out.println("logout status_code=="+status.getStatusCode());
                        System.out.println("logout status_message=="+status.getStatusMessage());
                        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                        updateUI(false);
                    }
                });
    }

   /* private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }*/

    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            final String account_id=acct.getId();

            if(dbInterface.isUserExists(account_id)){
                displayProfile(acct);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@ getByAccountID()");
            }
            else {
                System.out.println("%%%%%%%%%%%%%%%%%%%%%% AccountID pachhe na..");

                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(MainActivity.this);
                View parentView=getLayoutInflater().inflate(R.layout.dialog_signup,null);
                bottomSheetDialog.setContentView(parentView);
                bottomSheetDialog.show();
                Button btnSignup=parentView.findViewById(R.id.btnSignUp);
                Button btnCancel=parentView.findViewById(R.id.btnCancel);
                final EditText etname=parentView.findViewById(R.id.etName);
                final EditText etnickname=parentView.findViewById(R.id.etNickname);

                btnSignup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RegistrationModel user=new RegistrationModel();
                        user.setAccount_id(account_id);
                        user.setName(etname.getText().toString());
                        user.setNickname(etnickname.getText().toString());
                        dbInterface.insertUser(user);
                        bottomSheetDialog.dismiss();

                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

            }




            /*RegistrationModel user=new RegistrationModel();
            if (user.getAccount_id()==null){
                System.out.println("$$$$$$$$$###@#@@@@@@@@  user.getAccount_id()==null");
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(MainActivity.this);
                View parentView=getLayoutInflater().inflate(R.layout.activity_register,null);
                bottomSheetDialog.setContentView(parentView);
                bottomSheetDialog.show();
                String name=etname.getText().toString();
                String nickname=etnickname.getText().toString();
                user.setAccount_id(account_id);
                user.setName(name);
                user.setNickname(nickname);
                bottomSheetDialog.dismiss();

            }*/

        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void displayProfile(GoogleSignInAccount acct){
        System.out.println("##################################");
        Log.e(TAG, "display name: " + acct.getDisplayName());
        System.out.println("##################################");
        String personName = acct.getDisplayName();
        String personPhotoUrl = acct.getPhotoUrl().toString();
        String email = acct.getEmail();

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        Log.e(TAG, "Name: " + personName + ", email: " + email
                + ", Image: " + personPhotoUrl);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");


        txtName.setText(personName);
        txtEmail.setText(email);
        Glide.with(getApplicationContext()).load(personPhotoUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfilePic);
        updateUI(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                //String user_id= String.valueOf(dbInterface.getUser(id));
               /* if (user_id!=null){
                    signIn();
                }*/
                signIn();
                break;

            case R.id.btn_sign_out:
                signOut();
                break;

            case R.id.btn_revoke_access:
                //revokeAccess();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$   onActivityResult()");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
       /* if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("@#$%&%$%#$#$#$#$#$#$#$#$#$@#@%$@$#@#@@");
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        System.out.println("@#$%&%$%#$#$#$#$#$#$#$#$#$@#@%$@$#@#@@");

    }

    /*private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading..");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }*/

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
           // btnRevokeAccess.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            //btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
        }
    }
}