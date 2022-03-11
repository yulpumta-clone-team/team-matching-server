package com.projectmatching.app.controller.user;

import com.projectmatching.app.config.resTemplate.ResponeException;
import com.projectmatching.app.config.resTemplate.ResponseTemplate;
import com.projectmatching.app.domain.common.Paging;
import com.projectmatching.app.domain.user.dto.UserDto;
import com.projectmatching.app.domain.user.dto.UserJoinDto;
import com.projectmatching.app.domain.user.dto.UserLoginDto;
import com.projectmatching.app.domain.user.dto.UserProfileDto;
import com.projectmatching.app.service.user.UserService;
import com.projectmatching.app.service.user.UserSignInService;
import com.projectmatching.app.service.user.UserSignUpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.projectmatching.app.constant.ResponseTemplateStatus.SUCCESS;
import static com.projectmatching.app.constant.ServiceConstant.PAGING_SIZE;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "유저 컨트롤러")
public class UserController {

    private final UserService userService;
    private final UserSignUpService userSignUpService;
    private final UserSignInService userSignInService;

    /**
     * 일반 회원가입
     * 가입 성공시 유저 id반환
     */
    @ApiOperation(value = "일반 회원가입, 성공시 유저 id 반환됨 ")
    @PostMapping("/join")
    public ResponseTemplate<Long> join(@RequestBody UserJoinDto userJoinDto) throws ResponeException {

        try {
            return ResponseTemplate.valueOf(userSignUpService.join(userJoinDto));
        }catch (ResponeException e){
            return ResponseTemplate.of(e.getStatus());
        }
    }

    /**
     * 로그인
     */
    @ApiOperation(value = "일반 로그인, 성공시 유저 id 반환 및 헤더에 토큰 생성")
    @PostMapping("/login")
    public ResponseTemplate<?> login(@RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
            return ResponseTemplate.valueOf(userSignInService.userLogin(userLoginDto,response));
    }



    /**
     * 회원탈퇴
     */
    @ApiOperation(value = "회원 탈퇴, 해당 유저의 Status 칼럼을 NA(Not Avaliable)로 바꿈")
    @DeleteMapping("/withdrawal")
    public ResponseTemplate<String> withDrawal(){
        userSignInService.userDelete(userService.getAuthUserEmail());
        return ResponseTemplate.of(SUCCESS);

    }


    /**
     * 유저 카드(리스트) 조회
     */
    @ApiOperation(value = "유저 리스트(카드) 조회")
    @ApiImplicitParam(name="lastPage", required = true, value = "마지막 페이지 기준으로 10개씩 유저 리스트를 보내줌")
    @GetMapping
    public ResponseTemplate<List<UserProfileDto>> getUserList(@RequestParam(name="lastPage") int lastPage){
        Paging paging = new Paging(lastPage,PAGING_SIZE, Sort.by("created_at").descending());
        return ResponseTemplate.valueOf(userService.getUserList(paging));
    }


    /**
     * 특정 유저 카드 조회
     */
    @ApiOperation(value = "특정 유저 상세 조회")
    @ApiImplicitParam(name = "id",required = true,value = "유저 id")
    @GetMapping("/{id}")
    public ResponseTemplate<UserDto> getUserDetail(@PathVariable(name="id") Long id){

        return ResponseTemplate.valueOf(userService.getUserDetail(id));

    }



    /**
     * 유저 프로필 수정
     */
    @PatchMapping("/profile")
    public ResponseTemplate<Void> createUserProfile(@RequestBody UserDto userDto){
        userService.updateUser(userDto);
        return ResponseTemplate.of(SUCCESS);

    }



}
