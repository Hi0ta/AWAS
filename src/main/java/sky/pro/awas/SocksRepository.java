package sky.pro.awas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Long> {

    Socks findBySocksId(long socksId);

    List<Socks> findAllByColorAndCottonPartAfter(String color, int cottonPart);

    List<Socks> findAllByColorAndCottonPartBefore(String color, int cottonPart);

    Socks findAllByColorAndCottonPartEquals(String color, int cottonPart);

}
