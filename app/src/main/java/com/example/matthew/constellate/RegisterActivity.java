package com.example.matthew.constellate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import org.json.JSONObject;

public class RegisterActivity extends Activity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mPasswordRepeatView;
    private View mProgressView;
    private View mLoginFormView;

    // Global singleton
    ConstellateGlobals global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Assign global singleton
        global = ((ConstellateGlobals) this.getApplication());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the login form
        mUsernameView           = (EditText) findViewById(R.id.register_username);
        mEmailView              = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView           = (EditText) findViewById(R.id.password);
        mPasswordRepeatView     = (EditText) findViewById(R.id.password_repeat);
        mLoginFormView          = findViewById(R.id.scrollView);
        mProgressView           = findViewById(R.id.progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void attemptRegister(View view) {
        // Reset errors.
        mUsernameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordRepeatView.setError(null);

        // Store values at the time of the login attempt.
        String username         = mUsernameView.getText().toString();
        String email            = mEmailView.getText().toString();
        String password         = mPasswordView.getText().toString();
        String repeatPassword   = mPasswordRepeatView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(repeatPassword)) {
            mPasswordRepeatView.setError(getString(R.string.error_field_required));
            focusView = mPasswordRepeatView;
            cancel = true;
        } else if (!isPasswordValid(password, repeatPassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordRepeatView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_email));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email_forreal));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Setup new user
            global.authenticatedUser = new User(username);

            showProgress(true);
            CallAPI registerAccount = new CallAPI(new CallAPI.ResponseListener() {
                @Override
                public void responseReceived(String response) {
                    try {
                        JSONObject json = new JSONObject(response);

                        if (json.has("user_id")) {
                            showProgress(false);
                            finish();
                        } else {
                            showProgress(false);
                            mUsernameView.setError(getString(R.string.error_invalid_user));
                            mEmailView.setError(getString(R.string.error_invalid_user));
                            mPasswordView.setError(getString(R.string.error_invalid_user));
                        }

                    } catch(Exception e) {
                        showProgress(false);
                        finish();
                    }
                }

            }, global.authenticatedUser.getToken());

            String user = "{\"username\":\"" + username
                    + "\",\"email\":\"" + email
                    + "\",\"password\":\"" + password + "\"}";
            System.out.println(user);
            registerAccount.execute(
                    getString(R.string.api_url),
                    getString(R.string.user_endpoint),
                    "POST", "", user);
        }
    }

    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean isPasswordValid(String password1, String password2) {
        return password1.equals(password2) && password1.length() > 4;
    }

    public boolean isUsernameValid(String username) {
        return username.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
