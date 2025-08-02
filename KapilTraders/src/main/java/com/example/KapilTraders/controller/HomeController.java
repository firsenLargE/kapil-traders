
package com.example.KapilTraders.controller;
import com.example.KapilTraders.model.User;
import com.example.KapilTraders.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {


    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "KTDashboard";
    }

    @GetMapping("/signup")
    public String registerForm() {
        return "signup";
    }

//    @GetMapping("/login")
//    public String loginForm() {
//        return "index";
//    }


    @PostMapping("/login")
    public String postLogin(@ModelAttribute User user , Model model, HttpSession session){
    user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

    User usr = userService.login(user.getEmail().toLowerCase(),user.getPassword());
        if (usr != null) {
            session.setAttribute("validuser", usr);
            session.setMaxInactiveInterval(60);
            model.addAttribute("uname", usr.getUname());
            System.out.println("Incoming email: " + user.getEmail());
            System.out.println("Incoming password (plain): " + user.getPassword());

            return "KTDashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "index";
        }



    }}