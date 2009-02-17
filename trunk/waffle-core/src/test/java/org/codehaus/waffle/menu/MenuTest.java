package org.codehaus.waffle.menu;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.waffle.menu.Menu.Entry;
import org.codehaus.waffle.menu.Menu.Group;
import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class MenuTest {

    @Test
    public void canCreateMenu() {
        Map<String, List<String>> content = new HashMap<String, List<String>>();
        content.put("Group1", asList("entry11:path11", "entry12:path12"));
        content.put("Group2", asList("entry21:path21", "entry22:path22"));
        Menu menu = new Menu(content);
        List<Group> groups = menu.getGroups();
        assertEquals(2, groups.size());
        assertEquals("Group1", groups.get(0).getTitle());
        assertEquals("Group2", groups.get(1).getTitle());
        List<Entry> entries1 = groups.get(0).getEntries();
        assertEquals("entry11", entries1.get(0).getTitle());
        assertEquals("entry12", entries1.get(1).getTitle());
        assertEquals("path11", entries1.get(0).getPath());
        assertEquals("path12", entries1.get(1).getPath());
        List<Entry> entries2 = groups.get(1).getEntries();
        assertEquals("entry21", entries2.get(0).getTitle());
        assertEquals("entry22", entries2.get(1).getTitle());
        assertEquals("path21", entries2.get(0).getPath());
        assertEquals("path22", entries2.get(1).getPath());
    }

    @Test
    public void canCreateMenuAwareController() {
        Menu menu = new Menu();
        MenuAwareController controller = new MenuAwareController(menu);
        assertEquals(menu, controller.getMenu());
    }

}
