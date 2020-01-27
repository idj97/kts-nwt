package com.mbooking.service.impl;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.dto.ResultsDTO;
import com.mbooking.dto.UserDTO;
import com.mbooking.exception.ApiAuthException;
import com.mbooking.exception.ApiException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Admin;
import com.mbooking.model.Customer;
import com.mbooking.model.User;
import com.mbooking.repository.AdminRepository;
import com.mbooking.repository.AuthorityRepository;
import com.mbooking.repository.CustomerRepository;
import com.mbooking.repository.UserRepository;
import com.mbooking.service.EmailSenderService;
import com.mbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthorityRepository authorityRepo;

	@Autowired
	private EmailSenderService emailSenderService;

	@Override
	public UserDTO editProfile(EditProfileDTO profileDTO) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByEmail(email);
		user.setFirstname(profileDTO.getFirstname());
		user.setLastname(profileDTO.getLastname());
		user = userRepo.save(user);
		return new UserDTO(user);
	}

	@Override
	public ResultsDTO<UserDTO> searchAdmins(String firstname, String lastname, String email, int pageNum, int pageSize) {
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Page<Admin> admins = adminRepository.findByFirstnameContainingAndLastnameContainingAndEmailContaining(firstname, lastname, email, pageable);
		List<UserDTO> adminsDTO = admins
				.stream()
				.map(UserDTO::new)
				.collect(Collectors.toList());
		return new ResultsDTO(adminsDTO, admins.getTotalPages());
	}

	@Override
	public ResultsDTO<UserDTO> searchUsers(String firstname, String lastname, String email, int pageNum, int pageSize) {
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Page<Customer> customers = customerRepo.findByFirstnameContainingAndLastnameContainingAndEmailContaining(firstname, lastname, email, pageable);
		List<UserDTO> customersDTO = customers
				.stream()
				.map(UserDTO::new)
				.collect(Collectors.toList());
		return new ResultsDTO(customersDTO, customers.getTotalPages());
	}

	@Override
	public void banUser(Long id) {
		Optional<Customer> optionalCustomer = customerRepo.findById(id);
		if (optionalCustomer.isPresent()) {
			Customer customer = optionalCustomer.get();
			customer.setBanned(true);
			customerRepo.save(customer);
		}
		else {
			throw new ApiNotFoundException("User not exist.");
		}
	}

	@Override
	public void unbanUser(Long id) {
		Optional<Customer> optionalCustomer = customerRepo.findById(id);
		if (optionalCustomer.isPresent()) {
			Customer customer = optionalCustomer.get();
			customer.setBanned(false);
			customerRepo.save(customer);
		}
		else {
			throw new ApiNotFoundException("User not exist.");
		}
	}

	@Override
	public UserDTO register(UserDTO userDTO) {
		if (userRepo.findByEmail(userDTO.getEmail()) == null) {
			Customer customer = new Customer();
			customer.setEmail(userDTO.getEmail());
			customer.setFirstname(userDTO.getFirstname());
			customer.setLastname(userDTO.getLastname());
			customer.setEmailConfirmed(false);
			customer.setBanned(false);
			customer.setPassword(passwordEncoder.encode(userDTO.getPassword()));
			customer.getCollectionOfAuthorities().add(authorityRepo.findByName("ROLE_CUSTOMER"));
			customer.setEmailConfirmationId(Integer.toString(new Random().nextInt(10000000)));
			customer = customerRepo.save(customer);

			String subject = "Email confirmation for MBooking";
			String text = "Confirm registration by clicking on this link:\n";
			String link = "http://localhost:8080/api/users/confirm_registration/" + customer.getEmail() + "/" + customer.getEmailConfirmationId();
			emailSenderService.sendSimpleMessage(customer.getEmail(), subject, text + link);
			return new UserDTO(customer);
		} else {
			throw new ApiException("Email is reserved.", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public void confirmRegistration(String email, String emailConfirmationId) {
		Customer customer = customerRepo.findByEmail(email);
		if (customer != null && !customer.isEmailConfirmed() && customer.getEmailConfirmationId().equals(emailConfirmationId)) {
			customer.setEmailConfirmed(true);
			customer = customerRepo.save(customer);
		} else {
			throw new ApiAuthException("Not registered or invalid confirmation id.");
		}
	}

	@Override
	public UserDTO createAdmin(UserDTO adminDTO) {
		if (userRepo.findByEmail(adminDTO.getEmail()) == null) {
			Admin admin = new Admin();
			admin.setEmail(adminDTO.getEmail());
			admin.setFirstname(adminDTO.getFirstname());
			admin.setLastname(adminDTO.getLastname());
			admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
			admin.getCollectionOfAuthorities().add(authorityRepo.findByName("ROLE_ADMIN"));
			admin= userRepo.save(admin);
			return new UserDTO(admin);
		} else {
			throw new ApiException("Email is reserved.", HttpStatus.BAD_REQUEST);
		}
	}
}
