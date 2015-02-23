package com.github.rbrugier.gen;


import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.*;

import javax.annotation.Generated;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

import static javax.lang.model.element.Modifier.*;


/**
 * This processor uses the <a href="https://github.com/square/javapoet">JavaPoet library</a> to generate the code.
 */
@AutoService(Processor.class)
public class FactoryProcessorJavaPoet extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Filer filer = processingEnv.getFiler();


        Elements elements = processingEnv.getElementUtils();

        for (Element annotatedClass : roundEnv.getElementsAnnotatedWith(Factory.class)) {
            String className = annotatedClass.getSimpleName().toString();

            try {
                String packageName = elements.getPackageOf(annotatedClass).getQualifiedName().toString();

                ClassName targetClass = ClassName.get(packageName, className);

                MethodSpec create = MethodSpec.methodBuilder("create")
                        .addStatement("return new $T()", targetClass)
                        .returns(targetClass)
                        .addModifiers(PUBLIC, STATIC, FINAL)
                        .build();
                AnnotationSpec generatedAnnotation = AnnotationSpec.builder(Generated.class)
                        .addMember("value", "$S", this.getClass().getCanonicalName())
                        .build();
                TypeSpec typeSpec = TypeSpec.classBuilder(className + "Factory")
                        .addAnnotation(generatedAnnotation)
                        .addModifiers(PUBLIC, FINAL)
                        .addMethod(create)
                        .build();

                JavaFile javaFile = JavaFile.builder(packageName + ".gen", typeSpec)
                        .build();
                javaFile.writeTo(filer);

            } catch (IOException e) {
                print(e.getMessage());
            }
        }

        return false;
    }

    private void print(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(Factory.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
