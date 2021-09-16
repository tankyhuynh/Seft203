package com.kms.seft203.utils;

public class UrlConstraint {
    
    public static final String APP_URL = "/app";
    public static final String APP_VERSION_URL = "/version";
    
    public static final String AUTH_URL = "/auth";
    public static final String AUTH_REGISTER_URL = "/register";
    public static final String AUTH_VERIFY_URL = "http://localhost:8080/auth/register/verify/";
    public static final String AUTH_REGISTER_VERIFY_URL = "/register/verify/{username}";
    public static final String AUTH_LOGIN_URL = "/login";
    public static final String AUTH_LOGOUT_URL = "/logout";
    
    public static final String CONTACTS_URL = "/contacts";
    public static final String CONTACTS_GET_URL = "/{id}";
    public static final String CONTACTS_PUT_URL = "/{id}";
    public static final String CONTACTS_DELETE_URL = "/{id}";
    public static final String CONTACTS_UPLOAD_URL = "/upload";
    public static final String CONTACTS_DOWNLOAD_URL = "/download";
    public static final String CONTACTS_SEARCH_URL = "/search";
    
    public static final String DASHBOARDS_URL = "/dashboards";
    public static final String DASHBOARDS_PUT_URL = "/{id}";
    public static final String DASHBOARDS_DELETE_URL = "/{id}";

    public static final String REPORTS_URL = "/reports";
    public static final String REPORTS_COUNT_BY_FIELD_AND_COLLECTION_URL = "/_countBy/{collection}/{field}";

    public static final String TASKS_URL = "/tasks";
    public static final String TASKS_GET_URL = "/{id}";
    public static final String TASKS_PUT_URL = "/{id}";
    public static final String TASKS_DELETE_URL = "/{id}";
    public static final String TASKS_SEARCH_URL = "/search";
}
