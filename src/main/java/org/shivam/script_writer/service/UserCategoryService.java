package org.shivam.script_writer.service;

import org.shivam.script_writer.dto.UserCategoryRequest;
import org.shivam.script_writer.dto.UserCategoryResponse;
import org.shivam.script_writer.entity.UserCategory;
import org.shivam.script_writer.repo.UserCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserCategoryService {

    private final UserCategoryRepository userCategoryRepository;

    public UserCategoryService(UserCategoryRepository userCategoryRepository) {
        this.userCategoryRepository = userCategoryRepository;
    }

    public UserCategoryResponse createUserCategory(UserCategoryRequest userCategoryRequest){

         if(userCategoryRepository.findByName(userCategoryRequest.name()).isPresent()){
             throw new RuntimeException("category is already there");
         }


        UserCategory userCategory = userCategoryRepository.save(new UserCategory(userCategoryRequest.name()));


        return new UserCategoryResponse(userCategory.getId(),userCategory.getName());
    }


    public UserCategoryResponse updateUserCategory(Long id,UserCategoryRequest userCategoryRequest){

      UserCategory existingUserCategory =  userCategoryRepository.findById(id).orElseThrow(
              () -> new RuntimeException("Category is not present")
      );
        Optional<UserCategory> categoryWithSameName =
                userCategoryRepository.findByName(userCategoryRequest.name());

        if (categoryWithSameName.isPresent()
                && !categoryWithSameName.get().getId().equals(id)) {
            throw new RuntimeException("category name already exists");
        }
        existingUserCategory.setName(userCategoryRequest.name());


        UserCategory userCategory = userCategoryRepository.save(existingUserCategory);
        return new UserCategoryResponse(userCategory.getId(),userCategory.getName());
    }


    public List<UserCategoryResponse> getAllUserCategory(){

        List<UserCategory> listUserCategory = userCategoryRepository.findAll();

        return listUserCategory.stream()
                .map(
                        userCategory ->
                            new UserCategoryResponse(userCategory.getId(),userCategory.getName())

                )
                .toList();

    }

    public UserCategoryResponse deleteUserCategory(UserCategoryRequest userCategoryRequest){
        Optional<UserCategory> userCategory =  userCategoryRepository.findByName(userCategoryRequest.name());

        if(userCategory.isEmpty())  throw new RuntimeException("category is not there");

        UserCategory existingUserCategory = userCategory.get();

        userCategoryRepository.delete(existingUserCategory);

        return new UserCategoryResponse(existingUserCategory.getId(),existingUserCategory.getName());

    }

    public UserCategoryResponse getUserCategoryById(Long id) {
        UserCategory userCategory = userCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("category is not there"));

        return new UserCategoryResponse(
                userCategory.getId(),
                userCategory.getName()
        );
    }

}
