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
import com.mycompany.dashboard.service.CommonConditionQueryService;
import com.mycompany.dashboard.system.domain.SmsTemplate;
import com.mycompany.dashboard.system.repository.SmsTemplateRepository;
import com.mycompany.dashboard.system.service.SmsTemplateQueryService;
import com.mycompany.dashboard.system.service.SmsTemplateService;
import com.mycompany.dashboard.system.service.criteria.SmsTemplateCriteria;
import com.mycompany.dashboard.system.service.dto.SmsTemplateDTO;
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

 * ????????????{@link com.mycompany.dashboard.system.domain.SmsTemplate}???REST Controller???
 */
@RestController
@RequestMapping("/api")
@Api(value = "sms-templates", tags = "????????????API??????")
public class SmsTemplateResource {

    private final Logger log = LoggerFactory.getLogger(SmsTemplateResource.class);

    private static final String ENTITY_NAME = "smsTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SmsTemplateService smsTemplateService;

    private final SmsTemplateRepository smsTemplateRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final SmsTemplateQueryService smsTemplateQueryService;

    public SmsTemplateResource(
        SmsTemplateService smsTemplateService,
        SmsTemplateRepository smsTemplateRepository,
        CommonConditionQueryService commonConditionQueryService,
        SmsTemplateQueryService smsTemplateQueryService
    ) {
        this.smsTemplateService = smsTemplateService;
        this.smsTemplateRepository = smsTemplateRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.smsTemplateQueryService = smsTemplateQueryService;
    }

    /**
     * {@code POST  /sms-templates} : Create a new smsTemplate.
     *
     * @param smsTemplateDTO the smsTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smsTemplateDTO, or with status {@code 400 (Bad Request)} if the smsTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sms-templates")
    @ApiOperation(value = "??????????????????", notes = "???????????????????????????????????????")
    public ResponseEntity<SmsTemplateDTO> createSmsTemplate(@RequestBody SmsTemplateDTO smsTemplateDTO) throws URISyntaxException {
        log.debug("REST request to save SmsTemplate : {}", smsTemplateDTO);
        if (smsTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsTemplateDTO result = smsTemplateService.save(smsTemplateDTO);
        return ResponseEntity
            .created(new URI("/api/sms-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-templates/:id} : Updates an existing smsTemplate.
     *
     * @param id the id of the smsTemplateDTO to save.
     * @param smsTemplateDTO the smsTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the smsTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-templates/{id}")
    @ApiOperation(value = "??????????????????", notes = "?????????????????????????????????????????????????????????")
    public ResponseEntity<SmsTemplateDTO> updateSmsTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsTemplateDTO smsTemplateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SmsTemplate : {}, {}", id, smsTemplateDTO);
        if (smsTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!smsTemplateService.exists(SmsTemplate::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SmsTemplateDTO result = smsTemplateService.save(smsTemplateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, smsTemplateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sms-templates/:id} : Partial updates given fields of an existing smsTemplate, field will ignore if it is null.
     *
     * @param id the id of the smsTemplateDTO to save.
     * @param smsTemplateDTO the smsTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the smsTemplateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the smsTemplateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the smsTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "????????????????????????", notes = "??????????????????????????????????????????????????????null????????????????????????????????????????????????????????????")
    @PatchMapping(value = "/sms-templates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SmsTemplateDTO> partialUpdateSmsTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsTemplateDTO smsTemplateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SmsTemplate partially : {}, {}", id, smsTemplateDTO);
        if (smsTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (smsTemplateRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SmsTemplateDTO> result = smsTemplateService.partialUpdate(smsTemplateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, smsTemplateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sms-templates} : get all the smsTemplates.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of smsTemplates in body.
     */
    @GetMapping("/sms-templates")
    @ApiOperation(value = "??????????????????????????????", notes = "???????????????????????????????????????")
    public ResponseEntity<List<SmsTemplateDTO>> getAllSmsTemplates(
        SmsTemplateCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get SmsTemplates by criteria: {}", criteria);
        IPage<SmsTemplateDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = smsTemplateQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = smsTemplateQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = smsTemplateQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = smsTemplateQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /sms-templates/count} : count all the smsTemplates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sms-templates/count")
    public ResponseEntity<Long> countSmsTemplates(SmsTemplateCriteria criteria) {
        log.debug("REST request to count SmsTemplates by criteria: {}", criteria);
        return ResponseEntity.ok().body(smsTemplateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sms-templates/:id} : get the "id" smsTemplate.
     *
     * @param id the id of the smsTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the smsTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sms-templates/{id}")
    @ApiOperation(value = "?????????????????????????????????", notes = "???????????????????????????????????????")
    public ResponseEntity<SmsTemplateDTO> getSmsTemplate(@PathVariable Long id) {
        log.debug("REST request to get SmsTemplate : {}", id);
        Optional<SmsTemplateDTO> smsTemplateDTO = smsTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsTemplateDTO);
    }

