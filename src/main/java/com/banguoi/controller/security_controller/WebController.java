package com.banguoi.controller.security_controller;

import com.banguoi.model.Product;
import com.banguoi.model.User;
import com.banguoi.service.product.ProductService;
import com.banguoi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Transactional
public class WebController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;


    private String getPrincipal() {
        String email = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return email;
    }

    @RequestMapping(value = {"/", "home"})
    public ModelAndView home(Pageable pageable) {
        Page<Product> products = productService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("/home");

        for (Product p : products) {
            System.out.println(p.getImages().size());
        }

        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @RequestMapping(value = "/user")
    public ModelAndView user() {
        ModelAndView modelAndView = new ModelAndView("/userPage");

        String email = getPrincipal();
        User user = userService.findUserByEmail(email);

        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/admin")
    public String admin() {
        ModelAndView modelAndView = new ModelAndView("/adminPage");

        String email = getPrincipal();
        User admin = userService.findUserByEmail(email);

        modelAndView.addObject("admin", admin);
        return "/adminPage";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "accessDenied";
    }
}