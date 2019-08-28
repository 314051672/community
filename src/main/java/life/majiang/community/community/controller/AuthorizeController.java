package life.majiang.community.community.controller;

import life.majiang.community.community.dto.AccesstokenDTO;
import life.majiang.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setClient_id("Iv1.9d4fd14df8f3433d");
        accesstokenDTO.setClient_secret("93e89d8e32671796e425555e11164c80e4d239ec");
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri("http://localhost:8080/callback");
        accesstokenDTO.setState(state);
        githubProvider.getAccessToken(accesstokenDTO);
        return "index";
    }
}
