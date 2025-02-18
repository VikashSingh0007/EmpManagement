import { Injectable, CanActivate, ExecutionContext, ForbiddenException } from '@nestjs/common';
import { Reflector } from '@nestjs/core';

@Injectable()
export class RolesGuard implements CanActivate {
  constructor(private reflector: Reflector) {}

  canActivate(context: ExecutionContext): boolean {
    const requiredRoles = this.reflector.get<string[]>('roles', context.getHandler());
    if (!requiredRoles) {
      return true; // If no roles are specified, the user can access the route
    }
    console.log('requiredRoles:', requiredRoles);
    const { user } = context.switchToHttp().getRequest();

    console.log('user:', user);
    if (!user || !user.role) {
      throw new ForbiddenException('User role is missing');
    }

    return requiredRoles.includes(user.role); // Check if the user's role matches the required role
  }
}
