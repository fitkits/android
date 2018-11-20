package com.fitkits.Model;

import android.util.Log;
import android.widget.Toast;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {

    public Migration() {
        super();
        Log.e("RAGHU","Migration Constructor");
    }
    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Migration);
    }

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        Log.e("RAGHU",oldVersion + "/" +newVersion);

        if (oldVersion == 0 || oldVersion == 1) {
            RealmObjectSchema personSchema = schema.get("User");
            Log.e("RAGHU", personSchema.toString());
            if(!personSchema.hasField("email")) {
                personSchema.addField("email", String.class);
            }

            RealmObjectSchema membershipItemSchema = schema.get("MembershipItem");
            if(!membershipItemSchema.hasField("enabled")) {
                membershipItemSchema.addField("enable",Boolean.class);

            }
            oldVersion++;
        }
    }
}