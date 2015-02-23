package com.github.rbrugier.gen;

import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class JavaPoetFactoryProcessorTest {

    @Test
    public void generateFactory() throws Exception {
        assert_().about(javaSource())
                . that(JavaFileObjects.forResource("SimpleClass.java"))
                .processedWith(new JavaPoetFactoryProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forResource("SimpleClassFactory.java"))
        ;

    }
}
