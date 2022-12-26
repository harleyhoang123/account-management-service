package vn.edu.fpt.account.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.account.config.kafka.producer.CreateAccountActivityProducer;
import vn.edu.fpt.account.config.kafka.producer.SendEmailProducer;
import vn.edu.fpt.account.config.kafka.producer.SendSMSProducer;
import vn.edu.fpt.account.constant.ResponseStatusEnum;
import vn.edu.fpt.account.dto.cache.UserInfo;
import vn.edu.fpt.account.dto.common.PageableResponse;
import vn.edu.fpt.account.dto.event.CreateAccountActivityEvent;
import vn.edu.fpt.account.dto.event.CreateProfileEvent;
import vn.edu.fpt.account.dto.event.SendEmailEvent;
import vn.edu.fpt.account.dto.event.SendSmsEvent;
import vn.edu.fpt.account.dto.request.account.*;
import vn.edu.fpt.account.dto.response.account.*;
import vn.edu.fpt.account.entity.*;
import vn.edu.fpt.account.exception.BusinessException;
import vn.edu.fpt.account.repository.*;
import vn.edu.fpt.account.service.*;
import vn.edu.fpt.account.utils.AuthenticationUtils;
import vn.edu.fpt.account.utils.OTPHistoryComparator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static vn.edu.fpt.account.utils.AuthenticationUtils.addPermissionPrefix;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 20/11/2022 - 14:24
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final static Integer RECOMMEND_PASSWORD_LENGTH = 8;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final _TokenService tokenService;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserInfoService userInfoService;
    private final ObjectMapper objectMapper;
    private final SendEmailProducer sendEmailProducer;
    private final CreateAccountActivityProducer createAccountActivityProducer;
    private final MongoTemplate mongoTemplate;
    private final RoleService roleService;
    private final ProfileService profileService;
    private final OttHistoryRepository ottHistoryRepository;
    private final OTPHistoryRepository otpHistoryRepository;
    private final SendSMSProducer sendSMSProducer;
    private final ProfileRepository profileRepository;

    @Override
    public void init() {
        if (accountRepository.findAccountByEmailOrUsername("admin").isEmpty()) {
            _Role adminRole = roleRepository.findByRoleName("ADMIN")
                    .orElseThrow(() -> new BusinessException("Role ADMIN not exist"));
            Account account = Account.builder()
                    .email("admin.flab@gmail.com")
                    .username("admin")
                    .fullName("admin")
                    .password(passwordEncoder.encode(randomPassword()))
                    .roles(List.of(adminRole))
                    .build();
            try {
                account = accountRepository.save(account);
                pushAccountInfo(account);
                createForumAccountActivity(account);

                profileService.createProfile(CreateProfileEvent.builder()
                        .accountId(account.getAccountId())
                        .build());
                log.info("Init account success");
            } catch (Exception ex) {
                throw new BusinessException("Can't init account in database: " + ex.getMessage());
            }
        }
    }

    @Override
    public UserDetails getUserByUsername(String username) {
        Account account = accountRepository.findAccountByEmailOrUsername(username)
                .orElseThrow(() -> new BusinessException("Username or email not found!"));
        List<_Role> roles = account.getRoles();
        return User.builder()
                .username(account.getAccountId())
                .password(account.getPassword())
                .authorities(this.getAuthorities(roles).toArray(String[]::new))
                .build();
    }

    @Override
    public void changeEmail(String id, ChangeEmailRequest request) {
        Account account = accountRepository.findAccountByAccountId(id)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account ID not exist!"));

        if (accountRepository.findAccountByEmail(request.getNewEmail()).isPresent()) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Email already exist!");
        }
        account.setEmail(request.getNewEmail());

        pushAccountInfo(account);

        try {
            accountRepository.save(account);
            log.info("Change email success");
        } catch (Exception ex) {
            log.error("Change email failed: {}", ex.getMessage());
            throw new BusinessException(ResponseStatusEnum.INTERNAL_SERVER_ERROR, "Can't change email to database: " + ex.getMessage());
        }
    }

    @Override
    public void changePassword(String id, ChangePasswordRequest request) {
        Account account = accountRepository.findAccountByAccountId(id)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account ID not exist!"));
        if (Boolean.FALSE.equals(passwordEncoder.matches(request.getOldPassword(), account.getPassword()))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Password incorrect!");
        }
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        try {
            accountRepository.save(account);
            log.info("Change password success");
        } catch (Exception ex) {
            throw new BusinessException("Can't not save account to database when change password: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public CreateAccountResponse createAccount(CreateAccountRequest request) {

        _Role defaultRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new BusinessException("Role USER not found in database"));

        if(accountRepository.findAccountByUsername(request.getUsername()).isPresent()){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Username already exist");
        }
        if(accountRepository.findAccountByEmail(request.getEmail()).isPresent()){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Email already exist");
        }

        Account account = Account.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(defaultRole))
                .build();

        try {
            account = accountRepository.save(account);
            log.info("Create account success with email: {} and account id is: {}", account.getEmail(), account.getAccountId());
        } catch (Exception ex) {
            throw new BusinessException("Can't create account in database: " + ex.getMessage());
        }

        pushAccountInfo(account);
        createForumAccountActivity(account);

        profileService.createProfile(CreateProfileEvent.builder()
                .accountId(account.getAccountId())
                .build());

        return CreateAccountResponse.builder()
                .accountId(account.getAccountId())
                .build();
    }

    private void createForumAccountActivity(Account account) {
        CreateAccountActivityEvent createAccountActivityEvent = CreateAccountActivityEvent.builder()
                .accountId(account.getAccountId())
                .build();
        createAccountActivityProducer.sendMessage(createAccountActivityEvent);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Account account = accountRepository.findAccountByEmailOrUsername(request.getEmailOrUsername())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.UNAUTHORIZED, "Username or email not exist!"));
        if (Boolean.FALSE.equals(passwordEncoder.matches(request.getPassword(), account.getPassword()))) {
            throw new BusinessException(ResponseStatusEnum.UNAUTHORIZED, "Password incorrect!");
        }
        if (Boolean.FALSE.equals(account.getIsCredentialNonExpired())) {
            throw new BusinessException(ResponseStatusEnum.FORBIDDEN, "Account credential expired!");
        }
        if (Boolean.FALSE.equals(account.getIsNonExpired())) {
            throw new BusinessException(ResponseStatusEnum.FORBIDDEN, "Account expired!");
        }
        if (Boolean.FALSE.equals(account.getIsNonLocked())) {
            throw new BusinessException(ResponseStatusEnum.FORBIDDEN, "Account locked");
        }
        if (Boolean.FALSE.equals(account.getIsEnabled())) {
            throw new BusinessException(ResponseStatusEnum.FORBIDDEN, "Account disable!");
        }

        String token = tokenService.generateToken(account, getUserByUsername(account.getUsername()));
        String refreshToken = tokenService.generateRefreshToken(request);

        UserInfo userInfo = userInfoService.getUserInfo(account.getAccountId());
        List<String> roles = account.getRoles().stream().map(_Role::getRoleName).collect(Collectors.toList());
        return LoginResponse.builder()
                .accountId(account.getAccountId())
                .username(account.getUsername())
                .fullName(account.getFullName())
                .email(account.getEmail())
                .role(roles)
                .avatar(userInfo == null ? null : userInfo.getAvatar())
                .token(token)
                .tokenExpireTime(tokenService.getExpiredTimeFromToken(token))
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(tokenService.getExpiredTimeFromToken(refreshToken))
                .build();
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        LoginRequest loginRequest = tokenService.getLoginRequestFromToken(request.getRefreshToken());
        return login(loginRequest);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        Account account = accountRepository.findAccountByEmailOrUsername(request.getEmailOrUsername())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account not found"));
        String newPassword = randomPassword();
        account.setPassword(passwordEncoder.encode(newPassword));

        SendEmailEvent sendEmailEvent = SendEmailEvent.builder()
                .sendTo(account.getEmail())
                .bcc(null)
                .cc(null)
                .templateId("63977219509a6302a02a3568")
                .params(Map.of("NEW_PASSWORD", newPassword))
                .build();

        sendEmailProducer.sendMessage(sendEmailEvent);

        try {
            accountRepository.save(account);
            log.info("Reset password success");
        } catch (Exception ex) {
            throw new BusinessException("Can't reset password account to database: " + ex.getMessage());
        }
    }

    @Override
    public void addRoleToAccount(String id, AddRoleToAccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account id not found"));

        _Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Role id not found"));

        List<_Role> roles = account.getRoles();
        Optional<_Role> roleInAccount = roles.stream().filter(v -> v.getRoleId().equals(request.getRoleId())).findAny();
        if (roleInAccount.isPresent()) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Role already in account");
        }
        roles.add(role);
        account.setRoles(roles);

        try {
            accountRepository.save(account);
            log.info("Add role to account success");
            pushAccountInfo(account);
        } catch (Exception ex) {
            throw new BusinessException("Can't save account update role to database");
        }
    }

    @Override
    public void deleteAccountById(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account not found"));
        try {
            accountRepository.delete(account);
            log.info("Delete account success");
        } catch (Exception ex) {
            throw new BusinessException("Delete account failed: " + ex.getMessage());
        }

        removeAccountInfo(account);
    }

    @Override
    public void removeRoleFromAccount(String id, String roleId) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account not found"));
        Optional<_Role> optionalRole = account.getRoles().stream().filter(v -> v.getRoleId().equals(roleId)).findAny();
        if (optionalRole.isEmpty()) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Role id not exist in account");
        }
        _Role role = optionalRole.get();
        List<_Role> roles = account.getRoles();
        roles.remove(role);

        account.setRoles(roles);

        pushAccountInfo(account);
        try {
            accountRepository.save(account);
            log.info("Remove role from account success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save account after remove role to database: " + ex.getMessage());
        }
    }

    @Override
    public PageableResponse<GetAccountResponse> getAccountByCondition(GetAccountRequest request) {
        Query query = new Query();
        if (Objects.nonNull(request.getAccountId())) {
            query.addCriteria(Criteria.where("_id").is(request.getAccountId()));
        }

        if (Objects.nonNull(request.getEmail())) {
            query.addCriteria(Criteria.where("email").regex(request.getEmail()));
        }

        if (Objects.nonNull(request.getUsername())) {
            query.addCriteria(Criteria.where("username").regex(request.getUsername()));
        }

        if (Objects.nonNull(request.getFullName())) {
            query.addCriteria(Criteria.where("full_name").regex(request.getFullName()));
        }

        if (Objects.nonNull(request.getNonExpired())) {
            query.addCriteria(Criteria.where("is_non_expired").is(request.getNonExpired()));
        }

        if (Objects.nonNull(request.getNonLocked())) {
            query.addCriteria(Criteria.where("is_non_locked").is(request.getNonLocked()));
        }

        if (Objects.nonNull(request.getCredentialNonExpired())) {
            query.addCriteria(Criteria.where("is_credential_non_expired").is(request.getCredentialNonExpired()));
        }

        if (Objects.nonNull(request.getEnable())) {
            query.addCriteria(Criteria.where("is_enable").is(request.getEnable()));
        }
        if(Objects.nonNull(request.getRoleId())){
            query.addCriteria(Criteria.where("roles.$id").is(new ObjectId(request.getRoleId())));
        }
        if(Objects.nonNull(request.getRole()) && !request.getRole().isEmpty()){
            List<_Role> roles = roleRepository.findAllByRoleNameIn(request.getRole());
            if(!roles.isEmpty()){
                List<ObjectId> objectIds = roles.stream().map(_Role::getRoleId).map(ObjectId::new).collect(Collectors.toList());
                query.addCriteria(Criteria.where("roles.$id").in(objectIds));
            }else{
                return new PageableResponse<>(request, 0L, new ArrayList<>());
            }
        }

        query.addCriteria(Criteria.where("created_date").gte(request.getCreatedDateFrom()).lte(request.getCreatedDateTo()));
        query.addCriteria(Criteria.where("last_modified_date").gte(request.getLastModifiedDateFrom()).lte(request.getLastModifiedDateTo()));

        Long totalElements = mongoTemplate.count(query, Account.class);

        BaseMongoRepository.addCriteriaWithPageable(query, request);
        BaseMongoRepository.addCriteriaWithSorted(query, request);

        List<Account> accounts;
        try {
            accounts = mongoTemplate.find(query, Account.class);
        } catch (Exception ex) {
            throw new BusinessException(ResponseStatusEnum.INTERNAL_SERVER_ERROR, "Can't find account by condition: " + ex.getMessage());
        }
        List<GetAccountResponse> getAccountResponses = accounts.stream().map(this::convertToGetAccountResponse).collect(Collectors.toList());
        return new PageableResponse<>(request, totalElements, getAccountResponses);
    }

    @Override
    public PageableResponse<GetAccountNotInLabResponse> getAccountNotInLab(GetAccountNotInLabRequest request) {
        Query query = new Query();
        if(Objects.nonNull(request.getUsername())){
            query.addCriteria(Criteria.where("username").regex(request.getUsername()));
        }
        query.addCriteria(Criteria.where("_id").nin(request.getAccountIds().stream().map(ObjectId::new).collect(Collectors.toList())));
        Long totalElements = mongoTemplate.count(query, Account.class);
        BaseMongoRepository.addCriteriaWithPageable(query, request);
        BaseMongoRepository.addCriteriaWithSorted(query, request);
        List<Account> accounts = mongoTemplate.find(query, Account.class);
        List<GetAccountNotInLabResponse> responses = accounts.stream().map(this::convertToGetAccountNotInLab).collect(Collectors.toList());
        return new PageableResponse<>(request, totalElements, responses );
    }

    @Override
    public GetOTTResponse generateOTT(GenerateOTTRequest request) {
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername)
                .orElseThrow();

        String ott = tokenService.generateOTT(accountId, request.getProjectId());
        OttHistory ottHistory = OttHistory.builder()
                .ott(ott)
                .build();
        try {
            ottHistoryRepository.save(ottHistory);
        }catch (Exception ex){
            throw new BusinessException("Can't save ott history to database: "+ ex.getMessage());
        }

        return GetOTTResponse.builder()
                .ott(ott)
                .build();

    }

    @Override
    public LoginResponse verifyOTT(VerifyOTTRequest request) {
        try {
            OttHistory ottHistory = ottHistoryRepository.findOttHistoriesByOtt(request.getOtt()).orElseThrow();
            ottHistoryRepository.delete(ottHistory);
        }catch (Exception ex){
            throw new BusinessException(ResponseStatusEnum.UNAUTHORIZED, "OTT invalid");
        }
        String accountId = tokenService.getOTTClaims(request.getOtt(), "account-id");
        String documentId = tokenService.getOTTClaims(request.getOtt(), "document-id");
        Account account = accountRepository.findById(accountId)
                .orElseThrow();
        String token = tokenService.generateToken(account, getUserByUsername(account.getUsername()));
        UserInfo userInfo = userInfoService.getUserInfo(account.getAccountId());
        List<String> roles = account.getRoles().stream().map(_Role::getRoleName).collect(Collectors.toList());
        return LoginResponse.builder()
                .accountId(account.getAccountId())
                .username(account.getUsername())
                .fullName(account.getFullName())
                .email(account.getEmail())
                .role(roles)
                .avatar(userInfo == null ? null : userInfo.getAvatar())
                .token(token)
                .tokenExpireTime(tokenService.getExpiredTimeFromToken(token))
                .documentId(documentId)
                .build();
    }

    @Override
    public void sendVerifyPhoneNumber(String id, SendVerifyPhoneNumberRequest request) {
        accountRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account Id not exist"));
        String phoneNumber = AuthenticationUtils.validatePhoneNumber(request.getPhoneNumber());
        String code = generateVerifyPhoneNumberCode();
        List<OTPHistory> otpHistoryList = otpHistoryRepository.findByAccountIdAndEnable(id, true);

        otpHistoryList.forEach(
                v -> v.setEnable(false)
        );
        try {
            otpHistoryRepository.saveAll(otpHistoryList);
        }catch (Exception ex){

        }
        OTPHistory otpHistory = OTPHistory.builder()
                .accountId(id)
                .code(code)
                .data(phoneNumber)
                .enable(true)
                .expiredTime(LocalDateTime.now().plusMinutes(5))
                .build();

        if(Objects.nonNull(phoneNumber)){
            SendSmsEvent event = SendSmsEvent.builder()
                    .sendTo(phoneNumber)
                    .templateId("63a956d46158f173e38320c2")
                    .params(Map.of("CODE", code))
                    .build();
            try {
                sendSMSProducer.sendMessage(event);
                otpHistoryRepository.save(otpHistory);
            }catch (Exception ex){
                throw new BusinessException("Can't save otp history to database: "+ ex.getMessage());
            }
        }else{
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Phone number invalid");
        }

    }

    private String generateVerifyPhoneNumberCode() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int[] randomNumber;
        do {
            randomNumber = random.ints(6 * 5L, 48, 57)
                    .toArray();
        }while (randomNumber.length == 6);
        for (int i = 0; i < 6; i++) {
            stringBuilder.append((char) randomNumber[i]);
        }
        return stringBuilder.toString();
    }

    @Override
    public void verifyPhoneNumber(String id, VerifyPhoneNumberRequest request) {
        accountRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account Id not exist"));
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Profile Id not exist"));
        List<OTPHistory> otpHistoryList = otpHistoryRepository.findByAccountIdAndEnable(id, true);
        OTPHistory otpHistory;
        if(otpHistoryList.size()> 1){
            otpHistoryList.sort(new OTPHistoryComparator());
        }
        if(otpHistoryList.isEmpty()){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "OTP Invalid");
        }
        otpHistory = otpHistoryList.get(0);
        otpHistoryList.forEach(
                v -> v.setEnable(false)
        );
        try {
            otpHistoryRepository.saveAll(otpHistoryList);
        }catch (Exception ex){
            throw new BusinessException("Can't save all otp history to database:"+ ex.getMessage());
        }

        if(Objects.isNull(request.getOtp()) || !otpHistory.getCode().equals(request.getOtp()) || otpHistory.getExpiredTime().isBefore(LocalDateTime.now())){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "OTP Invalid");
        }
        try {
            profile.setPhoneNumber(otpHistory.getData());
            profileRepository.save(profile);
        }catch (Exception ex){
            throw new BusinessException("Can't save data to database: "+ ex.getMessage());
        }
    }

    private GetAccountNotInLabResponse convertToGetAccountNotInLab(Account account){
        return GetAccountNotInLabResponse.builder()
                .accountId(account.getAccountId())
                .username(account.getUsername())
                .email(account.getEmail())
                .fullName(account.getFullName())
                .build();
    }

    private GetAccountResponse convertToGetAccountResponse(Account account) {
        return GetAccountResponse.builder()
                .accountId(account.getAccountId())
                .username(account.getUsername())
                .email(account.getEmail())
                .fullName(account.getFullName())
                .isEnable(account.getIsEnabled())
                .isNonExpired(account.getIsNonExpired())
                .isNonLocked(account.getIsNonLocked())
                .isCredentialNonExpired(account.getIsCredentialNonExpired())
                .roles(account.getRoles().stream().map(roleService::convertToRoleResponse).collect(Collectors.toList()))
                .createdDate(account.getCreatedDate())
                .lastModifiedDate(account.getLastModifiedDate())
                .build();
    }

    private String randomPassword() {

        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int[] randomNumber;
        boolean upper = false;
        boolean lower =false;
        boolean number = false;
        do {
            randomNumber = random.ints(8 * 5L, 48, 122)
                    .filter(x -> x > 97 && x < 122 || x > 64 && x < 91 || x > 48 && x < 57)
                    .toArray();
            upper = Arrays.stream(randomNumber).anyMatch(x -> x > 48 && x < 57);
            lower =Arrays.stream(randomNumber).anyMatch(x -> x > 64 && x < 91);
            number = Arrays.stream(randomNumber).anyMatch(x ->  x > 97 && x < 122);

        } while (randomNumber.length == 8 && upper && lower && number);

        for (int i = 0; i < 8; i++) {
            stringBuilder.append((char) randomNumber[i]);
        }

        return stringBuilder.toString();
    }

    private List<String> getAuthorities(List<_Role> roles) {
        List<String> authorities = new ArrayList<>();
        List<_Role> roleEnable = roles.stream()
                .filter(_Role::getIsEnable).collect(Collectors.toList());
        authorities.addAll(roleEnable.stream()
                .map(_Role::getRoleName)
                .map(AuthenticationUtils::addRolePrefix)
                .collect(Collectors.toList()));
        authorities.addAll(roleEnable.stream()
                .filter(v -> Objects.nonNull(v.getPermissions()))
                .map(this::getPermissions)
                .flatMap(List::stream)
                .collect(Collectors.toList()));

        return authorities;
    }

    private List<String> getPermissions(_Role role) {
        return role.getPermissions().stream().filter(Objects::nonNull)
                .filter(_Permission::getIsEnable)
                .map(_Permission::getPermissionName)
                .map(v -> addPermissionPrefix(role.getRoleName(), v))
                .collect(Collectors.toList());
    }

    private void pushAccountInfo(Account account) {
        UserInfo userInfo = UserInfo.builder()
                .email(account.getEmail())
                .username(account.getUsername())
                .fullName(account.getFullName())
                .roles(account.getRoles().stream().map(_Role::getRoleName).collect(Collectors.toList()))
                .build();

        try {
            redisTemplate.opsForValue().set(String.format("userinfo:%s", account.getAccountId()), objectMapper.writeValueAsString(userInfo));
            log.info("Push UserInfo to Redis success");
        } catch (JsonProcessingException ex) {
            throw new BusinessException("Can't push user info to Redis: " + ex.getMessage());
        }
    }

    private void removeAccountInfo(Account account) {
        try {
            Boolean result = redisTemplate.delete(String.format("userinfo:%s", account.getAccountId()));
            if (Boolean.TRUE.equals(result)) {
                log.info("remove account info success");
            } else {
                log.info("account info has been deleted");
            }
        } catch (Exception ex) {
            throw new BusinessException("Can't remove userinfo in redis: " + ex.getMessage());
        }
    }
}
