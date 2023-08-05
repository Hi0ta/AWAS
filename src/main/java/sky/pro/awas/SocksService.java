package sky.pro.awas;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.pro.awas.exeption.FormatNotComplianceException;
import sky.pro.awas.exeption.ObjectAbsenceException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Service
@Transactional
public class SocksService {

    private static final Logger logger = LoggerFactory.getLogger(Socks.class);

    private final SocksRepository socksRepository;

    public SocksService(SocksRepository socksRepository) {
        this.socksRepository = socksRepository;
    }

    /**
     * Регистрирует приход носков на склад
     * <br> Использован метод репозитория {@link org.springframework.data.jpa.repository.JpaRepository#save(Object)}
     *
     * @param socks носки
     * @return оприходованные носки
     */
    public Socks createSocks(Socks socks) {
        logger.debug("launching the createSocks method");
        List<Socks> socksList = socksRepository.findAll();
        if (socksRepository.findAllByColorAndCottonPartEquals(socks.getColor(), socks.getCottonPart()) != null) {
            Socks changeSocks = socksRepository.findAllByColorAndCottonPartEquals(socks.getColor(), socks.getCottonPart());
            changeSocks.setQuantity(changeSocks.getQuantity() + socks.getQuantity());
            return socksRepository.save(changeSocks);
        }
        return socksRepository.save(socks);
    }

    /**
     * Позволяет отыскать носки в БД по их идентификационному номеру
     *
     * @param socksId идентификационный номер
     * @return носки
     */
    public Socks getSocksBySocksId(long socksId) {
        logger.debug("launching the getSocksBySocksId method");
        return socksRepository.findBySocksId(socksId);
    }

    /**
     * Регистрирует отпуск носков со склада.
     *
     * @param socksId  идентификатор носков
     * @param quantity отпущенное количество (не может быть отрицательным)
     * @return остаток носков.
     */
    public Socks editeSocks(@NotNull long socksId, @Positive int quantity) throws ObjectAbsenceException, FormatNotComplianceException {
        logger.debug("launching the editeSocks method");
        Socks changeSocks = getSocksBySocksId(socksId);
        if (changeSocks == null) {
            throw new ObjectAbsenceException("Носки с таким socksId не найдены в БД");
        }
        if (quantity > changeSocks.getQuantity()) {
            throw new FormatNotComplianceException("На складе нет такого количества носков");
        }
        int hangeQuantity = changeSocks.getQuantity() - quantity;
        changeSocks.setQuantity(hangeQuantity);
        return socksRepository.save(changeSocks);
    }

    /**
     * Возвращает общее количество носков на складе согласно заданным критериям (цвета и состава)
     *
     * @param color      цвет
     * @param operation  оператор
     * @param cottonPart содержание хлопка
     * @return общее количество носков на складе
     */
    public Integer findAllByColorAndCottonPart(String color, Function operation, int cottonPart) {
        logger.debug("launching the findAllByColorAndCottonPart method");
        return switch (operation) {
            case MORE_THAN ->
                    socksRepository.findAllByColorAndCottonPartAfter(color, cottonPart).stream().mapToInt(Socks::getQuantity).sum();
            case LESS_THAN ->
                    socksRepository.findAllByColorAndCottonPartBefore(color, cottonPart).stream().mapToInt(Socks::getQuantity).sum();
            case EQUAL -> socksRepository.findAllByColorAndCottonPartEquals(color, cottonPart).getQuantity();
        };
    }
}
