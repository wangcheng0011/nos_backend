package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ArrayListMultimap;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.common.service.AttachService;
import com.knd.front.common.util.JPushUtil;
import com.knd.front.dto.VoUrl;
import com.knd.front.entity.*;
import com.knd.front.home.mapper.CourseHeadMapper;
import com.knd.front.live.entity.BaseDifficulty;
import com.knd.front.live.mapper.BaseDifficultyMapper;
import com.knd.front.live.service.UserOrderRecordService;
import com.knd.front.train.dto.*;
import com.knd.front.train.mapper.*;
import com.knd.front.train.request.GetTrainProgramRequest;
import com.knd.front.train.request.SaveTrainProgramRequest;
import com.knd.front.train.request.TrainProgramWeekDetailRequest;
import com.knd.front.train.service.ProgramPlanGenerationService;
import com.knd.front.train.service.ProgramService;
import com.knd.front.user.dto.TrainDetailDto;
import com.knd.front.user.dto.TrainWeekDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author will
 */
@Service("programService")
@RequiredArgsConstructor
@Log4j2
public class ProgramServiceImpl extends ServiceImpl<ProgramDao, ProgramEntity> implements ProgramService {

    private final ProgramWeekDetailDao programWeekDetailDao;
    private final ProgramDayItemDao programDayItemDao;
    private final ProgramPlanGenerationDao programPlanGenerationDao;
    private final CourseHeadMapper courseHeadMapper;
    private final ActionArrayMapper actionArrayMapper;
    private final ProgramHeadDao programHeadDao;
    private final ProgramHolidayDao programHolidayDao;
    private final ProgramPlanGenerationService programPlanGenerationService;
    private final TrainProgramMapper trainProgramMapper;
    private final AttachService attachService;
    private final UserOrderRecordService userOrderRecordService;
    private final JPushUtil jPushUtil;
    @Resource
    private BaseDifficultyMapper baseDifficultyMapper;

