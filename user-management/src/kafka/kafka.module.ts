import { Module, forwardRef } from '@nestjs/common';
import { Kafka } from 'kafkajs';
import { KafkaConsumerService } from './kafka.consumer.service';
import { DepartmentsModule } from '../users/department.module';  // Import your departments module
import { UsersModule } from '../users/users.module';  // Import your users module

@Module({
  imports: [
    forwardRef(() => DepartmentsModule),  // Use forwardRef to handle circular dependency
    forwardRef(() => UsersModule),        // Use forwardRef to handle circular dependency
  ],
  providers: [
    {
      provide: 'KAFKA_CLIENT',
      useFactory: () => {
        return new Kafka({
          clientId: 'nestjs-service',
          brokers: ['localhost:9092'],  // Make sure to configure brokers correctly
        });
      },
    },
    KafkaConsumerService,  // Add KafkaConsumerService to handle messages from Kafka
  ],
  exports: ['KAFKA_CLIENT', KafkaConsumerService],  // Export them for use in other modules
})
export class KafkaModule {}