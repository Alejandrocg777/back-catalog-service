package com.asb.backCompanyService.business.Imple;


import com.asb.backCompanyService.business.Interfaces.IUserService;
import com.asb.backCompanyService.dto.*;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.*;
import com.asb.backCompanyService.repository.IRolRepository;
import com.asb.backCompanyService.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.asb.backCompanyService.util.Constants;
import com.asb.backCompanyService.util.ObjectMapperUtils;
import com.asb.backCompanyService.util.Utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author manuelm
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements IUserService {

    private final IUserRepository iRepository;
    private final IRolRepository iRolRepository;
    private final Utils utils;

    @Override
    public UserDto findByEmail(String email) {
        Optional<EntityUser> objectOptional = iRepository.findByEmail(email);
        try {
            if (objectOptional.isPresent()) {
                return mapUserDto(objectOptional.get());
            } else {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Objecto NO existe");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error desconocido");
        }
    }

    @Override
    @Transactional
    public UserDto save(GgpUserSaveAndUpdateDto userDto) {

        System.out.println("user.toString() " + userDto.toString());

        // Verificar si se está creando un nuevo usuario
        if (userDto.getId() == null) {
            // Es un nuevo usuario, validar que no exista otro con el mismo correo o login
            Optional<EntityUser> objectUserOptional = iRepository
                    .findByEmailOrLogin(userDto.getEmail(), userDto.getLogin());

            if (objectUserOptional.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Correo o Username ya existe");
            }
        } else {
            // Verificar si el usuario con el ID proporcionado ya existe
            Boolean objectExists = iRepository.existsById(userDto.getId());
            if (objectExists) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El usuario con este ID ya existe");
            }
        }

        // Continuar con la creación o actualización
        EntityRol ggpRolRepo = new EntityRol(userDto.getRol(), "");
        EntityCompany ggpCompanyRepo = new EntityCompany(userDto.getCompany(),"");
        EntityPosition ggpPositionRepo = new EntityPosition(userDto.getPosition(), "");
        EntityArea ggpAreaRepo = new EntityArea(userDto.getPosition(), "");

        EntityUser entityUserRepo = new EntityUser();
        entityUserRepo.setName(userDto.getName());
        entityUserRepo.setEmail(userDto.getEmail());
        entityUserRepo.setLogin(userDto.getLogin());
        entityUserRepo.setStatus(Constants.ACTIVE_STATUS);
        entityUserRepo.setRol(ggpRolRepo);
        entityUserRepo.setPosition(ggpPositionRepo);
        entityUserRepo.setCompany(ggpCompanyRepo);
        entityUserRepo.setArea(ggpAreaRepo);

        if (userDto.getPassword() != null) {
            String encodedPassword = utils.bcryptEncryptor(userDto.getPassword());
            entityUserRepo.setPassword(encodedPassword);
        } else {
            entityUserRepo.setPassword(utils.bcryptEncryptor(Constants.DEFAULT_PASSWORD));
        }


        // Guardar en la base de datos
        EntityUser newEntityUserRepo = iRepository.save(entityUserRepo);

        // Mapear a DTO y retornar
        return mapUserDto(newEntityUserRepo);
    }


    @Override
    @Transactional
    public UserDto update(long userId, GgpUserSaveAndUpdateDto userDto) {
        Boolean objectExists = iRepository.existsById(userId);
        ArrayList<Long> userDtoList = new ArrayList();
        userDtoList.add(userId);
        Integer offset = 1;
        Integer limit = 10;
        Pageable pagingSort = PageRequest.of(offset, limit);
        Page<EntityUser> objectUserOptional = iRepository
                .findByEmailOrLoginAndIdNotIn(userDto.getEmail(),
                        userDto.getLogin(),
                        userDtoList,
                        pagingSort
                );
        if (objectExists && objectUserOptional.getTotalElements() == 0) {
            Optional<EntityRol> optionlaGgpRolRepo = iRolRepository.findById(userDto.getRol());
            EntityRol ggpRolRepo = optionlaGgpRolRepo.orElse(new EntityRol(userDto.getRol(), ""));
            EntityCompany ggpCompanyRepo = new EntityCompany(userDto.getCompany(),"");
            EntityPosition ggpPositionRepo = new EntityPosition(userDto.getPosition(), "");
            EntityArea ggpAreaRepo = new EntityArea(userDto.getPosition(), "");


            EntityUser entityUserRepoOld = iRepository
                    .findById(userId).orElse(new EntityUser());


            EntityUser entityUserRepo = new EntityUser();
            entityUserRepo.setId(userId);
            entityUserRepo.setName(userDto.getName());
            entityUserRepo.setEmail(userDto.getEmail());
            entityUserRepo.setLogin(userDto.getLogin());
            entityUserRepo.setStatus(userDto.getStatus());
            entityUserRepo.setRol(ggpRolRepo);
            entityUserRepo.setPosition(ggpPositionRepo);
            entityUserRepo.setCompany(ggpCompanyRepo);
            entityUserRepo.setArea(ggpAreaRepo);

            if (userDto.getPassword() != null) {
                String encodedPassword = utils.bcryptEncryptor(userDto.getPassword());
                entityUserRepo.setPassword(encodedPassword);
            }

            EntityUser newObject = iRepository.save(entityUserRepo);

            return mapUserDto(newObject);
        } else {
            if (objectUserOptional.getTotalElements() > 0) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Correo o Username ya existe");
            } else {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Objecto NO existe");
            }
        }
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        try {
            Optional<EntityUser> objectOptional = iRepository.findById(id);
            UserDto objectDtoVo = null;
            if (objectOptional.isPresent()) {
                objectDtoVo = new UserDto();
                BeanUtils.copyProperties(objectOptional.get(), objectDtoVo);
                EntityUser objectTmp = objectOptional.get();
                objectTmp.setStatus(Constants.INACTIVE_STATUS);
                iRepository.save(objectTmp);
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    @Override
    public UserDto get(long id) {
        Optional<EntityUser> objectOptional = iRepository.findById(id);
        if (objectOptional.isPresent()) {
            return mapUserDto(objectOptional.get());
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Objecto No existe");
        }
    }

    @Override
    public Page<GgpUserGetAllDto> getAll(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 5;
        String status = Constants.ACTIVE_STATUS;
        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }
        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders");
        }

        if (customQuery.containsKey("sortBy")) {
            sortBy = customQuery.get("sortBy");
        }

        if (customQuery.containsKey("page")) {
            page = Integer.parseInt(customQuery.get("page"));
        }

        if (customQuery.containsKey("size")) {
            size = Integer.parseInt(customQuery.get("size"));
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);
        return mapPageUserDto(iRepository.findByStatus(status, pagingSort), pagingSort);
    }

    @Override
    public Page<GgpUserGetAllDto> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        return mapPageUserDto(iRepository.findByStatus("ACTIVE",pagingSort), pagingSort);
    }

    @Override
    public List<GgpUserGetAllDto> getAllWithOutPage(Map<String, String> customQuery) {
        String status = Constants.ACTIVE_STATUS;
        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }

        return iRepository.findByStatus(status).stream()
                .map(objects -> GgpUserGetAllDto.builder()
                        .id(objects.getId())
                        .name(objects.getName())
                        .login(objects.getLogin())
                        .password(objects.getPassword())
                        .email(objects.getEmail())
                        .rol(RolDto.builder()
                                .id(objects.getRol().getId())
                                .name(objects.getRol().getName())
                                .build())
                        .rolId(objects.getRol().getId())
                        .position(objects.getPosition())
                        .company(objects.getCompany())
                        .status(objects.getStatus())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public Page<GgpUserGetAllDto> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 5;
        String status = Constants.ACTIVE_STATUS;
        Long id = null;
        String name = null;
        String email = null;
        String login = null;
        String companyName = null;
        String positionDescription = null;
        String areaDescription = null;
        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders");
        }

        if (customQuery.containsKey("sortBy")) {
            sortBy = customQuery.get("sortBy");
        }

        if (customQuery.containsKey("page")) {
            page = Integer.parseInt(customQuery.get("page"));
        }

        if (customQuery.containsKey("size")) {
            size = Integer.parseInt(customQuery.get("size"));
        }

        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }
        if (customQuery.containsKey("id")) {
            //name = "%" + customQuery.get("name") + "%";
            id = Long.valueOf(customQuery.get("id"));
        }
        if (customQuery.containsKey("name")) {
            //name = "%" + customQuery.get("name") + "%";
            name = customQuery.get("name");
        }

        if (customQuery.containsKey("email")) {
            //email = "%" + customQuery.get("email") + "%";
            email = customQuery.get("email");
        }

        if (customQuery.containsKey("login")) {
            //login = "%" + customQuery.get("login") + "%";
            login = customQuery.get("login");
        }

        if (customQuery.containsKey("companyName")) {
            //login = "%" + customQuery.get("companyName") + "%";
            companyName = customQuery.get("companyName");
        }

        if (customQuery.containsKey("description")) {
            //login = "%" + customQuery.get("positionDescription") + "%";
            positionDescription = customQuery.get("description");
        }

        if (customQuery.containsKey("areaDescription")) {
            //login = "%" + customQuery.get("positionDescription") + "%";
            areaDescription = customQuery.get("areaDescription");
        }



        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);
        return mapPageUserDto(iRepository
                .findByIdOrNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrLoginContainingIgnoreCaseOrCompanyId_CompanyNameContainingIgnoreCaseOrPositionId_DescriptionContainingIgnoreCaseOrAreaId_DescriptionContainingIgnoreCaseOrAndStatus(
                       id, name, email, login,companyName,positionDescription,areaDescription, status,pagingSort), pagingSort
                );
    }

    @Override
    public ForgotPasswordUserDto forgotPassword(GgpForgotPasswordDto ggpForgotPasswordDto) {
        Optional<EntityUser> objectOptional = iRepository.findByEmail(ggpForgotPasswordDto.getEmail())
                .stream()
                .findFirst();

        if (!objectOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Objecto NO existe");
        }

        Random random = new Random();
        EntityUser entityUser = objectOptional.get();
        String password = Constants.PASSWORD_DEFUULT_PREFIX + "+" + random.nextInt(10);
        String encodedPassword = utils.bcryptEncryptor(password);
        entityUser.setPassword(encodedPassword);

        iRepository.save(entityUser);
/*
        try {
            //iMailService.forgotPassword(ggpForgotPasswordDto.getEmail(), password);

            return ForgotPasswordUserDto.builder()
                    .email(ggpForgotPasswordDto.getEmail())
                    .build();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }*/
        return ForgotPasswordUserDto.builder()
                .email(ggpForgotPasswordDto.getEmail())
                .build();
    }

    private UserDto mapUserDto(EntityUser objectUser) {
        UserDto objectDtoVo = new UserDto();
        BeanUtils.copyProperties(objectUser, objectDtoVo);
        objectDtoVo.setRolId(objectUser.getRol().getId());
        return objectDtoVo;
    }

    private Page<GgpUserGetAllDto> mapPageUserDto(Page<EntityUser> entityPage, Pageable pagingSort) {
        int totalElements = (int) entityPage.getTotalElements();
        return new PageImpl<>(
                ObjectMapperUtils.mapAll(entityPage.getContent(),
                        GgpUserGetAllDto.class),
                pagingSort, totalElements).map(ggpUserGetAllDto -> {
            ggpUserGetAllDto.setRolId(ggpUserGetAllDto.getRol().getId());
            return ggpUserGetAllDto;
        });
    }
}
