package com.fragrant.minecraft.util;

import org.apache.commons.lang3.StringUtils;

public class Identifier implements Comparable<Identifier> {
    protected final String namespace;
    protected final String path;

    protected Identifier(String[] strings) {
        this.namespace = StringUtils.isEmpty(strings[0]) ? "minecraft" : strings[0];
        this.path = strings[1];
        if (!isNamespaceValid(this.namespace)) {
            throw new IllegalArgumentException("Non [a-z0-9_.-] character in namespace of location: " + this.namespace + ":" + this.path);
        } else if (!isPathValid(this.path)) {
            throw new IllegalArgumentException("Non [a-z0-9/._-] character in path of location: " + this.namespace + ":" + this.path);
        }
    }

    public Identifier(String string) {
        this(split(string, ':'));
    }

    protected static String[] split(String string, char c) {
        String[] strings = new String[]{"minecraft", string};
        int i = string.indexOf(c);
        if (i >= 0) {
            strings[1] = string.substring(i + 1);
            if (i >= 1) {
                strings[0] = string.substring(0, i);
            }
        }

        return strings;
    }

    public String toString() {
        return this.namespace + ":" + this.path;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Identifier identifier)) {
            return false;
        } else {
            return this.namespace.equals(identifier.namespace) && this.path.equals(identifier.path);
        }
    }

    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    public int compareTo(Identifier identifier) {
        int i = this.path.compareTo(identifier.path);
        if (i == 0) {
            i = this.namespace.compareTo(identifier.namespace);
        }

        return i;
    }

    private static boolean isPathValid(String string) {
        for(int i = 0; i < string.length(); ++i) {
            if (!isPathCharacterValid(string.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private static boolean isNamespaceValid(String string) {
        for(int i = 0; i < string.length(); ++i) {
            if (!isNamespaceCharacterValid(string.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isPathCharacterValid(char c) {
        return c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '/' || c == '.';
    }

    private static boolean isNamespaceCharacterValid(char c) {
        return c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '.';
    }
}