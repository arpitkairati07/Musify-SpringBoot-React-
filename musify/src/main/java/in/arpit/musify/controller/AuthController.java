package in.arpit.musify.controller;

import in.arpit.musify.dto.RegisterRequest;
import in.arpit.musify.dto.UserResponse;
import in.arpit.musify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // For Login
    @PostMapping("/login")
    public String login(){
        return "This is a login API";
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
