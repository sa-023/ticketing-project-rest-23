package com.company.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/*
 * 🦋 Meta-annotations are used to annotate other annotations.
 * · @Target(): It marks another annotation to restrict what kind of Java elements the annotation can be applied to.
 *   Ex: @Target(ElementType.METHOD): It can be used at the method level.
 * · @Retention(): It takes RetentionPolicy argument whose Possible values are SOURCE, CLASS and RUNTIME.
 *   Ex: @Retention(RetentionPolicy.RUNTIME): It will be active in runtime.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultExceptionMessage { // Creating a custom annotation.

    String defaultMessage() default "";




}
