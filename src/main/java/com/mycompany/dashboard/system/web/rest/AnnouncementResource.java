package com.mycompany.dashboard.system.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.dashboard.domain.enumeration.AnnoCategory;
import com.mycompany.dashboard.domain.enumeration.AnnoSendStatus;
import com.mycompany.dashboard.domain.enumeration.ReceiverType;
import com.mycompany.dashboard.security.SecurityUtils;
import com.mycompany.dashboard.service.CommonConditionQueryService;
import com.mycompany.dashboard.system.domain.Announcement;
import com.mycompany.dashboard.system.domain.Announcement;
import com.mycompany.dashboard.system.repository.AnnouncementRepository;
import com.mycompany.dashboard.system.service.AnnouncementQueryService;
import com.mycompany.dashboard.system.service.AnnouncementRecordQueryService;
import com.mycompany.dashboard.system.service.AnnouncementRecordService;
import com.mycompany.dashboard.system.service.AnnouncementService;
import com.mycompany.dashboard.system.service.criteria.AnnouncementCriteria;
import com.mycompany.dashboard.system.service.criteria.AnnouncementRecordCriteria;
import com.mycompany.dashboard.system.service.dto.AnnouncementDTO;
import com.mycompany.dashboard.system.service.dto.AnnouncementRecordDTO;
import com.mycompany.dashboard.util.web.IPageUtil;
import com.mycompany.dashboard.util.web.PageableUtils;
import com.mycompany.dashboard.web.rest.errors.BadRequestAlertException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.mycompany.dashboard.system.domain.Announcement}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "announcements", tags = "系统通告API接口")
public class AnnouncementResource {

    private final Logger log = LoggerFactory.getLogger(AnnouncementResource.class);

    private static final String ENTITY_NAME = "announcement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnouncementRecordService announcementRecordService;

    private final AnnouncementRecordQueryService announcementRecordQueryService;

    private final AnnouncementService announcementService;

    private final AnnouncementRepository announcementRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final AnnouncementQueryService announcementQueryService;

    public AnnouncementResource(
        AnnouncementRecordService announcementRecordService,
        AnnouncementRecordQueryService announcementRecordQueryService,
        AnnouncementService announcementService,
        AnnouncementRepository announcementRepository,
        CommonConditionQueryService commonConditionQueryService,
        AnnouncementQueryService announcementQueryService
    ) {
        this.announcementRecordService = announcementRecordService;
        this.announcementRecordQueryService = announcementRecordQueryService;
        this.announcementService = announcementService;
        this.announcementRepository = announcementRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.announcementQueryService = announcementQueryService;
    }

    /**
     * {@code POST  /announcements} : Create a new announcement.
     *
     * @param announcementDTO the announcementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new announcementDTO, or with status {@code 400 (Bad Request)} if the announcement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/announcements")
    @ApiOperation(value = "新建系统通告", notes = "创建并返回一个新的系统通告")
    public ResponseEntity<AnnouncementDTO> createAnnouncement(@RequestBody AnnouncementDTO announcementDTO) throws URISyntaxException {
        log.debug("REST request to save Announcement : {}", announcementDTO);
        if (announcementDTO.getId() != null) {
            throw new BadRequestAlertException("A new announcement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnouncementDTO result = announcementService.save(announcementDTO);
        return ResponseEntity
            .created(new URI("/api/announcements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /announcements/:id} : Updates an existing announcement.
     *
     * @param id the id of the announcementDTO to save.
     * @param announcementDTO the announcementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementDTO,
     * or with status {@code 400 (Bad Request)} if the announcementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the announcementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/announcements/{id}")
    @ApiOperation(value = "更新系统通告", notes = "根据主键更新并返回一个更新后的系统通告")
    public ResponseEntity<AnnouncementDTO> updateAnnouncement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnnouncementDTO announcementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Announcement : {}, {}", id, announcementDTO);
        if (announcementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, announcementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!announcementService.exists(Announcement::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnnouncementDTO result = announcementService.save(announcementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, announcementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /announcements/current-user/unread/{category}} : get the  announcements of unread for current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the announcementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/announcements/current-user/unread/{category}")
    public ResponseEntity<List<AnnouncementDTO>> getUnReadAnnouncements(@PathVariable AnnoCategory category, Pageable page) {
        AnnouncementCriteria acriteria = new AnnouncementCriteria();
        acriteria.sendStatus().setEquals(AnnoSendStatus.RELEASED);
        acriteria.receiverType().setEquals(ReceiverType.ALL);
        announcementRecordService.updateRecord(announcementQueryService.findByCriteria(acriteria));
        AnnouncementRecordCriteria criteria = new AnnouncementRecordCriteria();
        criteria.userId().setEquals(SecurityUtils.getCurrentUserId().get());
        criteria.hasRead().setEquals(false);
        IPage<AnnouncementDTO> byCriteria;
        List<Long> ids = announcementRecordQueryService
            .findByCriteria(criteria)
            .stream()
            .map(AnnouncementRecordDTO::getAnntId)
            .collect(Collectors.toList());
        if (!ids.isEmpty()) {
            AnnouncementCriteria announcementCriteria = new AnnouncementCriteria();
            announcementCriteria.id().setIn(ids);
            announcementCriteria.category().setEquals(category);
            byCriteria = announcementQueryService.findByCriteria(announcementCriteria, PageableUtils.toPage(page));
        } else {
            byCriteria = new Page<>(0, page.getPageSize(), 0);
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), byCriteria);
        return ResponseEntity.ok().headers(headers).body(byCriteria.getRecords());
    }

    /**
     * {@code PUT  /announcements/:id} : release the "id" announcement.
     *
     * @param id the id of the announcementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the announcementDTO, or with status {@code 404 (Not Found)}.
     */
    @PutMapping("/announcements/{id}/release")
    public ResponseEntity<AnnouncementDTO> releaseAnnouncement(@PathVariable Long id) {
        log.debug("REST request to get Announcement : {}", id);
        announcementService.release(id);
        Optional<AnnouncementDTO> announcementDTO = announcementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(announcementDTO);
    }

