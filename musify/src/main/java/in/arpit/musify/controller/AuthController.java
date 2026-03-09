package in.arpit.musify.controller;

import in.arpit.musify.dto.AuthRequest;
import in.arpit.musify.dto.AuthResponse;
import in.arpit.musify.dto.RegisterRequest;
import in.arpit.musify.dto.UserResponse;
import in.arpit.musify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    // For Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                    request.getPassword()));
            return ResponseEntity.ok(new AuthResponse("token",request.getEmail(),"USER"));
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
