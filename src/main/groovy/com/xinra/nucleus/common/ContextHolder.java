package com.xinra.nucleus.common;

/**
 * Holds a context object of arbitrary type. A context holds data that is used across different
 * parts of an application but should not be passed around as method parameters. Typically,
 * a context is a bean with a suiting scope (e.g. 'request'). Instead of autowiring the
 * context directly, this wrapper is used to provide a (mock) context if context-dependent
 * code is executed outside of the bean's scope, e.g. in tests.
 * 
 * @author Erik Hofer
 * @param <T> the context type
 */
public interface ContextHolder<T> {
  
  /**
   * Returns the current context. Either the context is retrieved from the application context
   * (typically as a request-scoped bean) or a mock context is returned if it has been set 
   * previously by {@link #mock()}.
   * 
   * @throws IllegalStateException if there is neither a context provided by
   *     the application context nor a mock context.
   */
  T get();
  
  /**
   * Creates a mock context. This is used if the application context does not hold a context
   * (typically if the context is request-scoped but the current thread is not a web request).
   * 
   * <p>IMPORTANT: When the mock context is not needed anymore, it has to be discarded using
   * {@link #clearMock()}. The mock context is stored in a {@link ThreadLocal} and unlike
   * request-scoped beans it is not cleaned up automatically. To prevent leaking the
   * mock context wrap the context-dependent code in a try-finally block.
   * 
   * @return the created context
   * @throws IllegalStateException if a context is already provided by the application context.
   */
  T mock();
  
  /**
   * Clears the mock context that has been created with {@link #mock()}.
   */
  void clearMock();
  
}
