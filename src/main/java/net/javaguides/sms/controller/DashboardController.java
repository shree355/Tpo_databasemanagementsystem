package net.javaguides.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import net.javaguides.sms.entity.CompanyEntity;
import net.javaguides.sms.entity.RegisterEntity;
import net.javaguides.sms.service.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String listCompanies(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/registration";
        }
        RegisterEntity tpoUser = (RegisterEntity) session.getAttribute("tpoUser");
        var companies = dashboardService.getAllCompanies();
        model.addAttribute("companies", companies);
        model.addAttribute("totalCompanies", companies.size());
        long positive = companies.stream().filter(c -> "Positive".equalsIgnoreCase(c.getStatus())).count();
        long hold = companies.stream().filter(c -> "On Hold".equalsIgnoreCase(c.getStatus()) || "Hold".equalsIgnoreCase(c.getStatus())).count();
        long rejected = companies.stream().filter(c -> "Rejected".equalsIgnoreCase(c.getStatus())).count();
        model.addAttribute("positiveCount", positive);
        model.addAttribute("holdCount", hold);
        model.addAttribute("rejectedCount", rejected);
        model.addAttribute("campusCompanies", 0);
        model.addAttribute("offCampusCompanies", 0);
        model.addAttribute("industrialVisits", 0);
        model.addAttribute("tpoUser", tpoUser);
        return "dashboard";
    }

    @GetMapping("/dashboard/new")
    public String createCompanyForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/registration";
        }
        model.addAttribute("company", new CompanyEntity());
        return "create_company";
    }

    @PostMapping("/dashboard")
    public String saveCompany(@ModelAttribute("company") CompanyEntity company, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/registration";
        }
        dashboardService.saveCompany(company);
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard/edit/{id}")
    public String editCompanyForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/registration";
        }
        model.addAttribute("company", dashboardService.getCompanyById(id));
        return "edit_company";
    }

    @PostMapping("/dashboard/{id}")
    public String updateCompany(@PathVariable Long id,
            @ModelAttribute("company") CompanyEntity company, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/registration";
        }
        CompanyEntity existingCompany = dashboardService.getCompanyById(id);
        existingCompany.setCompanyName(company.getCompanyName());
        existingCompany.setHrName(company.getHrName());
        existingCompany.setEmail(company.getEmail());
        existingCompany.setPhone(company.getPhone());
        existingCompany.setWebsite(company.getWebsite());
        existingCompany.setAddress(company.getAddress());
        existingCompany.setCity(company.getCity());
        existingCompany.setState(company.getState());
        existingCompany.setStatus(company.getStatus());
        existingCompany.setCreatedAt(company.getCreatedAt() != null ? company.getCreatedAt() : existingCompany.getCreatedAt());
        dashboardService.updateCompany(existingCompany);
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard/delete/{id}")
    public String deleteCompany(@PathVariable Long id, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/registration";
        }
        dashboardService.deleteCompanyById(id);
        return "redirect:/dashboard";
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("tpoUser") != null;
    }
}
