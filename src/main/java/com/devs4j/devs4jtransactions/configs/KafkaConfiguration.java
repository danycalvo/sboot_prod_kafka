package com.devs4j.devs4jtransactions.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    //private static final Logger log = LoggerFactory.getLogger(ProdDevs4jTransactionsApplication.class);

    private Map<String, Object> producerProperties() {
        Map<String, Object> props=new HashMap<>();

        String bootStrap = System.getenv().getOrDefault("BOOTSTRAP_SERVERS","my-cluster-kafka-bootstrap-kafka-poc.apps.k8lab.redlink.com.ar:443");
        String ssltrust = System.getenv().getOrDefault("SSLTRUST","/ssl/truststore.jks");  // variable de entorno
        String password = System.getenv().getOrDefault("PASSWORD","redhat01");
        String protocol = System.getenv().getOrDefault("ENV PROTOCOL","ssl");

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrap);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, protocol);
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, ssltrust);
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, password);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;

        //props.put( SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,sslkey);
        //props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,"redhat01");
        //props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG,"PKCS12");
        //props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        //props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, ca_crt);
        //props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,  "redhat01");
        //props.put( SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,user_crt);
        //props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,"redhat01");
        //props.put(SslConfigs.SSL_KEYSTORE_KEY_CONFIG,"user_key");

    }
/*
        @Bean
        public ConsumerFactory<String, String> consumerFactory() {
            return new
                    DefaultKafkaConsumerFactory<>(consumerProperties());
        }

*/
    @Bean
    public KafkaTemplate<String, String> createTemplate() {
        Map<String, Object> senderProps = producerProperties();
        ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<String, String>(senderProps);
        KafkaTemplate<String, String> template = new KafkaTemplate<>(pf);
        return template;
    }

    @Bean
    public ObjectMapper mapper (){
        return new ObjectMapper();
    }


}
