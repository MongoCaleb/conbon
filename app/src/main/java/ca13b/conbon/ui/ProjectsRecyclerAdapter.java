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


package ca13b.conbon.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import ca13b.conbon.TasksActivity;
import ca13b.conbon.model.Project;
import io.realm.todo.R;

public class ProjectsRecyclerAdapter extends RealmRecyclerViewAdapter<Project, ProjectsRecyclerAdapter.MyViewHolder> {
    private final Context context;

    public ProjectsRecyclerAdapter(Context context, OrderedRealmCollection<Project> data) {
        super(data, true);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projects_list_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)  {
        final Project project = getItem(position);
        if (project != null) {
            holder.tvTitle.setText(project.getName());
            holder.tvBody.setText(String.valueOf(project.getTasks().size()) + " task(s) in this project");
            holder.taskCard.setOnClickListener(v -> {
                Intent intent = new Intent(context, TasksActivity.class);
                intent.putExtra(TasksActivity.INTENT_EXTRA_PROJECT_ID, project.getId());
                context.startActivity(intent);
            });
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvBody;
        CardView taskCard;

        MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(android.R.id.text1);
            tvBody = itemView.findViewById(android.R.id.text2);
            taskCard = itemView.findViewById(R.id.taskCard);
        }
    }
}
