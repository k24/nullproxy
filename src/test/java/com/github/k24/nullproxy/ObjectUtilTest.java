package com.github.k24.nullproxy;

import com.github.k24.nullproxy.test.AutoValue_Generated;
import com.github.k24.nullproxy.test.Generated_;
import com.github.k24.nullproxy.test.NulleanInterface;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by k24 on 2017/01/12.
 */
public class ObjectUtilTest {
    @Test
    public void classToName() throws Exception {
        // Primitive
        assertThat(ObjectUtil.classToName(int.class))
                .isEqualTo("int");
        assertThat(ObjectUtil.classToName(int[].class))
                .isEqualTo("int[]");

        // Standard
        assertThat(ObjectUtil.classToName(String.class))
                .isEqualTo("java.lang.String");
        assertThat(ObjectUtil.classToName(String[].class))
                .isEqualTo("java.lang.String[]");

        // InnerClass
        assertThat(ObjectUtil.classToName(InnerClass.class))
                .isEqualTo("com.github.k24.nullproxy.ObjectUtilTest.InnerClass");

        // Generated
        assertThat(ObjectUtil.classToName(AutoValue_Generated.class))
                .isEqualTo("com.github.k24.nullproxy.test.AutoValue_Generated");
        assertThat(ObjectUtil.classToName(Generated_.class))
                .isEqualTo("com.github.k24.nullproxy.test.Generated_");
    }

    @Test
    public void classToName_excludeWellKnown() throws Exception {
        // Primitive
        assertThat(ObjectUtil.classToName(int.class, ObjectUtil.Patterns.WELL_KNOWN_GENERATED_CLASS))
                .isEqualTo("int");
        assertThat(ObjectUtil.classToName(int[].class, ObjectUtil.Patterns.WELL_KNOWN_GENERATED_CLASS))
                .isEqualTo("int[]");

        // Standard
        assertThat(ObjectUtil.classToName(String.class, ObjectUtil.Patterns.WELL_KNOWN_GENERATED_CLASS))
                .isEqualTo("java.lang.String");
        assertThat(ObjectUtil.classToName(String[].class, ObjectUtil.Patterns.WELL_KNOWN_GENERATED_CLASS))
                .isEqualTo("java.lang.String[]");

        // InnerClass
        assertThat(ObjectUtil.classToName(InnerClass.class, ObjectUtil.Patterns.WELL_KNOWN_GENERATED_CLASS))
                .isEqualTo("com.github.k24.nullproxy.ObjectUtilTest.InnerClass");

        // Generated
        assertThat(ObjectUtil.classToName(AutoValue_Generated.class, ObjectUtil.Patterns.WELL_KNOWN_GENERATED_CLASS))
                .isEqualTo("com.github.k24.nullproxy.test.Generated");
        assertThat(ObjectUtil.classToName(Generated_.class, ObjectUtil.Patterns.WELL_KNOWN_GENERATED_CLASS))
                .isEqualTo("com.github.k24.nullproxy.test.Generated");
    }

    @Test
    public void isNull_null() throws Exception {
        assertThat(ObjectUtil.isNull(null))
                .isTrue();

        assertThat(ObjectUtil.isNull(NulleanInterface.NULL))
                .isTrue();
    }

    @Test
    public void isNull_notNull() throws Exception {
        assertThat(ObjectUtil.isNull(false))
                .isFalse();

        assertThat(ObjectUtil.isNull(new Object()))
                .isFalse();
    }

    @Test
    public void getNull() throws Exception {
        // Primitive
        assertThat(ObjectUtil.getNull(int.class))
                .isEqualTo(0);

        // Standard
        assertThat(ObjectUtil.getNull(String.class))
                .isNull();

        // Nullean
        assertThat(ObjectUtil.getNull(NulleanInterface.class))
                .isSameAs(NulleanInterface.NULL);
    }

    public static class InnerClass {
        public static final InnerClass NULL = NullProxy.newProxyInstance(InnerClass.class);
    }
}