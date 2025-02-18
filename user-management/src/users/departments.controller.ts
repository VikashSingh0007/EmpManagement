import { Controller, Post, Put, Get, Body, Param, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';
import { RolesGuard } from '../guards/roles.guard';
import { Roles } from '../common/decorators/roles.decorator';
import { DepartmentsService } from './departments.service';
import { CreateDepartmentDto } from './dto/create-department.dto';
import { UpdateDepartmentDto } from './dto/update-department.dto';
import { IsUUID } from 'class-validator';

@Controller('departments')
export class DepartmentsController {
  constructor(private readonly departmentsService: DepartmentsService) {}

  // Admin: Create department
  @Post()
  @Roles('Admin')
  @UseGuards(JwtAuthGuard, RolesGuard)
  async createDepartment(@Body() createDepartmentDto: CreateDepartmentDto) {
    return this.departmentsService.createDepartment(createDepartmentDto);
  }

  // Admin: Update department
  @Put(':id')
  @Roles('Admin')
  @UseGuards(JwtAuthGuard, RolesGuard)
  async updateDepartment(@Param('id') id: string, @Body() updateDepartmentDto: UpdateDepartmentDto) {
    return this.departmentsService.updateDepartment(id, updateDepartmentDto);
  }

  // Get all departments a user works in
  @Get('user/:userId')
  @UseGuards(JwtAuthGuard)
  async getDepartmentsOfUser(@Param('userId') userId: string) {
    return this.departmentsService.getDepartmentsOfUser(userId);
  }

  // Admin: Add user to department
  @Post(':departmentId/user/:userId')
  @Roles('Admin')
  @UseGuards(JwtAuthGuard, RolesGuard)
  async addUserToDepartment(
    @Param('userId') userId: string,
    @Param('departmentId') departmentId: string,
  ) {
    return this.departmentsService.addUserToDepartment(userId, departmentId);
  }
}
