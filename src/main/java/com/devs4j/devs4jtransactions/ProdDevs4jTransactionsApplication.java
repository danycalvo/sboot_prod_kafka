package com.devs4j.devs4jtransactions;

import com.devs4j.devs4jtransactions.models.Devs4jTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

//import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
@EnableScheduling		// es para habilitar el schedule
public class ProdDevs4jTransactionsApplication {


	@Autowired
	//private KafkaTemplate<String, String> kafkaTemplate producer;
    private KafkaTemplate<String, String>kafkaTemplate;

	@Autowired
	private ObjectMapper mapper ;

	private static final Logger log = LoggerFactory.getLogger(ProdDevs4jTransactionsApplication.class);

	@Scheduled(fixedRate = 10000)   // 1000 son milisegundos
	public void sendMessages () throws JsonProcessingException {
    	Faker faker = new Faker();

		String topico = System.getenv("TOPICO");
		//Integer mens_a_enviar = 2;
		//String topico = "streams-plaintext-input";
		//String topico = "dc-topic3";
		Integer mens_a_enviar = Integer.valueOf(System.getenv("MENS_A_ENVIAR"));  // variable de entorno
		//log.info("tiempo usado para enviar " +mens_a_enviar.toString()
		long startTime = System.currentTimeMillis(); //  tomo tiempo inicial
		for (int i=0 ; i < mens_a_enviar ; i++){
			Devs4jTransaction transaction = new Devs4jTransaction();
			transaction.setUsername(faker.name().username());
			transaction.setApellido(faker.name().firstName());
			transaction.setNombre(faker.name().lastName());
			transaction.setMonto(faker.number().randomDouble(4,0,20000000) );
			kafkaTemplate.send(topico,transaction.getUsername() , mapper.writeValueAsString(transaction));
		}
		long estimatedTime = System.currentTimeMillis() - startTime;  // tiempo de fin
		log.info("tiempo usado para enviar " +mens_a_enviar.toString() +" ==> " +String.valueOf(estimatedTime));
	}

	public static void main(String[] args) {
		SpringApplication.run(ProdDevs4jTransactionsApplication.class, args);
	}

}
