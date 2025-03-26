package org.jbd.JBD_MINOR1.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbd.JBD_MINOR1.Exception.UserException;
import org.jbd.JBD_MINOR1.Model.Operator;
import org.jbd.JBD_MINOR1.Model.User;
import org.jbd.JBD_MINOR1.Model.UserFiltertype;
import org.jbd.JBD_MINOR1.Model.UserType;
import org.jbd.JBD_MINOR1.Repository.UserCacheRepository;
import org.jbd.JBD_MINOR1.Repository.UserRepository;
import org.jbd.JBD_MINOR1.dto.UserRequest;
import org.jbd.JBD_MINOR1.minor1Configurations.CommonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private static final Log logger = LogFactory.getLog(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCacheRepository cacheRepository;

    @PersistenceContext
    private EntityManager em;

    @Value("${student.authority}")
    private String studentAuthority;

    @Value("${admin.authority}")
    private String adminAuthority;

    @Autowired
    private PasswordEncoder encoder;

    public User addStudent(UserRequest userRequest) {
        User user = userRequest.toUser();
        user.setAuthorities(studentAuthority);
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setUserType(UserType.STUDENT);
        return userRepository.save(user);
    }

    public List<User> filter(String filterBy, String operator, String value) {
        String[] filters = filterBy.split(",");
        String[] operators = operator.split(",");
        String[] values = value.split(",");
        //switch cannot be used as it will take single filterType for its checking
        StringBuilder query = new StringBuilder();
        query.append("select * from user where ");
        for(int i=0;i<operators.length;i++){
            UserFiltertype userFiltertype = UserFiltertype.valueOf(filters[i]);
            Operator operator1 = Operator.valueOf(operators[i]);
            String finalValue = values[i];
            query = query.
                    append(userFiltertype).
                    append(operator1.getValue()) .
                    append("'").
                    append(finalValue).
                    append("' ").append(" and ");//and coz it was giving error
        }
        //to remove the last and
        logger.info("query is : "+ query.substring(0,query.length()-4));
        Query query1 = em.createNativeQuery(query.substring(0,query.length()-4), User.class);
            return query1.getResultList();
            //Do not need the 61 return statement because i got the query
        //        return userRepository.findUsersByNativeQuery(query.substring(0,query.length()-4).toString());
    }

    public User getStudentByPhoneNo (String userPhoneNo) {
        return userRepository.findByPhoneNoAndUserType(userPhoneNo,UserType.STUDENT);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //load this user from redis first,
        //if user is present in redis, i want to get the data from redis

        //if the data is not present in redis, i want to go to db
        //i will be checking at db

        //if data is present in db, i will be keeping the data in my cache as well.

        User user = cacheRepository.getUser(email);
        if(user != null){
            return user;
        }
        user = userRepository.findByEmail(email);//since for in this case email is the unique identifier.
        //return (*now we are collecting it in user from db) will be providing error as the return type is userdetail.
        //so to cope the error we need to implement UserDetails in User and add the methods.
        if(user==null){
            new UserException("The user u are looking for doesn't belong to the library");
        }
        cacheRepository.setUser(email,user);//if not null
        return user;
    }

    public User addAdmin(UserRequest userRequest) {
        User user = userRequest.toUser();
        user.setAuthorities(adminAuthority);
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setUserType(UserType.ADMIN);
        return userRepository.save(user);
    }
}
