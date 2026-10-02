package com.example.demo.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.loginCredentials.AdminLogin;
import com.example.demo.services.ProductServices;
import com.example.demo.services.UserServices;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController 
{
	@Autowired
	private ProductServices productServices;

	@Autowired
	private UserServices userServices;

	@GetMapping(value = {"/home", "/"})
	public String home()
	{
		return "Home";
	}

	@GetMapping("/products")
	public String products( Model model)
	{ 
		List<Product> allProducts = this.productServices.getAllProducts();
		model.addAttribute("products", allProducts);
		return "Products";
	}

	@GetMapping("/location")
	public String location()
	{
		return "Locate_us";
	}

	@GetMapping("/about")
	public String about()
	{
		return "About";
	}

	@GetMapping("/login")
	public String login(Model model)
	{
		model.addAttribute("adminLogin",new AdminLogin());
		return "Login";
	}

	@GetMapping(value = {"/register", "/register.html"})
	public String registerPage(@ModelAttribute("userRegistration") User user, Model model)
	{
		if (user != null && user.getUemail() != null && !user.getUemail().trim().isEmpty()) {
			return registerUser(user, model);
		}
		if (!model.containsAttribute("userRegistration")) {
			model.addAttribute("userRegistration", new User());
		}
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute("userRegistration") User user, Model model)
	{
		if (user.getUemail() == null || user.getUemail().trim().isEmpty()) {
			model.addAttribute("error", "Email is required!");
			return "register";
		}
		if (this.userServices.getUserByEmail(user.getUemail()) != null) {
			model.addAttribute("error", "Email is already registered! Please log in.");
			return "register";
		}
		this.userServices.addUser(user);
		return "redirect:/login";
	}
}