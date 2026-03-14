package com.runloyal.controller;

import com.runloyal.dto.AffiliateLinkRequest;
import com.runloyal.dto.AffiliateLinkResponse;
import com.runloyal.service.AffiliateLinkService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AffiliateLinkController {

    private final AffiliateLinkService affiliateLinkService;

    @PostMapping("/affiliate-links")
    public ResponseEntity<AffiliateLinkResponse> generate(@Valid @RequestBody AffiliateLinkRequest request) {
        return ResponseEntity.ok(affiliateLinkService.generateLink(request));
    }

    @GetMapping("/r/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String trackingUrl = affiliateLinkService.resolveUrl(shortCode);
        response.sendRedirect(trackingUrl);
    }
}
