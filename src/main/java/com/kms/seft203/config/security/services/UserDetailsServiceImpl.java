package com.kms.seft203.config.security.services;

import java.util.ArrayList;
import java.util.Optional;

import com.kms.seft203.domain.auth.UserEntity;
import com.kms.seft203.domain.auth.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> userOption = userRepository.findByUsername(username);
		UserEntity user = new UserEntity();

		if(userOption.isPresent()){
			user = userOption.get();
			System.out.println("loadUserByUsername: " + user.getUsername());
			return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
		}
		else throw new UsernameNotFoundException(username);
	}

}
