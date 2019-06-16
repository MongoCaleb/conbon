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


package ca13b.conbon.model;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Item extends RealmObject {
    @PrimaryKey
    @Required
    private String itemId;
    @Required
    private String body;
    @Required
    private String title;
    @Required
    private Integer state;

    private String assigneeId;

    private String creatorId;
    @Required
    private String projectId;
    @Required
    private Date timestamp;

    public String getItemId() {
        return this.itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBody() {
        return this.body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) { this.title = title; }

    public Integer getState() {
        return this.state;
    }
    public void setState(Integer state) {
        this.state = state;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAssigneeId() {
        return this.assigneeId;
    }
    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getCreatorId() {
        return this.creatorId;
    }
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getProjectId() {
        return this.projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Item() {
        this.itemId = UUID.randomUUID().toString();
        this.body = "";
        this.state = 0;
        this.timestamp = new Date();
    }
}
