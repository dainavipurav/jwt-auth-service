# JWT Authentication and Authorization Flow in Spring Security

## 1. Authentication Flow (Login)

Authentication is the process of verifying the identity of a user using credentials such as username/email and password.

### Components Involved

1. **Client** – Sends login credentials.
2. **Security Filter Chain** – Intercepts incoming HTTP requests and processes security-related filters.
3. **Authentication Filter** – Extracts username and password from the login request.
4. **AuthenticationManager** – Coordinates the authentication process.
5. **DaoAuthenticationProvider** – Performs authentication using database-backed user details.
6. **UserDetailsService** – Loads user information from the database.
7. **PasswordEncoder** – Verifies the submitted password against the encoded password stored in the database.
8. **JWT Service** – Generates the access token after successful authentication.

### Authentication Steps

1. **Client sends a login request**
    - Client sends username/email and password to the authentication API.
    - Example: `POST /api/auth/login`

2. **Security Filter Chain intercepts the request**
    - The incoming request passes through the configured Spring Security filters.
    - The authentication filter extracts the credentials from the request.

3. **Authentication Filter calls AuthenticationManager**
    - The filter creates an unauthenticated `Authentication` object containing the submitted credentials.
    - It passes this object to `AuthenticationManager.authenticate()`.

4. **AuthenticationManager delegates authentication**
    - AuthenticationManager delegates the authentication request to the appropriate AuthenticationProvider.
    - In our case, it uses `DaoAuthenticationProvider`.

5. **DaoAuthenticationProvider loads user details**
    - DaoAuthenticationProvider calls `UserDetailsService.loadUserByUsername()`.
    - UserDetailsService fetches the user information from the database.
    - It returns a `UserDetails` object containing username, encoded password and authorities/roles.

6. **Password verification**
    - DaoAuthenticationProvider uses `PasswordEncoder.matches()` to compare the submitted password with the stored encoded password.
    - If credentials are valid, authentication succeeds.
    - Otherwise, an authentication exception is thrown.

7. **AuthenticationManager returns the result**
    - AuthenticationManager receives the authenticated `Authentication` object from DaoAuthenticationProvider.
    - It returns the authentication result to the authentication filter.

8. **JWT generation**
    - The authentication filter or authentication controller uses the authenticated user information to generate a JWT access token.
    - The token contains relevant claims such as user ID, subject and authorities.

9. **Response to client**
    - The server returns the access token to the client.
    - The client stores the token securely and uses it for subsequent API requests.

---

## 2. Authorization Flow (Accessing Protected APIs)

Authorization determines whether an authenticated user has permission to access a particular resource.

### Components Involved

1. **Client** – Sends API requests with the JWT access token.
2. **Security Filter Chain** – Processes incoming requests.
3. **JWT Authentication Filter** – Extracts and validates the JWT.
4. **JWT Service** – Verifies token signature, expiration and claims.
5. **SecurityContextHolder** – Stores the authenticated user's security information.
6. **AuthorizationFilter** – Checks whether the user has the required permissions.
7. **AuthorizationManager** – Determines whether the user is allowed to access the requested resource.

### Authorization Steps

1. **Client sends a protected API request**
    - Client sends an API request with the JWT in the Authorization header.
    - Example:

   `Authorization: Bearer <access_token>`

2. **Security Filter Chain intercepts the request**
    - The request passes through the configured security filters.

3. **JWT Authentication Filter extracts the token**
    - The filter extracts the JWT from the Authorization header.

4. **JWT validation**
    - JWT Service validates:
        - Token signature.
        - Token expiration.
        - Token structure and required claims.
    - If the token is invalid or expired, authentication fails.

5. **Set authentication in SecurityContextHolder**
    - After successful validation, the application establishes the authenticated user's identity and authorities.
    - An authenticated `Authentication` object is stored in SecurityContextHolder.

6. **AuthorizationFilter checks permissions**
    - AuthorizationFilter checks whether the authenticated user has permission to access the requested endpoint.
    - It delegates the decision to the configured AuthorizationManager.

7. **Authorization decision**
    - If the user has the required role or authority, access is granted.
    - Otherwise, access is denied with HTTP 403 Forbidden.
    - If authentication is missing or invalid, the request generally receives HTTP 401 Unauthorized.

8. **Request reaches the Controller**
    - If authentication and authorization succeed, the request continues to the appropriate controller.
    - The controller processes the request and returns the response to the client.

---

## 3. Overall JWT Authentication and Authorization Flow

### Login Flow

Client → Security Filter Chain → Authentication Filter → AuthenticationManager → DaoAuthenticationProvider → UserDetailsService → Database

After retrieving user details:

DaoAuthenticationProvider → PasswordEncoder → AuthenticationManager → Authentication Filter → JWT Service → Client

### Subsequent API Request Flow

Client → Security Filter Chain → JWT Authentication Filter → JWT Validation → SecurityContextHolder → AuthorizationFilter → AuthorizationManager → Controller → Client

---

## 4. Important Differences

| Authentication | Authorization |
|---|---|
| Verifies user identity | Verifies user permissions |
| Checks username and password during login | Checks roles and authorities |
| Uses AuthenticationManager | Uses AuthorizationManager |
| DaoAuthenticationProvider handles credential verification | AuthorizationFilter handles request authorization |
| Failure generally results in 401 | Failure generally results in 403 |

## 5. Important Spring Security Classes

| Class / Interface | Responsibility |
|---|---|
| SecurityFilterChain | Defines the security filters and their configuration |
| AuthenticationFilter | Extracts login credentials and initiates authentication |
| AuthenticationManager | Coordinates authentication |
| DaoAuthenticationProvider | Authenticates using UserDetailsService |
| UserDetailsService | Loads user information from the database |
| UserDetails | Represents user information and authorities |
| PasswordEncoder | Encodes and verifies passwords |
| SecurityContextHolder | Holds the current security context |
| OncePerRequestFilter | Useful base class for implementing a JWT filter |
| AuthorizationFilter | Handles request authorization |
| AuthorizationManager | Makes authorization decisions |

## 6. Important Interview Points

1. AuthenticationManager does not directly call SecurityFilterChain.
2. AuthenticationProvider performs the actual authentication.
3. DaoAuthenticationProvider uses UserDetailsService to load user details.
4. PasswordEncoder.matches() verifies the submitted password.
5. JWT is generated after successful authentication.
6. JWT is validated on subsequent protected requests.
7. SecurityContextHolder stores the authenticated user's security information.
8. AuthorizationFilter delegates authorization decisions to AuthorizationManager.
9. HTTP 401 means authentication is required or unsuccessful.
10. HTTP 403 means the user is authenticated but does not have sufficient permissions.