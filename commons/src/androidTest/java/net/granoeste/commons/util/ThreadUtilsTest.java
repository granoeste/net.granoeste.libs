package net.granoeste.commons.util;

import net.granoeste.commons.util.ThreadUtils;

/**
 * Created by oonishi on 2014/10/16.
 */
public class ThreadUtilsTest {
    public static void main(String[] args) {
        new ThreadUtilsTest().execute();
    }

    public void execute() {
        Class<?> cls = ThreadUtils.calledFromClass();
        System.out.println("ThreadUtilsTest#execute()...");
        System.out.println(cls.getName());
        new InternalClass().execute();
    }

    public static class InternalClass {
        public void execute() {
            Class<?> cls = ThreadUtils.calledFromClass();
            System.out.println("ThreadUtilsTest$InternalClass#execute()...");
            System.out.println(cls.getName());
        }
    }
}
