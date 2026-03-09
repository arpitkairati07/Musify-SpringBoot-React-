package in.arpit.musify.service;

import in.arpit.musify.document.User;
import in.arpit.musify.dto.RegisterRequest;
import in.arpit.musify.dto.UserResponse;
import in.arpit.musify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(RegisterRequest request){

        // Check if already exists
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already in use");
        }
       User newUser = User.builder().email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
               .role(User.Role.USER).build();

        userRepository.save(newUser);

        return UserResponse.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .role(UserResponse.Role.USER).build();
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"+email));
    }
}
