package org.codehaus.waffle.action.intercept;

import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.codehaus.waffle.controller.ControllerDefinition;
import org.junit.Test;

public class MethodInterceptorComparatorTest  {

    @Test
    public void canSortCollectionOfSortable() {
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

    @Test
    public void canSortCollectionWithNonSortable() {
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

        public Integer getIndex() {
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
