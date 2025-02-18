import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Department } from './department.entity';
import { CreateDepartmentDto } from './dto/create-department.dto';
import { UpdateDepartmentDto } from './dto/update-department.dto';
import { User } from '../users/user.entity';

@Injectable()
export class DepartmentsService {
  constructor(
    @InjectRepository(Department) private departmentRepository: Repository<Department>,
    @InjectRepository(User) private userRepository: Repository<User>,
  ) {}

  // Create a new department
  async createDepartment(createDepartmentDto: CreateDepartmentDto): Promise<Department> {
    const { name } = createDepartmentDto;
    const newDepartment = this.departmentRepository.create({ name });
    return this.departmentRepository.save(newDepartment);
  }

  // Update an existing department
  async updateDepartment(id: string, updateDepartmentDto: UpdateDepartmentDto): Promise<Department> {
    const department = await this.departmentRepository.findOne({ where: { id } });
    if (!department) {
      throw new NotFoundException('Department not found');
    }
    Object.assign(department, updateDepartmentDto);
    return this.departmentRepository.save(department);
  }

  // Get all departments a user works in
  async getDepartmentsOfUser(userId: string): Promise<Department[]> {
    const user = await this.userRepository.findOne({ where: { id: userId }, relations: ['departments'] });
    if (!user) {
      throw new NotFoundException('User not found');
    }
    return user.departments;
  }

  // Add user to a department
  async addUserToDepartment(userId: string, departmentId: string): Promise<Department> {
    const user = await this.userRepository.findOne({ where: { id: userId }, relations: ['departments'] });
    const department = await this.departmentRepository.findOne({ where: { id: departmentId }, relations: ['users'] });

    if (!user) throw new NotFoundException('User not found');
    if (!department) throw new NotFoundException('Department not found');

    // Prevent duplicate entries
    if (department.users.some(existingUser => existingUser.id === userId)) {
      return department;
    }

    department.users.push(user);
    return this.departmentRepository.save(department);
  }
}
