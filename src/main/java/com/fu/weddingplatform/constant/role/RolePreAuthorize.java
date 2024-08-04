package com.fu.weddingplatform.constant.role;

public final class RolePreAuthorize {
    public static final String ROLE_ADMIN = "hasRole('ADMIN')";
    public static final String ROLE_STAFF = "hasRole('STAFF')";
    public static final String ROLE_SUPPLIER = "hasRole('SUPPLIER')";
    public static final String IS_AUTHENTICATED = "isAuthenticated()";
    public static final String ROLE_ADMIN_STAFF = "hasRole('ADMIN') or hasRole('STAFF')";
    public static final String ROLE_ADMIN_COUPLE = "hasRole('ADMIN') or hasRole('COUPLE')";
    public static final String ROLE_ADMIN_COUPLE_SUPPLIER = "hasRole('ADMIN') or hasRole('COUPLE') or hasRole('SUPPLIER')";
    public static final String ROLE_ADMIN_SUPPLIER = "hasRole('ADMIN') or hasRole('SUPPLIER')";
    public static final String ROLE_COUPLE = "hasRole('COUPLE')";
}