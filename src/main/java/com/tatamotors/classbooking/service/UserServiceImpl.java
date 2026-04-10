////////package com.tatamotors.classbooking.service;
////////
////////import org.springframework.stereotype.Service;
////////
////////import com.tatamotors.classbooking.dto.LoginRequest;
////////import com.tatamotors.classbooking.dto.LoginResponse;
////////import com.tatamotors.classbooking.dto.SignupRequest;
////////import com.tatamotors.classbooking.entity.Role;
////////import com.tatamotors.classbooking.entity.User;
////////import com.tatamotors.classbooking.repository.RoleRepository;
////////import com.tatamotors.classbooking.repository.UserRepository;
////////
////////@Service
////////public class UserServiceImpl implements UserService {
////////
////////    private final UserRepository userRepository;
////////    private final RoleRepository roleRepository;
////////
////////    // ✅ CLEAN CONSTRUCTOR (NO PasswordEncoder, NO JwtUtil)
////////    public UserServiceImpl(UserRepository userRepository,
////////                           RoleRepository roleRepository) {
////////        this.userRepository = userRepository;
////////        this.roleRepository = roleRepository;
////////    }
////////
////////    @Override
////////    public User signup(SignupRequest request) {
////////
////////        if (userRepository.existsByEmail(request.getEmail())) {
////////            throw new RuntimeException("Email already registered");
////////        }
////////
////////        Role userRole = roleRepository.findByName("USER")
////////                .orElseThrow(() -> new RuntimeException("USER role not found"));
////////
////////        User user = new User();
////////        user.setName(request.getName());
////////        user.setEmail(request.getEmail());
////////
////////        // ⚠️ TEMP: plain password (JWT & encoder removed)
////////        user.setPassword(request.getPassword());
////////
////////        user.setRole(userRole);
////////        user.setCreatedAt(java.time.LocalDateTime.now());
////////
////////        return userRepository.save(user);
////////    }
////////
////////    @Override
////////    public LoginResponse login(LoginRequest request) {
////////
////////        User user = userRepository.findByEmail(request.getEmail())
////////                .orElseThrow(() -> new RuntimeException("User not found"));
////////
////////        // ⚠️ TEMP: plain password check
////////        if (!request.getPassword().equals(user.getPassword())) {
////////            throw new RuntimeException("Invalid password");
////////        }
////////
////////        // ⚠️ TEMP: dummy token
////////        String token = "DUMMY_TOKEN";
////////
////////        return new LoginResponse(
////////                token,
////////                user.getEmail(),
////////                user.getRole().getName(),
////////                user.getId()
////////        );
////////    }
////////}
//////
//////
//////
////////package com.tatamotors.classbooking.service;
////////
////////import org.springframework.security.crypto.password.PasswordEncoder;
////////import org.springframework.stereotype.Service;
////////
////////import com.tatamotors.classbooking.dto.LoginRequest;
////////import com.tatamotors.classbooking.dto.LoginResponse;
////////import com.tatamotors.classbooking.dto.SignupRequest;
////////import com.tatamotors.classbooking.entity.Role;
////////import com.tatamotors.classbooking.entity.User;
////////import com.tatamotors.classbooking.repository.RoleRepository;
////////import com.tatamotors.classbooking.repository.UserRepository;
////////
////////@Service
////////public class UserServiceImpl implements UserService {
////////
////////    private final UserRepository userRepository;
////////    private final RoleRepository roleRepository;
////////    private final PasswordEncoder passwordEncoder; // ✅ ADD THIS
////////
////////    // ✅ UPDATED CONSTRUCTOR
////////    public UserServiceImpl(UserRepository userRepository,
////////                           RoleRepository roleRepository,
////////                           PasswordEncoder passwordEncoder) {
////////        this.userRepository = userRepository;
////////        this.roleRepository = roleRepository;
////////        this.passwordEncoder = passwordEncoder;
////////    }
////////
////////    @Override
////////    public User signup(SignupRequest request) {
////////
////////        if (userRepository.existsByEmail(request.getEmail())) {
////////            throw new RuntimeException("Email already registered");
////////        }
////////
////////        Role userRole = roleRepository.findByName("USER")
////////                .orElseThrow(() -> new RuntimeException("USER role not found"));
////////
////////        User user = new User();
////////        user.setName(request.getName());
////////        user.setEmail(request.getEmail());
////////
////////        // ✅ HASH PASSWORD (FINAL FIX)
////////        user.setPassword(passwordEncoder.encode(request.getPassword()));
////////
////////        user.setRole(userRole);
////////        user.setCreatedAt(java.time.LocalDateTime.now());
////////
////////        return userRepository.save(user);
////////    }
////////
////////    @Override
////////    public LoginResponse login(LoginRequest request) {
////////
////////        User user = userRepository.findByEmail(request.getEmail())
////////                .orElseThrow(() -> new RuntimeException("User not found"));
////////
////////        // ✅ CORRECT PASSWORD CHECK (FINAL FIX)
////////        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
////////            throw new RuntimeException("Invalid credentials");
////////        }
////////
////////        // ⚠️ TEMP: dummy token (later we can add JWT)
////////        String token = "DUMMY_TOKEN";
////////
////////        return new LoginResponse(
////////                token,
////////                user.getEmail(),
////////                user.getRole().getName(),
////////                user.getId()
////////        );
////////    }
////////}
//////
////////}
//////
//////
//////
//////
//////
////////package com.tatamotors.classbooking.service;
////////
////////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////////import org.springframework.stereotype.Service;
////////
////////import com.tatamotors.classbooking.dto.LoginRequest;
////////import com.tatamotors.classbooking.dto.LoginResponse;
////////import com.tatamotors.classbooking.dto.SignupRequest;
////////import com.tatamotors.classbooking.entity.Role;
////////import com.tatamotors.classbooking.entity.User;
////////import com.tatamotors.classbooking.repository.RoleRepository;
////////import com.tatamotors.classbooking.repository.UserRepository;
////////
////////@Service
////////public class UserServiceImpl implements UserService {
////////
////////    private final UserRepository userRepository;
////////    private final RoleRepository roleRepository;
////////    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
////////
////////    public UserServiceImpl(UserRepository userRepository,
////////                           RoleRepository roleRepository) {
////////        this.userRepository = userRepository;
////////        this.roleRepository = roleRepository;
////////    }
////////
////////    @Override
////////    public User signup(SignupRequest request) {
////////
////////        if (userRepository.existsByEmail(request.getEmail())) {
////////            throw new RuntimeException("Email already registered");
////////        }
////////
////////        // Find the USER role - make sure your roles table has a row with name="USER"
////////        Role userRole = roleRepository.findByName("USER")
////////                .orElseThrow(() -> new RuntimeException(
////////                    "USER role not found in database. " +
////////                    "Please run: INSERT INTO roles (name) VALUES ('USER'), ('ADMIN');"));
////////
////////        User user = new User();
////////        user.setName(request.getName());
////////        user.setEmail(request.getEmail().trim().toLowerCase());
////////        user.setPassword(encoder.encode(request.getPassword())); // always hash password
////////        user.setRole(userRole);
////////        user.setCreatedAt(java.time.LocalDateTime.now());
////////
////////        return userRepository.save(user);
////////    }
////////
////////    @Override
////////    public LoginResponse login(LoginRequest request) {
////////
////////        System.out.println("=== LOGIN ATTEMPT ===");
////////        System.out.println("Email: " + request.getEmail());
////////
////////        // Find user by email (case-insensitive)
////////        User user = userRepository
////////                .findByEmail(request.getEmail().trim().toLowerCase())
////////                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));
////////
////////        // Safety check - if user has no role assigned
////////        if (user.getRole() == null) {
////////            throw new RuntimeException("User has no role assigned. Please fix in database.");
////////        }
////////
////////        String dbPassword = user.getPassword().trim();
////////        String inputPassword = request.getPassword().trim();
////////
////////        boolean isMatch;
////////        if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$")) {
////////            // BCrypt hashed password
////////            isMatch = encoder.matches(inputPassword, dbPassword);
////////        } else {
////////            // Plain text password (for old test users in DB)
////////            isMatch = dbPassword.equals(inputPassword);
////////        }
////////
////////        System.out.println("Password match: " + isMatch);
////////
////////        if (!isMatch) {
////////            throw new RuntimeException("Invalid credentials");
////////        }
////////
////////        return new LoginResponse(
////////                "DUMMY_TOKEN",
////////                user.getEmail(),
////////                user.getRole().getName(),
////////                user.getId()
////////        );
////////    }
////////}
//////
//////
//////
//////
//////package com.tatamotors.classbooking.service;
//////
//////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//////import org.springframework.stereotype.Service;
//////
//////import com.tatamotors.classbooking.dto.LoginRequest;
//////import com.tatamotors.classbooking.dto.LoginResponse;
//////import com.tatamotors.classbooking.dto.SignupRequest;
//////import com.tatamotors.classbooking.entity.Role;
//////import com.tatamotors.classbooking.entity.User;
//////import com.tatamotors.classbooking.repository.RoleRepository;
//////import com.tatamotors.classbooking.repository.UserRepository;
//////
//////@Service
//////public class UserServiceImpl implements UserService {
//////
//////    private final UserRepository userRepository;
//////    private final RoleRepository roleRepository;
//////    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//////
//////    public UserServiceImpl(UserRepository userRepository,
//////                           RoleRepository roleRepository) {
//////        this.userRepository = userRepository;
//////        this.roleRepository = roleRepository;
//////    }
//////
//////    @Override
//////    public User signup(SignupRequest request) {
//////
//////        if (userRepository.existsByEmail(request.getEmail())) {
//////            throw new RuntimeException("Email already registered");
//////        }
//////
//////        // Pick role from request — default to USER if nothing sent
//////        String roleName = "USER";
//////        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
//////            roleName = "ADMIN";
//////        }
//////        final String finalRoleName = roleName;
//////        Role role = roleRepository.findByName(roleName)
//////                .orElseThrow(() -> new RuntimeException(finalRoleName + " role not found in database"));
//////
//////        User user = new User();
//////        user.setName(request.getName());
//////        user.setEmail(request.getEmail().trim().toLowerCase());
//////        user.setPassword(encoder.encode(request.getPassword()));
//////        user.setRole(role);
//////        user.setCreatedAt(java.time.LocalDateTime.now());
//////
//////        return userRepository.save(user);
//////    }
//////
//////    @Override
//////    public LoginResponse login(LoginRequest request) {
//////
//////        User user = userRepository
//////                .findByEmail(request.getEmail().trim().toLowerCase())
//////                .orElseThrow(() -> new RuntimeException("User not found"));
//////
//////        if (user.getRole() == null) {
//////            throw new RuntimeException("User has no role assigned");
//////        }
//////
//////        String dbPassword = user.getPassword().trim();
//////        String inputPassword = request.getPassword().trim();
//////
//////        boolean isMatch;
//////        if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$")) {
//////            isMatch = encoder.matches(inputPassword, dbPassword);
//////        } else {
//////            isMatch = dbPassword.equals(inputPassword);
//////        }
//////
//////        if (!isMatch) {
//////            throw new RuntimeException("Invalid credentials");
//////        }
//////
//////        return new LoginResponse(
//////                "DUMMY_TOKEN",
//////                user.getEmail(),
//////                user.getRole().getName(),
//////                user.getId()
//////        );
//////    }
//////}
//////
//////
//////
//////
//////
////
////
////package com.tatamotors.classbooking.service;
////
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.stereotype.Service;
////
////import com.tatamotors.classbooking.dto.LoginRequest;
////import com.tatamotors.classbooking.dto.LoginResponse;
////import com.tatamotors.classbooking.dto.SignupRequest;
////import com.tatamotors.classbooking.entity.Role;
////import com.tatamotors.classbooking.entity.User;
////import com.tatamotors.classbooking.repository.RoleRepository;
////import com.tatamotors.classbooking.repository.UserRepository;
////
////@Service
////public class UserServiceImpl implements UserService {
////
////    private final UserRepository userRepository;
////    private final RoleRepository roleRepository;
////    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
////
////    public UserServiceImpl(UserRepository userRepository,
////                           RoleRepository roleRepository) {
////        this.userRepository = userRepository;
////        this.roleRepository = roleRepository;
////    }
////
////    @Override
////    public User signup(SignupRequest request) {
////
////        if (userRepository.existsByEmail(request.getEmail())) {
////            throw new RuntimeException("Email already registered");
////        }
////
////        // Pick role from request — default to USER if nothing sent
////        String roleName = "USER";
////        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
////            roleName = "ADMIN";
////        }
////
////        Role role = roleRepository.findByName(roleName)
////                .orElseThrow(() -> new RuntimeException(roleName + " role not found in database"));
////
////        User user = new User();
////        user.setName(request.getName());
////        user.setEmail(request.getEmail().trim().toLowerCase());
////        user.setPassword(encoder.encode(request.getPassword()));
////        user.setRole(role);
////        user.setCreatedAt(java.time.LocalDateTime.now());
////
////        return userRepository.save(user);
////    }
////
////    @Override
////    public LoginResponse login(LoginRequest request) {
////
////        User user = userRepository
////                .findByEmail(request.getEmail().trim().toLowerCase())
////                .orElseThrow(() -> new RuntimeException("User not found"));
////
////        if (user.getRole() == null) {
////            throw new RuntimeException("User has no role assigned");
////        }
////
////        String dbPassword = user.getPassword().trim();
////        String inputPassword = request.getPassword().trim();
////
////        boolean isMatch;
////        if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$")) {
////            isMatch = encoder.matches(inputPassword, dbPassword);
////        } else {
////            isMatch = dbPassword.equals(inputPassword);
////        }
////
////        if (!isMatch) {
////            throw new RuntimeException("Invalid credentials");
////        }
////
////        return new LoginResponse(
////                "DUMMY_TOKEN",
////                user.getEmail(),
////                user.getRole().getName(),
////                user.getId()
////        );
////    }
////}
//
//package com.tatamotors.classbooking.service;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.tatamotors.classbooking.dto.LoginRequest;
//import com.tatamotors.classbooking.dto.LoginResponse;
//import com.tatamotors.classbooking.dto.SignupRequest;
//import com.tatamotors.classbooking.entity.Role;
//import com.tatamotors.classbooking.entity.User;
//import com.tatamotors.classbooking.repository.RoleRepository;
//import com.tatamotors.classbooking.repository.UserRepository;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//    public UserServiceImpl(UserRepository userRepository,
//                           RoleRepository roleRepository) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//    }
//
//    @Override
//    public User signup(SignupRequest request) {
//
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("Email already registered");
//        }
//
//        // Pick role from request — default to USER if nothing sent
//        String roleName = "USER";
//        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
//            roleName = "ADMIN";
//        }
//        final String finalRoleName = roleName;
//        Role role = roleRepository.findByName(roleName)
//                .orElseThrow(() -> new RuntimeException(finalRoleName + " role not found in database"));
//
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail().trim().toLowerCase());
//        user.setPassword(encoder.encode(request.getPassword()));
//        user.setRole(role);
//        user.setCreatedAt(java.time.LocalDateTime.now());
//
//        return userRepository.save(user);
//    }
//
//    @Override
//    public LoginResponse login(LoginRequest request) {
//
//        User user = userRepository
//                .findByEmail(request.getEmail().trim().toLowerCase())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (user.getRole() == null) {
//            throw new RuntimeException("User has no role assigned");
//        }
//
//        String dbPassword = user.getPassword().trim();
//        String inputPassword = request.getPassword().trim();
//
//        boolean isMatch;
//        if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$")) {
//            isMatch = encoder.matches(inputPassword, dbPassword);
//        } else {
//            isMatch = dbPassword.equals(inputPassword);
//        }
//
//        if (!isMatch) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        return new LoginResponse(
//                "DUMMY_TOKEN",
//                user.getEmail(),
//                user.getRole().getName(),
//                user.getId()
//        );
//    }
//}


package com.tatamotors.classbooking.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tatamotors.classbooking.dto.LoginRequest;
import com.tatamotors.classbooking.dto.LoginResponse;
import com.tatamotors.classbooking.dto.SignupRequest;
import com.tatamotors.classbooking.entity.Role;
import com.tatamotors.classbooking.entity.User;
import com.tatamotors.classbooking.repository.RoleRepository;
import com.tatamotors.classbooking.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String roleName = (request.getRole() != null &&
                request.getRole().toUpperCase().equals("ADMIN")) ? "ADMIN" : "USER";

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == null) {
            throw new RuntimeException("User has no role assigned");
        }

        String dbPassword = user.getPassword().trim();
        String inputPassword = request.getPassword().trim();

        boolean isMatch;
        if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$")) {
            isMatch = encoder.matches(inputPassword, dbPassword);
        } else {
            isMatch = dbPassword.equals(inputPassword);
        }

        if (!isMatch) {
            throw new RuntimeException("Invalid credentials");
        }

        // Return name so frontend can show real name instead of email prefix
        return new LoginResponse(
                "DUMMY_TOKEN",
                user.getName(),       // ← real name from DB
                user.getEmail(),
                user.getRole().getName(),
                user.getId()
        );
    }
}