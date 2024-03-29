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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.SyncUser;
import io.realm.log.RealmLog;
import io.realm.todo.R;
import ca13b.conbon.model.Project;
import ca13b.conbon.ui.ProjectsRecyclerAdapter;

public class ProjectsActivity extends AppCompatActivity {
    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        //setSupportActionBar(findViewById(R.id.toolbar));

        findViewById(R.id.fab).setOnClickListener(view -> {
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_task, null);
            ((EditText) dialogView.findViewById(R.id.task)).setHint(R.string.project_description);
            EditText taskText = dialogView.findViewById(R.id.task);
            new AlertDialog.Builder(ProjectsActivity.this)
                    .setTitle("Add a new project")
                    .setView(dialogView)
                    .setPositiveButton("Add", (dialog, which) -> realm.executeTransactionAsync(realm -> {
                        String userId = SyncUser.current().getIdentity();
                        String name = taskText.getText().toString();

                        Project project = new Project();
                        project.setId(UUID.randomUUID().toString());
                        project.setOwner(userId);
                        project.setName(name);
                        project.setOrg(ConbonApplication.currentUser.getOrg());
                        project.setTimestamp(new Date());
                        realm.insert(project);

                    }, error -> RealmLog.error(error) ))
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        });

        realm = Realm.getDefaultInstance();

        //add the User object, which contains additional metadata about the user
        realm.executeTransactionAsync(realm -> {
            ConbonApplication.currentUser.setSyncUserId(SyncUser.current().getIdentity());
            realm.insertOrUpdate(ConbonApplication.currentUser);
        }, error -> RealmLog.error(error) );

        // Create a  subscription that only download the org's projects from the server.

        RealmResults<Project> projects = realm
                .where(Project.class)
                .equalTo("org", ConbonApplication.currentUser.getOrg())
                .sort("timestamp", Sort.DESCENDING)
                .findAllAsync();

        final ProjectsRecyclerAdapter itemsRecyclerAdapter = new ProjectsRecyclerAdapter(this, projects);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemsRecyclerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SyncUser syncUser = SyncUser.current();
            if (syncUser != null) {
                syncUser.logOut();
                Intent intent = new Intent(this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
