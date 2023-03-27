package com.api.users.transactions.repositories;

import com.api.users.transactions.enums.RoleName;
import com.api.users.transactions.models.RoleModel;
import com.api.users.transactions.models.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    };

    @Test
    public void CheckIfUsernameExists() {
        UserModel userModel = new UserModel();
        userModel.setUsername("sergiobrito");
        userModel.setName("Sérgio Wilson Rosa Brito");
        userModel.setCpf("06437245085");
        userModel.setEmail("sergiobrito@gmail.com");
        userModel.setPassword("123456");
        userModel.setBalance(new BigDecimal(1000));

        List<RoleModel> roles = new ArrayList<>();
        RoleModel roleModel = new RoleModel();
        roleModel.setRoleName(RoleName.ROLE_ADMIN);
        roles.add(roleModel);
        userModel.setRoles(roles);

        roleRepository.save(roleModel);
        userRepository.save(userModel);

        Optional<UserModel> expected =  userRepository.findByUsername("sergiobrito");

        assertThat(expected).isPresent();

    }

}
