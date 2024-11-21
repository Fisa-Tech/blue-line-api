package org.blueline.api.controller;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OAuth2SuccessController {

    @GetMapping(value = "/oauth2/success", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String oauth2Success() {
        return "<html><head><title>Synchronisation réussie</title></head><body><h1>OK, la synchro s'est bien passée</h1></body></html>";
    }
}
