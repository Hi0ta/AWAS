package sky.pro.awas;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
        return socksRepository.save(socks);
    }

    /**
     * Регистрирует отпуск носков со склада.
     *
     * @param socksId  идентификатор носков
     * @param quantity отпущенное количество (не может быть отрицательным)
     * @return остаток носков.
     */
    public Socks editeSocks(@NotNull long socksId, @Positive int quantity) {
        logger.debug("launching the editeSocks method");
        Socks changeSocks = socksRepository.findBySocksId(socksId);
        int hangeQuantity = changeSocks.getQuantity() - quantity;
        changeSocks.setQuantity(hangeQuantity);
        return socksRepository.save(changeSocks);
    }

    /**
     * Возвращает общее количество носков на складе согласно заданным критериям (цвета и состава)
     *
     * @param color      цвет
     * @param function  оператор
     * @param cottonPart содержание хлопка
     * @return общее количество носков на складе
     */
    public Integer findAllByColorAndCottonPart(String color, Function function, int cottonPart) {
        logger.debug("launching the findAllByColorAndCottonPart method");
        return switch (function) {
            case MORE_THAN ->
                    socksRepository.findAllByColorAndCottonPartAfter(color, cottonPart).stream().mapToInt(Socks::getQuantity).sum();
            case LESS_THAN ->
                    socksRepository.findAllByColorAndCottonPartBefore(color, cottonPart).stream().mapToInt(Socks::getQuantity).sum();
            case EQUAL ->
                    socksRepository.findAllByColorAndCottonPartEquals(color, cottonPart).stream().mapToInt(Socks::getQuantity).sum();
        };
    }
}
