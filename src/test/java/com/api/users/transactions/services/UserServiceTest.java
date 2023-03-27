package com.api.users.transactions.services;

import com.api.users.transactions.configs.security.JwtService;
import com.api.users.transactions.dtos.UserAuthResponseDto;
import com.api.users.transactions.dtos.UserLoginDto;
import com.api.users.transactions.dtos.UserRegisterDto;
import com.api.users.transactions.enums.RoleName;
import com.api.users.transactions.exception.ApiRequestException;
import com.api.users.transactions.models.RoleModel;
import com.api.users.transactions.models.UserModel;
import com.api.users.transactions.repositories.RoleRepository;
import com.api.users.transactions.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DataJpaTest
@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {

    private UserService userService;

    @Autowired
    private  RoleRepository roleRepository;

    @Mock
    private  UserRepository userRepository;
    @Mock
    private  JwtService jwtService;
    @Mock
    private  AuthenticationManager authenticationManager;
    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository,roleRepository,jwtService,authenticationManager);
    };

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
    };

    @Test
    void saveUser() {
        UserRegisterDto userRegisterDto =  new UserRegisterDto();
        userRegisterDto.setUsername("sergiobrito");
        userRegisterDto.setName("Sérgio Wilson Rosa Brito");
        userRegisterDto.setCpf("06437245085");
        userRegisterDto.setEmail("sergiobrito@gmail.com");
        userRegisterDto.setPassword("123456");
        userRegisterDto.setBalance(new BigDecimal(1000));
        List<RoleName> rolesNames = new ArrayList<>();
        rolesNames.add(RoleName.ROLE_ADMIN);
        userRegisterDto.setRoles(rolesNames);

        RoleModel roleModel = new RoleModel();
        roleModel.setRoleName(RoleName.ROLE_ADMIN);
        roleRepository.save(roleModel);

        userService.saveUser(userRegisterDto,bindingResult);
        verify(userRepository).save(Mockito.any(UserModel.class));
    }

    @Test
    void willThrowWhenUsernameAlreadyInUse() {
        UserRegisterDto userRegisterDto_1 =  new UserRegisterDto();
        userRegisterDto_1.setUsername("sergiobrito");
        userRegisterDto_1.setName("Sérgio Wilson Rosa Brito");
        userRegisterDto_1.setCpf("06437245085");
        userRegisterDto_1.setEmail("sergiobrito@gmail.com");
        userRegisterDto_1.setPassword("123456");
        userRegisterDto_1.setBalance(new BigDecimal(1000));
        List<RoleName> rolesNames = new ArrayList<>();
        rolesNames.add(RoleName.ROLE_ADMIN);
        userRegisterDto_1.setRoles(rolesNames);

        UserRegisterDto userRegisterDto_2 =  new UserRegisterDto();
        userRegisterDto_2.setUsername("sergiobrito");
        userRegisterDto_2.setName("Sérgio Wilson Rosa Brito");
        userRegisterDto_2.setCpf("06437245085");
        userRegisterDto_2.setEmail("sergiobrito@gmail.com");
        userRegisterDto_2.setPassword("123456");
        userRegisterDto_2.setBalance(new BigDecimal(1000));
        userRegisterDto_2.setRoles(rolesNames);

        RoleModel roleModel = new RoleModel();
        roleModel.setRoleName(RoleName.ROLE_ADMIN);
        roleRepository.save(roleModel);

        userService.saveUser(userRegisterDto_1,bindingResult);

        given(userRepository.findByUsername(userRegisterDto_2.getUsername()))
                .willReturn(Optional.of(new UserModel()));

        assertThatThrownBy(() -> userService.saveUser(userRegisterDto_2, bindingResult))
                .isInstanceOf(ApiRequestException.class)
                .hasMessage("Username already in use!");

    }

    @Test
    void UserLogin(){
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setUsername("sergiobrito");
        userLoginDto.setPassword("123456");

        given(userRepository.findByUsername(userLoginDto.getUsername()))
                .willReturn(Optional.of(new UserModel()));

        UserAuthResponseDto responseDto = userService.login(userLoginDto,bindingResult);
        assertThat(responseDto).isInstanceOf(UserAuthResponseDto.class);
    };


}