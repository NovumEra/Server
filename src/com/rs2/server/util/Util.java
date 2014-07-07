package com.rs2.server.util;

/**
 * Created by Cory Nelson
 * on 07/07/14.
 * at 13:26.
 */
public class Util {

    public static final int PLAYER_CHAT_UPDATE_MASK = 0x80;
    public static final int PLAYER_APPEARANCE_UPDATE_MASK = 0x10;

    public static final int EQUIPMENT_SLOT_HEAD = 0;
    public static final int EQUIPMENT_SLOT_CAPE = 1;
    public static final int EQUIPMENT_SLOT_AMULET = 2;
    public static final int EQUIPMENT_SLOT_WEAPON = 3;
    public static final int EQUIPMENT_SLOT_CHEST = 4;
    public static final int EQUIPMENT_SLOT_SHIELD = 5;
    public static final int EQUIPMENT_SLOT_LEGS = 7;
    public static final int EQUIPMENT_SLOT_HANDS = 9;
    public static final int EQUIPMENT_SLOT_FEET = 10;
    public static final int EQUIPMENT_SLOT_RING = 12;
    public static final int EQUIPMENT_SLOT_ARROWS = 13;

    public static final int APPEARANCE_SLOT_CHEST = 0;
    public static final int APPEARANCE_SLOT_ARMS = 1;
    public static final int APPEARANCE_SLOT_LEGS = 2;
    public static final int APPEARANCE_SLOT_HEAD = 3;
    public static final int APPEARANCE_SLOT_HANDS = 4;
    public static final int APPEARANCE_SLOT_FEET = 5;
    public static final int APPEARANCE_SLOT_BEARD = 6;

    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;

    /**
     * An array of valid characters in a long username.
     */
    public static final char VALID_CHARS[] = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
            '-', '+', '=', ':', ';', '.', '>', '<', ',', '"', '[', ']', '|', '?', '/', '`' };

    /**
     * Packed text translate table.
     */
    public static final char XLATE_TABLE[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p',
            'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-',
            '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']' };


    /**
     * Converts the username to a long value.
     *
     * @param s
     *            the username
     * @return the long value
     */
    public static long nameToLong(String s) {
        long l = 0L;
        for (int i = 0; i < s.length() && i < 12; i++) {
            char c = s.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z')
                l += (1 + c) - 65;
            else if (c >= 'a' && c <= 'z')
                l += (1 + c) - 97;
            else if (c >= '0' && c <= '9')
                l += (27 + c) - 48;
        }
        while (l % 37L == 0L && l != 0L)
            l /= 37L;
        return l;
    }

    /**
     * Converts a long to a name.
     *
     * @param l
     *            The long.
     * @return The name.
     */
    public static String longToName(long l) {
        int i = 0;
        char ac[] = new char[12];
        while (l != 0L) {
            long l1 = l;
            l /= 37L;
            ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
        }
        return new String(ac, 12 - i, i);
    }
}
