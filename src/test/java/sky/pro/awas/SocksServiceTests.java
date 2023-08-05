package sky.pro.awas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.awas.exeption.FormatNotComplianceException;
import sky.pro.awas.exeption.ObjectAbsenceException;

import javax.validation.constraints.Positive;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static sky.pro.awas.Function.*;

@ExtendWith(MockitoExtension.class)
public class SocksServiceTests {

    @Mock
    private SocksRepository socksRepository;
    @InjectMocks
    private SocksService socksService;

    private final Long socksId = 1L;
    private final String color = "red";
    private final int cottonPart = 70;
    @Positive
    private final int quantity = 20;
    Socks socks = new Socks(socksId, color, cottonPart, quantity);
    @Test
    public void checkCreateSocks(){

        when(socksRepository.save(socks)).thenReturn(socks);
        Socks checkedSokcs = socksService.createSocks(socks);
        assertEquals(checkedSokcs, socks);
    }

    @Test
    public void checkCreateSocksAlreadyExisting(){
        when(socksRepository.findAllByColorAndCottonPartEquals(socks.getColor(), socks.getCottonPart())).thenReturn(socks);
        when(socksRepository.save(socks)).thenReturn(socks);
        socks.setQuantity(socks.getQuantity() * 2);
        Socks checkedSokcs = socksService.createSocks(socks);
        assertEquals(checkedSokcs, socks);
    }

    @Test
    public void checkEditeSocks(){
        int minusQuantity = 5;
        when(socksRepository.findBySocksId(socksId)).thenReturn(socks);
        socks.setQuantity(socks.getQuantity() - minusQuantity);
        when(socksRepository.save(socks)).thenReturn(socks);
        Socks checkedSokcs = socksService.editeSocks(socksId, minusQuantity);
        assertEquals(checkedSokcs, socks);
    }

    @Test
    public void checkExceptionWhenEditeSocks(){
        when(socksRepository.findBySocksId(socksId)).thenReturn(null);
        assertThrows(ObjectAbsenceException.class, () -> socksService.editeSocks(socksId, quantity));
    }

    @Test
    public void checkExceptionWhenEditeSocks2(){
        int bigQuantity = 50;
        when(socksRepository.findBySocksId(socksId)).thenReturn(socks);
        assertThrows(FormatNotComplianceException.class, () -> socksService.editeSocks(socksId, bigQuantity));
    }
    @Test
    public void checkFindAllByColorAndCottonPartMore(){
        int cottonPartVariable = 50;
        when(socksRepository.findAllByColorAndCottonPartAfter(color, cottonPartVariable)).thenReturn(List.of(socks));
        int quantitySum = List.of(socks).stream().mapToInt(Socks::getQuantity).sum();
        int checkedQuantity = socksService.findAllByColorAndCottonPart(color, MORE_THAN, cottonPartVariable);
        assertEquals(checkedQuantity, quantitySum);
    }
    @Test
    public void checkFindAllByColorAndCottonPartLess(){
        int cottonPartVariable = 95;
        when(socksRepository.findAllByColorAndCottonPartBefore(color, cottonPartVariable)).thenReturn(List.of(socks));
        int quantitySum = List.of(socks).stream().mapToInt(Socks::getQuantity).sum();
        int checkedQuantity = socksService.findAllByColorAndCottonPart(color, LESS_THAN, cottonPartVariable);
        assertEquals(checkedQuantity, quantitySum);
    }

    @Test
    public void checkFindAllByColorAndCottonPart(){
        int cottonPartVariable = 70;
        when(socksRepository.findAllByColorAndCottonPartEquals(color, cottonPartVariable)).thenReturn(socks);
        int quantitySum = socks.getQuantity();
        int checkedQuantity = socksService.findAllByColorAndCottonPart(color, EQUAL, cottonPartVariable);
        assertEquals(checkedQuantity, quantitySum);
    }
}