    //????????????
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    //?????????????????????
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveTrainProgram(SaveTrainProgramRequest saveTrainProgramRequest) {
        log.info("--------------------???????????????????????????--------------------------");
        log.info("saveTrainProgram saveTrainProgramRequest:{{}}", saveTrainProgramRequest);
        String programId = "";
        String attachId = "";
        VoUrl picAttachUrl = saveTrainProgramRequest.getPicAttachUrl();
        log.info("saveTrainProgram picAttachUrl:{{}}", picAttachUrl);
        if (picAttachUrl != null
                && picAttachUrl.getPicAttachName() != null
                && picAttachUrl.getPicAttachNewName() != null
                && picAttachUrl.getPicAttachSize() != null) {
            //??????????????????
            Attach attach = attachService.saveAttach(saveTrainProgramRequest.getUserId(), picAttachUrl.getPicAttachName()
                    , picAttachUrl.getPicAttachNewName(), picAttachUrl.getPicAttachSize());
            attachId = attach.getId();
        }
        //????????????
        if ("1".equals(saveTrainProgramRequest.getSource())) {
            if (StringUtils.isEmpty(saveTrainProgramRequest.getUserId())) {
                return ResultUtil.error("U0995", "??????id????????????");
            }
            if (StringUtils.isEmpty(saveTrainProgramRequest.getBeginTime())) {
                return ResultUtil.error("U0995", "??????????????????????????????");
            }
            LocalDateTime startTime = saveTrainProgramRequest.getBeginTime();
            LocalDateTime endTime = saveTrainProgramRequest.getBeginTime().plusWeeks(saveTrainProgramRequest.getTrainWeekNum());
            QueryWrapper<ProgramEntity> programEntityQueryWrapper = new QueryWrapper<>();
            programEntityQueryWrapper.eq("deleted", 0);
            programEntityQueryWrapper.eq("userId", saveTrainProgramRequest.getUserId());
            List<ProgramEntity> programEntities = baseMapper.selectList(programEntityQueryWrapper);
            LocalDateTime beginTime = saveTrainProgramRequest.getBeginTime();
            LocalDateTime closeTime = beginTime.plusWeeks(saveTrainProgramRequest.getTrainWeekNum());
            int a = 0;
            for (ProgramEntity programEntity : programEntities) {
                if (programEntity.getBeginTime() != null && programEntity.getEndTime() != null) {
                    if (!(programEntity.getEndTime().isBefore(beginTime) || programEntity.getBeginTime().isAfter(closeTime))) {
                        log.info("saveTrainProgram beginTime:{{}}", programEntity.getBeginTime());
                        log.info("saveTrainProgram endTime:{{}}", programEntity.getEndTime());
                        a++;
                    }
                }
            }
            if (a >= 2) {
                return ResultUtil.error("U0995", "???????????????????????????????????????????????????");
            }
            if (saveTrainProgramRequest.getTrainProgramWeekDetailRequests().size() == 0) {
                return ResultUtil.error("U0995", "?????????????????????");
            }
            ProgramEntity programEntity = new ProgramEntity();
            programEntity.setProgramName(saveTrainProgramRequest.getProgramName());
            //?????????????????? ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            DateTime begin = new DateTime(saveTrainProgramRequest.getBeginTime().toString());
            programEntity.setBeginTime(getLocalDateTime(begin));
            boolean nextWeekFlag = false;
            //?????????????????????
            int beginDateWeekName = begin.dayOfWeek().get();
            //????????????????????????????????????
            AtomicBoolean sameFlag = new AtomicBoolean(false);
            List<TrainProgramWeekDetailRequest> orderTrainProgramWeekDetailRequests = new ArrayList<>();
            saveTrainProgramRequest.getTrainProgramWeekDetailRequests().forEach(i -> {
                if (i.getWeekDayName() >= beginDateWeekName) {
                    orderTrainProgramWeekDetailRequests.add(i);
                }
                if (i.getWeekDayName() == beginDateWeekName) {
                    sameFlag.set(true);
                }
            });
            //?????????????????????
            orderTrainProgramWeekDetailRequests.sort(Comparator.comparingInt(TrainProgramWeekDetailRequest::getWeekDayName));
            //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (orderTrainProgramWeekDetailRequests.size() == 0) {
                nextWeekFlag = true;
            }
            Integer trainWeekNum = saveTrainProgramRequest.getTrainWeekNum();
            //?????????????????????????????????
            DateTime endDateTime = begin.plusWeeks(trainWeekNum).minusDays(1);
            programEntity.setEndTime(getLocalDateTime(endDateTime).minusSeconds(1));
            //?????????????????????????????????
            int totalTrainDays = saveTrainProgramRequest.getTrainProgramWeekDetailRequests().size() * trainWeekNum;
            if (sameFlag.get()) {
                totalTrainDays++;
            }
            //????????????
            programEntity.setTrainTotalDayNum(totalTrainDays + "");
            programEntity.setTrainWeekNum(trainWeekNum + "");
            programEntity.setTrainWeekDayNum(saveTrainProgramRequest.getTrainProgramWeekDetailRequests().size() + "");
            programEntity.setUserId(saveTrainProgramRequest.getUserId());
            programEntity.setSource(saveTrainProgramRequest.getSource());
            programEntity.setType(saveTrainProgramRequest.getType());
            programEntity.setDeleted("0");
            log.info("saveTrainProgram programEntity:{{}}", programEntity);
            //       programEntity.setPicAttachId(attachId);
            baseMapper.insert(programEntity);
            programId = programEntity.getId();

            //??????????????????
            List<ProgramPlanGenerationEntity> programPlanGenerationEntityList = new ArrayList();
            DateTime tempBeginTime = new DateTime(saveTrainProgramRequest.getBeginTime().toString());
            log.info("saveTrainProgram tempBeginTime.isAfter(endDateTime):{{}}", tempBeginTime.isAfter(endDateTime));
            while (true) {
                if (tempBeginTime.isAfter(endDateTime)) {
                    break;
                }
                //???????????????????????????????????????
                for (int i = 0; i < saveTrainProgramRequest.getTrainProgramWeekDetailRequests().size(); i++) {
                    TrainProgramWeekDetailRequest trainProgramWeekDetailRequest = saveTrainProgramRequest.getTrainProgramWeekDetailRequests().get(i);
                    log.info("saveTrainProgram tempBeginTime.getDayOfWeek() == trainProgramWeekDetailRequest.getWeekDayName():{{}}", tempBeginTime.getDayOfWeek() == trainProgramWeekDetailRequest.getWeekDayName());
                    if (tempBeginTime.getDayOfWeek() == trainProgramWeekDetailRequest.getWeekDayName()) {
                        DateTime finalTempBeginTime = tempBeginTime;
                        trainProgramWeekDetailRequest.getTrainProgramDayItemRequests().forEach(z -> {
                            ProgramPlanGenerationEntity programPlanGenerationEntity = new ProgramPlanGenerationEntity();
                            programPlanGenerationEntity.setTrainProgramId(programEntity.getId());
                            programPlanGenerationEntity.setUserId(programEntity.getUserId());
                            //programPlanGenerationEntity.setTrainWeekNum(trainWeekNum +"");
                            programPlanGenerationEntity.setDayName(trainProgramWeekDetailRequest.getWeekDayName() + "");
                            programPlanGenerationEntity.setTrainDate(getLocalDateTime(finalTempBeginTime));
                            programPlanGenerationEntity.setTrainItemType(z.getItemType());
                            programPlanGenerationEntity.setTrainItemId(z.getItemId());
                            programPlanGenerationEntityList.add(programPlanGenerationEntity);
                            log.info("saveTrainProgram programPlanGenerationEntityList:{{}}", programPlanGenerationEntityList);
                        });
                    }
                }
                tempBeginTime = tempBeginTime.plusDays(1);
            }
            programPlanGenerationService.saveBatch(programPlanGenerationEntityList);
            userOrderRecordService.save(UserUtils.getUserId(), "4",
                    programEntity.getProgramName(),
                    "????????????????????????,????????????" + DateUtils.formatLocalDateTime(programEntity.getBeginTime(), "yyyy-MM-dd") + "???" + DateUtils.formatLocalDateTime(programEntity.getEndTime(), "yyyy-MM-dd"),
                    programEntity.getBeginTime(),
                    programEntity.getId());
        } else {
            //????????????
            ProgramEntity programEntity = new ProgramEntity();
            programEntity.setProgramName(saveTrainProgramRequest.getProgramName());
            programEntity.setTrainWeekNum(saveTrainProgramRequest.getTrainWeekNum() + "");
            programEntity.setTrainWeekDayNum(saveTrainProgramRequest.getTrainProgramWeekDetailRequests().size() + "");
            programEntity.setSource(saveTrainProgramRequest.getSource());
            programEntity.setType(saveTrainProgramRequest.getType());
            programEntity.setPicAttachId(attachId);
            programEntity.setCreateBy(saveTrainProgramRequest.getUserId());
            programEntity.setDeleted("0");
            baseMapper.insert(programEntity);
            //??????????????????
            programId = programEntity.getId();
            /*//???????????????????????????????????????
            for(int i=0;i<saveTrainProgramRequest.getTrainProgramWeekDetailRequests().size();i++) {
                TrainProgramWeekDetailRequest trainProgramWeekDetailRequest = saveTrainProgramRequest.getTrainProgramWeekDetailRequests().get(i);
                    trainProgramWeekDetailRequest.getTrainProgramDayItemRequests().forEach(z -> {
                        ProgramPlanGenerationEntity programPlanGenerationEntity = new ProgramPlanGenerationEntity();
                        programPlanGenerationEntity.setTrainProgramId(programEntity.getId());
                        programPlanGenerationEntity.setUserId(programEntity.getUserId());
                        //programPlanGenerationEntity.setTrainWeekNum(trainWeekNum +"");
                        programPlanGenerationEntity.setDayName(trainProgramWeekDetailRequest.getWeekDayName()+"");
                        programPlanGenerationEntity.setTrainItemType(z.getItemType());
                        programPlanGenerationEntity.setTrainItemId(z.getItemId());
                        log.info("saveTrainProgram programPlanGenerationEntity:{{}}",programPlanGenerationEntity);
                        int insert = programPlanGenerationDao.insert(programPlanGenerationEntity);
                        log.info("saveTrainProgram programPlanGenerationEntity insert:{{}}",insert);
                    });

            }*/

        }

