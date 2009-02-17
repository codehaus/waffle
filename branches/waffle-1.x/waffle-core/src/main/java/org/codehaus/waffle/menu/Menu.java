/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents the menu holding the navigation model. Menus entries are divided into groups.
 * Content can be provided as a map, keyed on group title, of lists of entries where each entry
 * is a colon-separated string "title:path".
 * 
 * @author Mauro Talevi
 */
public class Menu {

    private static final String COLON = ":";
    private List<Group> groups;

    public Menu() {
        this(new ArrayList<Group>());
    }

    public Menu(List<Group> groups) {
        this.groups = groups;
    }

    public Menu(Map<String, List<String>> content) {
        this(toGroups(content));
    }

    private static List<Group> toGroups(Map<String, List<String>> content) {
        List<Group> groups = new ArrayList<Group>();
        for (String title : content.keySet()) {
            groups.add(new Group(title, toEntries(content.get(title))));
        }
        return groups;
    }

    private static List<Entry> toEntries(List<String> content) {
        List<Entry> entries = new ArrayList<Entry>();
        for (String entry : content) {
            String[] split = entry.split(COLON);
            if (split.length > 1) {
                entries.add(new Entry(split[0], split[1]));
            }
        }
        return entries;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public static class Group {
        private String title;
        private List<Entry> entries;

        public Group(String title, List<Entry> entries) {
            this.title = title;
            this.entries = entries;
        }

        public String getTitle() {
            return title;
        }

        public List<Entry> getEntries() {
            return entries;
        }

    }

    public static class Entry {

        private String title;
        private String path;

        public Entry(String title, String path) {
            this.title = title;
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public String getPath() {
            return path;
        }

    }
}
