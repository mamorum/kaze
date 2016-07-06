package kaze.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Add to Class which has kaze.app.Action<R>.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebApi {
	String value() default "";
}
