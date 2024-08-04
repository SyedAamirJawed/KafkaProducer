package com.kafka.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.kafka.model.FileWrapper;

@Configuration
@EnableIntegration
@EnableKafka
public class KafkaProducerConfig {

	@Bean
	public ProducerFactory<String, FileWrapper> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
	    configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 20 * 1024 * 1024); 
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, FileWrapper> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	@InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "1000"))
	public FileReadingMessageSource fileReadingMessageSource() {
		FileReadingMessageSource reader = new FileReadingMessageSource();
		reader.setDirectory(new File("C:\\Users\\dell\\Desktop\\Test\\lookup"));
		return reader;
	}

	@Bean
	public MessageChannel fileInputChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "fileInputChannel")
	public MessageHandler kafkaMessageHandler() {
		return message -> {
			File file = (File) message.getPayload();
			try (FileInputStream fis = new FileInputStream(file)) {
				byte[] fileContent = fis.readAllBytes();
				FileWrapper fileWrapper = new FileWrapper(file.getName(), fileContent);
				kafkaTemplate().send("my-topic", fileWrapper);
				System.out.println("File transferred ---------> " + file.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
	}
}
