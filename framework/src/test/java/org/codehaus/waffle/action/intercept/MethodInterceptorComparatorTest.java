package org.codehaus.waffle.action.intercept;

import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.action.intercept.MethodInterceptor;
import org.codehaus.waffle.action.intercept.Sortable;
import org.codehaus.waffle.action.intercept.InterceptorChain;
import org.codehaus.waffle.action.intercept.MethodInterceptorComparator;
import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MethodInterceptorComparatorTest extends TestCase {

    public void testSortCollectionOfSortable() {
        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();

        MethodInterceptor one = new SortableMethodInterceptor(1);
        MethodInterceptor two = new SortableMethodInterceptor(2);
        MethodInterceptor three = new SortableMethodInterceptor(3);

        interceptors.add(two);
        interceptors.add(three);
        interceptors.add(one);

        Comparator<MethodInterceptor> comparator = new MethodInterceptorComparator();
        Collections.sort(interceptors, comparator);

        assertSame(one, interceptors.get(0));
        assertSame(two, interceptors.get(1));
        assertSame(three, interceptors.get(2));
    }

    public void testSortCollectionWithNonSortable() {
        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();

        MethodInterceptor one = new SortableMethodInterceptor(1);
        MethodInterceptor two = new SortableMethodInterceptor(2);
        MethodInterceptor three = new SortableMethodInterceptor(3);
        MethodInterceptor notSortable = new NotSortableMethodInterceptor();

        interceptors.add(three);
        interceptors.add(notSortable);
        interceptors.add(two);
        interceptors.add(one);

        Comparator<MethodInterceptor> comparator = new MethodInterceptorComparator();
        Collections.sort(interceptors, comparator);

        assertSame(one, interceptors.get(0));
        assertSame(two, interceptors.get(1));
        assertSame(three, interceptors.get(2));
        assertSame(notSortable, interceptors.get(3));
    }

    private static class SortableMethodInterceptor implements MethodInterceptor, Sortable {
        private int index;

        public SortableMethodInterceptor(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public boolean accept(Method method) {
            throw new UnsupportedOperationException("should not be called");
        }

        public Object intercept(ControllerDefinition controllerDefinition,
                                Method method,
                                InterceptorChain chain,
                                Object ... arguments) {
            throw new UnsupportedOperationException("should not be called");
        }
    }

    private static class NotSortableMethodInterceptor implements MethodInterceptor {

        public boolean accept(Method method) {
            throw new UnsupportedOperationException("should not be called");
        }

        public Object intercept(ControllerDefinition controllerDefinition,
                                Method method,
                                InterceptorChain chain,
                                Object ... arguments){
            throw new UnsupportedOperationException("should not be called");
        }
    }

}
