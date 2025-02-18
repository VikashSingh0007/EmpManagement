import { Injectable, OnModuleInit, OnModuleDestroy, Inject } from '@nestjs/common';
import { Consumer, Kafka, Producer } from 'kafkajs';
import { Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';
import { User } from '../users/user.entity';

@Injectable()
export class KafkaConsumerService implements OnModuleInit, OnModuleDestroy {
  private consumer: Consumer;
  private producer: Producer;

  constructor(
    @Inject('KAFKA_CLIENT') private kafka: Kafka,
    @InjectRepository(User) private userRepository: Repository<User>,
  ) {
    this.producer = this.kafka.producer();
  }

  async onModuleInit() {
    try {
      await this.producer.connect();
      this.consumer = this.kafka.consumer({ groupId: 'nestjs-consumer-group' });
      await this.consumer.connect();

      // Subscribe to topics
      await this.consumer.subscribe({ topic: 'user-created', fromBeginning: true });

      await this.consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
          const messageContent = message.value.toString();
          console.log(`Kafka Message Received [${topic}]:`, messageContent);

          try {
            const parsedMessage = JSON.parse(messageContent);

            if (topic === 'user-created') {
              await this.handleUserCreation(parsedMessage);
            }
          } catch (error) {
            console.error(`Error processing Kafka message [${topic}]:`, error);
          }
        },
      });

      console.log('Kafka Consumer Initialized and listening...');
    } catch (error) {
      console.error('Kafka initialization failed:', error);
    }
  }

  async onModuleDestroy() {
    try {
      await this.consumer.disconnect();
      await this.producer.disconnect();
      console.log('Kafka Consumer and Producer disconnected gracefully.');
    } catch (error) {
      console.error('Error disconnecting Kafka:', error);
    }
  }

  private async handleUserCreation(parsedMessage: any) {
    if (!parsedMessage?.userId) {
      console.error('Received message without userId. Skipping...');
      return;
    }
  
    try {
      const existingUser = await this.userRepository.findOne({ where: { id: parsedMessage.userId } });
  
      if (existingUser) {
        console.log(`User with ID ${parsedMessage.userId} already exists. Skipping creation.`);
        return;
      }
  
      const newUser = this.userRepository.create({
        id: parsedMessage.userId, // ✅ Manually setting userId from Kafka message
        email: parsedMessage.email,
        password: parsedMessage.password, // Ensure it's already hashed before sending via Kafka
        name: parsedMessage.name,
        profilePicture: parsedMessage.profilePicture,
        role: parsedMessage.role || 'USER',
      });
  
      await this.userRepository.save(newUser);
      console.log(`✅ New user created: UserID=${parsedMessage.userId}, Email=${parsedMessage.email}, Name=${parsedMessage.name}`);
    } catch (error) {
      console.error('❌ Error creating user:', error);
    }
  }
  
}
