package com.crediya.api;

public final class Constants {
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String MUST_PROVIDE_VALID_CREDENTIALS = "You must provide valid credentials";
    public static final String FORBIDDEN = "Forbidden";
    public static final String YOU_DONT_HAVE_PERMISSION_TO_ACCESS = "You do not have permission to access this resource";
    public static final String GRANTED_AUTHORITY = "Granted authority:";
    public static final String AUTHORIZATION_CHECK = "Authorization check: user={}, path={}, method={}, allowed={}";
    public static final String NO_PERMISSIONS_CLAIM = "No permissions claim in token for user={}";

    private Constants() {}
}
