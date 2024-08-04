# Kafka Producer
## Overview

This code configures a Spring Integration-based Kafka producer that reads files from a specified directory and sends the file content to a Kafka topic. The configuration involves setting up a Kafka producer, reading files from a directory, and sending them to a Kafka topic using Spring Integration's messaging capabilities.

### Key Components

1. **ProducerFactory & KafkaTemplate**
   - The `ProducerFactory` is responsible for creating Kafka producer instances with the necessary configuration properties.
   - The `KafkaTemplate` is used to send messages to a Kafka topic.

2. **FileReadingMessageSource**
   - This bean is responsible for reading files from a specified directory. It polls the directory every second (`fixedDelay = "1000"`) to check for new files.

3. **MessageChannel**
   - A `PublishSubscribeChannel` is used as a message channel to connect the file reader with the Kafka message handler.

4. **MessageHandler**
   - This handler processes the files read from the directory. It converts the file content into a `FileWrapper` object and sends it to the specified Kafka topic.

### Example Usage

1. **Directory Configuration**
   - The directory to monitor for files is set to `C:\\Users\\dell\\Desktop\\Test\\lookup`. Files placed in this directory will be read and processed.

2. **Kafka Configuration**
   - The Kafka producer is configured to connect to a Kafka broker running on `localhost:9092`.
   - The maximum request size is set to 20MB to accommodate larger file transfers.

3. **File Transfer**
   - Files read from the directory are wrapped in a `FileWrapper` object and sent to the Kafka topic named `my-topic`.
   - The file transfer process is logged to the console.

### Code Walkthrough

- **`producerFactory()`**: Configures Kafka producer properties like bootstrap servers, key and value serializers, and max request size.
  
- **`kafkaTemplate()`**: Provides a template for sending messages to Kafka.

- **`fileReadingMessageSource()`**: Configures a source for reading files from a specified directory.

- **`fileInputChannel()`**: Defines a channel for passing messages between components.

- **`kafkaMessageHandler()`**: Processes the files by reading their content, wrapping it in a `FileWrapper`, and sending it to Kafka.

### Dependencies

- **Spring Integration**
- **Spring Kafka**
- **Apache Kafka**
- **Java 8+**

### Conclusion

This configuration allows for seamless integration between a file system and a Kafka topic, enabling automatic file transfers and message-based processing using Spring Integration.