    /**
     * GET  /sms-templates/export : export the smsTemplates.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the smsTemplateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sms-templates/export")
    @ApiOperation(value = "????????????EXCEL??????", notes = "???????????????????????????EXCEL??????")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<SmsTemplateDTO> page = smsTemplateService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("?????????????????????", "??????"),
            SmsTemplateDTO.class,
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
     * POST  /sms-templates/import : import the smsTemplates from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the smsTemplateDTO, or with status 404 (Not Found)
     */
    @PostMapping("/sms-templates/import")
    @ApiOperation(value = "????????????EXCEL??????", notes = "??????????????????EXCEL????????????????????????")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws IOException {
        String fileRealName = file.getOriginalFilename(); //?????????????????????;
        int pointIndex = fileRealName.lastIndexOf("."); //???????????????
        String fileSuffix = fileRealName.substring(pointIndex); //??????????????????
        String fileNewName = UUID.randomUUID().toString(); //??????new???????????????
        String saveFileName = fileNewName.concat(fileSuffix); //???????????????
        String filePath = "import";
        File path = new File(filePath); //??????????????????????????????????????????????????????????????????
        if (!path.exists()) {
            path.mkdirs();
        }
        File savedFile = new File(filePath, saveFileName);
        file.transferTo(savedFile);
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<SmsTemplateDTO> list = ExcelImportUtil.importExcel(savedFile, SmsTemplateDTO.class, params);
        list.forEach(smsTemplateService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /sms-templates/:id} : delete the "id" smsTemplate.
     *
     * @param id the id of the smsTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sms-templates/{id}")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????????????????")
    public ResponseEntity<Void> deleteSmsTemplate(@PathVariable Long id) {
        log.debug("REST request to delete SmsTemplate : {}", id);
        smsTemplateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /sms-templates} : delete all the "ids" SmsTemplates.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sms-templates")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????????????????")
    public ResponseEntity<Void> deleteSmsTemplatesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SmsTemplates : {}", ids);
        if (ids != null) {
            ids.forEach(smsTemplateService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /sms-templates/specified-fields} : Updates an existing smsTemplate by specified fields.
     *
     * @param smsTemplateDTOAndSpecifiedFields the smsTemplateDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the smsTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-templates/specified-fields")
    @ApiOperation(value = "????????????????????????????????????", notes = "??????????????????????????????????????????????????????????????????????????????????????????null")
    public ResponseEntity<SmsTemplateDTO> updateSmsTemplateBySpecifiedFields(
        @RequestBody SmsTemplateDTOAndSpecifiedFields smsTemplateDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update SmsTemplate : {}", smsTemplateDTOAndSpecifiedFields);
        SmsTemplateDTO smsTemplateDTO = smsTemplateDTOAndSpecifiedFields.getSmsTemplate();
        Set<String> specifiedFields = smsTemplateDTOAndSpecifiedFields.getSpecifiedFields();
        if (smsTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsTemplateDTO result = smsTemplateService.updateBySpecifiedFields(smsTemplateDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsTemplateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-templates/specified-field} : Updates an existing smsTemplate by specified field.
     *
     * @param smsTemplateDTOAndSpecifiedFields the smsTemplateDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the smsTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-templates/specified-field")
    @ApiOperation(value = "??????????????????????????????", notes = "????????????????????????????????????????????????????????????????????????????????????null")
    public ResponseEntity<SmsTemplateDTO> updateSmsTemplateBySpecifiedField(
        @RequestBody SmsTemplateDTOAndSpecifiedFields smsTemplateDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update SmsTemplate : {}", smsTemplateDTOAndSpecifiedFields);
        SmsTemplateDTO smsTemplateDTO = smsTemplateDTOAndSpecifiedFields.getSmsTemplate();
        String fieldName = smsTemplateDTOAndSpecifiedFields.getSpecifiedField();
        if (smsTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsTemplateDTO result = smsTemplateService.updateBySpecifiedField(smsTemplateDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class SmsTemplateDTOAndSpecifiedFields {

        private SmsTemplateDTO smsTemplate;
        private Set<String> specifiedFields;
        private String specifiedField;

        private SmsTemplateDTO getSmsTemplate() {
            return smsTemplate;
        }

        private void setSmsTemplate(SmsTemplateDTO smsTemplate) {
            this.smsTemplate = smsTemplate;
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
