import { Entity, Column, PrimaryGeneratedColumn, ManyToMany, JoinTable } from 'typeorm';
import { Department } from './department.entity';

@Entity('users')
export class User {
  @PrimaryGeneratedColumn('uuid')
  id: string;  // UUID instead of number

  @Column({ unique: true })
  email: string;

  @Column()
  password: string;

  @Column({ default: 'User' })
  role: string;

  @Column({ nullable: true })
  name: string;

  @Column({ nullable: true })
  profilePicture: string;

  @ManyToMany(() => Department, (department) => department.users)
  @JoinTable({ name: 'user_department' })  // Defines the join table
  departments: Department[];
}
