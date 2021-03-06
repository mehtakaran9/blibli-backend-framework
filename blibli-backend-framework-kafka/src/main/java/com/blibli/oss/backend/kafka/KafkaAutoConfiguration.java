package com.blibli.oss.backend.kafka;

import com.blibli.oss.backend.kafka.interceptor.InterceptorUtil;
import com.blibli.oss.backend.kafka.interceptor.log.LogKafkaConsumerInterceptor;
import com.blibli.oss.backend.kafka.interceptor.log.LogKafkaProducerInterceptor;
import com.blibli.oss.backend.kafka.listener.KafkaListenerBeanProcessor;
import com.blibli.oss.backend.kafka.producer.KafkaProducer;
import com.blibli.oss.backend.kafka.properties.KafkaProperties;
import com.blibli.oss.backend.kafka.repository.KafkaProducerAwareBeanProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerConfigUtils;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableConfigurationProperties({
  KafkaProperties.class
})
public class KafkaAutoConfiguration implements ApplicationContextAware {

  @Setter
  private ApplicationContext applicationContext;

  @Bean
  public KafkaProducer kafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
    return new KafkaProducer(kafkaTemplate, objectMapper, InterceptorUtil.getProducerInterceptors(applicationContext));
  }

  @Bean
  public LogKafkaProducerInterceptor logKafkaProducerInterceptor(KafkaProperties kafkaProperties) {
    LogKafkaProducerInterceptor interceptor = new LogKafkaProducerInterceptor();
    interceptor.setKafkaProperties(kafkaProperties);
    return interceptor;
  }

  @Bean
  public LogKafkaConsumerInterceptor logKafkaConsumerInterceptor(KafkaProperties kafkaProperties) {
    LogKafkaConsumerInterceptor interceptor = new LogKafkaConsumerInterceptor();
    interceptor.setKafkaProperties(kafkaProperties);
    return interceptor;
  }

  @Bean
  public KafkaProducerAwareBeanProcessor kafkaProducerAwareBeanProcessor(KafkaProducer kafkaProducer) {
    return new KafkaProducerAwareBeanProcessor(kafkaProducer);
  }

  @SuppressWarnings("rawtypes")
  @Bean(KafkaListenerConfigUtils.KAFKA_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
  public KafkaListenerBeanProcessor kafkaListenerBeanProcessor() {
    return new KafkaListenerBeanProcessor();
  }

  @Bean(KafkaListenerConfigUtils.KAFKA_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME)
  public KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry() {
    return new KafkaListenerEndpointRegistry();
  }

}
