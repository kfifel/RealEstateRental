import {User} from "./core/models/auth.models";
import {JwtAuthenticationResponse} from "./account/auth/jwt-authentication-response.model";
import {AuthConst} from "./core/consts/auth.const";

class AuthUtils {


  constructor() {
  }


  /**
   * Logout the user
   */
  logout() {
    localStorage.clear();
  }

  setLoggedCredentials(user: User, jwtAuthenticationResponse: JwtAuthenticationResponse) {
    if (user)
      localStorage.setItem('authUser', JSON.stringify(user));
    if (jwtAuthenticationResponse) {
      this.setAccessToken(jwtAuthenticationResponse.accessToken);
      this.setRefreshToken(jwtAuthenticationResponse.refreshToken);
    }
  }

  setRefreshToken(refreshToken: string) {
    localStorage.setItem('refresh_token', refreshToken);
  }

  setAccessToken(accessToken: string) {
    localStorage.setItem('access_token', accessToken);
  }

  /**
   * Returns the authenticated user
   */
  getAuthenticatedUser(): User | null {
    const authUserString = localStorage.getItem('authUser');
    if (!authUserString) {
      return null;
    }
    return JSON.parse(authUserString) as User;
  }

  /**
   * Handle the error
   * @param {*} error
   */
  _handleError(error) {
    return error.message;
  }

  currentAccessToken() {
    return localStorage.getItem('access_token') ?? null;
  }

  currentUserValue() {
    return this.getAuthenticatedUser();
  }
}

export const authUtils = new AuthUtils();
