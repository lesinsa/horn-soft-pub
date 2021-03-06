package ru.prbb.common.it.samples;

import ru.prbb.common.jaxb.ContextDef;
import ru.prbb.common.jaxb.ContextRepository;

/**
 * @author lesinsa on 21.05.2015.
 */
@ContextRepository
public class BaseSampleRepo {

    public static final String CONTEXT_4 = "CONTEXT_4";

    @ContextDef(name = CONTEXT_4)
    public Class[] method1() {
        return new Class[]{Class1.class, Class2.class, Class3.class};
    }

}
