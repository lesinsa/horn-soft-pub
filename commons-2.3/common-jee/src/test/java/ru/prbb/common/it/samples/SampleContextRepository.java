package ru.prbb.common.it.samples;

import ru.prbb.common.jaxb.ContextDef;
import ru.prbb.common.jaxb.ContextRepository;

import java.util.Arrays;
import java.util.List;

/**
 * @author lesinsa on 21.05.2015.
 */
@ContextRepository
public class SampleContextRepository extends BaseSampleRepo {

    public static final String CONTEXT_1 = "CONTEXT_1";
    public static final String CONTEXT_2 = "CONTEXT_2";
    public static final String CONTEXT_3 = "CONTEXT_3";

    @ContextDef(name = CONTEXT_1)
    public Class[] method1() {
        return new Class[]{Class1.class};
    }

    @ContextDef(name = CONTEXT_2)
    public List<? extends Class> method2() {
        return Arrays.asList(Class2.class);
    }

    @ContextDef(name = CONTEXT_3)
    public Class method3() {
        return Class3.class;
    }
}
