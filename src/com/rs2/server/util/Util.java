package com.rs2.server.util;

import org.jboss.netty.buffer.ChannelBuffer;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cory Nelson
 * on 07/07/14.
 * at 13:26.
 */
public class Util {

    public static final int PLAYER_CHAT_UPDATE_MASK = 0x80;
    public static final int PLAYER_APPEARANCE_UPDATE_MASK = 0x10;


    /**
     * Difference in X coordinates for directions array.
     */
    public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1, 1, -1, 0, 1 };

    /**
     * Difference in Y coordinates for directions array.
     */
    public static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0, 0, -1, -1, -1 };

    public static final int LOGIN_RESPONSE_OK = 2;
    public static final int LOGIN_RESPONSE_INVALID_CREDENTIALS = 3;
    public static final int LOGIN_RESPONSE_ACCOUNT_DISABLED = 4;
    public static final int LOGIN_RESPONSE_ACCOUNT_ONLINE = 5;
    public static final int LOGIN_RESPONSE_UPDATED = 6;
    public static final int LOGIN_RESPONSE_WORLD_FULL = 7;
    public static final int LOGIN_RESPONSE_LOGIN_SERVER_OFFLINE = 8;
    public static final int LOGIN_RESPONSE_LOGIN_LIMIT_EXCEEDED = 9;
    public static final int LOGIN_RESPONSE_BAD_SESSION_ID = 10;
    public static final int LOGIN_RESPONSE_PLEASE_TRY_AGAIN = 11;
    public static final int LOGIN_RESPONSE_NEED_MEMBERS = 12;
    public static final int LOGIN_RESPONSE_COULD_NOT_COMPLETE_LOGIN = 13;
    public static final int LOGIN_RESPONSE_SERVER_BEING_UPDATED = 14;
    public static final int LOGIN_RESPONSE_LOGIN_ATTEMPTS_EXCEEDED = 16;
    public static final int LOGIN_RESPONSE_MEMBERS_ONLY_AREA = 17;

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

    private static char decodeBuf[] = new char[4096];

    public static int hexToInt(byte[] data) {
        int value = 0;
        int n = 1000;
        for (int i = 0; i < data.length; i++) {
            int num = (data[i] & 0xFF) * n;
            value += (int) num;
            if (n > 1) {
                n = n / 1000;
            }
        }
        return value;
    }

    public static String textUnpack(byte packedData[], int size) {
        int idx = 0, highNibble = -1;
        for (int i = 0; i < size * 2; i++) {
            int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
            if (highNibble == -1) {
                if (val < 13)
                    decodeBuf[idx++] = XLATE_TABLE[val];
                else
                    highNibble = val;
            } else {
                decodeBuf[idx++] = XLATE_TABLE[((highNibble << 4) + val) - 195];
                highNibble = -1;
            }
        }

        return new String(decodeBuf, 0, idx);
    }

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

    /**
     * Calculates the direction between the two coordinates.
     *
     * @param dx
     *            the first coordinate
     * @param dy
     *            the second coordinate
     * @return the direction
     */
    public static int direction(int dx, int dy) {
        if (dx < 0) {
            if (dy < 0) {
                return 5;
            } else if (dy > 0) {
                return 0;
            } else {
                return 3;
            }
        } else if (dx > 0) {
            if (dy < 0) {
                return 7;
            } else if (dy > 0) {
                return 2;
            } else {
                return 4;
            }
        } else {
            if (dy < 0) {
                return 6;
            } else if (dy > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * Lengths for the various packets.
     */
    public static final int packetLengths[] = { //
            //
            0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
            0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
            0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
            0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
            2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
            0, 0, 0, 12, 0, 0, 0, 0, 8, 0, // 50
            0, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
            6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
            0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
            0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
            0, 13, 0, -1, 0, 0, 0, 0, 0, 0,// 100
            0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
            1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
            0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
            0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
            0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
            0, 0, 0, 0, -1, -1, 0, 0, 0, 0,// 160
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
            0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
            0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
            2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
            4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
            0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
            1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
            0, 4, 0, 0, 0, 0, -1, 0, -1, 4,// 240
            0, 0, 6, 6, 0, 0, 0 // 250
    };

    /**
     * Reads a RuneScape string from a buffer.
     *
     * @param buf
     *            The buffer.
     * @return The string.
     */
    public static String getRS2String(final ChannelBuffer buf) {
        final StringBuilder bldr = new StringBuilder();
        byte b;
        while (buf.readable() && (b = buf.readByte()) != 10)
            bldr.append((char) b);
        return bldr.toString();
    }
}
