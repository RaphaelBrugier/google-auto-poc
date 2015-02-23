package com.github.rbrugier.gen;


import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.*;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static javax.lang.model.element.Modifier.*;


/**
 * This processor uses the Freemarker library to generate the source code
 */
@AutoService(Processor.class)
public class FactoryProcessorTemplate extends AbstractProcessor {


    Configuration cfg;

    public FactoryProcessorTemplate() {
        cfg = new Configuration(Configuration.VERSION_2_3_21);

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Filer filer = processingEnv.getFiler();
        Elements elements = processingEnv.getElementUtils();
        try {
            cfg.setClassForTemplateLoading(this.getClass(), "templates");
            cfg.setDefaultEncoding("UTF-8");
            Template template = cfg.getTemplate("FactoryTemplate.ftl");


            for (Element annotatedClass : roundEnv.getElementsAnnotatedWith(Factory.class)) {
                String className = annotatedClass.getSimpleName().toString();
                String packageName = elements.getPackageOf(annotatedClass).getQualifiedName().toString();


                String name = packageName + ".gen." + className + "TemplateFactory";
                JavaFileObject classFile = filer.createSourceFile(name);
                Writer writer = classFile.openWriter();
                Map<String, String> model = new HashMap<>();
                model.put("packageName", packageName + ".gen");
                model.put("classFullyQualified", packageName + "." + className);
                model.put("classname", className);
                template.process(model, writer);
                writer.close();

            }
        } catch (IOException e) {
            print(e.getMessage());
        }
        catch (TemplateException e) {
            e.printStackTrace();
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
