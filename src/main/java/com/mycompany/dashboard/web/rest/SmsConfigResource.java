package com.mycompany.dashboard.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.dashboard.domain.SmsConfig;
import com.mycompany.dashboard.repository.SmsConfigRepository;
import com.mycompany.dashboard.service.CommonConditionQueryService;
import com.mycompany.dashboard.service.SmsConfigQueryService;
import com.mycompany.dashboard.service.SmsConfigService;
import com.mycompany.dashboard.service.criteria.SmsConfigCriteria;
import com.mycompany.dashboard.service.dto.SmsConfigDTO;
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
import java.util.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

 * 管理实体{@link com.mycompany.dashboard.domain.SmsConfig}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "sms-configs", tags = "短信配置API接口")
public class SmsConfigResource {

    private final Logger log = LoggerFactory.getLogger(SmsConfigResource.class);

    private static final String ENTITY_NAME = "filesSmsConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SmsConfigService smsConfigService;

    private final SmsConfigRepository smsConfigRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final SmsConfigQueryService smsConfigQueryService;

    public SmsConfigResource(
        SmsConfigService smsConfigService,
        SmsConfigRepository smsConfigRepository,
        CommonConditionQueryService commonConditionQueryService,
        SmsConfigQueryService smsConfigQueryService
    ) {
        this.smsConfigService = smsConfigService;
        this.smsConfigRepository = smsConfigRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.smsConfigQueryService = smsConfigQueryService;
    }

