package uk.nhs.nhsbsa.employeeskills.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class Config {

  @Bean(name = "messageSource")
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:FieldValidationMessages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean(name = "validator")
  public LocalValidatorFactoryBean getValidator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }
}
