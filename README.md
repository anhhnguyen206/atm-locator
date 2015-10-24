# ATM LOCATOR
### Getting started
- Before you build the source, please supply your own Google Map Api Key at google_maps_api.xml.
- Set JAVA8_HOME and JAVA7_HOME environment variables. Both should point to home directory of jdk 1.8 and jdk 1.7. In case you on MacOs: put these 3 lines in your bash_profile:
   -  export JAVA6_HOME=`/usr/libexec/java_home -v 1.6`  
      export JAVA7_HOME=`/usr/libexec/java_home -v 1.7`  
      export JAVA8_HOME=`/usr/libexec/java_home -v 1.8`  

### Architecture
- Model View ViewModel
    * Model: are interactors.  These interactors are the bridge between the view models to the data sources (local databases, Restful services ...)
    * ViewModel: expose changes that to anything subscribe to it (in this case is a view), decide what to emit, when to emit it. And it's up to the subscriber what they want to do.
    * View: all activities, fragments, custom views are treated as views only. Holds an instance of a ViewModel then bind to it.
- Dagger 1.2 to inject dependencies: http://square.github.io/dagger/
- RxJava to perform asyncrhonous operation and reduce boilerplate code (https://github.com/ReactiveX/RxAndroid)
- RxPermission to perform run-time permission request
- ButterKnife for view injections;
- Interactors: are business-contained service that the ViewModel or other Interactors will (re)use.

### Unit Tests
- Mockito: use to mock objects and controll its behaviors.
- Junit 4
- Robolectric
- Run ./gradlew testDebugUnitTest jacocoTestReport to execute the tests and generate jacoco test report at app/build/reports/jacoco

### Known issues:
- Haven't done any UI testing.  Will do later on, probably using Espresso.
