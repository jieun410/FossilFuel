package edu.example.springbootblog.map;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
public class MapApiController {

    @PostMapping
    public ResponseEntity<String> receiveLocation(@RequestBody LocationRequest location) {
        System.out.println("받은 위치 데이터: 위도=" + location.getLatitude() + ", 경도=" + location.getLongitude());
        // 데이터를 처리하거나 저장하는 로직을 여기에 추가
        return ResponseEntity.ok("위치 데이터가 성공적으로 전송되었습니다.");
    }
}

