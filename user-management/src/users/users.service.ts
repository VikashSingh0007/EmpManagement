import { Injectable, NotFoundException, ConflictException, Inject } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import * as bcrypt from 'bcrypt';
import { User } from './user.entity';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { Kafka } from 'kafkajs';
import { v4 as uuidv4 } from 'uuid'; // Import UUID generator

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(User) private userRepository: Repository<User>,
    @Inject('KAFKA_CLIENT') private kafka: Kafka,
  ) {}

  // Utility method to check for existing user
  private async userExists(email: string): Promise<boolean> {
    const existingUser = await this.userRepository.findOne({ where: { email } });
    return existingUser !== null;
  }

  // Create a new user
  async createUser(createUserDto: CreateUserDto): Promise<User> {
    const { id, email, password, role, name, profilePicture } = createUserDto;
  
    if (await this.userExists(email)) {
      throw new ConflictException('User with this email already exists');
    }
  
    const hashedPassword = await bcrypt.hash(password, 10);
  
    const newUser = this.userRepository.create({
      id: id || uuidv4(), // Use provided ID if available, otherwise generate a new UUID
      email,
      password: hashedPassword,
      role: role || 'User',
      name,
      profilePicture,
    });
  
    const savedUser = await this.userRepository.save(newUser);
  
    // Send Kafka message
    const kafkaMessage = {
      userId: savedUser.id,
      password: savedUser.password,
      name: savedUser.name,
      email: savedUser.email,
      profilePicture: savedUser.profilePicture,
      role: savedUser.role,
    };
  
    const producer = this.kafka.producer();
    await producer.connect();
    await producer.send({
      topic: 'user-createdFromNestedService',
      messages: [{ value: JSON.stringify(kafkaMessage) }],
    });
    await producer.disconnect();
  
    return savedUser;
  }
  
  
  // Update user profile
  async updateProfile(userId: string, updateUserDto: UpdateUserDto): Promise<User> {
    const { email, password, name, profilePicture } = updateUserDto;
    const user = await this.userRepository.findOne({ where: { id: userId } });
  
    if (!user) {
      throw new NotFoundException('User not found');
    }

    if (password) {
      user.password = await bcrypt.hash(password, 10);
    }
    if (email) {
      user.email = email;
    }
    if (name) {
      user.name = name;
    }
    if (profilePicture) {
      user.profilePicture = profilePicture;
    }
  
    return this.userRepository.save(user);
  }
  
  // Fetch all users
  async findAll(): Promise<User[]> {
    return this.userRepository.find();
  }

  // Find user by ID
  async findOneById(id: string): Promise<User> {
    const user = await this.userRepository.findOne({ where: { id } });
    if (!user) {
      throw new NotFoundException('User not found');
    }
    return user;
  }

  // Update user details
  async updateUser(id: string, updates: Partial<User>): Promise<User> {
    const user = await this.findOneById(id);
    Object.assign(user, updates);
    return this.userRepository.save(user);
  }

  // Delete a user
  async deleteUser(id: string): Promise<{ message: string }> {
    const user = await this.findOneById(id);
    await this.userRepository.remove(user);
    return { message: 'User successfully removed' };
  }

  // Add user by admin
  async addUser(userData: CreateUserDto): Promise<User> {
    const { email, password, role, name, profilePicture } = userData;

    if (await this.userExists(email)) {
      throw new ConflictException('User with this email already exists');
    }

    const hashedPassword = await bcrypt.hash(password, 10);

    const newUser = this.userRepository.create({
      id: uuidv4(), // Generate UUID manually
      email,
      password: hashedPassword,
      role: role || 'User',
      name,
      profilePicture,
    });

    return this.userRepository.save(newUser);
  }

  // Find user by email
  async findByEmail(email: string): Promise<User | undefined> {
    return this.userRepository.findOne({ where: { email } });
  }
}
