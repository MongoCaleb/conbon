package ca13b.conbon.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {

    @Required
    private String userId;
    @PrimaryKey
    @Required
    private String syncUserId;
    @Required
    private String name;
    private String org;

    //no setter for ID; created as UUID in xtor
    public String getUserId() {
        return this.userId;
    }

    public String getSyncUserId() { return this.syncUserId; }
    public void setSyncUserId(String syncUserId) {  this.syncUserId = syncUserId; }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getOrg(){return this.org;}
    public void setOrg(String org){ this.org = org;}

    public User() {
        this.userId = UUID.randomUUID().toString();
    }
}
