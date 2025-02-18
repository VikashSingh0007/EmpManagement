import { Injectable } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy } from 'passport-jwt';

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy) {
  constructor() {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      ignoreExpiration: false,
      secretOrKey: 'Secret7077', // Use a secure secret key in production
    });
  }

  async validate(payload: any) {
    // Return the decoded payload (make sure to include role)
    console.log("payload", payload);
    return { userId: payload.sub, email: payload.username, role: payload.role };
  }
}
