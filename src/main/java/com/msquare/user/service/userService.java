package com.msquare.user.service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.msquare.user.repo.userRepo;
import com.msquare.user.entity.userEntity;
import com.msquare.user.model.userDTO;

@Service
public class userService {
    private final ModelMapper modelMapper;
    private final userRepo userRepo;

    @Autowired
    public userService(ModelMapper modelMapper, userRepo userRepo) {
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
    }

    public String getUsernameById(Long id) {
        return userRepo.findById(id)
                .map(userEntity -> userEntity.getUsername())
                .orElse(null);
    }

    public void saveUser(userDTO userDTO) {
        userEntity user = userEntity.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .build();
        userRepo.save(user);
    }

}
