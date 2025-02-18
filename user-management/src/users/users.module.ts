import { Module, forwardRef } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { UsersService } from './users.service';
import { UsersController } from './users.controller';
import { User } from './user.entity';
import { KafkaModule } from '../kafka/kafka.module'; // Import KafkaModule

@Module({
  imports: [
    TypeOrmModule.forFeature([User]), 
    forwardRef(() => KafkaModule), // circular dependency remover
  ],
  providers: [UsersService],
  controllers: [UsersController],
  exports: [UsersService,TypeOrmModule], 
  
})
export class UsersModule {}