    /**
     * {@code PUT  /announcements/:id/read} : Updates an existing announcementRecord to read by current user.
     *
     * @param id the id of the announcementId to read.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementRecordDTO,
     * or with status {@code 400 (Bad Request)} if the announcementRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the announcementRecordDTO couldn't be updated.
     */
    @PutMapping("/announcements/{id}/read")
    public ResponseEntity<Void> updateAnnouncementRecordRead(@PathVariable(value = "id", required = false) final Long id) {
        AnnouncementRecordCriteria criteria = new AnnouncementRecordCriteria();
        criteria.anntId().setEquals(id);
        criteria.userId().setEquals(SecurityUtils.getCurrentUserId().get());
        AtomicReference<AnnouncementRecordDTO> result = new AtomicReference<>();
        announcementRecordQueryService
            .getOneByCriteria(criteria)
            .ifPresent(
                announcementRecordDTO -> {
                    announcementRecordDTO.setHasRead(true);
                    announcementRecordDTO.setReadTime(ZonedDateTime.now());
                    result.set(
                        announcementRecordService.updateBySpecifiedFields(
                            announcementRecordDTO,
                            new HashSet<>(Arrays.asList("hasRead", "readTime"))
                        )
                    );
                }
            );
        if (result.get() != null) {
            return ResponseEntity.ok().build();
        } else {
            throw new BadRequestAlertException("指定的消息Id不存在", ENTITY_NAME, "IdNotExist");
        }
    }

