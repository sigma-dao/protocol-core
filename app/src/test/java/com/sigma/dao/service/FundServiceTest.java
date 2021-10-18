package com.sigma.dao.service;

import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Fund;
import com.sigma.dao.model.repository.FundRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FundServiceTest {
    private FundService fundService;
    private FundRepository fundRepository;
    @Before
    public void setup() {
        fundRepository = Mockito.mock(FundRepository.class);
        fundService = new FundService(fundRepository);
    }
    @Test
    public void testMissingDisbursementFrequency() {
        try {
            fundService.create(new Fund());
        } catch(ProtocolException e) {
            Assertions.assertEquals(e.getMessage(), ErrorCode.E0001);
        }
    }
}