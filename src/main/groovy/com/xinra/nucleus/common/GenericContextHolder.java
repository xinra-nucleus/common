package com.xinra.nucleus.common;

import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Default implementation of {@link ContextHolder}. Holds context of arbitrary type.
 * 
 * @author Erik Hofer
 * @param <T> the context type
 */
public class GenericContextHolder<T> implements ApplicationContextAware, ContextHolder<T> {
  
  private final ThreadLocal<T> mock = new ThreadLocal<>();
  private final Class<T> contextType;
  private final Supplier<T> contextSupplier;
  private final Supplier<Boolean> useApplicationContext;
  private ApplicationContext applicationContext;
  
  /**
   * Creates a {@link GenericContextHolder}. If this is not used to create a managed bean,
   * make sure to call {@link #setApplicationContext(ApplicationContext)}.
   * @param contextType the type used to retrieve the current context from the application context
   * @param contextSupplier used to create a new mock context
   * @param useApplicationContext used to determine if the application context should be used to
   *     retrieve the context (true) or if a mock context should be used (false).
   */
  public GenericContextHolder(Class<T> contextType, Supplier<T> contextSupplier,
      Supplier<Boolean> useApplicationContext) {
    this.contextType = Objects.requireNonNull(contextType);
    this.contextSupplier = Objects.requireNonNull(contextSupplier);
    this.useApplicationContext = Objects.requireNonNull(useApplicationContext);
  }

  @Override
  public T get() {
    if (useApplicationContext.get()) {
      return applicationContext.getBean(contextType);
    } else {
      T context = mock.get();
      if (context == null) {
        mock.remove();
        throw new IllegalStateException("Cannot get current context. This is not a web"
            + " request and there is no mock context!");
      }
      return context;
    }
  }
  
  @Override
  public T mock() {
    if (useApplicationContext.get()) {
      throw new IllegalStateException("This is a request. Use the request-scoped context"
          + " instead of mocking one!");
    }
    final T context = contextSupplier.get();
    mock.set(context);
    return context;
  }
  
  @Override
  public void clearMock() {
    mock.remove();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
