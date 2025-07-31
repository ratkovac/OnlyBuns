    package com.group27.OnlyBuns.controller;

    import com.group27.OnlyBuns.publisher.AdPostPublisher;
    import dto.AdPostDTO;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDateTime;

    @RestController
    @RequestMapping("/ads")
    @CrossOrigin(origins = "http://localhost:4200")
    public class AdPostController {

        private final AdPostPublisher adPostPublisher;

        public AdPostController(AdPostPublisher adPostPublisher) {
            this.adPostPublisher = adPostPublisher;
        }

        @PostMapping("/send")
        public ResponseEntity<String> sendAd(@RequestBody AdPostDTO adPostDTO) {
            adPostPublisher.sendAdPost(adPostDTO);
            return ResponseEntity.ok("Ad sent to all agencies via RabbitMQ.");
        }
    }
