A POC using Google-Auto project to register an annotation processor.

The annotation processors has two versions:
* The [JavaPoetProcessor](https://github.com/RaphaelBrugier/google-auto-poc/blob/master/poc-gen-apt/src/main/java/com/github/rbrugier/gen/JavaPoetFactoryProcessor.java)
uses the fluent api from the [JavaPoet](https://github.com/square/javapoet) library to generate the source code
* The [FreeMarkerFactoryProcessor](https://github.com/RaphaelBrugier/google-auto-poc/blob/master/poc-gen-apt/src/main/java/com/github/rbrugier/gen/FreeMarkerFactoryProcessor.java)
 uses the Freemarker template engine.