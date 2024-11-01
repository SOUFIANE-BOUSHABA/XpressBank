package org.example.xpresbank.Utils;

import org.example.xpresbank.Entity.User;

public class AuthUtils {

    public static boolean hasRole(User user, String roleName) {
        return user.getRole().getName().name().equalsIgnoreCase(roleName);
    }

    public static boolean hasPermission(User user, String permissionName) {
        return user.getRole().getPermissions().stream()
                .anyMatch(permission -> permission.getName().equalsIgnoreCase(permissionName));
    }
}
