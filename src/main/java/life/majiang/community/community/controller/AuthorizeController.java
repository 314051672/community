package life.majiang.community.community.controller;

import life.majiang.community.community.dto.AccesstokenDTO;
import life.majiang.community.community.dto.GithubUser;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.User;
import life.majiang.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;

    @Value(value = "${github.client.id}")
    private String clientId;
    @Value(value = "${github.client.secret}")
    private String clientSecret;
    @Value(value = "${github.redirect.uri}")
    private String redirectUri;


    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse res) {
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setClient_id(clientId);
        accesstokenDTO.setClient_secret(clientSecret);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri(redirectUri);
        accesstokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accesstokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            res.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            //登陆失败
            return "redirect:/";
        }
    }
}
