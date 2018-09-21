package com.lvmama.tnt.bms.admin.web.manage.domain;

import java.io.Serializable;

/**
 *
 */
public class ResourceDTO implements Serializable {
    private static final long serialVersionUID = 1214444054579691401L;

    private String title;
    private String path;
    private String index;
    private String permissions;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
