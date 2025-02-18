import { Controller, Post, Body, BadRequestException } from '@nestjs/common';
import { AuthService } from './auth.service';
import { UsersService } from '../users/users.service';
import { CreateUserDto } from '../users/dto/create-user.dto'; 
import { LoginDto } from './dto/login.dto';  

@Controller('auth')
export class AuthController {
  constructor(
    private authService: AuthService,
    private usersService: UsersService,
  ) {}

  @Post('register')
  async register(@Body() createUserDto: CreateUserDto) {
    const { email, password } = createUserDto;
    try {
      const user = await this.usersService.createUser(createUserDto);
      return { message: 'User registered successfully', user };
    } catch (error) {
      throw new BadRequestException('User registration failed !!  user already exist kr rha h');
    }
  }

  @Post('login')
  async login(@Body() loginDto: LoginDto) {
    const { email, password } = loginDto;
    const user = await this.authService.validateUser(email, password);
    if (!user) {
      throw new BadRequestException('Invalid credentials');
    }
    return this.authService.login(user); 
}
}
