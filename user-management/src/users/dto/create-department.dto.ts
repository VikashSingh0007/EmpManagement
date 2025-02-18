import { IsString, IsNotEmpty, IsUUID } from 'class-validator';

export class CreateDepartmentDto {
  
  @IsString()
  @IsNotEmpty()
  name: string;  // Department name
}