    /**
     * {@code POST  /sms-configs} : Create a new smsConfig.
     *
     * @param smsConfigDTO the smsConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smsConfigDTO, or with status {@code 400 (Bad Request)} if the smsConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sms-configs")
    @ApiOperation(value = "新建短信配置", notes = "创建并返回一个新的短信配置")
    public ResponseEntity<SmsConfigDTO> createSmsConfig(@RequestBody SmsConfigDTO smsConfigDTO) throws URISyntaxException {
        log.debug("REST request to save SmsConfig : {}", smsConfigDTO);
        if (smsConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsConfigDTO result = smsConfigService.save(smsConfigDTO);
        return ResponseEntity
            .created(new URI("/api/sms-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-configs/:id} : Updates an existing smsConfig.
     *
     * @param id the id of the smsConfigDTO to save.
     * @param smsConfigDTO the smsConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsConfigDTO,
     * or with status {@code 400 (Bad Request)} if the smsConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-configs/{id}")
    @ApiOperation(value = "更新短信配置", notes = "根据主键更新并返回一个更新后的短信配置")
    public ResponseEntity<SmsConfigDTO> updateSmsConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsConfigDTO smsConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SmsConfig : {}, {}", id, smsConfigDTO);
        if (smsConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!smsConfigService.exists(SmsConfig::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SmsConfigDTO result = smsConfigService.save(smsConfigDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, smsConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sms-configs/:id} : Partial updates given fields of an existing smsConfig, field will ignore if it is null.
     *
     * @param id the id of the smsConfigDTO to save.
     * @param smsConfigDTO the smsConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsConfigDTO,
     * or with status {@code 400 (Bad Request)} if the smsConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the smsConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the smsConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新短信配置", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的短信配置")
    @PatchMapping(value = "/sms-configs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SmsConfigDTO> partialUpdateSmsConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsConfigDTO smsConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SmsConfig partially : {}, {}", id, smsConfigDTO);
        if (smsConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (smsConfigRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SmsConfigDTO> result = smsConfigService.partialUpdate(smsConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, smsConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sms-configs} : get all the smsConfigs.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of smsConfigs in body.
     */
    @GetMapping("/sms-configs")
    @ApiOperation(value = "获取短信配置分页列表", notes = "获取短信配置的分页列表数据")
    public ResponseEntity<List<SmsConfigDTO>> getAllSmsConfigs(
        SmsConfigCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get SmsConfigs by criteria: {}", criteria);
        IPage<SmsConfigDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = smsConfigQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = smsConfigQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = smsConfigQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = smsConfigQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /sms-configs/count} : count all the smsConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sms-configs/count")
    public ResponseEntity<Long> countSmsConfigs(SmsConfigCriteria criteria) {
        log.debug("REST request to count SmsConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(smsConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sms-configs/:id} : get the "id" smsConfig.
     *
     * @param id the id of the smsConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the smsConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sms-configs/{id}")
    @ApiOperation(value = "获取指定主键的短信配置", notes = "获取指定主键的短信配置信息")
    public ResponseEntity<SmsConfigDTO> getSmsConfig(@PathVariable Long id) {
        log.debug("REST request to get SmsConfig : {}", id);
        Optional<SmsConfigDTO> smsConfigDTO = smsConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsConfigDTO);
    }

    /**
     * GET  /sms-configs/export : export the smsConfigs.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the smsConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sms-configs/export")
    @ApiOperation(value = "短信配置EXCEL导出", notes = "导出全部短信配置为EXCEL文件")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<SmsConfigDTO> page = smsConfigService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("计算机一班学生", "学生"), SmsConfigDTO.class, page.getRecords());
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
     * POST  /sms-configs/import : import the smsConfigs from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the smsConfigDTO, or with status 404 (Not Found)
     */
    @PostMapping("/sms-configs/import")
    @ApiOperation(value = "短信配置EXCEL导入", notes = "根据短信配置EXCEL文件导入全部数据")
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
        List<SmsConfigDTO> list = ExcelImportUtil.importExcel(savedFile, SmsConfigDTO.class, params);
        list.forEach(smsConfigService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /sms-configs/:id} : delete the "id" smsConfig.
     *
     * @param id the id of the smsConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sms-configs/{id}")
    @ApiOperation(value = "删除一个短信配置", notes = "根据主键删除单个短信配置")
    public ResponseEntity<Void> deleteSmsConfig(@PathVariable Long id) {
        log.debug("REST request to delete SmsConfig : {}", id);
        smsConfigService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /sms-configs} : delete all the "ids" SmsConfigs.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sms-configs")
    @ApiOperation(value = "删除多个短信配置", notes = "根据主键删除多个短信配置")
    public ResponseEntity<Void> deleteSmsConfigsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SmsConfigs : {}", ids);
        if (ids != null) {
            ids.forEach(smsConfigService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /sms-configs/specified-fields} : Updates an existing smsConfig by specified fields.
     *
     * @param smsConfigDTOAndSpecifiedFields the smsConfigDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsConfigDTO,
     * or with status {@code 400 (Bad Request)} if the smsConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-configs/specified-fields")
    @ApiOperation(value = "根据字段部分更新短信配置", notes = "根据指定字段部分更新短信配置，给定的属性值可以为任何值，包括null")
    public ResponseEntity<SmsConfigDTO> updateSmsConfigBySpecifiedFields(
        @RequestBody SmsConfigDTOAndSpecifiedFields smsConfigDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update SmsConfig : {}", smsConfigDTOAndSpecifiedFields);
        SmsConfigDTO smsConfigDTO = smsConfigDTOAndSpecifiedFields.getSmsConfig();
        Set<String> specifiedFields = smsConfigDTOAndSpecifiedFields.getSpecifiedFields();
        if (smsConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsConfigDTO result = smsConfigService.updateBySpecifiedFields(smsConfigDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-configs/specified-field} : Updates an existing smsConfig by specified field.
     *
     * @param smsConfigDTOAndSpecifiedFields the smsConfigDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsConfigDTO,
     * or with status {@code 400 (Bad Request)} if the smsConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-configs/specified-field")
    @ApiOperation(value = "更新短信配置单个属性", notes = "根据指定字段更新短信配置，给定的属性值可以为任何值，包括null")
    public ResponseEntity<SmsConfigDTO> updateSmsConfigBySpecifiedField(
        @RequestBody SmsConfigDTOAndSpecifiedFields smsConfigDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update SmsConfig : {}", smsConfigDTOAndSpecifiedFields);
        SmsConfigDTO smsConfigDTO = smsConfigDTOAndSpecifiedFields.getSmsConfig();
        String fieldName = smsConfigDTOAndSpecifiedFields.getSpecifiedField();
        if (smsConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsConfigDTO result = smsConfigService.updateBySpecifiedField(smsConfigDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class SmsConfigDTOAndSpecifiedFields {

        private SmsConfigDTO smsConfig;
        private Set<String> specifiedFields;
        private String specifiedField;

        private SmsConfigDTO getSmsConfig() {
            return smsConfig;
        }

        private void setSmsConfig(SmsConfigDTO smsConfig) {
            this.smsConfig = smsConfig;
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
