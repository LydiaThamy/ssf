package vttp2023.batch3.ssf.frontcontroller.respositories;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthenticationRepository {

	// TODO Task 5
	// Use this class to implement CRUD operations on Redis

	@Autowired
	@Qualifier("authentication")
	private RedisTemplate<String, Object> template;

	public void disableUser(String username) {
		System.out.println("disabled " + username);
	}

	public boolean isLocked(String username) {
		return false;
	}

}
