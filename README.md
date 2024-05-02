# Set up Kafka with Intellij idea

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Intellij Idea](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
</div>

## I. Download the latest version of Kafka from the official docs:

<div>
   
   ![Screenshot from 2024-04-29 10-22-01](https://github.com/Marouane-Elgoumiri/kafka_initiation/assets/96888594/52baa0b6-931f-4a8b-8172-615d7489f445)

   <a href="https://kafka.apache.org/downloads">Download</a>  
</div>


## II. Extract the file and run Zookeper and kafka-server
![Screenshot from 2024-04-29 10-25-40](https://github.com/Marouane-Elgoumiri/kafka_initiation/assets/96888594/3b81fe56-6737-447a-9cc9-a969c1283723)

### open the extracted folder:
```bash
   cd kafka_2.12-3.7.0
```

### run zookeper:
```bash
  bin/zookeeper-server-start.sh config/zookeeper.properties
```
### run Kafka-server client:

```bash
  bin/kafka-server-start.sh config/server.properties
```
## III. Create a maven project:

![Screenshot from 2024-04-29 10-27-58](https://github.com/Marouane-Elgoumiri/kafka_initiation/assets/96888594/c8084f3b-6c45-43b7-94cc-c9ffd38474d2)


### Add the necessary Dependencies from Apache Kafka docs (Client & Streams)

```bash
  <dependency>
      <groupId>org.apache.kafka</groupId>
      <artifactId>kafka-streams</artifactId>
      <version>3.7.0</version>
    </dependency>
    <dependency>
      <groupId>org.apache.kafka</groupId>
      <artifactId>kafka_2.13</artifactId>
      <version>3.7.0</version>
    </dependency>
    <dependency>
      <groupId>org.apache.kafka</groupId>
      <artifactId>kafka-clients</artifactId>
      <version>3.7.0</version>
    </dependency>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
      <version>1.7.25</version>
    </dependency>
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>
```
### Create Producer class:
```java
  public class ProducerApp {
    public static void main(String[] args) {
        ProducerApp producerApp = new ProducerApp();
        producerApp.sendMessage();
    }

    public void sendMessage() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("GROUP_ID_CONFIG", "test-group-1");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        int i = 0;
        while(true){
            ProducerRecord<String, String> message = new ProducerRecord<>("test-topic", Integer.toString(i));
            producer.send(message);
            try {
                i++;
                System.out.println("message" + i);
                Thread.sleep(1000);
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
```

### Create Consumer class:

```java
   public class ConsumerApp {
    public static void main(String[] args) {
        ConsumerApp newConsumer = new ConsumerApp();
        newConsumer.consume();
    }

    public static void consume() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("GROUP_ID_CONFIG", "test-group-1");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("test-topic"));
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(()-> {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            records.forEach(record -> {
                System.out.println("Key "+record.key()+" value "+record.value()+" topic:"+record.topic());
            });
        },1000, 1000, TimeUnit.MILLISECONDS);
    }
}
```

### Result:
![Screenshot from 2024-04-29 10-29-02](https://github.com/Marouane-Elgoumiri/kafka_initiation/assets/96888594/2e038c58-140f-4358-9ddc-ca89a6950fd0)

        
  
