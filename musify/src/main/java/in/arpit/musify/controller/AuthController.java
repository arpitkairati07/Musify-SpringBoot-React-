package in.arpit.musify.controller;

import in.arpit.musify.document.User;
import in.arpit.musify.dto.AuthRequest;
import in.arpit.musify.dto.AuthResponse;
import in.arpit.musify.dto.RegisterRequest;
import in.arpit.musify.dto.UserResponse;
import in.arpit.musify.service.AppUserDetailService;
import in.arpit.musify.service.UserService;
import in.arpit.musify.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailService userDetailService;
    private final JwtUtil jwtUtil;

    // For Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){
        try{
            // Authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                    request.getPassword()));
            // Load the user details
            UserDetails userDetails = userDetailService.loadUserByUsername(request.getEmail());
            User existingUser = userService.findByEmail(request.getEmail());

            // Generate the JWT token
            String token=jwtUtil.generateToken(userDetails,existingUser.getRole().name());

            return ResponseEntity.ok(new AuthResponse(token,request.getEmail(),existingUser.getRole().name()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Email/ Password is incorrect");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // For Registration
    @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        try{
           UserResponse response =  userService.registerUser(request);
           return ResponseEntity.ok(response);
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
