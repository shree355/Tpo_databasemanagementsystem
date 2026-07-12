package net.javaguides.sms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import net.javaguides.sms.entity.CompanyEntity;
import net.javaguides.sms.repository.CompanyRepository;

@Service
public class DashboardService {

    private final CompanyRepository companyRepository;

    public DashboardService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<CompanyEntity> getAllCompanies() {
        return companyRepository.findAll();
    }

    public CompanyEntity saveCompany(CompanyEntity company) {
        Objects.requireNonNull(company, "company must not be null");
        if (company.getCreatedAt() == null) {
            company.setCreatedAt(LocalDateTime.now());
        }
        return companyRepository.save(company);
    }

    public CompanyEntity getCompanyById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));
    }

    public CompanyEntity updateCompany(CompanyEntity company) {
        Objects.requireNonNull(company, "company must not be null");
        return companyRepository.save(company);
    }

    public void deleteCompanyById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        companyRepository.deleteById(id);
    }
}
