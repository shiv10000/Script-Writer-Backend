package org.shivam.script_writer.service;

import org.shivam.script_writer.dto.UserRequest;
import org.shivam.script_writer.dto.UserResponse;
import org.shivam.script_writer.entity.User;
import org.shivam.script_writer.entity.UserCategory;
import org.shivam.script_writer.repo.UserCategoryRepository;
import org.shivam.script_writer.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCategoryRepository userCategoryRepository;

    public UserResponse createUser(UserRequest userRequest){
        if(userRepository.findByName(userRequest.name()).isPresent()){
            throw new RuntimeException("User already exist");
        }

        UserCategory userCategory = userCategoryRepository.findById(userRequest.userCategoryId()).orElseThrow(
                () -> new  RuntimeException("user category is null")
        );


        User savedUser = userRepository.save(new User(
                userRequest.name(),
                userRequest.email(),
                userRequest.password(),
                userCategory));


        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                userRequest.userCategoryId()
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

        UserCategory userCategory = userCategoryRepository.findById(userRequest.userCategoryId()).orElseThrow(
                () -> new  RuntimeException("user category is null")
        );

        existingUser.setEmail(userRequest.email());
        existingUser.setName(userRequest.name());
        existingUser.setPasswordHash(userRequest.password());
        existingUser.setUserCategory(userCategory);

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
}
