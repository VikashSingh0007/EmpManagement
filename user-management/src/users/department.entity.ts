import { Entity, Column, PrimaryGeneratedColumn, ManyToMany } from 'typeorm';
import { User } from './user.entity';

@Entity('departments')
export class Department {
  @PrimaryGeneratedColumn('uuid')
  id: string;  // UUID instead of number

  @Column()
  name: string;

  @ManyToMany(() => User, (user) => user.departments)
  users: User[];
}
