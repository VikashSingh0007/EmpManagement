import { Body, Controller, Delete, Get, Param, Post, Put, Req, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';
import { RolesGuard } from '../guards/roles.guard';
import { Roles } from '../common/decorators/roles.decorator';
import { UsersService } from './users.service';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { KafkaConsumerService } from 'src/kafka/kafka.consumer.service';

@Controller('users')
export class UsersController {
  constructor(
    private readonly usersService: UsersService,
    private readonly kafkaService: KafkaConsumerService
  ) {}

  // Get user profile
  @Get('profile')
  @UseGuards(JwtAuthGuard)
  async getProfile(@Req() req: any) {
    const userId: string = req.user.userId; // Extract user ID as UUID from the request
    return this.usersService.findOneById(userId);
  }

  // Update user profile
  @Put('profile')
  @UseGuards(JwtAuthGuard)
  async updateProfile(@Req() req: any, @Body() updates: UpdateUserDto) {
    const userId: string = req.user.userId; // Extract user ID as UUID from the request
    return this.usersService.updateProfile(userId, updates);
  }

  // Admin: Get all users
  @Get('all')
  @Roles('Admin')
  @UseGuards(JwtAuthGuard, RolesGuard)
  async findAllForAdmin() {
    const users = await this.usersService.findAll();
    return { message: 'All users fetched successfully', users };
  }

  // Admin: Update any user
  @Put(':id')
  @Roles('Admin')
  @UseGuards(JwtAuthGuard, RolesGuard)
  async updateUser(@Param('id') id: string, @Body() updates: UpdateUserDto) {
    return this.usersService.updateUser(id, updates);
  }

  // Admin: Delete any user
  @Delete(':id')
  @Roles('Admin')
  @UseGuards(JwtAuthGuard, RolesGuard)
  async deleteUser(@Param('id') id: string) {
    return this.usersService.deleteUser(id);
  }

  // Admin: Register a new user
  @Post('registerUser')
  @Roles('Admin')
  @UseGuards(JwtAuthGuard, RolesGuard)
  async registerUser(@Body() createUserDto: CreateUserDto) {
    return this.usersService.addUser(createUserDto);
  }

  // Kafka Test Producer
  // @Post('test-producer')
  // async testProducer(@Body() data: any) {
  //   const topic = 'user-created'; // ✅ Change this to test different topics
  //   return this.kafkaService.sendTestMessage(topic, data);
  // }
}
