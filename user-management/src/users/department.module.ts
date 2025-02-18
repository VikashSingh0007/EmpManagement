import { Module, forwardRef } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Department } from './department.entity';
import { DepartmentsController } from './departments.controller';
import { DepartmentsService } from './departments.service';
import { UsersModule } from '../users/users.module';  // Import UsersModule
import { User } from '../users/user.entity';  // Explicitly import User entity
import { KafkaModule } from '../kafka/kafka.module'; // Import KafkaModule

@Module({
  imports: [
    TypeOrmModule.forFeature([Department, User]),  // Explicitly add User entity here
    forwardRef(() => UsersModule),  // circular dependency remover
    forwardRef(() => KafkaModule),  // circular dependency remover
  ],
  controllers: [DepartmentsController],
  providers: [DepartmentsService],
  exports: [DepartmentsService],
})
export class DepartmentsModule {}