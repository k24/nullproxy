package com.github.k24.nullproxy;

import com.github.k24.nullproxy.test.*;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by k24 on 2017/01/12.
 */
public class NullProxyTest {

    @Before
    public void setUp() throws Exception {
        NullProxy.clearCache();
    }

    @Test
    public void newProxyInstance() throws Exception {
        EmptyInterface emptyInterface = NullProxy.newProxyInstance(EmptyInterface.class);
        assertThat(emptyInterface)
                .isNotNull();

        PrimitiveInterface primitiveInterface = NullProxy.newProxyInstance(PrimitiveInterface.class);
        assertThat(primitiveInterface)
                .isNotNull();
        verifyNull(primitiveInterface);

        NulleanInterface nulleanInterface = NullProxy.newProxyInstance(NulleanInterface.class);
        assertThat(nulleanInterface)
                .isNotNull()
                .has(new Condition<NulleanInterface>() {
                    @Override
                    public boolean matches(NulleanInterface value) {
                        return value.intValue() == 0;
                    }
                });

        CompositeInterface compositeInterface = NullProxy.newProxyInstance(CompositeInterface.class);
        assertThat(compositeInterface)
                .isNotNull()
                .has(new Condition<CompositeInterface>() {
                    @Override
                    public boolean matches(CompositeInterface value) {
                        // Check only nullean
                        return value.nullean() == NulleanInterface.NULL;
                    }
                });
        verifyNull(compositeInterface.primitive());

        ExtendedInterface extendedInterface = NullProxy.newProxyInstance(ExtendedInterface.class);
        assertThat(extendedInterface)
                .isNotNull();
        verifyNull(extendedInterface.primitive());

        InternalInterface internalInterface = NullProxy.newProxyInstance(InternalInterface.class);
        assertThat(internalInterface)
                .isNotNull()
                .has(new Condition<InternalInterface>() {
                    @Override
                    public boolean matches(InternalInterface value) {
                        return value.intValue() == 0;
                    }
                });
    }

    @Test
    public void newProxyInstance_failed() throws Exception {
        try {
            NullProxy.newProxyInstance(Concrete.class);
            fail();
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void clearCache() throws Exception {
        // Twice
        NullProxy.clearCache();
        NullProxy.clearCache();
    }

    public interface InternalInterface {
        int intValue();
    }

    private void verifyNull(PrimitiveInterface primitiveInterface) throws Exception {
        assertThat(primitiveInterface.booleanValue()).isFalse();
        assertThat(primitiveInterface.charValue()).isEqualTo((char) 0);
        assertThat(primitiveInterface.shortValue()).isEqualTo((short) 0);
        assertThat(primitiveInterface.intValue()).isEqualTo(0);
        assertThat(primitiveInterface.longValue()).isEqualTo(0L);
        assertThat(primitiveInterface.floatValue()).isEqualTo(0f);
        assertThat(primitiveInterface.doubleValue()).isEqualTo(0.0);
        assertThat(primitiveInterface.stringValue()).isNull();
    }
}