    /**
     * {@code PATCH  /announcements/:id} : Partial updates given fields of an existing announcement, field will ignore if it is null.
     *
     * @param id the id of the announcementDTO to save.
     * @param announcementDTO the announcementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementDTO,
     * or with status {@code 400 (Bad Request)} if the announcementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the announcementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the announcementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新系统通告", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的系统通告")
    @PatchMapping(value = "/announcements/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AnnouncementDTO> partialUpdateAnnouncement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnnouncementDTO announcementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Announcement partially : {}, {}", id, announcementDTO);
        if (announcementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, announcementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (announcementRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnnouncementDTO> result = announcementService.partialUpdate(announcementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, announcementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /announcements} : get all the announcements.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of announcements in body.
     */
    @GetMapping("/announcements")
    @ApiOperation(value = "获取系统通告分页列表", notes = "获取系统通告的分页列表数据")
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements(
        AnnouncementCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get Announcements by criteria: {}", criteria);
        IPage<AnnouncementDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = announcementQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = announcementQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = announcementQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = announcementQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /announcements/count} : count all the announcements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/announcements/count")
    public ResponseEntity<Long> countAnnouncements(AnnouncementCriteria criteria) {
        log.debug("REST request to count Announcements by criteria: {}", criteria);
        return ResponseEntity.ok().body(announcementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /announcements/:id} : get the "id" announcement.
     *
     * @param id the id of the announcementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the announcementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/announcements/{id}")
    @ApiOperation(value = "获取指定主键的系统通告", notes = "获取指定主键的系统通告信息")
    public ResponseEntity<AnnouncementDTO> getAnnouncement(@PathVariable Long id) {
        log.debug("REST request to get Announcement : {}", id);
        Optional<AnnouncementDTO> announcementDTO = announcementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(announcementDTO);
    }

    /**
     * GET  /announcements/export : export the announcements.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the announcementDTO, or with status 404 (Not Found)
     */
    @GetMapping("/announcements/export")
    @ApiOperation(value = "系统通告EXCEL导出", notes = "导出全部系统通告为EXCEL文件")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<AnnouncementDTO> page = announcementService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("计算机一班学生", "学生"),
            AnnouncementDTO.class,
            page.getRecords()
        );
        File savefile = new File("export");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("export/personDTO_2018_09_10.xls");
        workbook.write(fos);
        fos.close();
        return ResponseEntity.ok().build();
    }

    /**
     * POST  /announcements/import : import the announcements from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the announcementDTO, or with status 404 (Not Found)
     */
    @PostMapping("/announcements/import")
    @ApiOperation(value = "系统通告EXCEL导入", notes = "根据系统通告EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws IOException {
        String fileRealName = file.getOriginalFilename(); //获得原始文件名;
        int pointIndex = fileRealName.lastIndexOf("."); //点号的位置
        String fileSuffix = fileRealName.substring(pointIndex); //截取文件后缀
        String fileNewName = UUID.randomUUID().toString(); //文件new名称时间戳
        String saveFileName = fileNewName.concat(fileSuffix); //文件存取名
        String filePath = "import";
        File path = new File(filePath); //判断文件路径下的文件夹是否存在，不存在则创建
        if (!path.exists()) {
            path.mkdirs();
        }
        File savedFile = new File(filePath, saveFileName);
        file.transferTo(savedFile);
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<AnnouncementDTO> list = ExcelImportUtil.importExcel(savedFile, AnnouncementDTO.class, params);
        list.forEach(announcementService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /announcements/:id} : delete the "id" announcement.
     *
     * @param id the id of the announcementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/announcements/{id}")
    @ApiOperation(value = "删除一个系统通告", notes = "根据主键删除单个系统通告")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        log.debug("REST request to delete Announcement : {}", id);
        announcementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /announcements} : delete all the "ids" Announcements.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/announcements")
    @ApiOperation(value = "删除多个系统通告", notes = "根据主键删除多个系统通告")
    public ResponseEntity<Void> deleteAnnouncementsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete Announcements : {}", ids);
        if (ids != null) {
            ids.forEach(announcementService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /announcements/specified-fields} : Updates an existing announcement by specified fields.
     *
     * @param announcementDTOAndSpecifiedFields the announcementDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementDTO,
     * or with status {@code 400 (Bad Request)} if the announcementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the announcementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/announcements/specified-fields")
    @ApiOperation(value = "根据字段部分更新系统通告", notes = "根据指定字段部分更新系统通告，给定的属性值可以为任何值，包括null")
    public ResponseEntity<AnnouncementDTO> updateAnnouncementBySpecifiedFields(
        @RequestBody AnnouncementDTOAndSpecifiedFields announcementDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update Announcement : {}", announcementDTOAndSpecifiedFields);
        AnnouncementDTO announcementDTO = announcementDTOAndSpecifiedFields.getAnnouncement();
        Set<String> specifiedFields = announcementDTOAndSpecifiedFields.getSpecifiedFields();
        if (announcementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnnouncementDTO result = announcementService.updateBySpecifiedFields(announcementDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, announcementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /announcements/specified-field} : Updates an existing announcement by specified field.
     *
     * @param announcementDTOAndSpecifiedFields the announcementDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementDTO,
     * or with status {@code 400 (Bad Request)} if the announcementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the announcementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/announcements/specified-field")
    @ApiOperation(value = "更新系统通告单个属性", notes = "根据指定字段更新系统通告，给定的属性值可以为任何值，包括null")
    public ResponseEntity<AnnouncementDTO> updateAnnouncementBySpecifiedField(
        @RequestBody AnnouncementDTOAndSpecifiedFields announcementDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update Announcement : {}", announcementDTOAndSpecifiedFields);
        AnnouncementDTO announcementDTO = announcementDTOAndSpecifiedFields.getAnnouncement();
        String fieldName = announcementDTOAndSpecifiedFields.getSpecifiedField();
        if (announcementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnnouncementDTO result = announcementService.updateBySpecifiedField(announcementDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class AnnouncementDTOAndSpecifiedFields {

        private AnnouncementDTO announcement;
        private Set<String> specifiedFields;
        private String specifiedField;

        private AnnouncementDTO getAnnouncement() {
            return announcement;
        }

        private void setAnnouncement(AnnouncementDTO announcement) {
            this.announcement = announcement;
        }

        private Set<String> getSpecifiedFields() {
            return specifiedFields;
        }

        private void setSpecifiedFields(Set<String> specifiedFields) {
            this.specifiedFields = specifiedFields;
        }

        public String getSpecifiedField() {
            return specifiedField;
        }

        public void setSpecifiedField(String specifiedField) {
            this.specifiedField = specifiedField;
        }
    }
}
