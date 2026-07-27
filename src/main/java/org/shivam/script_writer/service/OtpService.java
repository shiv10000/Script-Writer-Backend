package org.shivam.script_writer.service;


import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class OtpService {
    private final RedisService redisService;


    public OtpService(RedisService redisService) {
        this.redisService = redisService;
    }

    public String generateAndStore(String email){
        String code = String.valueOf(100000 + new Random().nextInt(900000));
        boolean stored = redisService.set(email,code,300L);
        if(!stored){
            throw new RuntimeException("Unable to create verification code. Please try again.");
        }
        return code;
    }

    public boolean verify(String email, String code) {
        String stored = redisService.get(email,String.class);
        return stored != null && stored.equals(code);
    }


    public void invalidate(String email) {
        redisService.delete(email);
    }
}
