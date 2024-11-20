package org.blueline.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/strava/webhook")
@Tag(name = "Strava Webhook", description = "Manage the Strava webhook validation and payload")
public class StravaWebhookController {

    @GetMapping
    @Operation(
            summary = "Validate the Strava webhook",
            description = "Validate the Strava webhook by returning the challenge parameter"
    )
    public ResponseEntity<?> validateWebhook(@RequestParam Map<String, String> params) {
        // Manage the webhook validation
        String challenge = params.get("hub.challenge");
        Map<String, String> response = new HashMap<>();
        response.put("hub.challenge", challenge);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "Handle the Strava webhook",
            description = "Handle the Strava webhook payload"
    )
    public ResponseEntity<?> handleWebhook(@RequestBody Map<String, Object> payload) {
        // Traiter le payload du webhook
        return ResponseEntity.ok().build();
    }
}
