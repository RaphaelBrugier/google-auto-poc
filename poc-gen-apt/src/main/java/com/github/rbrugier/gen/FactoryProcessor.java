package com.github.rbrugier.gen;


import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javawriter.JavaWriter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import static javax.lang.model.element.Modifier.*;


@AutoService(Processor.class)
public class FactoryProcessor extends AbstractProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        print("entering processor");
        Filer filer = processingEnv.getFiler();

        for (Element element : roundEnv.getElementsAnnotatedWith(Factory.class)) {
            String className = element.getSimpleName().toString();
            print(className);

            try {
                String packageName = "com.github.rbrugier";
                JavaFileObject sourceFile = filer.createSourceFile(packageName +"." + className + "Factory");
                JavaWriter writer = new JavaWriter(sourceFile.openWriter());
                writer.emitPackage(packageName);
                writer.emitImports(packageName + "."  + className);
                writer.beginType(className + "Factory", "class", EnumSet.of(PUBLIC, FINAL));
                writer.beginMethod(className, "create", EnumSet.of(PUBLIC, FINAL, STATIC));
                writer.emitStatement(" return new " + className + "()");
                writer.endMethod();
                writer.endType();
                writer.close();

            } catch (IOException e) {
                print(e.getMessage());
            }
        }

        return true;
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