        //???????????????
        String finalProgramId = programId;
        log.info("saveTrainProgram TrainProgramWeekDetail:{{}}", saveTrainProgramRequest.getTrainProgramWeekDetailRequests());
        saveTrainProgramRequest.getTrainProgramWeekDetailRequests().forEach(e -> {
            ProgramWeekDetailEntity programWeekDetailEntity = new ProgramWeekDetailEntity();
            programWeekDetailEntity.setProgramId(finalProgramId);
            programWeekDetailEntity.setWeekDayName(e.getWeekDayName() + "");
            log.info("saveTrainProgram ProgramWeekDetailEntity:{{}}", programWeekDetailEntity);
            int insert1 = programWeekDetailDao.insert(programWeekDetailEntity);
            log.info("saveTrainProgram ProgramWeekDetailEntity insert:{{}}", insert1);
            //???????????????????????????
            log.info("saveTrainProgram TrainProgramDayItemRequest:{{}}", e.getTrainProgramDayItemRequests());
            e.getTrainProgramDayItemRequests().forEach(i -> {
                ProgramDayItemEntity programDayItemEntity = new ProgramDayItemEntity();
                programDayItemEntity.setTrainProgramWeekDetailId(programWeekDetailEntity.getId());
                programDayItemEntity.setItemType(i.getItemType());
                programDayItemEntity.setItemId(i.getItemId());
                log.info("saveTrainProgram programDayItemEntity:{{}}", programDayItemEntity);
                int insert = programDayItemDao.insert(programDayItemEntity);
                log.info("saveTrainProgram programDayItemEntity insert:{{}}", insert);
            });
        });
        log.info("--------------------?????????????????????????????????--------------------------");
        return ResultUtil.success("????????????");
    }

    @Override
    public Result getTrainProgram(GetTrainProgramRequest getTrainProgramRequest) {
        log.info("--------------------???????????????????????????--------------------------");
        log.info("getTrainProgram getTrainProgramRequest:{{}}", getTrainProgramRequest);
        QueryWrapper<ProgramPlanGenerationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", getTrainProgramRequest.getUserId());
        if (StringUtils.isNotEmpty(getTrainProgramRequest.getEndDate())) {
            queryWrapper.le("date_format(trainDate ,'%Y-%m-%d')", getTrainProgramRequest.getEndDate());
        }
        if (StringUtils.isNotEmpty(getTrainProgramRequest.getBeginDate())) {
            queryWrapper.ge("date_format(trainDate ,'%Y-%m-%d')", getTrainProgramRequest.getBeginDate());
        }
        // queryWrapper.le("date_format(date_add(trainDate ,interval 7 day),'%Y-%m-%d')",DateUtils.getCurrentDateStr());
        if (StringUtils.isNotEmpty(getTrainProgramRequest.getTrainProgramId())) {
            queryWrapper.eq("trainProgramId", getTrainProgramRequest.getTrainProgramId());
        }
        queryWrapper.select("id", "userId", "trainProgramId", "date_format(trainDate ,'%Y-%m-%d') trainDate", "trainItemType", "trainItemId", "trainFinishFlag");
        List<ProgramPlanGenerationEntity> programPlanGenerationEntities = programPlanGenerationDao.selectList(queryWrapper);
        log.info("getTrainProgram programPlanGenerationEntities:{{}}", programPlanGenerationEntities);
        List<TrainProgramQueryDto> trainProgramQueryDtos = new ArrayList<>();
        ArrayListMultimap<String, ProgramPlanGenerationEntity> programMultimap = ArrayListMultimap.create();
        log.info("getTrainProgram programMultimap:{{}}", programMultimap);
        for (ProgramPlanGenerationEntity e : programPlanGenerationEntities) {
            programMultimap.put(e.getTrainProgramId(), e);
        }
        Map<String, Collection<ProgramPlanGenerationEntity>> programMap = programMultimap.asMap();
        log.info("getTrainProgram programMap:{{}}", programMap);
        log.info("getTrainProgram programMap.entrySet():{{}}", programMap.entrySet());
        int countsize = 0;
        for (Map.Entry<String, Collection<ProgramPlanGenerationEntity>> j : programMap.entrySet()) {
            countsize++;
            System.out.println(countsize);
            String programId = j.getKey();
            log.info("getTrainProgram programId:{{}}", programId);
            ProgramEntity programEntity = baseMapper.selectById(programId);
            log.info("getTrainProgram programEntity:{{}}", programEntity);
            TrainProgramQueryDto trainProgramQueryDto = new TrainProgramQueryDto();
            if (StringUtils.isNotEmpty(programEntity) && StringUtils.isNotEmpty(programEntity.getEndTime()) && programEntity.getEndTime().isAfter(LocalDateTime.now())) {
                trainProgramQueryDto.setId(programEntity.getId());
                trainProgramQueryDto.setProgramName(programEntity.getProgramName());
                trainProgramQueryDto.setBeginTime(programEntity.getBeginTime());
                trainProgramQueryDto.setEndTime(programEntity.getEndTime());
                Collection<ProgramPlanGenerationEntity> programPlanGenerationEntitys0 = j.getValue();
                log.info("getTrainProgram programPlanGenerationEntitys0:{{}}", programPlanGenerationEntitys0);
                ArrayListMultimap<String, ProgramPlanGenerationDto> multimap = ArrayListMultimap.create();
                for (ProgramPlanGenerationEntity i : programPlanGenerationEntitys0) {
                    ProgramPlanGenerationDto programPlanGenerationDto = new ProgramPlanGenerationDto();
                    BeanUtils.copyProperties(i, programPlanGenerationDto);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    programPlanGenerationDto.setTrainDate(i.getTrainDate().format(df));
                    log.info("getTrainProgram programPlanGenerationDto:{{}}", programPlanGenerationDto);
                    multimap.put(i.getTrainDate().format(df), programPlanGenerationDto);
                }
                log.info("getTrainProgram multimap:{{}}", multimap);
                List<TrainProgramPlanDto> trainProgramPlanDtoList = new ArrayList<>();
                Map<String, Collection<ProgramPlanGenerationDto>> map1 = multimap.asMap();
                log.info("getTrainProgram map1.entrySet:{{}}", map1.entrySet());
                for (Map.Entry<String, Collection<ProgramPlanGenerationDto>> p : map1.entrySet()) {
                    TrainProgramPlanDto trainProgramPlanDto = new TrainProgramPlanDto();
                    trainProgramPlanDto.setTrainDate(p.getKey());
                    Collection<ProgramPlanGenerationDto> programPlanGenerationEntitys = p.getValue();
                    log.info("getTrainProgram programPlanGenerationEntitys:{{}}", programPlanGenerationEntitys);
                    for (ProgramPlanGenerationDto e : programPlanGenerationEntitys) {
                        if ("0".equals(e.getTrainItemType())) {
                            CourseHead courseHead = courseHeadMapper.selectById(e.getTrainItemId());
                            log.info("saveTrainProgram courseHead:{{}}", courseHead);
                            e.setTrainItemName(courseHead.getCourse());
                            CourseHeadDto courseHeadDto = new CourseHeadDto();
                            BeanUtils.copyProperties(courseHead, courseHeadDto);
                            BaseDifficulty baseDifficulty = baseDifficultyMapper.selectById(courseHead.getDifficultyId());
                            courseHeadDto.setDifficulty(StringUtils.isNotEmpty(baseDifficulty) ? baseDifficulty.getDifficulty() : "");
                            e.setCourseHead(courseHeadDto);
                        } else {
                            ActionArray actionArray = actionArrayMapper.selectById(e.getTrainItemId());
                            log.info("saveTrainProgram actionArray:{{}}", actionArray);
                            ActionArrayDto actionArrayDto = new ActionArrayDto();
                            BeanUtils.copyProperties(actionArray, actionArrayDto);
                            e.setTrainItemName(actionArray.getActionArrayName());
                            e.setActionArrayDto(actionArrayDto);
                        }
                        QueryWrapper<ProgramHeadEntity> programHeadEntityQueryWrapper = new QueryWrapper<>();
                        programHeadEntityQueryWrapper.eq("planGenerationId", e.getId());
                        programHeadEntityQueryWrapper.eq("deleted", "0");

                        ProgramHeadEntity programHeadEntity = programHeadDao.selectOne(programHeadEntityQueryWrapper);
                        log.info("saveTrainProgram programHeadEntity:{{}}", programHeadEntity);
                        if (programHeadEntity != null) {
                            e.setTrainHeadId(programHeadEntity.getTrainHeadInfoId());
                        }
                    }
                    trainProgramPlanDto.setProgramPlanGenerationDtoList(p.getValue());
                    trainProgramPlanDtoList.add(trainProgramPlanDto);
                }
                log.info("saveTrainProgram trainProgramPlanDtoList:{{}}", trainProgramPlanDtoList);
                trainProgramPlanDtoList.sort((e1, e2) -> DateTime.parse(e1.getTrainDate()).isBefore(DateTime.parse(e2.getTrainDate())) ? -1 : 1);
                trainProgramQueryDto.setTrainProgramPlanDtoList(trainProgramPlanDtoList);
                trainProgramQueryDtos.add(trainProgramQueryDto);
                log.info("saveTrainProgram trainProgramPlanDtoList:{{}}", trainProgramPlanDtoList);
            }
        }
        log.info("saveTrainProgram trainProgramQueryDtos:{{}}", trainProgramQueryDtos);
        log.info("--------------------?????????????????????????????????--------------------------");
        return ResultUtil.success(trainProgramQueryDtos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result takeRestTrainProgram(String trainProgramId, String userId, String restDate) throws ParseException {
        log.info("---------------------------------????????????---------------------------------");
        log.info("takeRestTrainProgram trainProgramId:{{}}", trainProgramId);
        log.info("takeRestTrainProgram userId:{{}}", userId);
        log.info("takeRestTrainProgram restDate:{{}}", restDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ss");
        Date parseDate = simpleDateFormat.parse(restDate);
        String formatDate = simpleDateFormat.format(parseDate);
        QueryWrapper<ProgramPlanGenerationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("date_format(trainDate ,'%Y-%m-%d')", formatDate);
        queryWrapper.eq("trainProgramId", trainProgramId);
        queryWrapper.eq("trainFinishFlag", "0");
        log.info("takeRestTrainProgram userId:{{}}", userId);
        log.info("takeRestTrainProgram restDate:{{}}", restDate);
        log.info("takeRestTrainProgram trainProgramId:{{}}", trainProgramId);
        log.info("takeRestTrainProgram trainFinishFlag:{{}}", "0");
        List<ProgramPlanGenerationEntity> programPlanGenerationEntities = programPlanGenerationDao.selectList(queryWrapper);
        log.info("-----------------------------------------------------------------------------------------------------------");
        log.info("programPlanGenerationEntities number:{{}}", programPlanGenerationEntities.size());
        if (programPlanGenerationEntities.size() == 0) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "????????????????????????");
        }
        for (ProgramPlanGenerationEntity p : programPlanGenerationEntities) {
            //??????????????????????????????????????????
            p.setTrainFinishFlag("2");
            programPlanGenerationDao.updateById(p);
        }
        String endTrainDateStr = programPlanGenerationDao.getEndTrainDate(userId, trainProgramId);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime endTrainDate = LocalDateTime.parse(endTrainDateStr, df);
        QueryWrapper<ProgramWeekDetailEntity> programWeekDetailEntityQueryWrapper = new QueryWrapper<>();
        queryWrapper.eq("programId", trainProgramId);
        log.info("programPlanGenerationEntities programId:{{}}", trainProgramId);
        List<ProgramWeekDetailEntity> programWeekDetailEntities = programWeekDetailDao.selectList(programWeekDetailEntityQueryWrapper);
        ProgramEntity programEntity = baseMapper.selectById(trainProgramId);
        log.info("takeRestTrainProgram programEntity:{{}}", programEntity);
        LocalDateTime endTime = programEntity.getEndTime().minusDays(1);
        log.info("takeRestTrainProgram endTime:{{}}", endTime);
        LocalDateTime localDateTime = LocalDateTime.parse(restDate, df);
        boolean overFlag = false;
        while (!overFlag) {
            while (!overFlag) {
                if (endTrainDate.isAfter(endTime)) {
                    break;
                }
                for (ProgramWeekDetailEntity m : programWeekDetailEntities) {
                    log.info("------------------------------------??????????????????-----------------------------------------");
                    DayOfWeek dayOfWeek = endTrainDate.getDayOfWeek();
                    Integer value = endTrainDate.getDayOfWeek().getValue();
                    if (endTrainDate.getDayOfWeek().getValue() == Integer.valueOf(m.getWeekDayName())) {
                        log.info("programPlanGenerationEntities programPlanGenerationEntities:{{}}", programPlanGenerationEntities);
                        for (ProgramPlanGenerationEntity p : programPlanGenerationEntities) {
                            //????????????????????????
                            QueryWrapper<ProgramPlanGenerationEntity> programPlanGenerationEntityQueryWrapper = new QueryWrapper<>();
                            log.info("takeRestTrainProgram id:{{}}", p.getId());
                            programPlanGenerationEntityQueryWrapper.eq("id", p.getId());
                            programPlanGenerationEntityQueryWrapper.eq("deleted", "0");
                            ProgramPlanGenerationEntity programPlanGenerationEntity = programPlanGenerationDao.selectOne(programPlanGenerationEntityQueryWrapper);
                            programPlanGenerationEntity.setTrainProgramId(p.getTrainProgramId());
                            programPlanGenerationEntity.setUserId(p.getUserId());
                            programPlanGenerationEntity.setDayName(p.getDayName());
                            //programPlanGenerationEntity.setTrainDate(endTrainDate);
                            programPlanGenerationEntity.setTrainDate(localDateTime);
                            programPlanGenerationEntity.setTrainItemType(p.getTrainItemType());
                            programPlanGenerationEntity.setTrainItemId(p.getTrainItemId());
                            programPlanGenerationEntity.setTrainFinishFlag("2");
                            log.info("takeRestTrainProgram programPlanGenerationEntity:{{}}", programPlanGenerationEntity);
                            programPlanGenerationDao.update(programPlanGenerationEntity, programPlanGenerationEntityQueryWrapper);
                        }
                        overFlag = true;
                        break;
                    }
                }
            }
            if (!overFlag) {
                LocalDateTime newEndTime = programEntity.getEndTime().minusDays(1);
                log.info("takeRestTrainProgram newEndTime:{{}}", newEndTime);
                while (!overFlag) {
                    if (endTime.isAfter(newEndTime)) {
                        break;
                    }
                    for (ProgramWeekDetailEntity m : programWeekDetailEntities) {
                        log.info("------------------------------------??????????????????-----------------------------------------");
                        log.info("takeRestTrainProgram number:{{}}", programWeekDetailEntities.size());
                        if (StringUtils.isNotEmpty(m.getWeekDayName())) {
                            if (endTime.getDayOfWeek().getValue() == Integer.valueOf(m.getWeekDayName())) {
                                log.info("programPlanGenerationEntities programPlanGenerationEntities:{{}}", programPlanGenerationEntities);
                                for (ProgramPlanGenerationEntity p : programPlanGenerationEntities) {
                                    //????????????????????????
                                    QueryWrapper<ProgramPlanGenerationEntity> programPlanGenerationEntityQueryWrapper = new QueryWrapper<>();
                                    log.info("takeRestTrainProgram id:{{}}", p.getId());
                                    programPlanGenerationEntityQueryWrapper.eq("id", p.getId());
                                    programPlanGenerationEntityQueryWrapper.eq("deleted", "0");
                                    ProgramPlanGenerationEntity programPlanGenerationEntity = programPlanGenerationDao.selectOne(programPlanGenerationEntityQueryWrapper);
                                    programPlanGenerationEntity.setTrainProgramId(p.getTrainProgramId());
                                    programPlanGenerationEntity.setUserId(p.getUserId());
                                    programPlanGenerationEntity.setDayName(p.getDayName());
                                    //programPlanGenerationEntity.setTrainDate(endTrainDate);
                                    programPlanGenerationEntity.setTrainDate(localDateTime);
                                    programPlanGenerationEntity.setTrainItemType(p.getTrainItemType());
                                    programPlanGenerationEntity.setTrainItemId(p.getTrainItemId());
                                    programPlanGenerationEntity.setTrainFinishFlag("2");
                                    log.info("takeRestTrainProgram programPlanGenerationEntity:{{}}", programPlanGenerationEntity);
                                    programPlanGenerationDao.update(programPlanGenerationEntity, programPlanGenerationEntityQueryWrapper);
                                }
                                overFlag = true;
                                break;
                            }
                        }
                    }
                    //??????????????????????????????????????????????????????
                    programEntity.setEndTime(newEndTime.minusSeconds(1));
                    programEntity.setTrainWeekNum((Integer.parseInt(programEntity.getTrainWeekNum()) + 1) + "");
                    baseMapper.updateById(programEntity);
                }
            }
        }
        ProgramHolidayEntity programHolidayEntity = new ProgramHolidayEntity();
        programHolidayEntity.setTrainProgramId(trainProgramId);
        programHolidayEntity.setDayNum("1");
        programHolidayEntity.setHolidayType("0");
        programHolidayEntity.setRestDate(localDateTime);
        programHolidayDao.insert(programHolidayEntity);
        log.info("---------------------------------????????????---------------------------------");
        return ResultUtil.success(programEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteTrainProgram(String userId, String id) {
        ProgramEntity programEntity = baseMapper.selectById(id);
        if (StringUtils.isNotEmpty(programEntity)) {
            List<ProgramWeekDetailEntity> programWeekDetailEntities = programWeekDetailDao.selectList(new QueryWrapper<ProgramWeekDetailEntity>().eq("programId", programEntity.getId()).eq("deleted", 0));
            for (ProgramWeekDetailEntity week : programWeekDetailEntities) {
                programDayItemDao.delete(new QueryWrapper<ProgramDayItemEntity>().eq("deleted", 0).eq("trainProgramWeekDetailId", week.getId()));
            }
            programWeekDetailDao.delete(new QueryWrapper<ProgramWeekDetailEntity>().eq("programId", programEntity.getId()).eq("deleted", 0));
            baseMapper.deleteById(id);
        }
        programPlanGenerationDao.delete(new QueryWrapper<ProgramPlanGenerationEntity>().eq("trainProgramId", id).eq("deleted", 0));
        //   userOrderRecordService.remove(userId, id);
        return ResultUtil.success();
    }

    @Override
    public Result queryHistoryTrainProgram(String userId, String current) {
        Page<ProgramEntity> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        QueryWrapper<ProgramEntity> programEntityQueryWrapper = new QueryWrapper<>();
        programEntityQueryWrapper.eq("userId", userId);
        programEntityQueryWrapper.le("endTime", LocalDateTime.now());
        programEntityQueryWrapper.eq("deleted", "0");

        Page<ProgramEntity> programEntityPage = baseMapper.selectPage(page, programEntityQueryWrapper);
        List<TrainDetailDto> trainDetailDtos = new ArrayList<TrainDetailDto>();
        Page<TrainDetailDto> trainDetailDtoPage = new Page<>();
        for (ProgramEntity programEntity : programEntityPage.getRecords()) {
            TrainDetailDto dto = new TrainDetailDto();
            List<TrainWeekDetailDto> detail = trainProgramMapper.getDetail(programEntity.getId());
            BeanUtils.copyProperties(programEntity, dto);
            dto.setPicAttach(attachService.getImgDto(programEntity.getPicAttachId()));
            dto.setTrainDetailList(detail);
            Long weekdaynum = 0l;
            if (detail.size() > 0) {
                weekdaynum = detail.get(detail.size() - 1).getWeekdaynum();
            }
            dto.setLastTrainTime(programEntity.getBeginTime().plusWeeks(Long.parseLong(programEntity.getTrainWeekNum()) - 1l).plusDays(weekdaynum));
            trainDetailDtos.add(dto);
        }
        trainDetailDtoPage.setRecords(trainDetailDtos);
        trainDetailDtoPage.setTotal(page.getTotal());
        trainDetailDtoPage.setCurrent(page.getCurrent());
        trainDetailDtoPage.setSize(page.getSize());
        return ResultUtil.success(trainDetailDtoPage);

    }

    @Override
    public Result trainProgramPush() {
        System.err.println("??????????????????????????????: " + LocalDateTime.now());
        log.info("???????????? trainProgramPush--------------------????????????????????????--------------------------------");
        log.info("--------------------??????????????????-----------------------");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String formatDate = dtf.format(now);
        System.out.println("trainProgramPush:" + formatDate);
        QueryWrapper<ProgramPlanGenerationEntity> programPlanGenerationEntityQueryWrapper = new QueryWrapper<>();
        programPlanGenerationEntityQueryWrapper.eq("date_format(trainDate ,'%Y-%m-%d')", formatDate);
        programPlanGenerationEntityQueryWrapper.eq("trainFinishFlag", "0");
        programPlanGenerationEntityQueryWrapper.eq("deleted", "0");
        programPlanGenerationEntityQueryWrapper.groupBy("trainDate", "userId");
        List<ProgramPlanGenerationEntity> programPlanGenerationEntities = programPlanGenerationDao.selectList(programPlanGenerationEntityQueryWrapper);
        log.info("trainProgramPush programPlanGenerationEntities:{{}}", programPlanGenerationEntities);
        programPlanGenerationEntities.stream().forEach(programPlanGenerationEntity -> {
            log.info("???????????? trainProgramPush programPlanGenerationEntity:{{}}", programPlanGenerationEntity);
            QueryWrapper<ProgramEntity> programEntityQueryWrapper = new QueryWrapper<>();
            programEntityQueryWrapper.eq("id", programPlanGenerationEntity.getTrainProgramId());
            programEntityQueryWrapper.eq("deleted", 0);
            ProgramEntity programEntity = trainProgramMapper.selectOne(programEntityQueryWrapper);
            log.info("???????????? trainProgramPush programEntity:{{}}", programEntity);
            // ??????????????????
            // ????????????????????????????????????
            Map<String, String> parm = new HashMap<>();
            // ??????????????????,?????????????????????
            parm.put("title", "???????????????");
            parm.put("alias", programEntity.getUserId());
            parm.put("msg", programEntity.getProgramName());
            parm.put("trainProgramId", programEntity.getId());
            log.info("???????????? trainProgramPush parm:{{}}", parm);
            jPushUtil.jpushAll(parm);
            log.info("???????????? trainProgramPush parm:{{}}", parm);
            log.info("???????????? trainProgramPush userId:{{}}", programEntity.getUserId());
            log.info("???????????? trainProgramPush content:{{}}", programEntity.getProgramName() + "??????????????????" + DateUtils.formatLocalDateTime(programPlanGenerationEntity.getTrainDate(), "yyyy???MM???dd??? HH???"));
            log.info("???????????? trainProgramPush userId:{{}}", programPlanGenerationEntity.getUserId());
            userOrderRecordService.save(programEntity.getUserId(), "5",
                    "????????????",
                    //"????????????????????????,????????????" + DateUtils.formatLocalDateTime(LocalDateTime.now(), "yyyy-MM-dd"),
                    //"????????????????????????,????????????" + DateUtils.formatLocalDateTime(programPlanGenerationEntity.getTrainDate(),"yyyy-MM-dd") + "???" + DateUtils.formatLocalDateTime(programEntity.getEndTime(),"yyyy-MM-dd"),
                    programEntity.getProgramName() + "??????????????????" + DateUtils.formatLocalDateTime(LocalDateTime.now(), "yyyy???MM???dd???HH???"),
                    programPlanGenerationEntity.getTrainDate(),
                    programEntity.getId());
        });
        return ResultUtil.success();
    }

    private LocalDateTime getLocalDateTime(DateTime dateTime) {
        Instant instant = dateTime.toDate().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }
}