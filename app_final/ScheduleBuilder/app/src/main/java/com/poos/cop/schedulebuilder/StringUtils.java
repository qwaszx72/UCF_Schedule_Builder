package com.poos.cop.schedulebuilder;

/**
 * Created by timothy on 3/27/17.
 */

public class StringUtils {

    public static boolean matches(String a, String b) {
        return a.toLowerCase().equals(b.toLowerCase());
    }

    public static boolean matchesOrEmpty(String a, String b) {
        return a.length() == 0 || b.length() == 0 || matches(a, b);
    }

    public static boolean approximatelyMatches(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();

        int[][] memo = new int[a.length() + 1][b.length() + 1];

        for(int i = 0; i <= a.length(); i++)
            memo[i][0] = i;
        for(int i = 0; i <= b.length(); i++)
            memo[0][i] = i;

        for(int i = 1; i <= a.length(); i++) {
            for(int j = 1; j <= b.length(); j++) {
                char ca = a.charAt(i - 1);
                char cb = b.charAt(j - 1);
                if(ca == cb)
                    memo[i][j] = memo[i - 1][j - 1];
                else
                    memo[i][j] = 1 + Math.min(memo[i - 1][j - 1], Math.min(memo[i][j - 1], memo[i - 1][j]));
            }
        }

        int lim = Math.max(2, (a.length() + b.length()) / 10);
        if(Math.min(a.length(), b.length()) <= 5)
            lim = 1;

        return memo[a.length()][b.length()] <= lim;
    }

    public static boolean approximatelyMatchesOrEmpty(String a, String b) {
        return a.length() == 0 || b.length() == 0 || approximatelyMatches(a, b);
    }

    public static boolean approximatelyTokenMatches(String a, String b) {
        String[] tokenAs = a.split(" ");
        String[] tokenBs = b.split(" ");
        for(String tokenA : tokenAs) {
            tokenA = tokenA.trim();
            if(tokenA.length() == 0) continue;
            boolean matchesToken = false;
            for(String tokenB : tokenBs) {
                if(approximatelyMatches(tokenA, tokenB)) {
                    matchesToken = true;
                    break;
                }
            }
            if(!matchesToken)
                return false;
        }
        return true;
    }

    public static int compareTimes(String time1, String time2) {
        if(time1 == null)
            return time2 == null ? -1 : 0;
        if(time2 == null)
            return 1;
        time1 = time1.replace(" ", "").toUpperCase();
        time2 = time2.replace(" ", "").toUpperCase();
        if(time1.length() < 2)
            return -1;
        if(time2.length() < 2)
            return 1;
        String ampm1 = time1.substring(time1.length() - 2);
        String ampm2 = time2.substring(time2.length() - 2);
        if(ampm1.equals(ampm2)) {
            time1 = time1.substring(0, time1.length() - 2);
            time2 = time2.substring(0, time2.length() - 2);
            String[] tmp1 = time1.split(":");
            String[] tmp2 = time2.split(":");
            if(tmp1 == null || tmp1.length != 2)
                return -1;
            if(tmp2 == null || tmp2.length != 2)
                return 1;
            try {
                int h1 = Integer.parseInt(tmp1[0]) % 12;
                int m1 = Integer.parseInt(tmp1[1]);

                int h2 = Integer.parseInt(tmp2[0]) % 12;
                int m2 = Integer.parseInt(tmp2[1]);

                if(h1 == h2)
                    return m1 - m2;
                else
                    return h1 - h2;
            } catch (Exception e) {
                return 0;
            }
        } else if(ampm1.equals("AM")) {
            return -1;
        } else {
            return 1;
        }
    }

}
