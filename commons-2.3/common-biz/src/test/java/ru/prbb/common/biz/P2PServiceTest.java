package ru.prbb.common.biz;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

/**
 * @author lesinsa on 10.07.2015.
 */
public class P2PServiceTest {

    @InjectMocks
    private P2PService sut;
    @Mock
    private P2PConfig config;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void test1() throws Exception {
        P2PParams input = new P2PParams();
        input.setNonce("b87ab2b03616ee04241c8a8e7517ed2e");
        input.setAmount("10.00");
        input.setOrder("123456");
        input.setTimestamp("20150710134121");
        input.setTrType("1");
        input.setTerminal("CISPAYMB");

        sut.ecommPrepare(input, "0732d0f58cfddfca492de86a872d033e", TrType.WRITE_OFF);

        assertEquals("e1488cde030fba2a372b76b13573e63cdd179681", input.getSign());

    }
}