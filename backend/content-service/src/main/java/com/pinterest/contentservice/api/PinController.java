package com.pinterest.contentservice.api;

import com.pinterest.contentservice.dto.PinRequest;
import com.pinterest.contentservice.dto.PinResponse;
import com.pinterest.contentservice.service.PinService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pins")
public class PinController {

  private final PinService pinService;

  public PinController(PinService pinService) {
    this.pinService = pinService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PinResponse createPin(@Valid @RequestBody PinRequest request) {
    return pinService.createPin(request);
  }

  @GetMapping
  public List<PinResponse> listPins(@RequestParam(value = "q", required = false) String query,
      @RequestParam(value = "boardId", required = false) Long boardId) {
    return pinService.listPins(query, boardId);
  }

  @GetMapping("/{id}")
  public PinResponse getPin(@PathVariable Long id) {
    return pinService.getPin(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePin(@PathVariable Long id) {
    pinService.deletePin(id);
  }
}
