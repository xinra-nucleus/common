package com.xinra.nucleus.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Provides access to the Spring application context for classes that are not managed
 * by Spring. Needs to be a managed bean itself.
 * 
 * <p>Note: <i>Be careful if you add this to more than one application context!</i></p>
 * 
 * @author Erik Hofer
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
  
  private static ApplicationContext applicationContext;
  
  protected ApplicationContextProvider() {}

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    ApplicationContextProvider.applicationContext = applicationContext;
  }
  
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

}
