/*
 * Copyright 2018 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca13b.conbon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import ca13b.conbon.model.User;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import io.realm.todo.R;

import static ca13b.conbon.Constants.AUTH_URL;

public class WelcomeActivity extends AppCompatActivity {

    private EditText nameView;
    private EditText passwordView;
    private EditText orgView;
    private CheckBox isNewUser;
    private View progressView;
    private View loginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (SyncUser.current() != null) {
            SyncUser.current().logOut();
           // setUpRealmAndGoToTaskListActivity();
        }

        // Set up the login form.
        nameView = findViewById(R.id.name);
        passwordView = findViewById(R.id.password);
        orgView = findViewById(R.id.org);
        isNewUser = findViewById(R.id.chkNewUser);
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> attemptLogin());
        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        // Reset errors.
        nameView.setError(null);
        passwordView.setError(null);
        // Store values at the time of the login attempt.
        String name = nameView.getText().toString();
        String password = passwordView.getText().toString();
        String org = orgView.getText().toString();
        showProgress(true);

        SyncCredentials credentials = SyncCredentials.usernamePassword(name, password, isNewUser.isChecked());
        SyncUser.logInAsync(credentials, AUTH_URL, new SyncUser.Callback<SyncUser>() {
            @Override
            public void onSuccess(SyncUser user) {
                showProgress(false);
                setUpRealmAndGoToTaskListActivity();
                ConbonApplication.currentUser = new User();
                ConbonApplication.currentUser.setName(name);
                ConbonApplication.currentUser.setOrg(org);
            }

            @Override
            public void onError(ObjectServerError error) {
                showProgress(false);
                nameView.setError("Uh oh something went wrong! (check your logcat please)");
                nameView.requestFocus();
                Log.e("Login error", error.toString());
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        loginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void setUpRealmAndGoToTaskListActivity() {
        SyncConfiguration configuration = SyncUser.current().getDefaultConfiguration();
        Realm.setDefaultConfiguration(configuration);
        Intent intent = new Intent(WelcomeActivity.this, ProjectsActivity.class);
        startActivity(intent);
    }
}

