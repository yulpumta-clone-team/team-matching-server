package com.projectmatching.app.domain.user.controller;
import com.projectmatching.app.config.resTemplate.ResponeException;
import com.projectmatching.app.config.resTemplate.ResponseTemplate;
import com.projectmatching.app.domain.user.User;
import com.projectmatching.app.domain.user.UserRepository;
import com.projectmatching.app.domain.user.dto.UserDto;
import com.projectmatching.app.domain.user.dto.UserLoginDto;
import com.projectmatching.app.domain.user.service.UserService;
import com.projectmatching.app.util.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.projectmatching.app.config.resTemplate.ResponseTemplateStatus.LOGIN_USER_ERROR;
import static com.projectmatching.app.config.resTemplate.ResponseTemplateStatus.WITHDRAWAL_USER_ERROR;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final AuthTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;


    //일반 회원가입
    @PostMapping("/join")
    public ResponseTemplate<String> join(@RequestBody @Valid UserDto userDto, BindingResult result) throws ResponeException {
        if(result.hasErrors()){
            //에러 List로 저장
            List<ObjectError> list = result.getAllErrors();
            for(ObjectError error : list){
                log.info("{}",error);
            }
            return new ResponseTemplate<>(LOGIN_USER_ERROR);
        }

        userService.join(userDto);
        return new ResponseTemplate<>("회원가입 성공!");

    }


    /**
     * 로그인
     */
    @PostMapping("/login")
    public void login(@RequestBody @Valid UserLoginDto user, HttpServletResponse response,BindingResult result) {

        if(!result.hasErrors()){
            User member = userRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
            if (!passwordEncoder.matches(user.getPwd(), member.getPwd())) {
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }
            String token = jwtTokenProvider.createToken(member.getName(), member.getRole());
            jwtTokenProvider.createCookie(response,token); //쿠키 발급

        }
        else{
            //에러 List로 저장
            List<ObjectError> list = result.getAllErrors();
            for(ObjectError error : list){
                log.info("{}",error);
            }
        }

    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/user/withdrawal")
    public ResponseTemplate<String> withDrawal(){
        try {
            userService.DeleteUser();
            return new ResponseTemplate<>("회원탈퇴 성공!");
        }catch (ResponeException e){
            return new ResponseTemplate<>(WITHDRAWAL_USER_ERROR);
        }
    }



//    /**
//     * 유저 프로필 최초 생성
//     * @param userProfileDto
//     * @return
//     */
//    @PostMapping("/user/myprofile")
//    public ResponseTemplate<> createUserProfile(@RequestBody UserProfileDto userProfileDto){
//
//
//    }



}
