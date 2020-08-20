package test.util.convertor;

import org.scopemvc.util.convertor.LongStringConvertor;
import org.scopemvc.util.convertor.StringConvertors;

/**
 * Demonstrates custom factory with reload strategy.
 */
public class StringConvertorsConfig extends StringConvertors {

    @Override
    public void initConvertors() {
        //does not use default implementation from super

        registerConvertor(Long.TYPE, new LongStringConvertor());
        registerConvertor(Long.class, new LongStringConvertor());
    }
}
