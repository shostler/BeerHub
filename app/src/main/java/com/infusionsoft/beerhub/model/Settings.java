package com.infusionsoft.beerhub.model;

import io.realm.RealmObject;

/**
 * File description here...
 */
public class Settings extends RealmObject {

    private boolean initialized = false;

    public Settings() {
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
