package net.javaguides.sms.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

import org.springframework.stereotype.Service;

import net.javaguides.sms.entity.RegisterEntity;
import net.javaguides.sms.repository.RegisterRepository;
import net.javaguides.sms.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final RegisterRepository registerRepository;

    public RegisterServiceImpl(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    @Override
    public List<RegisterEntity> getAllStudents() {
        return registerRepository.findAll();
    }

    @Override
    public RegisterEntity saveStudent(RegisterEntity student) {
        Objects.requireNonNull(student, "student must not be null");
        return registerRepository.save(student);
    }

    @Override
    public RegisterEntity getStudentById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return registerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
    }

    @Override
    public Optional<RegisterEntity> getStudentByUserName(String userName) {
        Objects.requireNonNull(userName, "userName must not be null");
        return registerRepository.findByUserName(userName);
    }

    @Override
    public Optional<RegisterEntity> getStudentByEmail(String email) {
        Objects.requireNonNull(email, "email must not be null");
        return registerRepository.findByEmail(email);
    }

    @Override
    public RegisterEntity updateStudent(RegisterEntity student) {
        Objects.requireNonNull(student, "student must not be null");
        return registerRepository.save(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        registerRepository.deleteById(id);
    }
}
