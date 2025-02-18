import { IsEmail, IsOptional, IsString } from 'class-validator';

export class UpdateUserDto {
  @IsOptional()  // Making email optional
  @IsEmail()
  email?: string;

  @IsOptional()  // Making password optional
  @IsString()
  password?: string;

  @IsOptional()  // Making role optional
  @IsString()
  role?: string;

  @IsOptional()  // Making name optional
  @IsString()
  name?: string;

  @IsOptional()  // Making profilePicture optional
  @IsString()
  profilePicture?: string;
}
