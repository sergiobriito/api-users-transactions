package com.api.users.transactions.services;

import com.api.users.transactions.configs.rabbitmq.RabbitMQService;
import com.api.users.transactions.dtos.TransactionDto;
import com.api.users.transactions.models.TransactionModel;
import com.api.users.transactions.models.UserModel;
import com.api.users.transactions.repositories.TransactionRepository;
import com.api.users.transactions.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@DataJpaTest
@RunWith(MockitoJUnitRunner.class)
class TransactionServiceTest {

    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserService userService;
    @Mock
    private RabbitMQService rabbitMQService;
    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp(){
        transactionService = new TransactionService(transactionRepository,userService,rabbitMQService);
    };

    @Test
    void saveTransaction() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setPayer("sergio");
        transactionDto.setPayee("wilson");
        transactionDto.setValue(new BigDecimal(150));

        UserModel payer = new UserModel();
        payer.setUsername("sergio");
        payer.setBalance(new BigDecimal(500));

        UserModel payee = new UserModel();
        payee.setUsername("wilson");
        payee.setBalance(new BigDecimal(0));

        given(userService.findByUsername(transactionDto.getPayer()))
                .willReturn(Optional.of(payer));

        given(userService.findByUsername(transactionDto.getPayee()))
                .willReturn(Optional.of(payee));

        TransactionModel transactionModel = transactionService.save(transactionDto, bindingResult);
        verify(transactionRepository).save(Mockito.any(TransactionModel.class));
        assertThat(transactionModel).isInstanceOf(TransactionModel.class);
    }


}