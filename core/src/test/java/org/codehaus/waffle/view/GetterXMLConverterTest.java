package org.codehaus.waffle.view;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class GetterXMLConverterTest {

    @Test
    public void testChecksIfGetIsAGetter() throws SecurityException,
            NoSuchMethodException {
        Method get = GetterXMLConverterTestClass.class.getMethod("get",
                new Class[0]);
        Assert.assertFalse(GetterXMLConverter.isGetter(get));
    }

    @Test
    public void testChecksIfIsIsAGetter() throws SecurityException,
            NoSuchMethodException {
        Method is = GetterXMLConverterTestClass.class.getMethod("is",
                new Class[0]);
        Assert.assertFalse(GetterXMLConverter.isGetter(is));
    }

    @Test
    public void testChecksIfANonReturnMethodIsAGetter()
            throws SecurityException, NoSuchMethodException {
        Method getVoidProperty = GetterXMLConverterTestClass.class.getMethod(
                "getVoidProperty", new Class[0]);
        Assert.assertFalse(GetterXMLConverter.isGetter(getVoidProperty));
    }

    @Test
    public void testChecksIfAMethodWhichReceivesAParameterIsAGetter()
            throws SecurityException, NoSuchMethodException {
        Method getBizarre = GetterXMLConverterTestClass.class.getMethod(
                "getBizarre", new Class[] { Integer.TYPE });
        Assert.assertFalse(GetterXMLConverter.isGetter(getBizarre));
    }

    @Test
    public void testChecksIfAMethodNotStartingWithGetIsAGetter()
            throws SecurityException, NoSuchMethodException {
        Method bizarreGetter3 = GetterXMLConverterTestClass.class.getMethod(
                "bizarreGetter3", new Class[0]);
        Assert.assertFalse(GetterXMLConverter.isGetter(bizarreGetter3));
    }

    @Test
    public void testChecksIfAnIsMethodReturningStringIsAGetter()
            throws SecurityException, NoSuchMethodException {
        Method isBizarre = GetterXMLConverterTestClass.class.getMethod(
                "isBizarre", new Class[0]);
        Assert.assertFalse(GetterXMLConverter.isGetter(isBizarre));
    }

    @Test
    public void testChecksForAValidGetter() throws SecurityException,
            NoSuchMethodException {
        Method getInternal = GetterXMLConverterTestClass.class.getMethod(
                "getInternal", new Class[0]);
        Assert.assertTrue(GetterXMLConverter.isGetter(getInternal));
    }

    @Test
    public void testChecksForAValidIs() throws SecurityException,
            NoSuchMethodException {
        Method isClosed = GetterXMLConverterTestClass.class.getMethod(
                "isClosed", new Class[0]);
        Assert.assertTrue(GetterXMLConverter.isGetter(isClosed));
    }

    @Test
    public void testChecksForAStaticMethodGetter() throws SecurityException,
            NoSuchMethodException {
        Method getStatic = GetterXMLConverterTestClass.class.getMethod(
                "getStatic", new Class[0]);
        Assert.assertFalse(GetterXMLConverter.isGetter(getStatic));
    }

    @Test
    public void testGetGettersIgnoresGetClass() {
        Map<String, Method> x = GetterXMLConverter
                .getGetters(GetterXMLConverterTestClass.class);
        Assert.assertFalse(x.containsKey("class"));
    }

    @Test
    public void testGetGettersIgnoresGettersAndIsersWithoutAName() {
        Map<String, Method> x = GetterXMLConverter
                .getGetters(GetterXMLConverterTestClass.class);
        Assert.assertFalse(x.containsKey(""));
    }

    @Test
    public void testGetGettersIgnoresGettersReturningVoid() {
        Map<String, Method> x = GetterXMLConverter
                .getGetters(GetterXMLConverterTestClass.class);
        Assert.assertFalse(x.containsKey("voidProperty"));
    }

    @Test
    public void testGetGettersFindsIs() {
        Map<String, Method> x = GetterXMLConverter
                .getGetters(GetterXMLConverterTestClass.class);
        Assert.assertTrue(x.containsKey("closed"));
    }

    @Test
    public void testGetGettersForCapsPROPERTIES() {
        Map<String, Method> x = GetterXMLConverter
                .getGetters(GetterXMLConverterTestClass.class);
        Assert.assertTrue(x.containsKey("URLocationFoo"));
    }

    @Test
    public void testGetGettersForFieldWithLiength1() {
        Map<String, Method> x = GetterXMLConverter
                .getGetters(GetterXMLConverterTestClass.class);
        Assert.assertTrue(x.containsKey("a"));
    }

    public static class GetterXMLConverterTestClass {

        @SuppressWarnings("unused")
        private int internal;

        private boolean closed;

        public int getA() {
            return 0;
        }

        public void getVoidProperty() {
        }

        public void simpleMethod() {
        }

        public String getURLocationFoo() {
            return "";
        }

        public String is() {
            return null;
        }

        public void simpleWrongMethod() {
            @SuppressWarnings("unused")
            int i = 1 / 0;
        }

        public void argumentMethod(int i) {
        }

        public String isBizarre() {
            return null;
        }

        @SuppressWarnings("unused")
        private String value;

        public void setValue(String value) {
            this.value = value;
        }

        public static int getStatic() {
            return 0;
        }

        protected int getProtected() {
            return 0;
        }

        public int getInternal() {
            return internal;
        }

        public boolean isClosed() {
            return closed;
        }

        public void bizarreGetter1() {
        }

        public int bizarreGetter2(int x) {
            return x;
        }

        public int bizarreGetter3() {
            return 0;
        }

        public int getBizarre(int x) {
            return x;
        }

        public void get() {

        }

    }

}
