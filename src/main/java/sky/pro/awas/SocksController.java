package sky.pro.awas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.awas.exeption.FormatNotComplianceException;
import sky.pro.awas.exeption.ObjectAbsenceException;

import java.io.IOException;

@RestController
@RequestMapping("/socks")
public class SocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @Operation(
            tags = "Склад носков",
            summary = "Регистрация прихода носков на склад",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос выполнен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Socks.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "параметры запроса отсутствуют или имеют некорректный формат"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "произошла ошибка, не зависящая от вызывающей стороны")})
    @PostMapping("/income")
    public ResponseEntity<Socks> createSocks(@RequestBody Socks socks) {
        return ResponseEntity.ok(socksService.createSocks(socks));
    }

    @Operation(
            tags = "Склад носков",
            summary = "Регистрация отпуска носков со склада",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос выполнен"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "параметры запроса отсутствуют или имеют некорректный формат"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "произошла ошибка, не зависящая от вызывающей стороны")})
    @PostMapping("/outcome/{socksId}")
    public ResponseEntity<?> editeSocks(@Parameter(name = "socksId", description = "обязательно правильно заполнить <b>socksId</b> <br/>(если указать неверно носки не будут найдены в БД)")
                                        @PathVariable long socksId,
                                        @Parameter(name = "quantity", description = "количество отпущенных носков")
                                        @RequestParam int quantity) {
        try {
            return ResponseEntity.ok(socksService.editeSocks(socksId, quantity));
        } catch (ObjectAbsenceException | FormatNotComplianceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(
            tags = "Склад носков",
            summary = "Общее количество носков на складе согласно заданным критериям (цвета и состава)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос выполнен"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "параметры запроса отсутствуют или имеют некорректный формат"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "произошла ошибка, не зависящая от вызывающей стороны")})
    @GetMapping
    public ResponseEntity<Integer> findAllByColorAndCottonPart(@Parameter(name = "color", description = "цвет")
                                                               @RequestParam String color,
                                                               @Parameter(name = "operation", description = "MORE_THAN=больше чем / LESS_THAN=меньше чем / EQUAL=равное")
                                                               @RequestParam Function operation,
                                                               @Parameter(name = "cottonPart", description = "содержание хлопка")
                                                               @RequestParam int cottonPart) {
        return ResponseEntity.ok(socksService.findAllByColorAndCottonPart(color, operation, cottonPart));
    }
}
