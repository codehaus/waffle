package com.thoughtworks.waffle.example.paranamer;

import com.thoughtworks.waffle.view.View;

import java.util.List;
import java.util.ArrayList;

public class AjaxExample {
    private final List<Item> selected;
    private final List<Item> available;

    public AjaxExample() {
        selected = new ArrayList<Item>();
        available = new ArrayList<Item>();

        available.add(new Item(1, "one"));
        available.add(new Item(2, "two"));
        available.add(new Item(3, "three"));
        available.add(new Item(4, "four"));
        available.add(new Item(5, "five"));
    }
    public List<Item> getSelected() {
        return selected;
    }

    public List<Item> getAvailable() {
        return available;
    }

    public View add(int index) {
        AjaxExample.Item item = available.remove(index);
        selected.add(item);

        return new View("foo.jspx", this);
    }

    public View remove(int index) {
        AjaxExample.Item item = selected.remove(index);
        available.add(item);

        return new View("foo.jspx", this);
    }

    public class Item {
        private int id;
        private String name;

        public Item(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
