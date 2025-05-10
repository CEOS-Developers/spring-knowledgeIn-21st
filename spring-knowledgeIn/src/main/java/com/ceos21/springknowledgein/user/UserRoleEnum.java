package com.ceos21.springknowledgein.user;

public enum UserRoleEnum {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }
}
