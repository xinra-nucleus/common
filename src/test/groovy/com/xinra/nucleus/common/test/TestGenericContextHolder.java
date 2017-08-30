package com.xinra.nucleus.common.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.xinra.nucleus.common.GenericContextHolder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

public class TestGenericContextHolder {
  
  private static enum TestContext {
    BEAN,
    MOCK
  }
  
  private static GenericContextHolder<TestContext> contextHolder;
  private static boolean useApplicationContext;
  
  /**
   * Creates the instance of {@link GenericContextHolder} and a mock application
   * context. The instance is shared across all tests because it is stateless
   * aside from {@link #useApplicationContext}.
   */
  @BeforeClass
  public static void setUp() {
    contextHolder = new GenericContextHolder<>(TestContext.class, () -> TestContext.MOCK, 
        () -> useApplicationContext);
    
    ApplicationContext applicationContext = mock(ApplicationContext.class);
    when(applicationContext.getBean(TestContext.class)).thenReturn(TestContext.BEAN);
    contextHolder.setApplicationContext(applicationContext);
  }
  
  @Test
  public void getContextBean() {
    useApplicationContext = true;
    assertThat(contextHolder.get()).isEqualTo(TestContext.BEAN);
  }
  
  @Test
  public void createAndGetMockContext() {
    useApplicationContext = false;
    assertThat(contextHolder.mock()).isEqualTo(TestContext.MOCK);
    assertThat(contextHolder.get()).isEqualTo(TestContext.MOCK);
  }
  
  @Test
  public void createMockContextWhenNotApplicable() {
    useApplicationContext = true;
    assertThatIllegalStateException().isThrownBy(contextHolder::mock);
  }
  
  @Test
  public void getMockContextWihtoutCreatingOne() {
    useApplicationContext = false;
    assertThatIllegalStateException().isThrownBy(contextHolder::get);
  }
  
  @Test
  public void clearMockContext() {
    useApplicationContext = false;
    contextHolder.mock();
    contextHolder.clearMock();
    assertThatIllegalStateException().isThrownBy(contextHolder::get);
  }
}
