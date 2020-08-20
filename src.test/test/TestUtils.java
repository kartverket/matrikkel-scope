package test;

import org.assertj.core.api.Assertions;
import org.scopemvc.util.ScopeConfig;
import org.scopemvc.util.convertor.StringConvertors;

import java.lang.reflect.Field;
import java.util.Map;
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

    public static void withScopeProperty(String key, Object value, Runnable runnable) {
        final Map<String, Object> scopeProperties = getScopeProperties();
        final Object oldValue = scopeProperties.put(key, value);
        try {
            runnable.run();
        } finally {
            if (oldValue == null) {
                scopeProperties.remove(key);
            } else {
                scopeProperties.put(key, oldValue);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static Map<String, Object> getScopeProperties() {
        try {
            final Field field = ScopeConfig.class.getDeclaredField("properties");
            field.setAccessible(true);
            return (Map) field.get(ScopeConfig.getInstance());
        } catch (Throwable t) {
            Assertions.fail("Test harness failure", t);
        }
        return null;
    }
}
