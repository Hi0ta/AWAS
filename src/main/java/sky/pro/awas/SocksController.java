package sky.pro.awas;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@Slf4j
//@RequiredArgsConstructor
@RestController
@RequestMapping("/socks")
public class SocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping("/income") // POST http://localhost:8080/socks/income
    public ResponseEntity<Socks> createSocks(@RequestBody Socks socks) {
        return ResponseEntity.ok(socksService.createSocks(socks));
    }

    @PostMapping("/outcome/{socksId}") // POST http://localhost:8080/socks/outcome/{socksId}
    public ResponseEntity<Socks> editeSocks(@Parameter(name = "socksId", description = "обязательно правильно заполнить <b>socksId</b> <br/>(если указать неверно носки не будут найдены в БД)")
                                            @PathVariable long socksId,
                                            @Parameter(name = "quantity", description = "количество отпущенных носков")
                                            @RequestParam int quantity){
        return ResponseEntity.ok(socksService.editeSocks(socksId, quantity));
    }

    @GetMapping()
    public ResponseEntity<Integer> findAllByColorAndCottonPart(@Parameter(name = "color", description = "цвет")
                                                               @RequestParam String color,
                                                               @Parameter(name = "operation", description = "MORE_THAN=больше чем / LESS_THAN=меньше чем / EQUAL=равное")
                                                               @RequestParam Operation operation,
                                                               @Parameter(name = "cottonPart", description = "содержание хлопка")
                                                               @RequestParam int cottonPart){
        return ResponseEntity.ok(socksService.findAllByColorAndCottonPart(color,operation,cottonPart));
    }
}
