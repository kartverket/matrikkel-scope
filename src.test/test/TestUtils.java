package test;

import org.assertj.core.api.Assertions;
import org.scopemvc.util.ScopeConfig;
import org.scopemvc.util.convertor.StringConvertors;

import java.lang.reflect.Field;
import java.util.ResourceBundle;

public class TestUtils {

    public static void withScopeConfig(Class<? extends ResourceBundle> resourceBundleClass, Runnable runnable) {
        try {
            ScopeConfig.setPropertiesName(resourceBundleClass.getName());
            clearStringConvertorsInstance();

            runnable.run();
        } finally {
            ScopeConfig.setPropertiesName(org.scopemvc.util.DefaultScopeConfig.class.getName());
            clearStringConvertorsInstance();
        }
    }

    static void clearStringConvertorsInstance() {
        try {
            synchronized (StringConvertors.class) {
                final Field field = StringConvertors.class.getDeclaredField("instance");
                field.setAccessible(true);
                field.set(null, null);
            }
        } catch (Throwable t) {
            Assertions.fail("Test harness failure", t);
        }
    }
}
