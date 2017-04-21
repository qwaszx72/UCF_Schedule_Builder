package com.poos.cop.schedulebuilder;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Created by timothy on 3/23/17.
 */

public class ClassUtils {
    public static HashMap<String, String[]> COURSE_NUMBERS_MAP;
    public static HashMap<String, String[]> COURSE_NAMES_MAP;
    public static String[] SUBJECTS;
    public static String[][] COURSE_NUMBERS;
    public static String[][] COURSE_NAMES;
    public static ArrayList<Section> ALL_SECTIONS;
    public static HashSet<SectionPair> CART;

    public static String PREFS_NAME = "prefs";
    public static String PREF_CART = "cart";
    public static String PREF_LAST_DATABASE_UPDATE_TIME = "last_database_update_time";

    public static String COURSES_FILENAME = "courses";

    public static File getCoursesFile(Context context) {
        File file = new File(context.getFilesDir(), COURSES_FILENAME);
        return file;
    }

    public static boolean initCart(Context context) {
        if(CART != null && CART.size() > 0) return true;

        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        String cartStr = preferences.getString(PREF_CART, "");
        String[] cartSections = cartStr.split(";");
        CART = new HashSet<>();
        for(String cartSection : cartSections) {
            String[] tmp = cartSection.split(",");
            if(tmp.length != 2) continue;
            try {
                String term = tmp[0];
                int nbr = Integer.parseInt(tmp[1]);
                SectionPair sectionPair = new SectionPair(term, nbr);
                CART.add(sectionPair);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        return true;
    }

    public static boolean saveCart(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(SectionPair sectionPair : CART) {
            if(!first) sb.append(';');
            else first = false;
            sb.append(sectionPair.term + "," + sectionPair.nbr);
        }
        editor.putString(PREF_CART, sb.toString());

        editor.commit();

        return true;
    }

    public static boolean initCourses(Context context) {
        if(SUBJECTS != null && SUBJECTS.length > 0) return true;

        try {
            Scanner in = new Scanner(context.getResources().getAssets().open("course_list.txt"));

            int numSubjects = in.nextInt();
            SUBJECTS = new String[numSubjects];
            COURSE_NUMBERS = new String[numSubjects][];
            COURSE_NAMES = new String[numSubjects][];

            COURSE_NUMBERS_MAP = new HashMap<>();
            COURSE_NAMES_MAP = new HashMap<>();

            for(int i = 0; i < numSubjects; i++) {
                SUBJECTS[i] = in.next();

                int numNumbers = in.nextInt();
                int numNames = in.nextInt();

                COURSE_NUMBERS[i] = new String[numNumbers];
                COURSE_NAMES[i] = new String[numNames];

                for(int j = 0; j < numNumbers; j++)
                    COURSE_NUMBERS[i][j] = in.next();
                in.nextLine();
                for(int j = 0; j < numNames; j++)
                    COURSE_NAMES[i][j] = in.nextLine().trim();

                COURSE_NUMBERS_MAP.put(SUBJECTS[i], COURSE_NUMBERS[i]);
                COURSE_NAMES_MAP.put(SUBJECTS[i], COURSE_NAMES[i]);
            }

            in.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            SUBJECTS = new String[0];
            COURSE_NUMBERS = new String[0][0];
            COURSE_NUMBERS_MAP = new HashMap<>();

            return false;
        }
    }

    public static ArrayList<Section> getSections(Context context) {
        if(ALL_SECTIONS != null && ALL_SECTIONS.size() > 0)
            return (ArrayList<Section>) ALL_SECTIONS.clone();

        ALL_SECTIONS = new ArrayList<>();

        try {
            //BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("courses.txt")));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getCoursesFile(context))));

            while(true) {
                String line = br.readLine();
                if(line == null) break;
                line = line.trim();
                if(line.length() == 0) continue;

                Section section = new Section(line);
                ALL_SECTIONS.add(section);
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ALL_SECTIONS;
    }

    public static long getLastDatabaseUpdateTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        return preferences.getLong(PREF_LAST_DATABASE_UPDATE_TIME, -1);
    }

    public static boolean updateClassDatabase(Context context, String allCoursesString) {
        File file = getCoursesFile(context);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(allCoursesString);
            bw.close();

            SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(PREF_LAST_DATABASE_UPDATE_TIME, System.currentTimeMillis());
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
