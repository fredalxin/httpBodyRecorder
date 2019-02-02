# httpBodyRecorder

## Extend HttpBodyRecorderFilter

```java
public class MyRecorderFilter extends HttpBodyRecorderFilter {

    @Override
    protected void recordBody(String payload, String response) {
        //do something for request payload and response body
    }
    
    @Override
    protected abstract String recordCode(){
        return "400,500"; //record when code in [400,500]
    }
}
```

then configure your filter in any way.

## Use Pattern Mapping

you can use PatternMappingFilterProxy for pattern matching jusk like spring mvc.

```java
    @Bean
    public FilterRegistrationBean patternMappingFilter() {
        Filter filter =new PatternMappingFilterProxy(new MyRecorderFilter(),
         "/test","/test/{id:[0-9]+}"); //support pathvariable
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
```
