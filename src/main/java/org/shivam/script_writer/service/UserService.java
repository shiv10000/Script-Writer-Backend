package org.shivam.script_writer.service;

import org.shivam.script_writer.dto.UserRequest;
import org.shivam.script_writer.dto.UserResponse;
import org.shivam.script_writer.entity.User;
import org.shivam.script_writer.entity.UserCategory;
import org.shivam.script_writer.repo.UserCategoryRepository;
import org.shivam.script_writer.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final String DEFAULT_USER_CATEGORY = "USER";
    private static final String ADMIN_CATEGORY = "ADMIN";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCategoryRepository userCategoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserResponse createUser(UserRequest userRequest){
        if(userRepository.findByName(userRequest.name()).isPresent()){
            throw new RuntimeException("User already exist");
        }

        UserCategory userCategory = userCategoryRepository.findByName(DEFAULT_USER_CATEGORY)
                .orElseGet(() -> userCategoryRepository.save(new UserCategory(DEFAULT_USER_CATEGORY)));

        User savedUser = userRepository.save(new User(
                userRequest.name(),
                userRequest.email(),
                passwordEncoder.encode(userRequest.password()),
                userCategory));


        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getUserCategory().getId()
        );
    }
    public UserResponse deleteUser(Long userId){
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Not found user"));
        userRepository.delete(existingUser);
        return new UserResponse(
                existingUser.getId(),
                existingUser.getName(),
                existingUser.getEmail(),
                existingUser.getUserCategory().getId()
        );

    }

    public UserResponse updateUser(Long id,UserRequest userRequest){
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found user"));

        existingUser.setEmail(userRequest.email());
        existingUser.setName(userRequest.name());
        existingUser.setPasswordHash(passwordEncoder.encode(userRequest.password()));

        userRepository.save(existingUser);

        return new UserResponse(
                existingUser.getId(),
                existingUser.getName(),
                existingUser.getEmail(),
                existingUser.getUserCategory().getId()
        );

    }

    public List<UserResponse> getAllUsers(){
        List<User> listOfUser = userRepository.findAll();
        return  listOfUser.stream()
                .map(user -> {
                            return new UserResponse(
                                    user.getId(),
                                    user.getName(),
                                    user.getEmail(),
                                    user.getUserCategory().getId()
                            );
                        }
                ).toList();
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserCategory().getId()
        );
    }

    public UserResponse promoteToAdmin(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserCategory adminCategory = userCategoryRepository.findByName(ADMIN_CATEGORY)
                .orElseGet(() -> userCategoryRepository.save(new UserCategory(ADMIN_CATEGORY)));

        user.setUserCategory(adminCategory);
        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getUserCategory().getId()
        );
    }








}
