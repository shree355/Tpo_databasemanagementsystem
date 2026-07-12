package net.javaguides.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import net.javaguides.sms.entity.RegisterEntity;
import net.javaguides.sms.service.RegisterService;

@Controller
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/registration";
    }

    @GetMapping("/registration")
    public String registrationForm(Model model, HttpSession session,
                                   @RequestParam(value = "error", required = false) String error) {
        if (session.getAttribute("tpoUser") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("user", new RegisterEntity());
        if (error != null) {
            model.addAttribute("registerError", error);
        }
        return "registration";
    }

    @PostMapping("/registration")
    public String saveRegistration(@ModelAttribute("user") RegisterEntity registerEntity, Model model) {
        var existingUser = registerService.getStudentByUserName(registerEntity.getUserName());
        if (existingUser.isPresent()) {
            model.addAttribute("registerError", "Username already exists. Choose another.");
            model.addAttribute("user", registerEntity);
            return "registration";
        }
        var existingEmail = registerService.getStudentByEmail(registerEntity.getEmail());
        if (existingEmail.isPresent()) {
            model.addAttribute("registerError", "Email already registered. Use a different email.");
            model.addAttribute("user", registerEntity);
            return "registration";
        }
        registerService.saveStudent(registerEntity);
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String loginForm(Model model, HttpSession session,
                            @RequestParam(value = "registered", required = false) String registered) {
        if (session.getAttribute("tpoUser") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("loginUser", new RegisterEntity());
        if (registered != null) {
            model.addAttribute("registerSuccess", "Account created successfully. Please login.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginUser") RegisterEntity loginUser, Model model, HttpSession session) {
        var userOpt = registerService.getStudentByUserName(loginUser.getUserName());
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginUser.getPassword())) {
            session.setAttribute("tpoUser", userOpt.get());
            return "redirect:/dashboard";
        }
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("loginError", "Invalid username or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/registration";
    }

    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", registerService.getAllStudents());
        return "students";
    }

    @GetMapping("/students/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new RegisterEntity());
        return "create_student";
    }

    @PostMapping("/students")
    public String saveStudent(@ModelAttribute("student") RegisterEntity student) {
        registerService.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", registerService.getStudentById(id));
        return "edit_student";
    }

    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id,
            @ModelAttribute("student") RegisterEntity student) {
        RegisterEntity existingStudent = registerService.getStudentById(id);
        existingStudent.setId(id);
        existingStudent.setFullName(student.getFullName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setPhone(student.getPhone());
        existingStudent.setUserName(student.getUserName());
        registerService.updateStudent(existingStudent);
        return "redirect:/students";
    }

    @GetMapping("/students/{id}")
    public String deleteStudent(@PathVariable Long id) {
        registerService.deleteStudentById(id);
        return "redirect:/students";
    }
}