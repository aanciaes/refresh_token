package example.refreshtokens.spring;

import example.refreshtokens.auth.JwtService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SpringBootApplication
public class SpringBoot {

    @RequestMapping("/login")
    @ResponseBody
    @CrossOrigin("*")
    String home(HttpServletRequest request, HttpServletResponse response) {
        Cookie c = new Cookie("refresh_token", JwtService.issueRefreshToken("miguel"));
        c.setPath("/");
        c.setDomain("localhost");
        response.addCookie(c);
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBoot.class, args);
    }
}