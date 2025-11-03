package fi.haagahelia.expensetracker.web;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fi.haagahelia.expensetracker.model.AppUser;
import fi.haagahelia.expensetracker.model.AppUserRepository;

/**
 * This class is used by spring security to authenticate and authorize user
 **/
@Service
public class AppUserDetailService implements UserDetailsService {

    // @Autowired
    AppUserRepository repository;

    // Constructor Injection
    public AppUserDetailService(AppUserRepository appUserRepository) {
        this.repository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser curruser = repository.findByUsername(username);
        UserDetails user = new org.springframework.security.core.userdetails.User(username, curruser.getPasswordHash(),
                AuthorityUtils.createAuthorityList("ROLE_" + curruser.getRole()));
        return user;
    }
}